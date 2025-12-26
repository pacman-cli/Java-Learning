@echo off
REM ============================================================
REM ZedCode Backend - Windows Startup Script
REM ============================================================

echo.
echo ============================================================
echo    ZedCode Backend - Spring Boot Application
echo ============================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17 or higher
    pause
    exit /b 1
)

echo [INFO] Java installation found
java -version
echo.

REM Check if Maven is installed
call mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven 3.6 or higher
    pause
    exit /b 1
)

echo [INFO] Maven installation found
echo.

REM Display menu
:menu
echo ============================================================
echo Choose an option:
echo ============================================================
echo 1. Build and Run (Development Profile)
echo 2. Build and Run (Production Profile)
echo 3. Run without building (Development Profile)
echo 4. Run tests
echo 5. Clean and Build
echo 6. Exit
echo ============================================================
echo.

set /p choice="Enter your choice (1-6): "

if "%choice%"=="1" goto run_dev
if "%choice%"=="2" goto run_prod
if "%choice%"=="3" goto run_only
if "%choice%"=="4" goto test
if "%choice%"=="5" goto clean_build
if "%choice%"=="6" goto end

echo [ERROR] Invalid choice. Please try again.
echo.
goto menu

:run_dev
echo.
echo [INFO] Building and running application with Development profile...
echo.
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)
echo.
echo [INFO] Starting application on http://localhost:8080/api
echo [INFO] Swagger UI: http://localhost:8080/api/swagger-ui.html
echo [INFO] H2 Console: http://localhost:8080/api/h2-console
echo.
call mvn spring-boot:run -Dspring-boot.run.profiles=dev
goto end

:run_prod
echo.
echo [INFO] Building and running application with Production profile...
echo.
call mvn clean install -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)
echo.
echo [INFO] Starting application in Production mode...
echo.
call mvn spring-boot:run -Dspring-boot.run.profiles=prod
goto end

:run_only
echo.
echo [INFO] Running application without building (Development profile)...
echo.
echo [INFO] Starting application on http://localhost:8080/api
echo [INFO] Swagger UI: http://localhost:8080/api/swagger-ui.html
echo [INFO] H2 Console: http://localhost:8080/api/h2-console
echo.
call mvn spring-boot:run -Dspring-boot.run.profiles=dev
goto end

:test
echo.
echo [INFO] Running tests...
echo.
call mvn clean test
if %errorlevel% neq 0 (
    echo [ERROR] Tests failed
    pause
    exit /b 1
)
echo.
echo [INFO] All tests passed successfully!
echo.
pause
goto menu

:clean_build
echo.
echo [INFO] Cleaning and building project...
echo.
call mvn clean install
if %errorlevel% neq 0 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)
echo.
echo [INFO] Build completed successfully!
echo.
pause
goto menu

:end
echo.
echo [INFO] Exiting...
echo.
exit /b 0
