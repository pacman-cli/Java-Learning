@echo off
REM =============================================================================
REM Hospital Management System - Frontend Startup Script (Windows)
REM =============================================================================
REM This script starts only the frontend application (Next.js)
REM =============================================================================

setlocal enabledelayedexpansion

REM Color codes are limited in batch, using echo for messages
echo.
echo ==================================================
echo Hospital Management System - Frontend (Windows)
echo ==================================================
echo.

REM Function to check Node.js
echo [CHECK] Checking Node.js installation...
where node >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Node.js is not installed!
    echo Please install Node.js 18+ from: https://nodejs.org/
    pause
    exit /b 1
)

REM Check Node.js version
for /f "tokens=*" %%i in ('node -v') do set NODE_VERSION=%%i
echo [OK] Node.js version: %NODE_VERSION%

REM Function to check npm
echo [CHECK] Checking npm installation...
where npm >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] npm is not installed!
    pause
    exit /b 1
)

for /f "tokens=*" %%i in ('npm -v') do set NPM_VERSION=%%i
echo [OK] npm version: %NPM_VERSION%

REM Check if frontend directory exists
echo [CHECK] Checking frontend directory...
if not exist "frontend" (
    echo [ERROR] Frontend directory not found!
    echo Please run this script from the hospital management system root directory.
    pause
    exit /b 1
)
echo [OK] Frontend directory found

REM Change to frontend directory
cd frontend

REM Check if node_modules exists and is valid
echo [CHECK] Checking dependencies...
set NEED_INSTALL=0

if not exist "node_modules" (
    set NEED_INSTALL=1
) else (
    REM Check if Next.js is properly installed
    if not exist "node_modules\next\dist\server\require-hook.js" (
        echo [WARNING] Next.js installation appears corrupted!
        set NEED_INSTALL=1
    )
)

if %NEED_INSTALL%==1 (
    echo.
    echo ==================================================
    echo Installing Frontend Dependencies
    echo ==================================================
    echo This may take a few minutes...
    echo.

    REM Clean up corrupted installation
    if exist "node_modules" (
        echo [INFO] Cleaning up corrupted node_modules...
        rmdir /s /q node_modules 2>nul
        del package-lock.json 2>nul
        rmdir /s /q .next 2>nul
    )

    call npm install
    if %ERRORLEVEL% NEQ 0 (
        echo [ERROR] Failed to install dependencies!
        cd ..
        pause
        exit /b 1
    )
    echo [OK] Dependencies installed successfully
) else (
    echo [OK] Dependencies already installed
)

REM Check environment file
echo [CHECK] Checking environment configuration...
if not exist ".env.local" (
    echo [WARNING] .env.local file not found!
    echo Creating .env.local with default settings...
    (
        echo # API Configuration
        echo NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
        echo.
        echo # Application Configuration
        echo NEXT_PUBLIC_APP_NAME="Hospital Management System"
        echo NEXT_PUBLIC_APP_VERSION="2.0.0"
        echo.
        echo # Feature Flags
        echo NEXT_PUBLIC_ENABLE_REGISTRATION=true
        echo NEXT_PUBLIC_ENABLE_DARK_MODE=true
        echo NEXT_PUBLIC_ENABLE_NOTIFICATIONS=true
        echo.
        echo # File Upload Configuration
        echo NEXT_PUBLIC_MAX_FILE_SIZE=10485760
        echo NEXT_PUBLIC_ALLOWED_FILE_TYPES="image/*,application/pdf,.doc,.docx"
    ) > .env.local
    echo [OK] Created .env.local with default settings
) else (
    echo [OK] Environment file found
)

REM Check if port 3000 is in use
echo [CHECK] Checking if port 3000 is available...
netstat -ano | findstr :3000 | findstr LISTENING >nul 2>&1
if %ERRORLEVEL% EQU 0 (
    echo [WARNING] Port 3000 is already in use!
    echo Please close the application using port 3000 or the script will attempt to use another port.
)

echo.
echo ==================================================
echo Starting Frontend Application
echo ==================================================
echo.
echo Frontend will be available at: http://localhost:3000
echo Press Ctrl+C to stop the server
echo.
echo [INFO] You may see warnings about next.config.ts - these are safe to ignore
echo [INFO] The server will still run correctly. Look for 'Ready in Xs' message.
echo.

REM Start the development server
call npm run dev

set EXIT_CODE=%ERRORLEVEL%

REM Return to root directory
cd ..

REM If exit with error, provide helpful message
REM Note: Exit code 1 can occur from warnings but server might still be running
if %EXIT_CODE% NEQ 0 (
    if %EXIT_CODE% NEQ 3221225786 (
        echo.
        echo [WARNING] Server stopped with exit code: %EXIT_CODE%
        echo [INFO] If the server was running but showing warnings, that's normal
        echo [INFO] If it didn't start at all, try deleting:
        echo   - frontend\node_modules
        echo   - frontend\package-lock.json
        echo   - frontend\.next
        echo.
        echo Or run: rmdir /s /q frontend\node_modules frontend\.next ^&^& del frontend\package-lock.json
        echo.
    )
)

pause
