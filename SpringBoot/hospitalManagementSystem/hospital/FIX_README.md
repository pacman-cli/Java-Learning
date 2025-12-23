# Quick Fix for Flyway Migration Error

## Problem
The application fails to start with errors:
```
Key column 'deleted_at' doesn't exist in table
Migration V8__add_doctor_audit_columns.sql failed

AND/OR

Schema-validation: missing column [covered_amount] in table [billings]

AND/OR

Schema-validation: missing table [doctor_working_days]

AND/OR

Schema-validation: missing table [medical_documents]
```

## Solution
**Five fixes have been applied:**
1. V8 migration - FIXED (adds audit columns before creating indexes)
2. V11 migration - NEW (adds missing insurance columns to billings table)
3. V12 migration - NEW (creates doctor_working_days table)
4. V13 migration - NEW (adds all missing columns to doctors table)
5. V14 migration - NEW (creates medical_documents table)

Now you need to reset your database.

---

## Quick Fix (Choose ONE option)

### Option A: Fresh Start (RECOMMENDED - Fastest)
This deletes all data and starts clean:

```bash
# 1. Stop the application (Ctrl+C if running)

# 2. Reset the database
mysql -u root -p < reset_database.sql

# 3. Start the application
./mvnw spring-boot:run
```

**Done!** The app will start with fresh test data.

---

### Option B: Keep Your Data
This preserves existing data:

```bash
# 1. Stop the application (Ctrl+C if running)

# 2. Run the repair script
mysql -u root -p hospital_db < repair_flyway.sql

# 3. Start the application
./mvnw spring-boot:run
```

---

## What Was Fixed?

### Fix #1: V8 Migration
The V8 migration was trying to create indexes on columns that didn't exist yet.

**Before (broken):**
- Tried to create index on `deleted_at` column
- But `deleted_at` column didn't exist → ERROR

**After (fixed):**
- First adds `deleted_at` and other audit columns to `patients` and `doctors` tables
- Then creates indexes on those columns → SUCCESS

### Fix #2: V11 Migration (NEW)
The `Billing` entity has insurance-related fields but the database was missing these columns.

**Added columns to billings table:**
- `insurance_id` - Foreign key to insurance table
- `covered_amount` - Amount covered by insurance
- `patient_payable` - Amount patient needs to pay

### Fix #3: V12 Migration (NEW)
The `Doctor` entity has `@ElementCollection` for working days but the table was missing.

**Created table:**
- `doctor_working_days` - Stores which days each doctor works
- Includes proper foreign key and indexes

### Fix #4: V13 Migration (NEW)
The `Doctor` entity has many additional fields but the database was missing them.

**Added 19 columns to doctors table:**
- `email`, `license_number`, `license_expiry_date`
- `department`, `years_of_experience`, `date_of_birth`
- `address`, `emergency_contact`, `emergency_contact_name`
- `salary`, `consultation_fee`
- `start_time`, `end_time`
- `is_available`, `is_active`
- `bio`, `languages_spoken`, `awards_certifications`
- `profile_image_url`

### Fix #5: V14 Migration (NEW)
The `Document` entity exists but the `medical_documents` table was never created.

**Created table:**
- `medical_documents` - Complete table with all Document entity fields
- Includes foreign keys to patients, doctors, and appointments
- Added 7 indexes for performance

**Columns in medical_documents:**
- File info: `file_name`, `original_file_name`, `file_path`, `file_size`, `content_type`
- Document data: `document_type`, `title`, `description`, `document_date`
- Security: `is_confidential`, `access_level`, `checksum`
- Metadata: `tags`, `version`
- Relationships: `patient_id`, `doctor_id`, `related_appointment_id`
- Audit: `created_at`, `updated_at`, `created_by`, `updated_by`, `deleted_at`, `deleted_by`

---

## Verify It Works

After starting the app, check the logs for:
```
✓ Migrating schema `hospital_db` to version "8 - add doctor audit columns"
✓ Migrating schema `hospital_db` to version "11 - add insurance columns to billings"
✓ Migrating schema `hospital_db` to version "12 - create doctor working days table"
✓ Migrating schema `hospital_db` to version "13 - add missing doctor columns"
✓ Migrating schema `hospital_db` to version "14 - create medical documents table"
✓ Successfully applied X migrations
✓ Data Seeding Completed Successfully!
```

---

## Test Credentials (after reset)
- **Admin:** `admin` / `password123`
- **Patient:** `patient1` / `password123`
- **Doctor:** `doctor1` / `password123`

---

## Need More Details?
See `FLYWAY_FIX_GUIDE.md` for comprehensive troubleshooting.

---

**Quick Summary:**
1. Migration V8 is now fixed ✓
2. Migration V11 has been created ✓
3. Migration V12 has been created ✓
4. Migration V13 has been created ✓
5. Migration V14 has been created ✓
6. Run `reset_database.sql` to start fresh
7. Restart the app
8. All migrations (V1-V14) will run successfully!