-- Fix V12 migration failure
-- This script cleans up the failed migration and allows it to run again

USE hospital_db;

-- Step 1: Remove the failed V12 migration record from Flyway history
DELETE FROM flyway_schema_history WHERE version = '12' AND success = 0;

-- Step 2: Drop the doctor_working_days table if it exists (it was created but indexes failed)
DROP TABLE IF EXISTS doctor_working_days;

-- Step 3: The V12 migration will now run successfully on next application start
SELECT 'V12 migration cleanup completed. Restart your application.' AS status;
