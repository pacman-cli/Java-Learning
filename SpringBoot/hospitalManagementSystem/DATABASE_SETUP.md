# 🗄️ Database Setup Guide

Complete guide for setting up the MySQL database for the Hospital Management System.

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [MySQL Installation](#mysql-installation)
3. [Database Creation](#database-creation)
4. [User Configuration](#user-configuration)
5. [Database Schema](#database-schema)
6. [Flyway Migrations](#flyway-migrations)
7. [Sample Data](#sample-data)
8. [Troubleshooting](#troubleshooting)
9. [Backup and Restore](#backup-and-restore)
10. [Production Setup](#production-setup)

---

## Prerequisites

Before setting up the database, ensure you have:

- **MySQL 8.0+** installed
- **MySQL Client** (command-line or GUI tool like MySQL Workbench)
- **Database credentials** (username and password)
- **Appropriate permissions** to create databases and users

### System Requirements

| Component | Minimum | Recommended |
|-----------|---------|-------------|
| MySQL Version | 8.0 | 8.0+ |
| RAM | 2GB | 4GB+ |
| Storage | 5GB | 10GB+ |
| CPU | 2 cores | 4+ cores |

---

## MySQL Installation

### macOS

```bash
# Using Homebrew
brew install mysql@8.0

# Start MySQL service
brew services start mysql@8.0

# Secure MySQL installation
mysql_secure_installation
```

### Linux (Ubuntu/Debian)

```bash
# Update package index
sudo apt update

# Install MySQL Server
sudo apt install mysql-server

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure MySQL installation
sudo mysql_secure_installation
```

### Windows

1. Download MySQL Installer from [MySQL Downloads](https://dev.mysql.com/downloads/installer/)
2. Run the installer and select "MySQL Server"
3. Follow the installation wizard
4. Configure MySQL as a Windows Service
5. Set root password during installation

### Docker (Alternative)

```bash
# Pull MySQL 8.0 image
docker pull mysql:8.0

# Run MySQL container
docker run --name hospital-mysql \
  -e MYSQL_ROOT_PASSWORD=your_root_password \
  -e MYSQL_DATABASE=hospital_db \
  -e MYSQL_USER=hospital_user \
  -e MYSQL_PASSWORD=your_password \
  -p 3306:3306 \
  -v hospital_mysql_data:/var/lib/mysql \
  -d mysql:8.0

# Verify container is running
docker ps
```

---

## Database Creation

### Method 1: MySQL Command Line

```bash
# Connect to MySQL as root
mysql -u root -p

# Enter your root password when prompted
```

```sql
-- Create the database
CREATE DATABASE hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Verify database creation
SHOW DATABASES;

-- Use the database
USE hospital_db;
```

### Method 2: MySQL Workbench

1. Open MySQL Workbench
2. Connect to your MySQL server
3. Click on **"Create a new schema"** icon
4. Enter Schema Name: `hospital_db`
5. Select Character Set: `utf8mb4`
6. Select Collation: `utf8mb4_unicode_ci`
7. Click **Apply**

### Method 3: Automated Script

Create a file named `create_database.sql`:

```sql
-- create_database.sql
DROP DATABASE IF EXISTS hospital_db;
CREATE DATABASE hospital_db 
  CHARACTER SET utf8mb4 
  COLLATE utf8mb4_unicode_ci;

-- Display success message
SELECT 'Database hospital_db created successfully!' AS Result;
```

Execute the script:

```bash
mysql -u root -p < create_database.sql
```

---

## User Configuration

### Create Database User

```sql
-- Connect as root
mysql -u root -p
```

```sql
-- Create a dedicated user for the application
CREATE USER 'hospital_user'@'localhost' IDENTIFIED BY 'StrongPassword123!';

-- Grant all privileges on hospital_db to the user
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'localhost';

-- If accessing from a different host (e.g., Docker)
CREATE USER 'hospital_user'@'%' IDENTIFIED BY 'StrongPassword123!';
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'%';

-- Flush privileges to apply changes
FLUSH PRIVILEGES;

-- Verify user creation
SELECT User, Host FROM mysql.user WHERE User = 'hospital_user';
```

### Security Best Practices

1. **Use Strong Passwords**: Minimum 12 characters with uppercase, lowercase, numbers, and special characters
2. **Limit Host Access**: Use `'localhost'` instead of `'%'` when possible
3. **Grant Minimum Privileges**: Only grant necessary permissions
4. **Regular Password Rotation**: Change passwords periodically

### Recommended User Setup

```sql
-- Production user with restricted privileges
CREATE USER 'hospital_app'@'localhost' IDENTIFIED BY 'SecurePassword123!@#';

GRANT SELECT, INSERT, UPDATE, DELETE ON hospital_db.* TO 'hospital_app'@'localhost';
GRANT CREATE, ALTER, INDEX ON hospital_db.* TO 'hospital_app'@'localhost';
GRANT REFERENCES ON hospital_db.* TO 'hospital_app'@'localhost';

FLUSH PRIVILEGES;
```

---

## Database Schema

The Hospital Management System uses the following database structure:

### Core Tables

#### 1. Users and Authentication

```sql
-- users: System users (doctors, patients, staff)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- roles: User roles (ADMIN, DOCTOR, PATIENT, etc.)
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- user_roles: Many-to-many relationship
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
```

#### 2. Medical Entities

```sql
-- patients: Patient information
CREATE TABLE patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    contact_info VARCHAR(255),
    address VARCHAR(500),
    emergency_contact VARCHAR(255),
    blood_group VARCHAR(5),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- doctors: Doctor information
CREATE TABLE doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    specialization VARCHAR(255) NOT NULL,
    qualifications VARCHAR(255),
    contact_info VARCHAR(255),
    daily_schedule VARCHAR(2000),
    license_number VARCHAR(100) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- appointments: Appointment scheduling
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_datetime DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    reason VARCHAR(500),
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id)
);

-- medical_documents: Document management
CREATE TABLE medical_documents (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    document_type VARCHAR(50) NOT NULL,
    upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    uploaded_by BIGINT,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (uploaded_by) REFERENCES users(id)
);
```

#### 3. Billing and Payments

```sql
-- billings: Billing information
CREATE TABLE billings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(10, 2) NOT NULL,
    billing_date DATETIME NOT NULL,
    payment_method VARCHAR(50),
    status VARCHAR(50),
    description VARCHAR(1000),
    paid_at DATETIME,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- invoices: Invoice management
CREATE TABLE invoices (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_number VARCHAR(50) UNIQUE NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT,
    total_amount DECIMAL(10, 2) NOT NULL,
    paid_amount DECIMAL(10, 2) DEFAULT 0,
    status VARCHAR(50) NOT NULL,
    issue_date DATETIME NOT NULL,
    due_date DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- payments: Payment records
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    invoice_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_date DATETIME NOT NULL,
    transaction_id VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id)
);
```

#### 4. Laboratory and Prescriptions

```sql
-- lab_tests: Laboratory test definitions
CREATE TABLE lab_tests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    test_name VARCHAR(255) NOT NULL,
    test_code VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(1000),
    price DECIMAL(10, 2),
    normal_range VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- lab_orders: Laboratory test orders
CREATE TABLE lab_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lab_test_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    appointment_id BIGINT,
    ordered_at DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,
    report_path VARCHAR(2000),
    notes VARCHAR(1000),
    FOREIGN KEY (lab_test_id) REFERENCES lab_tests(id),
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- prescriptions: Medication prescriptions
CREATE TABLE prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_id BIGINT,
    medication_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(100) NOT NULL,
    frequency VARCHAR(100) NOT NULL,
    duration VARCHAR(100) NOT NULL,
    instructions VARCHAR(1000),
    prescribed_date DATETIME NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patients(id),
    FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);
```

---

## Flyway Migrations

The application uses **Flyway** for database version control and migrations.

### How Flyway Works

1. **Version-based migrations**: Files are named `V{version}__{description}.sql`
2. **Automatic execution**: Migrations run automatically on application startup
3. **Version tracking**: Flyway tracks applied migrations in `flyway_schema_history` table
4. **Idempotent**: Same migrations won't run twice

### Migration Files Location

```
hospital/src/main/resources/db/migration/
├── V1__alter_appointment_bill_doctor_patient_insurance_lab_medicalRec_medicine_prescriptions.sql
├── V2__alter_lab_ord_&_Create_User_Tables.sql
├── V3__insurance.sql
├── V4__invoice.sql
├── V5__alter_table_invoice_status.sql
└── V6__Payments.sql
```

### Flyway Configuration

In `application.properties`:

```properties
# Flyway settings
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.validate-on-migrate=false
spring.flyway.ignore-missing-migrations=true
```

### Manual Flyway Execution

If you need to run migrations manually:

```bash
cd hospital

# Run migrations
./mvnw flyway:migrate

# View migration status
./mvnw flyway:info

# Clean database (WARNING: Deletes all data)
./mvnw flyway:clean

# Repair migration metadata
./mvnw flyway:repair
```

### Verify Flyway Migrations

```sql
-- Connect to database
mysql -u hospital_user -p hospital_db

-- Check migration history
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

---

## Sample Data

### Initial Roles

```sql
INSERT INTO roles (name) VALUES 
('ROLE_ADMIN'),
('ROLE_DOCTOR'),
('ROLE_PATIENT'),
('ROLE_NURSE'),
('ROLE_RECEPTIONIST'),
('ROLE_BILLING_STAFF'),
('ROLE_LAB_TECHNICIAN'),
('ROLE_PHARMACIST');
```

### Sample Admin User

```sql
-- Password is 'admin123' (BCrypt hashed)
INSERT INTO users (username, password, full_name, email, enabled) VALUES
('admin', '$2a$10$XQ1qU5h8QsNj1G1K1G1K1eOOvLQJLQJLQJLQJLQJLQJLQJLQJLQJL', 'System Administrator', 'admin@hospital.com', TRUE);

-- Assign admin role
INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';
```

### Sample Doctor

```sql
INSERT INTO doctors (full_name, specialization, qualifications, contact_info, license_number) VALUES
('Dr. John Smith', 'Cardiology', 'MD, MBBS, Cardiologist', 'john.smith@hospital.com, +1-555-0101', 'DOC-001');

INSERT INTO users (username, password, full_name, email, enabled) VALUES
('doctor', '$2a$10$XQ1qU5h8QsNj1G1K1G1K1eOOvLQJLQJLQJLQJLQJLQJLQJLQJLQJL', 'Dr. John Smith', 'doctor@hospital.com', TRUE);

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'doctor' AND r.name = 'ROLE_DOCTOR';
```

### Sample Patient

```sql
INSERT INTO patients (full_name, date_of_birth, gender, contact_info, blood_group) VALUES
('Jane Doe', '1990-05-15', 'Female', 'jane.doe@email.com, +1-555-0202', 'A+');

INSERT INTO users (username, password, full_name, email, enabled) VALUES
('patient', '$2a$10$XQ1qU5h8QsNj1G1K1G1K1eOOvLQJLQJLQJLQJLQJLQJLQJLQJLQJL', 'Jane Doe', 'patient@email.com', TRUE);

INSERT INTO user_roles (user_id, role_id) 
SELECT u.id, r.id FROM users u, roles r 
WHERE u.username = 'patient' AND r.name = 'ROLE_PATIENT';
```

---

## Troubleshooting

### Common Issues and Solutions

#### 1. Cannot Connect to MySQL

**Error**: `Communications link failure`

**Solutions**:
```bash
# Check if MySQL is running
sudo systemctl status mysql  # Linux
brew services list | grep mysql  # macOS

# Start MySQL if not running
sudo systemctl start mysql  # Linux
brew services start mysql@8.0  # macOS

# Check MySQL port
netstat -an | grep 3306
```

#### 2. Access Denied for User

**Error**: `Access denied for user 'hospital_user'@'localhost'`

**Solutions**:
```sql
-- Verify user exists
SELECT User, Host FROM mysql.user WHERE User = 'hospital_user';

-- Reset user password
ALTER USER 'hospital_user'@'localhost' IDENTIFIED BY 'NewPassword123!';
FLUSH PRIVILEGES;

-- Grant privileges again
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
```

#### 3. Database Does Not Exist

**Error**: `Unknown database 'hospital_db'`

**Solutions**:
```sql
-- Create the database
CREATE DATABASE hospital_db;

-- Verify creation
SHOW DATABASES LIKE 'hospital_db';
```

#### 4. Flyway Migration Errors

**Error**: `FlywayException: Validate failed`

**Solutions**:
```bash
# Check migration status
./mvnw flyway:info

# Repair metadata (if migrations were manually modified)
./mvnw flyway:repair

# In development, you can clean and restart
./mvnw flyway:clean
./mvnw spring-boot:run
```

#### 5. Connection Pool Exhausted

**Error**: `HikariPool - Connection is not available`

**Solutions**:
```properties
# Add to application.properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

#### 6. Charset/Collation Issues

**Error**: `Incorrect string value` or encoding errors

**Solutions**:
```sql
-- Convert database to utf8mb4
ALTER DATABASE hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Convert specific table
ALTER TABLE patients CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

---

## Backup and Restore

### Backup Database

#### Full Database Backup

```bash
# Backup entire database
mysqldump -u hospital_user -p hospital_db > hospital_db_backup_$(date +%Y%m%d_%H%M%S).sql

# Backup with compression
mysqldump -u hospital_user -p hospital_db | gzip > hospital_db_backup_$(date +%Y%m%d_%H%M%S).sql.gz

# Backup structure only (no data)
mysqldump -u hospital_user -p --no-data hospital_db > hospital_db_structure.sql

# Backup data only (no structure)
mysqldump -u hospital_user -p --no-create-info hospital_db > hospital_db_data.sql
```

#### Specific Table Backup

```bash
# Backup specific tables
mysqldump -u hospital_user -p hospital_db patients doctors appointments > important_tables.sql
```

### Restore Database

#### Restore from Backup

```bash
# Restore full backup
mysql -u hospital_user -p hospital_db < hospital_db_backup_20250115_143000.sql

# Restore from compressed backup
gunzip < hospital_db_backup_20250115_143000.sql.gz | mysql -u hospital_user -p hospital_db

# Create new database and restore
mysql -u root -p -e "CREATE DATABASE hospital_db_restored;"
mysql -u hospital_user -p hospital_db_restored < hospital_db_backup_20250115_143000.sql
```

### Automated Backup Script

Create a file named `backup_database.sh`:

```bash
#!/bin/bash

# Configuration
DB_NAME="hospital_db"
DB_USER="hospital_user"
DB_PASS="your_password"
BACKUP_DIR="/path/to/backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/${DB_NAME}_${DATE}.sql.gz"

# Create backup directory if not exists
mkdir -p $BACKUP_DIR

# Create backup
mysqldump -u $DB_USER -p$DB_PASS $DB_NAME | gzip > $BACKUP_FILE

# Keep only last 7 days of backups
find $BACKUP_DIR -name "${DB_NAME}_*.sql.gz" -mtime +7 -delete

echo "Backup completed: $BACKUP_FILE"
```

Make it executable and schedule with cron:

```bash
chmod +x backup_database.sh

# Add to crontab (daily at 2 AM)
crontab -e
# Add: 0 2 * * * /path/to/backup_database.sh
```

---

## Production Setup

### Security Hardening

#### 1. Secure MySQL Configuration

Edit `/etc/mysql/my.cnf` or `/etc/my.cnf`:

```ini
[mysqld]
# Bind to localhost only (if backend is on same server)
bind-address = 127.0.0.1

# Disable remote root login
skip-networking = 0

# Enable SSL
require_secure_transport = ON
ssl-ca=/path/to/ca.pem
ssl-cert=/path/to/server-cert.pem
ssl-key=/path/to/server-key.pem

# Set max connections
max_connections = 200

# Set query cache (if needed)
query_cache_type = 1
query_cache_size = 32M

# Log slow queries
slow_query_log = 1
slow_query_log_file = /var/log/mysql/slow-query.log
long_query_time = 2
```

#### 2. Production User Setup

```sql
-- Create production user with limited privileges
CREATE USER 'hospital_prod'@'localhost' IDENTIFIED BY 'VeryStrongPassword123!@#$';

-- Grant only necessary privileges
GRANT SELECT, INSERT, UPDATE, DELETE ON hospital_db.* TO 'hospital_prod'@'localhost';

-- For migrations/updates, create separate admin user
CREATE USER 'hospital_admin'@'localhost' IDENTIFIED BY 'AnotherStrongPassword456!@#$';
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_admin'@'localhost';

FLUSH PRIVILEGES;
```

#### 3. Enable Binary Logging

```sql
-- For point-in-time recovery
SET GLOBAL binlog_format = 'ROW';
SET GLOBAL expire_logs_days = 7;
```

### Performance Optimization

#### 1. Add Indexes

```sql
-- Frequently queried columns
CREATE INDEX idx_appointments_datetime ON appointments(appointment_datetime);
CREATE INDEX idx_appointments_patient ON appointments(patient_id);
CREATE INDEX idx_appointments_doctor ON appointments(doctor_id);
CREATE INDEX idx_appointments_status ON appointments(status);

CREATE INDEX idx_patients_name ON patients(full_name);
CREATE INDEX idx_doctors_specialization ON doctors(specialization);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Composite indexes for common queries
CREATE INDEX idx_appointments_patient_date ON appointments(patient_id, appointment_datetime);
CREATE INDEX idx_appointments_doctor_date ON appointments(doctor_id, appointment_datetime);
```

#### 2. Table Optimization

```sql
-- Analyze tables
ANALYZE TABLE patients, doctors, appointments, users;

-- Optimize tables
OPTIMIZE TABLE patients, doctors, appointments, users;

-- Check table status
SHOW TABLE STATUS FROM hospital_db;
```

#### 3. Query Performance Monitoring

```sql
-- Enable performance schema
SET GLOBAL performance_schema = ON;

-- View slow queries
SELECT * FROM mysql.slow_log ORDER BY start_time DESC LIMIT 10;

-- View table locks
SHOW OPEN TABLES WHERE In_use > 0;
```

### Monitoring and Maintenance

#### 1. Database Health Check

```bash
# Check database size
mysql -u hospital_user -p -e "
SELECT 
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables
WHERE table_schema = 'hospital_db'
GROUP BY table_schema;"

# Check table sizes
mysql -u hospital_user -p -e "
SELECT 
    table_name AS 'Table',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS 'Size (MB)'
FROM information_schema.tables
WHERE table_schema = 'hospital_db'
ORDER BY (data_length + index_length) DESC;"
```

#### 2. Set Up Monitoring

```bash
# Install and configure Prometheus MySQL Exporter
# Or use MySQL Enterprise Monitor
# Or CloudWatch (AWS), Azure Monitor, Google Cloud Monitoring
```

---

## Quick Reference

### Essential Commands

```bash
# Connect to MySQL
mysql -u hospital_user -p hospital_db

# Create database
CREATE DATABASE hospital_db;

# Show databases
SHOW DATABASES;

# Use database
USE hospital_db;

# Show tables
SHOW TABLES;

# Describe table structure
DESCRIBE patients;

# Show table create statement
SHOW CREATE TABLE patients;

# Export database
mysqldump -u hospital_user -p hospital_db > backup.sql

# Import database
mysql -u hospital_user -p hospital_db < backup.sql

# Check MySQL version
SELECT VERSION();

# Check current user
SELECT USER();

# Show running processes
SHOW PROCESSLIST;
```

### Application Configuration

```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=hospital_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.flyway.enabled=true
```

---

## Additional Resources

### Documentation Links

- [MySQL 8.0 Reference Manual](https://dev.mysql.com/doc/refman/8.0/en/)
- [Flyway Documentation](https://flywaydb.org/documentation/)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP)

### Tools

- **MySQL Workbench**: GUI for MySQL management
- **phpMyAdmin**: Web-based MySQL administration
- **DBeaver**: Universal database tool
- **Adminer**: Lightweight database management

---

## Support

For issues related to database setup:

1. Check the [Troubleshooting](#troubleshooting) section
2. Review application logs: `hospital/logs/`
3. Check MySQL error log: `/var/log/mysql/error.log`
4. Review Flyway migration history: `SELECT * FROM flyway_schema_history;`

---

**Last Updated**: January 2025  
**Database Version**: MySQL 8.0+  
**Application Version**: 2.0.0