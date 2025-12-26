# 🗄️ Database Migrations with Flyway - Complete Guide

## 🎯 **What are Database Migrations?**

Database migrations are version-controlled scripts that manage database schema changes over time. They ensure consistent database structure across different environments and team members.

---

## 🚀 **Flyway Overview**

### **What is Flyway?**
Flyway is a database migration tool that:
- Automatically applies database schema changes
- Tracks migration history
- Ensures database consistency
- Supports multiple databases (MySQL, PostgreSQL, Oracle, etc.)

### **Key Concepts**
- **Migration**: A single change to the database
- **Version**: Unique identifier for each migration
- **Checksum**: Ensures migration files haven't been modified
- **Baseline**: Starting point for existing databases

---

## 🏗️ **Migration File Naming Convention**

### **Standard Format**
```
V<Version>__<Description>.sql
```

### **Examples**
```
V1__Create_Base_Tables.sql
V2__Insert_Sample_Data.sql
V3__Add_User_Indexes.sql
V4__Update_User_Table.sql
V5__Create_Audit_Tables.sql
```

### **Version Numbering**
- Use sequential numbers: 1, 2, 3, 4, 5...
- Or use timestamps: V20240101__Create_Users.sql
- Or use semantic versioning: V1.0.0__Initial_Schema.sql

---

## 📁 **Project Structure**

```
src/
└── main/
    └── resources/
        └── db/
            └── migration/
                ├── V1__Create_Base_Tables.sql
                ├── V2__Insert_Sample_Data.sql
                ├── V3__Add_Indexes.sql
                └── V4__Update_Schema.sql
```

---

## ⚙️ **Spring Boot Configuration**

### **application.properties**
```properties
# Enable Flyway
spring.flyway.enabled=true

# Database connection for Flyway
spring.flyway.url=jdbc:mysql://localhost:3306/your_database
spring.flyway.user=your_username
spring.flyway.password=your_password

# Migration settings
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.validate-on-migrate=true
spring.flyway.clean-disabled=false

# Optional: Custom table name
spring.flyway.table=flyway_schema_history

# Optional: Encoding
spring.flyway.encoding=UTF-8

# Optional: Placeholder replacement
spring.flyway.placeholders.environment=development
```

### **application.yml**
```yaml
spring:
  flyway:
    enabled: true
    url: jdbc:mysql://localhost:3306/your_database
    user: your_username
    password: your_password
    baseline-on-migrate: true
    locations: classpath:db/migration
    validate-on-migrate: true
    clean-disabled: false
    table: flyway_schema_history
    encoding: UTF-8
    placeholders:
      environment: development
```

---

## 📝 **Creating Migration Files**

### **1. Table Creation Migration**
```sql
-- V1__Create_Users_Table.sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_created_at ON users(created_at);
```

### **2. Data Insertion Migration**
```sql
-- V2__Insert_Default_Users.sql
INSERT INTO users (username, email, password, first_name, last_name) VALUES
('admin', 'admin@example.com', '$2a$10$encrypted_password_hash', 'Admin', 'User'),
('user1', 'user1@example.com', '$2a$10$encrypted_password_hash', 'John', 'Doe'),
('user2', 'user2@example.com', '$2a$10$encrypted_password_hash', 'Jane', 'Smith');
```

### **3. Schema Update Migration**
```sql
-- V3__Add_User_Profile_Fields.sql
ALTER TABLE users 
ADD COLUMN phone_number VARCHAR(20),
ADD COLUMN date_of_birth DATE,
ADD COLUMN profile_picture_url VARCHAR(255);

-- Update existing records
UPDATE users SET phone_number = 'N/A' WHERE phone_number IS NULL;
```

### **4. Complex Migration with Procedures**
```sql
-- V4__Create_User_Audit_System.sql

-- Create audit table
CREATE TABLE user_audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_values JSON,
    new_values JSON,
    changed_by VARCHAR(50),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create audit trigger
DELIMITER //
CREATE TRIGGER user_audit_trigger
AFTER UPDATE ON users
FOR EACH ROW
BEGIN
    INSERT INTO user_audit_log (user_id, action, old_values, new_values, changed_by)
    VALUES (
        NEW.id,
        'UPDATE',
        JSON_OBJECT(
            'username', OLD.username,
            'email', OLD.email,
            'first_name', OLD.first_name,
            'last_name', OLD.last_name
        ),
        JSON_OBJECT(
            'username', NEW.username,
            'email', NEW.email,
            'first_name', NEW.first_name,
            'last_name', NEW.last_name
        ),
        USER()
    );
END//
DELIMITER ;
```

---

## 🔧 **IntelliJ IDEA Integration**

### **1. Database Tool Window**
```
View → Tool Windows → Database
```

### **2. Connect to Database**
1. Click **+** in Database tool window
2. Select your database type (MySQL, PostgreSQL, etc.)
3. Enter connection details:
   - Host: localhost
   - Port: 3306 (MySQL) or 5432 (PostgreSQL)
   - Database: your_database_name
   - User: your_username
   - Password: your_password

### **3. Automatic Migration Execution**

#### **Option A: Spring Boot Auto-Execution**
- Place migration files in `src/main/resources/db/migration/`
- Start your Spring Boot application
- Flyway automatically detects and executes new migrations

#### **Option B: IntelliJ Database Tools**
1. Right-click on your database connection
2. Select **Run SQL Script**
3. Choose your migration file
4. Execute the script

#### **Option C: Flyway Command Line**
```bash
# Install Flyway CLI
# Download from: https://flywaydb.org/download

# Run migrations
flyway -url=jdbc:mysql://localhost:3306/your_database \
       -user=your_username \
       -password=your_password \
       migrate

# Check migration status
flyway -url=jdbc:mysql://localhost:3306/your_database \
       -user=your_username \
       -password=your_password \
       info
```

---

## 🎯 **Migration Best Practices**

### **1. Naming Conventions**
```sql
-- Good examples
V1__Create_Users_Table.sql
V2__Add_User_Indexes.sql
V3__Insert_Default_Data.sql
V4__Update_User_Schema.sql

-- Avoid these
V1_Users.sql                    -- Missing description
V1.0__Users.sql                -- Version with dots
V1__Create_Users_Table_v2.sql  -- Redundant version info
```

### **2. Migration Content**
```sql
-- Always include comments
-- V1__Create_Users_Table.sql
-- This migration creates the initial users table structure

-- Use explicit column definitions
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Add indexes for performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- Include rollback information in comments
-- Rollback: DROP TABLE users;
```

### **3. Data Migrations**
```sql
-- V5__Migrate_User_Data.sql

-- Backup existing data (if needed)
CREATE TABLE users_backup AS SELECT * FROM users;

-- Perform data migration
UPDATE users 
SET email = LOWER(email), 
    username = LOWER(username)
WHERE email != LOWER(email) OR username != LOWER(username);

-- Verify migration
SELECT COUNT(*) as total_users FROM users;
SELECT COUNT(*) as migrated_users FROM users WHERE email = LOWER(email);

-- Rollback: UPDATE users SET email = (SELECT email FROM users_backup WHERE users_backup.id = users.id);
```

---

## 🚨 **Common Migration Issues**

### **1. Checksum Mismatch**
```bash
# Error: Migration V1__Create_Users.sql has checksum mismatch
# Solution: Repair the migration
flyway repair -url=jdbc:mysql://localhost:3306/your_database \
              -user=your_username \
              -password=your_password
```

### **2. Migration Already Applied**
```bash
# Error: Migration V1__Create_Users.sql is already applied
# Solution: Check migration history
flyway info -url=jdbc:mysql://localhost:3306/your_database \
            -user=your_username \
            -password=your_password
```

### **3. Database Connection Issues**
```properties
# Ensure database is running and accessible
# Check firewall settings
# Verify credentials
# Test connection manually first
```

---

## 🔄 **Migration Lifecycle**

### **1. Development Workflow**
```
1. Create new migration file
2. Test locally
3. Commit to version control
4. Deploy to staging
5. Deploy to production
```

### **2. Testing Migrations**
```bash
# Test on local database first
flyway clean -url=jdbc:mysql://localhost:3306/test_db
flyway migrate -url=jdbc:mysql://localhost:3306/test_db

# Verify schema
flyway info -url=jdbc:mysql://localhost:3306/test_db
```

### **3. Production Deployment**
```bash
# Always backup production database first
mysqldump -u username -p database_name > backup_$(date +%Y%m%d_%H%M%S).sql

# Run migrations
flyway migrate -url=jdbc:mysql://localhost:3306/production_db \
               -user=prod_user \
               -password=prod_password

# Verify deployment
flyway info -url=jdbc:mysql://localhost:3306/production_db
```

---

## 🛠️ **Advanced Flyway Features**

### **1. Placeholder Replacement**
```sql
-- V6__Create_${environment}_Config.sql
CREATE TABLE ${environment}_configuration (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(100) NOT NULL,
    config_value TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

```properties
# application.properties
spring.flyway.placeholders.environment=development
spring.flyway.placeholders.schema_name=myapp
```

### **2. Multiple Migration Locations**
```properties
# Multiple migration sources
spring.flyway.locations=classpath:db/migration,classpath:db/data,classpath:db/views
```

### **3. Custom Migration Strategies**
```java
@Component
public class CustomMigrationStrategy implements FlywayMigrationStrategy {
    
    @Override
    public void migrate(Flyway flyway) {
        // Custom migration logic
        flyway.migrate();
        
        // Additional post-migration tasks
        performPostMigrationTasks();
    }
    
    private void performPostMigrationTasks() {
        // Custom logic after migration
    }
}
```

---

## 📊 **Monitoring and Maintenance**

### **1. Migration Status**
```sql
-- Check migration history
SELECT * FROM flyway_schema_history ORDER BY installed_rank;

-- Check current schema version
SELECT version FROM flyway_schema_history 
WHERE success = 1 
ORDER BY installed_rank DESC 
LIMIT 1;
```

### **2. Migration Reports**
```bash
# Generate migration report
flyway info -url=jdbc:mysql://localhost:3306/your_database \
            -user=your_username \
            -password=your_password \
            -outputType=json > migration_report.json
```

### **3. Cleanup Old Migrations**
```sql
-- Remove old migration records (be careful!)
DELETE FROM flyway_schema_history 
WHERE installed_rank < (
    SELECT MAX(installed_rank) - 10 
    FROM flyway_schema_history
);
```

---

## 🎯 **Summary**

Flyway provides:
- **Version Control**: Track all database changes
- **Automation**: Apply migrations automatically
- **Consistency**: Ensure same schema across environments
- **Rollback Support**: Handle failed migrations
- **Team Collaboration**: Share database changes via version control

**Key Benefits:**
- Eliminate manual database updates
- Ensure database consistency
- Track schema evolution
- Simplify deployment process
- Reduce human errors

---

## 🚀 **Quick Start Checklist**

- [ ] Add Flyway dependency to `build.gradle` or `pom.xml`
- [ ] Configure `application.properties` with database connection
- [ ] Create `src/main/resources/db/migration/` directory
- [ ] Create first migration file `V1__Initial_Schema.sql`
- [ ] Test migration locally
- [ ] Commit migration to version control
- [ ] Deploy and verify

---

**Happy Database Migration! 🗄️✨**
