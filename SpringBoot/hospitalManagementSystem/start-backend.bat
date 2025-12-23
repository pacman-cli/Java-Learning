@echo off
REM =============================================================================
REM Hospital Management System - Backend Startup Script (Windows)
REM =============================================================================
REM This script starts only the backend application (Spring Boot)
REM =============================================================================

setlocal enabledelayedexpansion

REM Color codes are limited in batch, using echo for messages
echo.
echo ==================================================
echo Hospital Management System - Backend (Windows)
echo ==================================================
echo.

REM Function to check Java
echo [CHECK] Checking Java installation...
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not installed!
    echo Please install Java 17+ from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Check Java version
for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do set JAVA_VERSION=%%i
set JAVA_VERSION=%JAVA_VERSION:"=%
echo [OK] Java version: %JAVA_VERSION%

REM Check if backend directory exists
echo [CHECK] Checking backend directory...
if not exist "hospital" (
    echo [ERROR] Backend directory (hospital) not found!
    echo Please run this script from the hospital management system root directory.
    pause
    exit /b 1
)
echo [OK] Backend directory found

REM Check if Maven wrapper exists
echo [CHECK] Checking build tool...
if exist "hospital\mvnw.cmd" (
    echo [OK] Maven wrapper found
    set MAVEN_CMD=mvnw.cmd
) else (
    where mvn >nul 2>&1
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Maven is not installed and mvnw not found!
        echo Please install Maven 3.8+ from: https://maven.apache.org/download.cgi
        pause
        exit /b 1
    )
    echo [OK] Maven found in PATH
    set MAVEN_CMD=mvn
)

REM Check configuration file
echo [CHECK] Checking configuration file...
if not exist "hospital\src\main\resources\application.properties" (
    echo [ERROR] application.properties not found!
    pause
    exit /b 1
)
echo [OK] Configuration file found

echo.
echo ==================================================
echo Important Configuration Notes
echo ==================================================
echo.
echo Please ensure the following are configured correctly in application.properties:
echo   - spring.datasource.url
echo   - spring.datasource.username
echo   - spring.datasource.password
echo.
echo Database should be running on: localhost:3306
echo Database name: hospital_db
echo.
echo For database setup instructions, see: DATABASE_SETUP.md
echo.

REM Check MySQL connection (basic check)
echo [CHECK] Checking if MySQL port is accessible...
netstat -ano | findstr :3306 | findstr LISTENING >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [WARNING] MySQL does not appear to be running on port 3306!
    echo Please ensure MySQL is started before continuing.
    echo.
    set /p CONTINUE="Do you want to continue anyway? (Y/N): "
    if /i not "!CONTINUE!"=="Y" (
        echo Startup cancelled by user.
        pause
        exit /b 1
    )
) else (
    echo [OK] MySQL port 3306 is accessible
)

REM Check if port 8080 is in use
echo [CHECK] Checking if port 8080 is available...
netstat -ano | findstr :8080 | findstr LISTENING >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [WARNING] Port 8080 is already in use!
    echo Attempting to find and terminate the process...
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :8080 ^| findstr LISTENING') do (
        echo Killing process ID: %%a
        taskkill /F /PID %%a >nul 2>&1
    )
    timeout /t 2 /nobreak >nul
    netstat -ano | findstr :8080 | findstr LISTENING >nul 2>&1
    if %ERRORLEVEL% EQU 0 (
        echo [ERROR] Failed to free port 8080
        echo Please manually stop the process using port 8080
        pause
        exit /b 1
    )
    echo [OK] Port 8080 is now available
) else (
    echo [OK] Port 8080 is available
)

REM Ask if user wants to clean build
echo.
set /p CLEAN="Do you want to clean previous builds? (Y/N): "
if /i "%CLEAN%"=="Y" (
    echo.
    echo [INFO] Cleaning previous builds...
    cd hospital
    call %MAVEN_CMD% clean -q
    cd ..
    echo [OK] Previous builds cleaned
)

echo.
echo ==================================================
echo Useful Information
echo ==================================================
echo.
echo Backend Endpoints:
echo   - Application:        http://localhost:8080
echo   - API Documentation:  http://localhost:8080/swagger-ui.html
echo   - API Docs (JSON):    http://localhost:8080/v3/api-docs
echo.
echo Database Information:
echo   - Host:      localhost:3306
echo   - Database:  hospital_db
echo   - User:      (check application.properties)
echo.
echo Default Users:
echo   - Admin:    admin / admin123
echo   - Doctor:   doctor / doctor123
echo   - Patient:  patient / patient123
echo.
echo For database setup, see: DATABASE_SETUP.md
echo.

echo.
echo ==================================================
echo Starting Backend Application
echo ==================================================
echo.
echo Building and starting Spring Boot application...
echo This may take a few minutes on first run...
echo.
echo Press Ctrl+C to stop the server
echo.

REM Change to backend directory
cd hospital

REM Start the Spring Boot application
call %MAVEN_CMD% spring-boot:run

REM Return to root directory
cd ..

pause
