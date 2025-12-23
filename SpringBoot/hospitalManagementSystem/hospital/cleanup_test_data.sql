-- Cleanup test data script
-- This removes all seed data added by V9 migration

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Clear lab orders created by seed data
DELETE FROM lab_orders WHERE id >= 4;

-- Clear appointments created by seed data (keep first 4)
DELETE FROM appointments WHERE id >= 5;

-- Clear lab tests created by seed data (keep first 3)
DELETE FROM lab_tests WHERE id > 3;

-- Clear patients created by seed data (keep first 8)
DELETE FROM patients WHERE id > 8;

-- Clear doctors created by seed data (keep first 3)
DELETE FROM doctors WHERE id > 3;

-- Clear user roles for seed users
DELETE FROM user_roles WHERE user_id IN (
    SELECT id FROM users WHERE
    username IN ('dr.smith', 'dr.johnson', 'dr.williams',
                 'patient.james', 'patient.mary', 'patient.robert',
                 'patient.patricia', 'patient.william', 'patient.jennifer',
                 'admin.user')
);

-- Clear seed users
DELETE FROM users WHERE username IN (
    'dr.smith', 'dr.johnson', 'dr.williams',
    'patient.james', 'patient.mary', 'patient.robert',
    'patient.patricia', 'patient.william', 'patient.jennifer',
    'admin.user'
);

-- Clear seed roles (keep first 5 if they exist)
DELETE FROM roles WHERE id > 5;

-- Remove failed V9 migration record if exists
DELETE FROM flyway_schema_history WHERE version = '9';

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Test data cleaned up successfully' as Status;
