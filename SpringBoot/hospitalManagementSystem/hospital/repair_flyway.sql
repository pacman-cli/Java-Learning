-- Flyway Repair Script for Hospital Management System
-- This script repairs failed migrations without losing data
-- Use this if you want to keep your existing data

-- Step 1: Remove failed migration records from Flyway history
DELETE FROM flyway_schema_history WHERE version = '8' AND success = 0;
DELETE FROM flyway_schema_history WHERE version = '12' AND success = 0;

-- Step 2: Manually add the missing columns to patients table (if they don't exist)

-- Check and add deleted_at to patients
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'patients'
                   AND COLUMN_NAME = 'deleted_at');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE patients ADD COLUMN deleted_at DATETIME NULL',
    'SELECT ''Column deleted_at already exists in patients table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add deleted_by to patients
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'patients'
                   AND COLUMN_NAME = 'deleted_by');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE patients ADD COLUMN deleted_by VARCHAR(50) NULL',
    'SELECT ''Column deleted_by already exists in patients table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add created_by to patients
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'patients'
                   AND COLUMN_NAME = 'created_by');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE patients ADD COLUMN created_by VARCHAR(50) NULL',
    'SELECT ''Column created_by already exists in patients table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add updated_by to patients
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'patients'
                   AND COLUMN_NAME = 'updated_by');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE patients ADD COLUMN updated_by VARCHAR(50) NULL',
    'SELECT ''Column updated_by already exists in patients table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add updated_at to patients
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'patients'
                   AND COLUMN_NAME = 'updated_at');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE patients ADD COLUMN updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP',
    'SELECT ''Column updated_at already exists in patients table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 3: Manually add the missing columns to doctors table (if they don't exist)

-- Check and add deleted_at to doctors
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'doctors'
                   AND COLUMN_NAME = 'deleted_at');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE doctors ADD COLUMN deleted_at DATETIME NULL',
    'SELECT ''Column deleted_at already exists in doctors table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add deleted_by to doctors
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'doctors'
                   AND COLUMN_NAME = 'deleted_by');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE doctors ADD COLUMN deleted_by VARCHAR(50) NULL',
    'SELECT ''Column deleted_by already exists in doctors table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add created_at to doctors
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'doctors'
                   AND COLUMN_NAME = 'created_at');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE doctors ADD COLUMN created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP',
    'SELECT ''Column created_at already exists in doctors table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add created_by to doctors
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'doctors'
                   AND COLUMN_NAME = 'created_by');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE doctors ADD COLUMN created_by VARCHAR(50) NULL',
    'SELECT ''Column created_by already exists in doctors table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add updated_at to doctors
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'doctors'
                   AND COLUMN_NAME = 'updated_at');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE doctors ADD COLUMN updated_at DATETIME NULL ON UPDATE CURRENT_TIMESTAMP',
    'SELECT ''Column updated_at already exists in doctors table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add updated_by to doctors
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'doctors'
                   AND COLUMN_NAME = 'updated_by');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE doctors ADD COLUMN updated_by VARCHAR(50) NULL',
    'SELECT ''Column updated_by already exists in doctors table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 4: Clean up and recreate doctor_working_days table if needed
DROP TABLE IF EXISTS doctor_working_days;

CREATE TABLE IF NOT EXISTS doctor_working_days
(
    doctor_id   BIGINT       NOT NULL,
    working_day VARCHAR(20)  NOT NULL,
    CONSTRAINT FK_DOCTOR_WORKING_DAYS_ON_DOCTOR FOREIGN KEY (doctor_id) REFERENCES doctors (id) ON DELETE CASCADE
);

-- Add indexes for doctor_working_days
SET @index_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
                     WHERE TABLE_SCHEMA = 'hospital_db'
                     AND TABLE_NAME = 'doctor_working_days'
                     AND INDEX_NAME = 'idx_doctor_working_days_doctor_id');

SET @sql = IF(@index_exists = 0,
    'CREATE INDEX idx_doctor_working_days_doctor_id ON doctor_working_days(doctor_id)',
    'SELECT ''Index idx_doctor_working_days_doctor_id already exists''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @index_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.STATISTICS
                     WHERE TABLE_SCHEMA = 'hospital_db'
                     AND TABLE_NAME = 'doctor_working_days'
                     AND INDEX_NAME = 'idx_doctor_working_days_doctor_day');

SET @sql = IF(@index_exists = 0,
    'CREATE INDEX idx_doctor_working_days_doctor_day ON doctor_working_days(doctor_id, working_day)',
    'SELECT ''Index idx_doctor_working_days_doctor_day already exists''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 5: Add missing insurance columns to billings table (V11 migration content)

-- Check and add insurance_id to billings
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'billings'
                   AND COLUMN_NAME = 'insurance_id');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE billings ADD COLUMN insurance_id BIGINT NULL',
    'SELECT ''Column insurance_id already exists in billings table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add covered_amount to billings
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'billings'
                   AND COLUMN_NAME = 'covered_amount');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE billings ADD COLUMN covered_amount DECIMAL(38, 2) NULL',
    'SELECT ''Column covered_amount already exists in billings table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add patient_payable to billings
SET @col_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS
                   WHERE TABLE_SCHEMA = 'hospital_db'
                   AND TABLE_NAME = 'billings'
                   AND COLUMN_NAME = 'patient_payable');

SET @sql = IF(@col_exists = 0,
    'ALTER TABLE billings ADD COLUMN patient_payable DECIMAL(38, 2) NULL',
    'SELECT ''Column patient_payable already exists in billings table''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Check and add foreign key for insurance_id
SET @fk_exists = (SELECT COUNT(*) FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
                  WHERE TABLE_SCHEMA = 'hospital_db'
                  AND TABLE_NAME = 'billings'
                  AND CONSTRAINT_NAME = 'FK_BILLINGS_ON_INSURANCE');

SET @sql = IF(@fk_exists = 0,
    'ALTER TABLE billings ADD CONSTRAINT FK_BILLINGS_ON_INSURANCE FOREIGN KEY (insurance_id) REFERENCES insurance (id)',
    'SELECT ''Foreign key FK_BILLINGS_ON_INSURANCE already exists''');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 6: Now V8, V11, V12, and V13 migrations can run successfully on next application start
-- Restart your Spring Boot application

SELECT 'Flyway repair completed successfully (V8, V11, V12 fixed). Restart your application.' AS status;
