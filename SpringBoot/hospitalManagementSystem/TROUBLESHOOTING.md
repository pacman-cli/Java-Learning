# 🔧 Troubleshooting Guide

Quick solutions to common issues in the Hospital Management System.

---

## 🚨 Quick Fixes

### Frontend Won't Start - "Cannot find module" Error

**Error Message:**
```
Error: Cannot find module '../server/require-hook'
Error: Cannot find module 'next'
```

**Quick Fix:**
```bash
# Linux/macOS
rm -rf frontend/node_modules frontend/package-lock.json frontend/.next
./start-frontend.sh

# Windows
rmdir /s /q frontend\node_modules frontend\.next
del frontend\package-lock.json
start-frontend.bat
```

**Why it happens:** Corrupted or incomplete npm installation. Common with Node.js v25+ and Next.js 16.

**Prevention:** Always use the startup scripts, they now detect and fix this automatically.

---

## 📋 Common Issues by Component

### Frontend Issues

#### 1. Port 3000 Already in Use

**Symptoms:**
- Error: "Port 3000 is already in use"
- Frontend won't start

**Solution:**
```bash
# Find and kill process (Linux/macOS)
lsof -ti:3000 | xargs kill -9

# Find and kill process (Windows)
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Or let the script handle it
./start-frontend.sh  # Automatically kills the process
```

#### 2. Dependencies Installation Failed

**Symptoms:**
- npm install fails
- Missing packages errors

**Solution:**
```bash
cd frontend

# Clear npm cache
npm cache clean --force

# Remove everything
rm -rf node_modules package-lock.json .next

# Reinstall with legacy peer deps (if needed)
npm install --legacy-peer-deps

# Or use the script
cd ..
./start-frontend.sh
```

#### 3. Environment Variables Not Working

**Symptoms:**
- API calls fail
- Features not working
- Console errors about undefined variables

**Solution:**
1. Check `frontend/.env.local` exists
2. Verify contents:
```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```
3. Restart frontend completely (Ctrl+C and restart)
4. Clear browser cache

#### 4. Build Errors

**Symptoms:**
- TypeScript errors
- Build fails
- Page not found

**Solution:**
```bash
cd frontend

# Delete build cache
rm -rf .next

# Type check
npm run type-check

# Build
npm run build

# If still failing, reinstall
rm -rf node_modules
npm install
```

---

### Backend Issues

#### 1. Port 8080 Already in Use

**Symptoms:**
- Error: "Port 8080 was already in use"
- Backend won't start

**Solution:**
```bash
# Find and kill process (Linux/macOS)
lsof -ti:8080 | xargs kill -9

# Find and kill process (Windows)
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Or let the script handle it
./start-backend.sh  # Automatically kills the process
```

#### 2. Database Connection Failed

**Symptoms:**
- Error: "Communications link failure"
- Error: "Access denied for user"
- Backend won't start

**Solution:**

**Step 1:** Verify MySQL is running
```bash
# Linux
sudo systemctl status mysql
sudo systemctl start mysql

# macOS
brew services list | grep mysql
brew services start mysql@8.0

# Windows
net start MySQL80
```

**Step 2:** Verify database exists
```bash
mysql -u root -p -e "SHOW DATABASES LIKE 'hospital_db';"
```

**Step 3:** If database doesn't exist, create it:
```sql
mysql -u root -p

CREATE DATABASE hospital_db;
CREATE USER 'hospital_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

**Step 4:** Update credentials in `hospital/src/main/resources/application.properties`:
```properties
spring.datasource.username=hospital_user
spring.datasource.password=your_password
```

#### 3. Flyway Migration Failed

**Symptoms:**
- Error: "FlywayException: Validate failed"
- Error: "Migration checksum mismatch"
- Backend starts but database tables missing

**Solution:**
```bash
cd hospital

# Check migration status
./mvnw flyway:info

# Repair metadata (if checksums changed)
./mvnw flyway:repair

# In development, clean and restart (WARNING: Deletes all data)
./mvnw flyway:clean
./mvnw flyway:migrate

# Or just restart the backend (Flyway runs automatically)
cd ..
./start-backend.sh
```

#### 4. Java Version Error

**Symptoms:**
- Error: "Unsupported class file major version"
- Error: "java.lang.UnsupportedClassVersionError"

**Solution:**
```bash
# Check Java version
java -version  # Must be 17+

# If wrong version, install Java 17+
# https://www.oracle.com/java/technologies/downloads/

# Set JAVA_HOME (Linux/macOS)
export JAVA_HOME=/path/to/java17

# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\path\to\java17

# Verify
java -version
```

#### 5. Maven Build Failed

**Symptoms:**
- Compilation errors
- Dependencies not downloading
- Build timeout

**Solution:**
```bash
cd hospital

# Clean build
./mvnw clean

# Update dependencies
./mvnw dependency:resolve

# Force update
./mvnw clean install -U

# Skip tests (if needed)
./mvnw spring-boot:run -DskipTests

# Clear Maven cache (if persistent issues)
rm -rf ~/.m2/repository  # Linux/macOS
rmdir /s %USERPROFILE%\.m2\repository  # Windows
```

---

### Database Issues

#### 1. Cannot Connect to MySQL

**Symptoms:**
- Connection refused
- Can't reach database server

**Solution:**
```bash
# Check if MySQL is running
netstat -an | grep 3306  # Linux/macOS
netstat -ano | findstr :3306  # Windows

# Start MySQL
sudo systemctl start mysql  # Linux
brew services start mysql@8.0  # macOS
net start MySQL80  # Windows

# Test connection
mysql -h localhost -u hospital_user -p
```

#### 2. Access Denied

**Symptoms:**
- Error: "Access denied for user 'hospital_user'@'localhost'"

**Solution:**
```sql
# Connect as root
mysql -u root -p

# Reset user password
ALTER USER 'hospital_user'@'localhost' IDENTIFIED BY 'new_password';
FLUSH PRIVILEGES;

# Or recreate user
DROP USER 'hospital_user'@'localhost';
CREATE USER 'hospital_user'@'localhost' IDENTIFIED BY 'new_password';
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;

# Update application.properties with new password
```

#### 3. Database Not Found

**Symptoms:**
- Error: "Unknown database 'hospital_db'"

**Solution:**
```sql
# Connect as root
mysql -u root -p

# Create database
CREATE DATABASE hospital_db;

# Verify
SHOW DATABASES LIKE 'hospital_db';
```

#### 4. Tables Not Created

**Symptoms:**
- Backend starts but tables missing
- API errors about missing tables

**Solution:**
```bash
# Check Flyway migrations ran
mysql -u hospital_user -p hospital_db -e "SELECT * FROM flyway_schema_history;"

# If no migrations ran, manually run Flyway
cd hospital
./mvnw flyway:migrate

# Or restart backend (Flyway runs automatically)
./mvnw spring-boot:run
```

---

## 🔍 Diagnostic Commands

### Check Everything is Running

```bash
# Backend running?
lsof -i :8080  # Should show Java process

# Frontend running?
lsof -i :3000  # Should show node process

# MySQL running?
lsof -i :3306  # Should show mysqld

# Test backend health
curl http://localhost:8080/actuator/health

# Test frontend
curl http://localhost:3000
```

### View Logs

```bash
# Backend logs (in terminal output)
# Look for errors marked with ERROR or WARN

# Frontend logs (in terminal output)
# Look for compilation errors or runtime errors

# MySQL logs
sudo tail -f /var/log/mysql/error.log  # Linux
tail -f /usr/local/var/mysql/*.err     # macOS

# Browser console (F12 -> Console tab)
# Look for network errors or JavaScript errors
```

### Clean Everything

**Nuclear option - start fresh:**
```bash
# Kill all processes
lsof -ti:8080,3000,3306 | xargs kill -9

# Clean frontend
rm -rf frontend/node_modules frontend/.next frontend/package-lock.json

# Clean backend
cd hospital
./mvnw clean
rm -rf target
cd ..

# Clean database (WARNING: Deletes all data)
mysql -u root -p -e "DROP DATABASE IF EXISTS hospital_db; CREATE DATABASE hospital_db;"

# Reinstall everything
./start-backend.sh  # Will run Flyway migrations
./start-frontend.sh  # Will reinstall node_modules
```

---

## 📱 Browser Issues

#### 1. Login Not Working

**Symptoms:**
- Can't login with default credentials
- Network error on login

**Solution:**
1. Verify backend is running: http://localhost:8080/swagger-ui.html
2. Check browser console (F12) for errors
3. Verify `.env.local` has correct API URL
4. Clear browser cache and cookies
5. Try different browser
6. Check credentials:
   - Username: `admin`
   - Password: `admin123`

#### 2. CORS Errors

**Symptoms:**
- Console error: "has been blocked by CORS policy"
- API calls failing

**Solution:**
1. Verify backend CORS configuration
2. Check frontend `.env.local` has: `NEXT_PUBLIC_API_BASE_URL=http://localhost:8080`
3. Restart both frontend and backend
4. Clear browser cache

#### 3. Blank Page / White Screen

**Symptoms:**
- Nothing loads
- Console shows JavaScript errors

**Solution:**
```bash
# Rebuild frontend
cd frontend
rm -rf .next
npm run build
npm run dev

# Or use the script
cd ..
./start-frontend.sh
```

---

## 🎯 Prevention Tips

### Best Practices

1. **Always use startup scripts** - They handle most issues automatically
2. **Keep Node.js and Java updated** - Use recommended versions
3. **Don't interrupt npm install** - Let it complete
4. **Backup database regularly** - Before major changes
5. **Clean build periodically** - Prevents cached issues
6. **Check logs first** - Errors are usually clear
7. **One problem at a time** - Fix backend before frontend
8. **Use version control** - Git helps track issues

### Development Workflow

```bash
# Daily startup routine
1. Start MySQL
2. Run ./start-backend.sh
3. Wait for "Started HospitalApplication"
4. Run ./start-frontend.sh (new terminal)
5. Wait for "✓ Ready"
6. Open http://localhost:3000

# When done
1. Ctrl+C in frontend terminal
2. Ctrl+C in backend terminal
3. Optionally stop MySQL
```

---

## 🆘 Still Having Issues?

### Checklist

- [ ] Read error message carefully
- [ ] Check if MySQL is running
- [ ] Check if ports 8080, 3000, 3306 are available
- [ ] Verify Java 17+, Node.js 18+ installed
- [ ] Check database credentials in application.properties
- [ ] Try clean reinstall (see Nuclear Option above)
- [ ] Check logs in terminal output
- [ ] Review browser console (F12)

### Get Help

1. **Check Documentation:**
   - [STARTUP_GUIDE.md](STARTUP_GUIDE.md) - Complete startup guide
   - [DATABASE_SETUP.md](DATABASE_SETUP.md) - Database configuration
   - [QUICK_START.md](QUICK_START.md) - Fast setup
   - [README.md](README.md) - Project overview

2. **Search Error Message:**
   - Copy exact error text
   - Search in project documentation
   - Search online (Stack Overflow, GitHub Issues)

3. **Debug Step by Step:**
   - Isolate the problem (frontend, backend, or database?)
   - Check one service at a time
   - Use diagnostic commands above

4. **Ask for Help:**
   - Provide error logs
   - List steps you tried
   - Share configuration (hide passwords!)
   - Mention OS and versions

---

## 📊 Quick Reference

### Port Usage
- Frontend: 3000
- Backend: 8080
- MySQL: 3306

### Default Credentials
- Admin: admin / admin123
- Doctor: doctor / doctor123
- Patient: patient / patient123

### Key URLs
- Frontend: http://localhost:3000
- Backend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

### Config Files
- Backend: `hospital/src/main/resources/application.properties`
- Frontend: `frontend/.env.local`

### Startup Scripts
- Backend: `./start-backend.sh` or `start-backend.bat`
- Frontend: `./start-frontend.sh` or `start-frontend.bat`

---

**Last Updated**: January 2025  
**Version**: 2.0.0

For more detailed help, see [STARTUP_GUIDE.md](STARTUP_GUIDE.md)