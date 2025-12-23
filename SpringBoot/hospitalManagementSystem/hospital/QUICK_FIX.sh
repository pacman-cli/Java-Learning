#!/bin/bash

# Quick Fix Script for Hospital Management System Database Issues
# This script provides easy commands to fix all database schema problems

echo "=============================================="
echo "Hospital Management System - Database Fix"
echo "=============================================="
echo ""
echo "This will fix all database schema issues:"
echo "  - V8: Missing audit columns"
echo "  - V11: Missing insurance columns in billings"
echo "  - V12: Missing doctor_working_days table"
echo "  - V13: Missing doctor entity columns"
echo ""
echo "⚠️  WARNING: This will DELETE ALL DATA in hospital_db"
echo ""
read -p "Are you sure you want to continue? (yes/no): " confirm

if [ "$confirm" != "yes" ]; then
    echo "Aborted."
    exit 1
fi

echo ""
echo "Step 1: Resetting database..."
mysql -u root -p << EOF
DROP DATABASE IF EXISTS hospital_db;
CREATE DATABASE hospital_db;
USE hospital_db;
SELECT 'Database reset complete!' AS status;
EOF

if [ $? -ne 0 ]; then
    echo "❌ Database reset failed!"
    exit 1
fi

echo ""
echo "✅ Database reset successful!"
echo ""
echo "Step 2: Starting Spring Boot application..."
echo "This will run all migrations (V1-V13) and seed test data"
echo ""

./mvnw spring-boot:run

echo ""
echo "=============================================="
echo "Done! Check logs above for:"
echo "  ✓ Migration success messages"
echo "  ✓ Data Seeding Completed Successfully!"
echo "  ✓ Started HospitalApplication"
echo ""
echo "Visit: http://localhost:8080"
echo "Login: patient1 / password123"
echo "=============================================="
