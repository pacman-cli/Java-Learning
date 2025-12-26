# CodeArena Auth Service - Deployment Guide

## Overview

This guide provides step-by-step instructions for deploying the CodeArena Authentication Service with MySQL database, including database setup, migrations, and production deployment.

## Prerequisites

### System Requirements
- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Minimum 2GB RAM
- 10GB available disk space

### Required Software
- MySQL Server 8.0+
- MySQL Client or MySQL Workbench
- Git
- Text editor or IDE

## Database Setup

### 1. Install MySQL Server

#### Ubuntu/Debian
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

#### CentOS/RHEL
```bash
sudo yum install mysql-server
sudo systemctl start mysqld
sudo mysql_secure_installation
```

#### macOS (using Homebrew)
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

### 2. Create Database and User

Connect to MySQL as root:
```bash
mysql -u root -p
```

Create database and user:
```sql
-- Create database
CREATE DATABASE codearena_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'codearena'@'localhost' IDENTIFIED BY 'your_secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON codearena_auth.* TO 'codearena'@'localhost';

-- For remote access (if needed)
CREATE USER 'codearena'@'%' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON codearena_auth.* TO 'codearena'@'%';

-- Flush privileges
FLUSH PRIVILEGES;

-- Verify database creation
SHOW DATABASES;

-- Exit MySQL
EXIT;
```

### 3. Test Database Connection

```bash
mysql -u codearena -p codearena_auth
```

If successful, you should see:
```
Welcome to the MySQL monitor.  Commands end with ; or \g.
Your MySQL connection id is X
Server version: 8.0.x MySQL Community Server - GPL

Type 'help;' or '\h' for help. Type '\c' to clear the current input statement.

mysql> 
```

## Application Deployment

### 1. Clone Repository

```bash
git clone <repository-url>
cd codeArene/auth-service
```

### 2. Configure Application Properties

Create production configuration file:
```bash
cp src/main/resources/application.properties src/main/resources/application-prod.properties
```

Edit `application-prod.properties`:
```properties
# Application Configuration
spring.application.name=auth-service
server.port=8081

# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/codearena_auth?useSSL=true&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=codearena
spring.datasource.password=${DB_PASSWORD:your_secure_password}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=false

# Flyway Configuration
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-version=0

# JWT Configuration
app.jwt.secret=${JWT_SECRET:change-this-to-a-very-secure-secret-key-of-at-least-32-characters}
app.jwt.expiration=3600000

# Logging Configuration
logging.level.com.puspo.codearena.authservice=INFO
logging.level.org.springframework.security=WARN
logging.level.root=WARN
logging.file.name=logs/auth-service.log

# JPA Optimization
spring.jpa.open-in-view=false

# Actuator Configuration
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=when-authorized
management.security.enabled=true

# SSL Configuration (if using HTTPS)
# server.ssl.key-store=classpath:keystore.p12
# server.ssl.key-store-password=password
# server.ssl.keyStoreType=PKCS12
# server.ssl.keyAlias=tomcat
```

### 3. Environment Variables

Create environment configuration:
```bash
# Create environment file
sudo nano /etc/environment
```

Add the following variables:
```bash
# Database Configuration
DB_PASSWORD=your_secure_database_password

# JWT Configuration
JWT_SECRET=your-super-secure-jwt-secret-key-that-is-at-least-32-characters-long

# Application Profile
SPRING_PROFILES_ACTIVE=prod
```

Reload environment:
```bash
source /etc/environment
```

### 4. Build Application

```bash
# Clean and build
mvn clean package -DskipTests

# Verify build
ls -la target/
```

You should see `auth-service-0.0.1-SNAPSHOT.jar` in the target directory.

### 5. Run Database Migrations

First, test the database connection:
```bash
mvn flyway:info -Dspring.profiles.active=prod
```

Run migrations:
```bash
mvn flyway:migrate -Dspring.profiles.active=prod
```

Verify migrations:
```bash
mysql -u codearena -p codearena_auth -e "SHOW TABLES;"
mysql -u codearena -p codearena_auth -e "SELECT * FROM flyway_schema_history;"
mysql -u codearena -p codearena_auth -e "SELECT username, email, role FROM users;"
```

### 6. Start Application

#### Development Mode
```bash
java -jar -Dspring.profiles.active=prod target/auth-service-0.0.1-SNAPSHOT.jar
```

#### Production Mode (with systemd)

Create systemd service file:
```bash
sudo nano /etc/systemd/system/auth-service.service
```

Add the following content:
```ini
[Unit]
Description=CodeArena Auth Service
After=mysql.service
Requires=mysql.service

[Service]
Type=simple
User=codearena
WorkingDirectory=/opt/codearena/auth-service
ExecStart=/usr/bin/java -Xmx1024m -Xms512m -Dspring.profiles.active=prod -jar auth-service-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=syslog
StandardError=syslog
SyslogIdentifier=auth-service
Environment=DB_PASSWORD=your_secure_database_password
Environment=JWT_SECRET=your-super-secure-jwt-secret-key-that-is-at-least-32-characters-long
Environment=SPRING_PROFILES_ACTIVE=prod

[Install]
WantedBy=multi-user.target
```

Create deployment directory and copy files:
```bash
sudo mkdir -p /opt/codearena/auth-service
sudo mkdir -p /opt/codearena/auth-service/logs
sudo cp target/auth-service-0.0.1-SNAPSHOT.jar /opt/codearena/auth-service/
sudo chown -R codearena:codearena /opt/codearena/
```

Enable and start service:
```bash
sudo systemctl daemon-reload
sudo systemctl enable auth-service
sudo systemctl start auth-service
sudo systemctl status auth-service
```

### 7. Verify Deployment

Check application logs:
```bash
sudo journalctl -u auth-service -f
```

Test health endpoint:
```bash
curl http://localhost:8081/api/health
```

Expected response:
```json
{
    "status": "UP",
    "service": "auth-service",
    "timestamp": "2024-01-01T10:00:00",
    "version": "1.0.0"
}
```

Test authentication:
```bash
# Login with admin user
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "password123"
  }'
```

### 8. SSL/HTTPS Configuration (Optional)

#### Generate SSL Certificate

For development/testing (self-signed):
```bash
keytool -genkeypair -alias auth-service -keyalg RSA -keysize 2048 \
  -storetype PKCS12 -keystore auth-service.p12 -validity 3650
```

For production, use Let's Encrypt or a commercial certificate.

#### Update Application Properties

Add to `application-prod.properties`:
```properties
server.ssl.key-store=classpath:auth-service.p12
server.ssl.key-store-password=your_keystore_password
server.ssl.keyStoreType=PKCS12
server.ssl.keyAlias=auth-service
server.port=8443
```

## Production Optimizations

### 1. Database Optimizations

#### MySQL Configuration (`/etc/mysql/mysql.conf.d/mysqld.cnf`)
```ini
[mysqld]
# Performance Settings
innodb_buffer_pool_size = 1G
innodb_log_file_size = 256M
innodb_flush_log_at_trx_commit = 1
innodb_file_per_table = 1

# Connection Settings
max_connections = 200
wait_timeout = 28800
interactive_timeout = 28800

# Query Cache (MySQL 5.7 and earlier)
query_cache_type = 1
query_cache_size = 64M

# Logging
slow_query_log = 1
slow_query_log_file = /var/log/mysql/slow.log
long_query_time = 2
```

#### Database Maintenance Scripts

Create backup script (`backup-db.sh`):
```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_DIR="/opt/backups/mysql"
DB_NAME="codearena_auth"
DB_USER="codearena"

mkdir -p $BACKUP_DIR

mysqldump -u $DB_USER -p$DB_PASSWORD \
  --single-transaction \
  --routines \
  --triggers \
  $DB_NAME > $BACKUP_DIR/auth_service_$DATE.sql

# Compress backup
gzip $BACKUP_DIR/auth_service_$DATE.sql

# Remove backups older than 30 days
find $BACKUP_DIR -name "auth_service_*.sql.gz" -mtime +30 -delete

echo "Backup completed: auth_service_$DATE.sql.gz"
```

### 2. Application Optimizations

#### JVM Tuning

Update systemd service with optimized JVM settings:
```ini
ExecStart=/usr/bin/java -Xmx2048m -Xms1024m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/opt/codearena/auth-service/logs/ \
  -Dspring.profiles.active=prod \
  -jar auth-service-0.0.1-SNAPSHOT.jar
```

#### Connection Pooling

Add to `application-prod.properties`:
```properties
# HikariCP Configuration
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.leak-detection-threshold=60000
```

### 3. Security Hardening

#### Firewall Configuration
```bash
# Allow SSH (if needed)
sudo ufw allow 22

# Allow HTTP/HTTPS
sudo ufw allow 80
sudo ufw allow 443

# Allow application port (if not behind reverse proxy)
sudo ufw allow 8081

# Enable firewall
sudo ufw enable
```

#### Reverse Proxy with Nginx

Install Nginx:
```bash
sudo apt install nginx
```

Create Nginx configuration (`/etc/nginx/sites-available/auth-service`):
```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    # Security headers
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    add_header X-XSS-Protection "1; mode=block";
}
```

Enable site:
```bash
sudo ln -s /etc/nginx/sites-available/auth-service /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## Monitoring and Maintenance

### 1. Log Management

Configure log rotation (`/etc/logrotate.d/auth-service`):
```
/opt/codearena/auth-service/logs/*.log {
    daily
    missingok
    rotate 30
    compress
    delaycompress
    notifempty
    create 644 codearena codearena
    postrotate
        systemctl reload auth-service
    endscript
}
```

### 2. Health Monitoring

Create monitoring script (`check-health.sh`):
```bash
#!/bin/bash
HEALTH_URL="http://localhost:8081/api/health"
RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" $HEALTH_URL)

if [ $RESPONSE -eq 200 ]; then
    echo "$(date): Service is healthy"
    exit 0
else
    echo "$(date): Service is down (HTTP $RESPONSE)"
    # Restart service
    systemctl restart auth-service
    exit 1
fi
```

Add to crontab:
```bash
crontab -e
# Add line:
*/5 * * * * /opt/codearena/scripts/check-health.sh >> /var/log/auth-service-health.log
```

### 3. Database Monitoring

Monitor key metrics:
```sql
-- Active connections
SHOW PROCESSLIST;

-- Database size
SELECT 
    table_schema AS 'Database',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.tables 
WHERE table_schema = 'codearena_auth'
GROUP BY table_schema;

-- User statistics
SELECT role, COUNT(*) as count FROM users GROUP BY role;

-- Recent registrations
SELECT DATE(created_at) as date, COUNT(*) as registrations 
FROM users 
WHERE created_at >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY DATE(created_at)
ORDER BY date DESC;
```

## Troubleshooting

### Common Issues

#### 1. Database Connection Issues
```bash
# Check MySQL status
sudo systemctl status mysql

# Check MySQL logs
sudo tail -f /var/log/mysql/error.log

# Test connection
mysql -u codearena -p codearena_auth
```

#### 2. Migration Issues
```bash
# Check migration status
mvn flyway:info -Dspring.profiles.active=prod

# Repair migration (if needed)
mvn flyway:repair -Dspring.profiles.active=prod

# Baseline existing database
mvn flyway:baseline -Dspring.profiles.active=prod
```

#### 3. Application Won't Start
```bash
# Check application logs
sudo journalctl -u auth-service -n 50

# Check Java process
ps aux | grep java

# Check port usage
netstat -tlnp | grep 8081
```

#### 4. Performance Issues
```bash
# Check system resources
top
free -h
df -h

# Check database performance
mysql -u codearena -p codearena_auth -e "SHOW PROCESSLIST;"
mysql -u codearena -p codearena_auth -e "SHOW ENGINE INNODB STATUS;"
```

### Recovery Procedures

#### Database Recovery
```bash
# Stop application
sudo systemctl stop auth-service

# Restore from backup
gunzip -c /opt/backups/mysql/auth_service_YYYYMMDD_HHMMSS.sql.gz | \
  mysql -u codearena -p codearena_auth

# Start application
sudo systemctl start auth-service
```

#### Application Recovery
```bash
# Restart service
sudo systemctl restart auth-service

# Check logs
sudo journalctl -u auth-service -f

# Verify health
curl http://localhost:8081/api/health
```

## Security Checklist

- [ ] Database user has minimal required privileges
- [ ] Strong passwords for all accounts
- [ ] JWT secret is cryptographically secure
- [ ] SSL/TLS enabled for production
- [ ] Firewall configured properly
- [ ] Regular security updates applied
- [ ] Log files protected with proper permissions
- [ ] Backup files encrypted and secured
- [ ] Database connections encrypted
- [ ] Application running as non-root user

## Support

For issues and support:
1. Check application logs: `sudo journalctl -u auth-service`
2. Check database logs: `sudo tail -f /var/log/mysql/error.log`
3. Verify configuration files
4. Review this deployment guide
5. Contact development team with detailed error messages

## Version History

- v1.0.0: Initial deployment guide
- Database migrations: V1 (schema), V2 (seed data)
- Last updated: November 2024