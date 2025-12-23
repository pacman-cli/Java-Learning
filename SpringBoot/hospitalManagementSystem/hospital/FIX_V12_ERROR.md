# Fix V12 Migration Syntax Error

## Error Message
```
You have an error in your SQL syntax near 'IF NOT EXISTS idx_doctor_working_days_doctor_id'
Migration V12__create_doctor_working_days_table.sql failed
Line: 12
```

## Problem
MySQL doesn't support `CREATE INDEX IF NOT EXISTS` syntax. The V12 migration used this unsupported syntax.

## Solution Applied
✅ V12 migration has been **FIXED** to use proper MySQL conditional syntax with INFORMATION_SCHEMA checks.

---

## Quick Fix Steps

### Option 1: Clean Reset (Fastest - 30 seconds)
```bash
cd /Users/puspo/JavaCourse/SpringBoot/hospitalManagementSystem/hospital

# Reset database
mysql -u root -p < reset_database.sql

# Start application
./mvnw spring-boot:run
```

### Option 2: Repair Without Data Loss (45 seconds)
```bash
cd /Users/puspo/JavaCourse/SpringBoot/hospitalManagementSystem/hospital

# Clean up failed migration
mysql -u root -p hospital_db < fix_v12_migration.sql

# OR use comprehensive repair
mysql -u root -p hospital_db < repair_flyway.sql

# Start application
./mvnw spring-boot:run
```

### Option 3: Manual Fix (If you prefer manual steps)
```sql
-- Connect to MySQL
mysql -u root -p hospital_db

-- Remove failed migration record
DELETE FROM flyway_schema_history WHERE version = '12' AND success = 0;

-- Drop the partially created table
DROP TABLE IF EXISTS doctor_working_days;

-- Exit MySQL
exit;

-- Restart application
./mvnw spring-boot:run
```

---

## What Was Fixed in V12

**Before (Broken):**
```sql
CREATE INDEX IF NOT EXISTS idx_doctor_working_days_doctor_id ON doctor_working_days(doctor_id);
```

**After (Fixed):**
```sql
SET @index_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS...);
SET @sql = IF(@index_exists = 0, 'CREATE INDEX ...', 'SELECT ...');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
```

---

## Verification

After restart, check logs for:
```
✓ Migrating schema `hospital_db` to version "12 - create doctor working days table"
✓ Successfully applied migrations
✓ Data Seeding Completed Successfully!
✓ Started HospitalApplication
```

---

## Files Available

- `fix_v12_migration.sql` - Quick fix for V12 only
- `repair_flyway.sql` - Comprehensive fix (V8, V11, V12)
- `reset_database.sql` - Clean reset (deletes all data)

---

## Summary

The V12 migration is now fixed with proper MySQL-compatible syntax. Just run **Option 1** for the cleanest solution!

**Time to fix:** < 1 minute
**Status:** 🟢 READY TO GO