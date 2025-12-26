# 🚀 Startup Guide - Hospital Management System

Complete guide for starting the Hospital Management System frontend and backend applications.

---

## 📋 Table of Contents

1. [Quick Start](#quick-start)
2. [Startup Scripts](#startup-scripts)
3. [Prerequisites](#prerequisites)
4. [Starting the Applications](#starting-the-applications)
5. [Verification](#verification)
6. [Common Issues](#common-issues)
7. [Manual Startup](#manual-startup)
8. [Development Workflow](#development-workflow)

---

## Quick Start

### For Linux/macOS Users

```bash
# Start Backend Only
./start-backend.sh

# Start Frontend Only (in a new terminal)
./start-frontend.sh
```

### For Windows Users

```cmd
# Start Backend Only
start-backend.bat

# Start Frontend Only (in a new terminal/command prompt)
start-frontend.bat
```

---

## Startup Scripts

The project includes automated startup scripts for both frontend and backend:

### Backend Scripts

| Script | Platform | Purpose |
|--------|----------|---------|
| `start-backend.sh` | Linux/macOS | Start Spring Boot backend |
| `start-backend.bat` | Windows | Start Spring Boot backend |

### Frontend Scripts

| Script | Platform | Purpose |
|--------|----------|---------|
| `start-frontend.sh` | Linux/macOS | Start Next.js frontend |
| `start-frontend.bat` | Windows | Start Next.js frontend |

### Script Features

✅ **Automated checks**: Verifies all prerequisites are installed  
✅ **Dependency management**: Installs dependencies if missing  
✅ **Port conflict resolution**: Detects and resolves port conflicts  
✅ **Environment setup**: Creates default configuration files  
✅ **Error handling**: Provides clear error messages and solutions  
✅ **Database verification**: Checks MySQL connection (backend only)  

---

## Prerequisites

### System Requirements

#### Backend Requirements
- ☕ **Java 17+** - [Download](https://www.oracle.com/java/technologies/downloads/)
- 📦 **Maven 3.8+** - [Download](https://maven.apache.org/download.cgi) (or use included wrapper)
- 🗄️ **MySQL 8.0+** - [Installation Guide](DATABASE_SETUP.md)
- 💾 **RAM**: 2GB minimum, 4GB recommended
- 💿 **Storage**: 5GB minimum

#### Frontend Requirements
- 🟢 **Node.js 18+** - [Download](https://nodejs.org/)
- 📦 **npm** (included with Node.js)
- 💾 **RAM**: 1GB minimum, 2GB recommended
- 💿 **Storage**: 500MB minimum

### Installation Verification

#### Check Java (Backend)
```bash
java -version
# Should show: openjdk version "17.0.x" or higher
```

#### Check Node.js (Frontend)
```bash
node -v
# Should show: v18.x.x or higher

npm -v
# Should show: 9.x.x or higher
```

#### Check MySQL (Backend)
```bash
mysql --version
# Should show: mysql Ver 8.0.x or higher

# Test connection
mysql -u root -p -e "SELECT VERSION();"
```

---

## Starting the Applications

### Complete Setup (First Time)

#### Step 1: Database Setup

**IMPORTANT**: Set up the database before starting the backend!

```bash
# See DATABASE_SETUP.md for detailed instructions
mysql -u root -p
```

```sql
CREATE DATABASE hospital_db;
CREATE USER 'hospital_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON hospital_db.* TO 'hospital_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

Update `hospital/src/main/resources/application.properties`:
```properties
spring.datasource.username=hospital_user
spring.datasource.password=your_password
```

#### Step 2: Start Backend

**Linux/macOS:**
```bash
cd /path/to/hospitalManagementSystem
./start-backend.sh
```

**Windows:**
```cmd
cd C:\path\to\hospitalManagementSystem
start-backend.bat
```

Wait for the message:
```
Started HospitalApplication in X.XXX seconds
```

#### Step 3: Start Frontend (New Terminal)

**Linux/macOS:**
```bash
cd /path/to/hospitalManagementSystem
./start-frontend.sh
```

**Windows:**
```cmd
cd C:\path\to\hospitalManagementSystem
start-frontend.bat
```

Wait for the message:
```
✓ Ready in Xs
```

#### Step 4: Access the Application

🌐 **Frontend**: http://localhost:3000  
🔌 **Backend API**: http://localhost:8080  
📚 **API Documentation**: http://localhost:8080/swagger-ui.html  

---

## Verification

### Backend Health Check

```bash
# Using curl
curl http://localhost:8080/actuator/health

# Using browser
# Navigate to: http://localhost:8080/swagger-ui.html
```

Expected response:
```json
{
  "status": "UP"
}
```

### Frontend Health Check

```bash
# Navigate to: http://localhost:3000
```

You should see the Hospital Management System login page.

### Test Login

Use default credentials:

| Role | Username | Password | Access Level |
|------|----------|----------|--------------|
| Admin | `admin` | `admin123` | Full system access |
| Doctor | `doctor` | `doctor123` | Medical staff access |
| Patient | `patient` | `patient123` | Patient portal |

---

## Common Issues

### Backend Issues

#### Issue 1: Port 8080 Already in Use

**Error:**
```
Port 8080 was already in use
```

**Solution:**
```bash
# Linux/macOS
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

Or let the script handle it automatically.

#### Issue 2: Database Connection Failed

**Error:**
```
Communications link failure
```

**Solutions:**

1. **Check if MySQL is running:**
```bash
# Linux
sudo systemctl status mysql

# macOS
brew services list | grep mysql

# Windows
net start | findstr MySQL
```

2. **Start MySQL if stopped:**
```bash
# Linux
sudo systemctl start mysql

# macOS
brew services start mysql@8.0

# Windows
net start MySQL80
```

3. **Verify database credentials** in `application.properties`

4. **Check if database exists:**
```bash
mysql -u hospital_user -p -e "SHOW DATABASES LIKE 'hospital_db';"
```

#### Issue 3: Java Version Too Old

**Error:**
```
Unsupported class file major version XX
```

**Solution:**
```bash
# Install Java 17 or higher
# Update JAVA_HOME environment variable
export JAVA_HOME=/path/to/java17  # Linux/macOS
set JAVA_HOME=C:\path\to\java17   # Windows
```

### Frontend Issues

#### Issue 1: Port 3000 Already in Use

**Error:**
```
Port 3000 is already in use
```

**Solution:**
```bash
# Linux/macOS
lsof -ti:3000 | xargs kill -9

# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

Or let the script handle it automatically, or Next.js will use an alternative port.

#### Issue 2: Node Modules Not Found / Corrupted

**Error:**
```
Cannot find module 'next'
```
OR
```
Error: Cannot find module '../server/require-hook'
```

**Cause:** Corrupted or incomplete node_modules installation. This is common with Next.js 16 and Node.js v25+.

**Solution 1 - Use the script (Recommended):**
```bash
# Linux/macOS
rm -rf frontend/node_modules frontend/package-lock.json frontend/.next
./start-frontend.sh

# Windows
rmdir /s /q frontend\node_modules frontend\.next
del frontend\package-lock.json
start-frontend.bat
```

The script now automatically detects and fixes corrupted installations.

**Solution 2 - Manual fix:**
```bash
cd frontend

# Clean everything
rm -rf node_modules package-lock.json .next  # Linux/macOS
# OR
rmdir /s /q node_modules .next & del package-lock.json  # Windows

# Reinstall
npm install

# Verify installation
ls node_modules/next/dist/server/require-hook.js  # Should exist

# Start
npm run dev
```

**Prevention:**
- Always use the startup scripts
- Don't interrupt npm install
- Keep Node.js and npm updated
- Clear cache if issues persist: `npm cache clean --force`

#### Issue 3: Node.js Version Compatibility
### Quick Fix Commands

**For corrupted node_modules:**
```bash
# Linux/macOS
cd frontend && rm -rf node_modules package-lock.json .next && npm install

# Windows
cd frontend && rmdir /s /q node_modules .next & del package-lock.json & npm install
```

**For all frontend issues:**
```bash
# Linux/macOS
rm -rf frontend/node_modules frontend/package-lock.json frontend/.next
./start-frontend.sh

# Windows
rmdir /s /q frontend\node_modules frontend\.next
del frontend\package-lock.json
start-frontend.bat
```

**Verify Next.js installation:**
```bash
cd frontend
ls node_modules/next/dist/server/require-hook.js  # Should exist
npm list next  # Should show next@16.0.0
```

#### Issue 4: Cannot Connect to Backend

**Error:**
```
Network Error / ECONNREFUSED
```

**Solutions:**

1. **Verify backend is running** on http://localhost:8080
2. **Check `.env.local`** in frontend directory:
```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
```
3. **Clear browser cache** and reload
4. **Check browser console** for CORS errors

#### Issue 5: Environment File Missing

**Solution:**

Create `frontend/.env.local`:
```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
NEXT_PUBLIC_APP_NAME="Hospital Management System"
NEXT_PUBLIC_APP_VERSION="2.0.0"
NEXT_PUBLIC_ENABLE_REGISTRATION=true
NEXT_PUBLIC_ENABLE_DARK_MODE=true
NEXT_PUBLIC_ENABLE_NOTIFICATIONS=true
NEXT_PUBLIC_MAX_FILE_SIZE=10485760
NEXT_PUBLIC_ALLOWED_FILE_TYPES="image/*,application/pdf,.doc,.docx"
```

---

## Manual Startup

If you prefer not to use the scripts or need more control:

### Backend Manual Start

```bash
cd hospital

# Clean previous builds (optional)
./mvnw clean

# Run Spring Boot application
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

### Frontend Manual Start

```bash
cd frontend

# Install dependencies (first time)
npm install

# Run development server
npm run dev

# Or production build
npm run build
npm run start
```

---

## Development Workflow

### Recommended Workflow

1. **Start Backend First**: Database operations and API must be ready
2. **Verify Backend**: Check Swagger UI at http://localhost:8080/swagger-ui.html
3. **Start Frontend**: In a separate terminal
4. **Development**: Make changes and test
5. **Hot Reload**: Both support hot reload for development

### Using Multiple Terminals

#### Terminal 1: Backend
```bash
./start-backend.sh
# Keep this running
```

#### Terminal 2: Frontend
```bash
./start-frontend.sh
# Keep this running
```

#### Terminal 3: Database Operations (Optional)
```bash
mysql -u hospital_user -p hospital_db
# For direct database access
```

### IDE Integration

#### IntelliJ IDEA (Backend)
1. Open `hospital` directory as project
2. Right-click `HospitalApplication.java`
3. Select "Run 'HospitalApplication'"

#### VS Code (Frontend)
1. Open `frontend` directory
2. Open integrated terminal (Ctrl+`)
3. Run `npm run dev`

---

## Script Configuration

### Backend Script Options

The `start-backend.sh` / `start-backend.bat` scripts support:

- ✅ Automatic Java version check
- ✅ Maven wrapper detection
- ✅ Database connection verification
- ✅ Port conflict detection and resolution
- ✅ Clean build option (interactive)
- ✅ Configuration file validation

### Frontend Script Options

The `start-frontend.sh` / `start-frontend.bat` scripts support:

- ✅ Automatic Node.js version check
- ✅ Dependency installation
- ✅ Environment file creation
- ✅ Port conflict detection
- ✅ Development server start

---

## Stopping the Applications

### Backend

1. **In the terminal running backend**:
   - Press `Ctrl+C`
   - Wait for graceful shutdown

2. **Force stop** (if needed):
```bash
# Linux/macOS
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Frontend

1. **In the terminal running frontend**:
   - Press `Ctrl+C`
   - Confirm termination if prompted

2. **Force stop** (if needed):
```bash
# Linux/macOS
lsof -ti:3000 | xargs kill -9

# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

---

## Production Deployment

For production deployment, see:

- **Backend**: Build with `./mvnw clean package` and deploy JAR
- **Frontend**: Build with `npm run build` and deploy
- **Database**: See [DATABASE_SETUP.md](DATABASE_SETUP.md) - Production Setup section

---

## Environment Variables

### Backend Environment Variables

Set in `hospital/src/main/resources/application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.datasource.username=hospital_user
spring.datasource.password=your_password

# Server
server.port=8080

# JWT
app.jwt.secret=your_jwt_secret_key
app.jwt.expiration=86400000

# File Upload
storage.local.base-dir=uploads
spring.servlet.multipart.max-file-size=10MB
```

### Frontend Environment Variables

Set in `frontend/.env.local`:

```env
# API Configuration
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080

# Application Configuration
NEXT_PUBLIC_APP_NAME="Hospital Management System"
NEXT_PUBLIC_APP_VERSION="2.0.0"

# Feature Flags
NEXT_PUBLIC_ENABLE_REGISTRATION=true
NEXT_PUBLIC_ENABLE_DARK_MODE=true
NEXT_PUBLIC_ENABLE_NOTIFICATIONS=true

# File Upload Configuration
NEXT_PUBLIC_MAX_FILE_SIZE=10485760
NEXT_PUBLIC_ALLOWED_FILE_TYPES="image/*,application/pdf,.doc,.docx"
```

---

## Logs and Debugging

### Backend Logs

```bash
# Logs are in the console output
# For file logging, configure in application.properties:
logging.file.name=logs/hospital.log
logging.level.com.pacman=DEBUG
```

### Frontend Logs

```bash
# Logs are in the console output
# Browser console: F12 -> Console tab
# Next.js build logs: Check terminal output
```

### Enable Debug Mode

#### Backend
```properties
# application.properties
logging.level.com.pacman=DEBUG
logging.level.org.springframework.web=DEBUG
```

#### Frontend
```bash
# Set in terminal before starting
export DEBUG=*  # Linux/macOS
set DEBUG=*     # Windows
```

---

## Quick Reference

### URLs

| Service | URL | Description |
|---------|-----|-------------|
| Frontend | http://localhost:3000 | Main application |
| Backend API | http://localhost:8080 | REST API |
| Swagger UI | http://localhost:8080/swagger-ui.html | API documentation |
| API Docs JSON | http://localhost:8080/v3/api-docs | OpenAPI spec |

### Default Ports

| Service | Port | Configurable |
|---------|------|--------------|
| Frontend | 3000 | Yes (Next.js auto-selects if busy) |
| Backend | 8080 | Yes (application.properties) |
| MySQL | 3306 | Yes (MySQL config) |

### File Locations

| File | Location | Purpose |
|------|----------|---------|
| Backend Config | `hospital/src/main/resources/application.properties` | Backend configuration |
| Frontend Config | `frontend/.env.local` | Frontend environment variables |
| Database Setup | `DATABASE_SETUP.md` | Database setup guide |
| JWT Guide | `JWT_AUTHENTICATION_GUIDE.md` | Authentication documentation |

---

## Getting Help

### Documentation

- 📖 [README.md](README.md) - Project overview
- 🗄️ [DATABASE_SETUP.md](DATABASE_SETUP.md) - Database setup
- 🔐 [JWT_AUTHENTICATION_GUIDE.md](JWT_AUTHENTICATION_GUIDE.md) - Authentication
- 🚀 This guide - Startup instructions

### Troubleshooting Steps

1. **Check all prerequisites** are installed and correct versions
2. **Review error messages** carefully
3. **Check logs** in terminal output
4. **Verify database** is running and accessible
5. **Check port availability** (8080, 3000, 3306)
6. **Review configuration files** for correct settings

### Common Commands

```bash
# Check if services are running
lsof -i :8080    # Backend
lsof -i :3000    # Frontend
lsof -i :3306    # MySQL

# View running Java processes
jps -l

# View running Node processes
ps aux | grep node

# Check disk space
df -h

# Check memory usage
free -h    # Linux
vm_stat    # macOS
```

---

## Success Indicators

You know everything is working when:

✅ Backend terminal shows: `Started HospitalApplication in X.XXX seconds`  
✅ Frontend terminal shows: `✓ Ready in Xs`  
✅ Browser loads: http://localhost:3000  
✅ Swagger UI accessible: http://localhost:8080/swagger-ui.html  
✅ Can login with default credentials  
✅ No error messages in terminal or browser console  

---

## Next Steps

After successful startup:

1. **Login** with default credentials
2. **Explore** the application features
3. **Review** API documentation in Swagger UI
4. **Check** database tables in MySQL
5. **Test** different user roles
6. **Read** comprehensive improvements documentation

---

**Happy Developing! 🎉**

For issues or questions, refer to the documentation or check the project's issue tracker.

---

**Last Updated**: January 2025  
**Version**: 2.0.0  
**Compatibility**: Linux, macOS, Windows