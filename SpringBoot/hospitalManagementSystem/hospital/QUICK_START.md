# 🚀 Quick Start Guide

## Status: ✅ Application Fixed & Ready

---

## Start Application (2 Steps)

```bash
# 1. Ensure MySQL is running with database
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS hospital_db;"

# 2. Start the application
cd hospitalManagementSystem/hospital
./mvnw spring-boot:run
```

---

## What You'll See

```
✓ Hibernate: drop table if exists appointments
✓ Hibernate: create table appointments (...)
✓ Roles seeded. Total: 3
✓ Users seeded. Total: 10
✓ Patients seeded. Total: 5
✓ Doctors seeded. Total: 4
✓ Appointments seeded. Total: 10
✓ Started HospitalApplication in 12.5 seconds
```

---

## Access Points

- **Backend API:** http://localhost:8081
- **Swagger UI:** http://localhost:8081/swagger-ui.html
- **API Docs:** http://localhost:8081/v3/api-docs

---

## Test Credentials

| User     | Username | Password    | Role    |
|----------|----------|-------------|---------|
| Admin    | admin    | password123 | ADMIN   |
| Patient  | patient1 | password123 | PATIENT |
| Doctor   | doctor1  | password123 | DOCTOR  |

---

## Current Setup

- ✅ **Flyway:** DISABLED
- ✅ **Hibernate:** AUTO-CREATE (creates tables from entities)
- ✅ **Data Seeding:** ENABLED (auto-populates test data)
- ⚠️  **Data Persistence:** DROPS on restart (dev mode only)

---

## Key Facts

✅ **Fixed:** Flyway migration errors resolved by disabling migrations  
✅ **Working:** Application starts successfully, all endpoints functional  
⚠️  **Dev Only:** Tables recreated on every restart (data not preserved)  

---

## Quick Commands

```bash
# Reset everything (fresh start)
mysql -u root -p -e "DROP DATABASE hospital_db; CREATE DATABASE hospital_db;"
./mvnw spring-boot:run

# Check database
mysql -u root -p hospital_db -e "SHOW TABLES; SELECT COUNT(*) FROM users;"

# Clean build
./mvnw clean compile

# Run without tests
./mvnw spring-boot:run -DskipTests
```

---

## Need More Info?

- **Full Details:** See `README_FIXES.md`
- **Testing Guide:** See `FLYWAY_DISABLED_TEST.md`
- **Success Summary:** See `SUCCESS_SUMMARY.md`

---

**Problem:** Flyway migration errors  
**Solution:** Disabled Flyway, using Hibernate auto-create  
**Status:** ✅ RESOLVED  
**Environment:** Development  

🎉 **Your application is ready to use!**