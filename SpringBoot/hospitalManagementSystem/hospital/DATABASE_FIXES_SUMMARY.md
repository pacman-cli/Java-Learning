# Database Fixes Summary

## Overview
This document summarizes all database schema issues found and fixes applied to the Hospital Management System.

---

## Issues Found

### Issue #1: V8 Migration - Missing Audit Columns
**Error:**
```
Key column 'deleted_at' doesn't exist in table
Location: db/migration/V8__add_doctor_audit_columns.sql
Line: 48
SQL State: 42000
Error Code: 1072
```

**Root Cause:**
The V8 migration script was attempting to create indexes on `deleted_at` columns in the `patients` and `doctors` tables before these columns were added to the database.

**Impact:**
- Application fails to start
- Flyway migration marked as failed
- Entity Manager Factory cannot be initialized

---

### Issue #2: Missing Insurance Columns in Billings Table
**Error:**
```
Schema-validation: missing column [covered_amount] in table [billings]
org.hibernate.tool.schema.spi.SchemaManagementException
```

**Root Cause:**
The `Billing` entity model has three insurance-related fields:
- `coveredAmount` (BigDecimal)
- `patientPayable` (BigDecimal)
- `insurance` (ManyToOne relationship)

However, the database table `billings` was missing these columns because:
1. V3 migration had these ALTER statements but they were **commented out** with `#`
2. V4 migration also had these statements but they were **commented out**
3. V6 migration (Payments) didn't add them either

**Impact:**
- Hibernate schema validation fails
- Application cannot start
- EntityManagerFactory creation fails

---

## Fixes Applied

### Fix #1: V8 Migration - Added Audit Columns First

**File:** `V8__add_doctor_audit_columns.sql`

**Changes:**
1. **PATIENTS table** - Added audit columns:
   - `created_at` (DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)
   - `updated_at` (DATETIME NULL ON UPDATE CURRENT_TIMESTAMP)
   - `created_by` (VARCHAR(50) NULL)
   - `updated_by` (VARCHAR(50) NULL)
   - `deleted_at` (DATETIME NULL) ← **KEY FIX**
   - `deleted_by` (VARCHAR(50) NULL)

2. **DOCTORS table** - Added audit columns:
   - `created_at` (DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)
   - `updated_at` (DATETIME NULL ON UPDATE CURRENT_TIMESTAMP)
   - `created_by` (VARCHAR(50) NULL)
   - `updated_by` (VARCHAR(50) NULL)
   - `deleted_at` (DATETIME NULL) ← **KEY FIX**
   - `deleted_by` (VARCHAR(50) NULL)

3. **Indexes created** (after columns exist):
   - `idx_patient_created_at`
   - `idx_patient_deleted_at`
   - `idx_doctor_created_at`
   - `idx_doctor_deleted_at`

**Implementation:**
- All changes are idempotent (safe to run multiple times)
- Uses conditional SQL with INFORMATION_SCHEMA checks
- Follows MySQL prepared statement pattern

---

### Fix #2: V11 Migration - Added Insurance Columns to Billings

**File:** `V11__add_insurance_columns_to_billings.sql` (NEW)

**Changes:**
1. Added `insurance_id` (BIGINT NULL) - Foreign key to insurance table
2. Added `covered_amount` (DECIMAL(38, 2) NULL) - Amount covered by insurance
3. Added `patient_payable` (DECIMAL(38, 2) NULL) - Amount patient must pay
4. Added foreign key constraint `FK_BILLINGS_ON_INSURANCE`
5. Added index `idx_billing_insurance` for query performance

**Implementation:**
- Idempotent migration using INFORMATION_SCHEMA checks
- Proper foreign key relationship to insurance table
- Performance optimization with indexes

---

## Helper Scripts Created

### 1. `reset_database.sql`
**Purpose:** Complete database reset (development only)

**What it does:**
```sql
DROP DATABASE IF EXISTS hospital_db;
CREATE DATABASE hospital_db;
USE hospital_db;
```

**When to use:**
- Clean start after migration failures
- Development environment setup
- Testing migration scripts from scratch

**⚠️ WARNING:** This deletes ALL data!

---

### 2. `repair_flyway.sql`
**Purpose:** Fix failed migrations without losing data

**What it does:**
1. Removes failed V8 migration record from `flyway_schema_history`
2. Manually adds missing audit columns to `patients` table
3. Manually adds missing audit columns to `doctors` table
4. All changes are idempotent

**When to use:**
- You have important data to preserve
- Production or staging environments
- Want to fix the issue without data loss

---

### 3. `FLYWAY_FIX_GUIDE.md`
**Purpose:** Comprehensive troubleshooting guide

**Contents:**
- Detailed problem explanation
- Step-by-step fix instructions
- Verification steps
- Troubleshooting tips
- Prevention guidelines for future migrations

---

### 4. `FIX_README.md`
**Purpose:** Quick-start guide for developers

**Contents:**
- Problem summary
- Quick fix commands
- Two options (reset vs repair)
- What was fixed
- Test credentials after reset

---

## How to Apply Fixes

### Option A: Clean Reset (Recommended for Development)

```bash
# 1. Navigate to project
cd hospitalManagementSystem/hospital

# 2. Stop the application (Ctrl+C if running)

# 3. Reset database
mysql -u root -p < reset_database.sql

# 4. Restart application
./mvnw spring-boot:run
```

**Result:**
- Fresh database with all migrations (V1 through V11)
- DataLoader seeds test data automatically
- No migration failures

---

### Option B: Repair Without Data Loss

```bash
# 1. Navigate to project
cd hospitalManagementSystem/hospital

# 2. Stop the application (Ctrl+C if running)

# 3. Repair Flyway and add missing columns
mysql -u root -p hospital_db < repair_flyway.sql

# 4. Restart application
./mvnw spring-boot:run
```

**Result:**
- Existing data preserved
- V8 migration cleaned up
- Missing columns added manually
- V11 will run on next startup

---

## Verification Steps

### 1. Check Flyway Migration History
```sql
USE hospital_db;
SELECT version, description, success, installed_on 
FROM flyway_schema_history 
ORDER BY version;
```

**Expected output:**
- V1 through V11 all with `success = 1`
- No failed migrations

### 2. Verify Patients Table Schema
```sql
DESCRIBE patients;
```

**Expected columns include:**
- id, full_name, date_of_birth, gender, contact_info, address, emergency_contact
- **created_at, updated_at, created_by, updated_by, deleted_at, deleted_by** ← NEW

### 3. Verify Doctors Table Schema
```sql
DESCRIBE doctors;
```

**Expected columns include:**
- id, full_name, specialization, qualifications, contact_info, daily_schedule
- **created_at, updated_at, created_by, updated_by, deleted_at, deleted_by** ← NEW

### 4. Verify Billings Table Schema
```sql
DESCRIBE billings;
```

**Expected columns include:**
- id, amount, billing_date, payment_method, patient_id, appointment_id
- status, description, paid_at
- **insurance_id, covered_amount, patient_payable** ← NEW

### 5. Check Indexes
```sql
-- Patients indexes
SHOW INDEX FROM patients WHERE Key_name LIKE 'idx_patient_%';

-- Doctors indexes  
SHOW INDEX FROM doctors WHERE Key_name LIKE 'idx_doctor_%';

-- Billings indexes
SHOW INDEX FROM billings WHERE Key_name LIKE 'idx_billing_%';
```

### 6. Verify Foreign Keys
```sql
SELECT 
    CONSTRAINT_NAME,
    TABLE_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'hospital_db'
AND TABLE_NAME = 'billings'
AND CONSTRAINT_NAME LIKE 'FK_%';
```

**Expected:** Should include `FK_BILLINGS_ON_INSURANCE`

---

## Application Startup Verification

After applying fixes and restarting the application, check logs for:

### ✅ Success Indicators:
```
✓ Migrating schema `hospital_db` to version "8 - add doctor audit columns"
✓ Migrating schema `hospital_db` to version "11 - add insurance columns to billings"
✓ Successfully applied X migrations
✓ Data Seeding Completed Successfully!
✓ Seeded Roles: 3
✓ Seeded Users: X
✓ Seeded Patients: X
✓ Seeded Doctors: X
✓ Application started successfully
```

### ❌ Failure Indicators (if still present):
```
✗ Key column 'deleted_at' doesn't exist in table
✗ Schema-validation: missing column [covered_amount]
✗ Migration failed
✗ Unable to build Hibernate SessionFactory
✗ Error creating bean with name 'entityManagerFactory'
```

---

## Prevention for Future Migrations

### Best Practices:

1. **Always add columns before creating indexes/constraints on them**
   ```sql
   -- GOOD:
   ALTER TABLE my_table ADD COLUMN new_col VARCHAR(50);
   CREATE INDEX idx_new_col ON my_table(new_col);
   
   -- BAD:
   CREATE INDEX idx_new_col ON my_table(new_col); -- Column doesn't exist yet!
   ```

2. **Make migrations idempotent**
   ```sql
   SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS ...);
   SET @sql = IF(@col_exists = 0, 'ALTER TABLE ...', 'SELECT ...');
   PREPARE stmt FROM @sql;
   EXECUTE stmt;
   DEALLOCATE PREPARE stmt;
   ```

3. **Don't comment out migrations that should run**
   - If you don't want a migration to run, delete it or rename it with `.disabled`
   - Commented SQL in migration files can cause confusion

4. **Test migrations on a copy of production data**
   - Use `mysqldump` to backup before running new migrations
   - Test on dev database first

5. **Use Flyway validation in CI/CD**
   ```bash
   ./mvnw flyway:validate
   ```

6. **Keep entity models in sync with database schema**
   - When adding fields to entities, create corresponding migration
   - Run schema validation in tests

---

## Files Modified/Created

### Modified:
- ✏️ `src/main/resources/db/migration/V8__add_doctor_audit_columns.sql`

### Created:
- ➕ `src/main/resources/db/migration/V11__add_insurance_columns_to_billings.sql`
- ➕ `reset_database.sql`
- ➕ `repair_flyway.sql`
- ➕ `FLYWAY_FIX_GUIDE.md`
- ➕ `FIX_README.md`
- ➕ `DATABASE_FIXES_SUMMARY.md` (this file)

---

## Quick Command Reference

```bash
# Reset database (loses all data)
mysql -u root -p < reset_database.sql

# Repair without data loss
mysql -u root -p hospital_db < repair_flyway.sql

# Start application
./mvnw spring-boot:run

# Check migration status
mysql -u root -p -e "USE hospital_db; SELECT * FROM flyway_schema_history;"

# Describe a table
mysql -u root -p -e "USE hospital_db; DESCRIBE billings;"

# Maven clean build
./mvnw clean install

# Flyway commands
./mvnw flyway:info      # Show migration status
./mvnw flyway:validate  # Validate migrations
./mvnw flyway:repair    # Repair failed migrations
```

---

## Contact & Support

If issues persist after applying these fixes:

1. **Check application logs** for detailed error messages
2. **Verify database connectivity:** `mysql -u root -p hospital_db`
3. **Ensure no port conflicts:** Check that port 8080 is available
4. **Verify Java version:** Should be Java 17+
5. **Check MySQL version:** Should be MySQL 8.0+

---

## Summary

✅ **V8 Migration Fixed:** Audit columns now added before indexes are created  
✅ **V11 Migration Created:** Insurance columns added to billings table  
✅ **Helper Scripts Provided:** Easy database reset or repair  
✅ **Documentation Complete:** Comprehensive guides available  
✅ **Idempotent Migrations:** Safe to run multiple times  
✅ **Application Ready:** Should start successfully after applying fixes  

**Next Steps:**
1. Choose reset or repair option
2. Apply the fix
3. Restart application
4. Verify successful startup
5. Test patient portal functionality

Good luck! 🚀