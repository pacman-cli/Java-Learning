# Flyway Migration Fix Guide

## Problem Summary

The application failed to start due to a Flyway migration error in `V8__add_doctor_audit_columns.sql`.

**Error Message:**
```
Key column 'deleted_at' doesn't exist in table
Location: db/migration/V8__add_doctor_audit_columns.sql
Line: 48
```

**Root Cause:** 
The V8 migration script was trying to create indexes on `deleted_at` and other audit columns in the `doctors` and `patients` tables, but these columns didn't exist yet. The migration was attempting to create indexes before adding the columns.

## Solution Applied

The `V8__add_doctor_audit_columns.sql` migration has been **fixed** to:
1. First add all missing audit columns to both `patients` and `doctors` tables
2. Then create indexes on those columns

### Columns Added:
- `created_at` (DATETIME)
- `updated_at` (DATETIME)
- `created_by` (VARCHAR(50))
- `updated_by` (VARCHAR(50))
- `deleted_at` (DATETIME) - **This was missing and causing the error**
- `deleted_by` (VARCHAR(50))

## How to Fix Your Database

You have **two options** depending on whether you need to keep existing data:

---

### Option 1: Clean Reset (Recommended for Development)

This will **delete all data** but gives you a fresh start with all migrations running correctly.

#### Steps:

1. **Stop the Spring Boot application** if it's running

2. **Run the reset script:**
   ```bash
   cd hospitalManagementSystem/hospital
   mysql -u root -p < reset_database.sql
   ```
   
   Or manually in MySQL:
   ```sql
   DROP DATABASE IF EXISTS hospital_db;
   CREATE DATABASE hospital_db;
   ```

3. **Start the Spring Boot application:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verify:** 
   - All Flyway migrations should run successfully
   - DataLoader will seed test data automatically
   - Check logs for "Data Seeding Completed Successfully!"

---

### Option 2: Repair Without Data Loss

This keeps your existing data but manually fixes the failed migration.

#### Steps:

1. **Stop the Spring Boot application** if it's running

2. **Run the repair script:**
   ```bash
   cd hospitalManagementSystem/hospital
   mysql -u root -p hospital_db < repair_flyway.sql
   ```

3. **Start the Spring Boot application:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Verify:**
   - Check logs to ensure V8 migration completes successfully
   - Application should start without errors

---

## Verification Steps

After applying either fix, verify the migration succeeded:

### 1. Check Flyway History
```sql
USE hospital_db;
SELECT version, description, success, installed_on 
FROM flyway_schema_history 
ORDER BY version;
```

You should see V8 with `success = 1`.

### 2. Check Column Existence
```sql
-- Check patients table
DESCRIBE patients;

-- Check doctors table
DESCRIBE doctors;
```

Both tables should have these audit columns:
- `created_at`
- `updated_at`
- `created_by`
- `updated_by`
- `deleted_at`
- `deleted_by`

### 3. Check Indexes
```sql
-- Check patients indexes
SHOW INDEX FROM patients WHERE Key_name LIKE 'idx_patient_%';

-- Check doctors indexes
SHOW INDEX FROM doctors WHERE Key_name LIKE 'idx_doctor_%';
```

You should see:
- `idx_patient_created_at`
- `idx_patient_deleted_at`
- `idx_doctor_created_at`
- `idx_doctor_deleted_at`

---

## What Changed in V8 Migration

### Before (Broken):
The script tried to create indexes on columns that didn't exist:
```sql
CREATE INDEX idx_patient_deleted_at ON patients(deleted_at);  -- FAILS: deleted_at doesn't exist!
```

### After (Fixed):
The script now:
1. Adds the `deleted_at` column (and all other audit columns)
2. Then creates indexes on those columns

All changes are **idempotent** (safe to run multiple times).

---

## Troubleshooting

### If you still see errors after applying the fix:

1. **Check MySQL version compatibility:**
   ```bash
   mysql --version
   ```
   Should be MySQL 5.7+ or 8.0+

2. **Check for locked tables:**
   ```sql
   SHOW OPEN TABLES WHERE In_use > 0;
   ```

3. **Check application.properties:**
   Ensure Flyway is enabled:
   ```properties
   spring.flyway.enabled=true
   spring.flyway.baseline-on-migrate=true
   ```

4. **View detailed Flyway logs:**
   Add to `application.properties`:
   ```properties
   logging.level.org.flywaydb=DEBUG
   ```

5. **Manual Flyway repair (last resort):**
   ```bash
   ./mvnw flyway:repair
   ```

---

## Prevention for Future Migrations

When creating new Flyway migrations:

1. **Always add columns before creating indexes/constraints on them**
2. **Make migrations idempotent** (use `IF NOT EXISTS` checks)
3. **Test migrations on a copy of production data** before deploying
4. **Use conditional SQL** for better compatibility:
   ```sql
   SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS ...);
   SET @sql = IF(@col_exists = 0, 'ALTER TABLE ...', 'SELECT ...');
   PREPARE stmt FROM @sql;
   EXECUTE stmt;
   DEALLOCATE PREPARE stmt;
   ```

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

# Maven clean build
./mvnw clean install

# Flyway repair command
./mvnw flyway:repair
```

---

## Contact & Support

If you continue to experience issues after following this guide:
1. Check the application logs in `target/` or console output
2. Verify database connectivity: `mysql -u root -p hospital_db`
3. Ensure no other applications are using port 8080 or accessing the database

The migration has been fixed and should work correctly now. Choose Option 1 (reset) for the cleanest solution in development.