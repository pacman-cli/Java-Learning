-- Fix Flyway schema history for V8 migration
-- All columns already exist, just clean up the failed migration record and add indexes

-- Delete the failed migration record
DELETE FROM flyway_schema_history WHERE version = '8';

-- Add indexes that may be missing
CREATE INDEX IF NOT EXISTS idx_patient_created_at ON patients(created_at);
CREATE INDEX IF NOT EXISTS idx_patient_deleted_at ON patients(deleted_at);

CREATE INDEX IF NOT EXISTS idx_doctor_created_at ON doctors(created_at);
CREATE INDEX IF NOT EXISTS idx_doctor_deleted_at ON doctors(deleted_at);

-- Verify the fix
SELECT version, description, success FROM flyway_schema_history ORDER BY installed_rank;
