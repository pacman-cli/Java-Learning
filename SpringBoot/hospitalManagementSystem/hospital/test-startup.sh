#!/bin/bash

# Hospital Management System - Test Startup Script
# This script tests if the application starts successfully with Hibernate auto-create

echo "=========================================="
echo "Hospital Management System - Test Startup"
echo "=========================================="
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if MySQL is running
echo "1️⃣  Checking MySQL connection..."
if mysql -u root -pMdAshikur123+ -e "SELECT 1" &> /dev/null; then
    echo -e "${GREEN}✅ MySQL is running${NC}"
else
    echo -e "${RED}❌ MySQL is not running or credentials are incorrect${NC}"
    echo "Please start MySQL and try again"
    exit 1
fi

# Check if database exists
echo ""
echo "2️⃣  Checking database..."
DB_EXISTS=$(mysql -u root -pMdAshikur123+ -e "SHOW DATABASES LIKE 'hospital_db'" | grep hospital_db)
if [ -z "$DB_EXISTS" ]; then
    echo -e "${YELLOW}⚠️  Database 'hospital_db' not found. Creating...${NC}"
    mysql -u root -pMdAshikur123+ -e "CREATE DATABASE hospital_db"
    echo -e "${GREEN}✅ Database created${NC}"
else
    echo -e "${GREEN}✅ Database 'hospital_db' exists${NC}"
fi

# Show current configuration
echo ""
echo "3️⃣  Current Configuration:"
echo "   - Flyway: DISABLED"
echo "   - Hibernate DDL: create (auto-creates tables)"
echo "   - Data Seeding: ENABLED"
echo ""

# Offer to drop existing tables
echo "4️⃣  Database cleanup..."
echo -e "${YELLOW}Do you want to drop all existing tables for a fresh start? (y/N)${NC}"
read -r response
if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    echo "Dropping all tables..."
    mysql -u root -pMdAshikur123+ hospital_db -e "
        SET FOREIGN_KEY_CHECKS = 0;
        DROP TABLE IF EXISTS appointments;
        DROP TABLE IF EXISTS billings;
        DROP TABLE IF EXISTS doctor_working_days;
        DROP TABLE IF EXISTS doctors;
        DROP TABLE IF EXISTS invoices;
        DROP TABLE IF EXISTS lab_orders;
        DROP TABLE IF EXISTS lab_tests;
        DROP TABLE IF EXISTS medical_documents;
        DROP TABLE IF EXISTS medical_records;
        DROP TABLE IF EXISTS medicines;
        DROP TABLE IF EXISTS patients;
        DROP TABLE IF EXISTS payments;
        DROP TABLE IF EXISTS prescription_medicines;
        DROP TABLE IF EXISTS prescriptions;
        DROP TABLE IF EXISTS roles;
        DROP TABLE IF EXISTS user_roles;
        DROP TABLE IF EXISTS users;
        DROP TABLE IF EXISTS flyway_schema_history;
        SET FOREIGN_KEY_CHECKS = 1;
    " 2>/dev/null
    echo -e "${GREEN}✅ All tables dropped${NC}"
else
    echo "Skipping table cleanup..."
fi

echo ""
echo "5️⃣  Starting application..."
echo -e "${YELLOW}Press Ctrl+C to stop the application${NC}"
echo ""
echo "=========================================="
echo ""

# Start the application
./mvnw spring-boot:run

# This part runs after the app is stopped
echo ""
echo "=========================================="
echo "Application stopped"
echo "=========================================="
