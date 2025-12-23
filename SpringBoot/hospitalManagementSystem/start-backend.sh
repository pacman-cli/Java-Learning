#!/bin/bash

# =============================================================================
# Hospital Management System - Backend Startup Script
# =============================================================================
# This script starts only the backend application (Spring Boot)
# =============================================================================

# Color codes for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_message() {
    local color=$1
    local message=$2
    echo -e "${color}${message}${NC}"
}

# Function to print section headers
print_header() {
    echo ""
    print_message "$BLUE" "=============================================="
    print_message "$BLUE" "$1"
    print_message "$BLUE" "=============================================="
}

# Function to check if Java is installed
check_java() {
    if ! command -v java &> /dev/null; then
        print_message "$RED" "❌ Error: Java is not installed!"
        print_message "$YELLOW" "Please install Java 17+ from: https://www.oracle.com/java/technologies/downloads/"
        exit 1
    fi

    local java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$java_version" -lt 17 ]; then
        print_message "$RED" "❌ Error: Java 17+ is required!"
        print_message "$YELLOW" "Current version: $(java -version 2>&1 | head -n 1)"
        print_message "$YELLOW" "Please upgrade Java from: https://www.oracle.com/java/technologies/downloads/"
        exit 1
    fi

    print_message "$GREEN" "✓ Java version: $(java -version 2>&1 | head -n 1 | cut -d'"' -f2)"
}

# Function to check if Maven is installed
check_maven() {
    if [ ! -f "hospital/mvnw" ]; then
        if ! command -v mvn &> /dev/null; then
            print_message "$RED" "❌ Error: Maven is not installed and mvnw not found!"
            print_message "$YELLOW" "Please install Maven 3.8+ from: https://maven.apache.org/download.cgi"
            exit 1
        fi
        print_message "$GREEN" "✓ Maven version: $(mvn -v | head -n 1)"
    else
        print_message "$GREEN" "✓ Maven wrapper found"
    fi
}

# Function to check if backend directory exists
check_backend_directory() {
    if [ ! -d "hospital" ]; then
        print_message "$RED" "❌ Error: Backend directory (hospital) not found!"
        print_message "$YELLOW" "Please run this script from the hospital management system root directory."
        exit 1
    fi
    print_message "$GREEN" "✓ Backend directory found"
}

# Function to check MySQL connection
check_mysql() {
    print_message "$YELLOW" "⏳ Checking MySQL connection..."

    if ! command -v mysql &> /dev/null; then
        print_message "$YELLOW" "⚠️  MySQL client not found in PATH"
        print_message "$YELLOW" "⚠️  Skipping database connection check"
        print_message "$YELLOW" "⚠️  Please ensure MySQL is running on localhost:3306"
        return
    fi

    # Try to connect to MySQL (you may need to adjust credentials)
    if mysql -h localhost -P 3306 -e "SELECT 1;" &> /dev/null; then
        print_message "$GREEN" "✓ MySQL is running and accessible"
    else
        print_message "$YELLOW" "⚠️  Warning: Cannot connect to MySQL on localhost:3306"
        print_message "$YELLOW" "⚠️  Please ensure MySQL is running before starting the backend"
        print_message "$YELLOW" "⚠️  Check DATABASE_SETUP.md for database configuration"
        echo ""
        read -p "Do you want to continue anyway? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_message "$RED" "Startup cancelled by user"
            exit 1
        fi
    fi
}

# Function to check database exists
check_database() {
    if command -v mysql &> /dev/null; then
        if mysql -h localhost -P 3306 -e "USE hospital_db;" 2>/dev/null; then
            print_message "$GREEN" "✓ Database 'hospital_db' exists"
        else
            print_message "$YELLOW" "⚠️  Warning: Database 'hospital_db' does not exist!"
            print_message "$YELLOW" "⚠️  The application will try to create it via Flyway"
            print_message "$YELLOW" "⚠️  Check DATABASE_SETUP.md for manual database setup"
        fi
    fi
}

# Function to check application.properties
check_config() {
    if [ ! -f "hospital/src/main/resources/application.properties" ]; then
        print_message "$RED" "❌ Error: application.properties not found!"
        exit 1
    fi
    print_message "$GREEN" "✓ Configuration file found"

    print_message "$YELLOW" "⚠️  Please ensure database credentials in application.properties are correct:"
    print_message "$YELLOW" "   - spring.datasource.url"
    print_message "$YELLOW" "   - spring.datasource.username"
    print_message "$YELLOW" "   - spring.datasource.password"
}

# Function to check if port 8080 is available
check_port() {
    if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        print_message "$YELLOW" "⚠️  Warning: Port 8080 is already in use!"
        print_message "$YELLOW" "Attempting to kill the process..."
        kill -9 $(lsof -t -i:8080) 2>/dev/null
        sleep 2

        if lsof -Pi :8080 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
            print_message "$RED" "❌ Failed to free port 8080"
            print_message "$YELLOW" "Please manually stop the process using port 8080"
            exit 1
        fi
        print_message "$GREEN" "✓ Port 8080 is now available"
    else
        print_message "$GREEN" "✓ Port 8080 is available"
    fi
}

# Function to clean previous builds
clean_build() {
    print_message "$YELLOW" "🧹 Cleaning previous builds..."
    cd hospital

    if [ -f "mvnw" ]; then
        ./mvnw clean -q
    else
        mvn clean -q
    fi

    if [ $? -eq 0 ]; then
        print_message "$GREEN" "✓ Previous builds cleaned"
    else
        print_message "$YELLOW" "⚠️  Warning: Could not clean previous builds"
    fi

    cd ..
}

# Function to start the backend
start_backend() {
    print_header "🚀 Starting Backend Application"

    cd hospital

    print_message "$GREEN" "Building and starting Spring Boot application..."
    print_message "$BLUE" "Backend will be available at: http://localhost:8080"
    print_message "$BLUE" "API Documentation (Swagger): http://localhost:8080/swagger-ui.html"
    print_message "$YELLOW" "Press Ctrl+C to stop the server"
    echo ""

    # Start the Spring Boot application
    if [ -f "mvnw" ]; then
        ./mvnw spring-boot:run
    else
        mvn spring-boot:run
    fi
}

# Function to display helpful information
display_info() {
    print_header "📋 Useful Information"
    echo ""
    print_message "$GREEN" "Backend Endpoints:"
    print_message "$BLUE" "  • Application:        http://localhost:8080"
    print_message "$BLUE" "  • API Documentation:  http://localhost:8080/swagger-ui.html"
    print_message "$BLUE" "  • API Docs (JSON):    http://localhost:8080/v3/api-docs"
    print_message "$BLUE" "  • Health Check:       http://localhost:8080/actuator/health"
    echo ""
    print_message "$GREEN" "Database Information:"
    print_message "$BLUE" "  • Host:      localhost:3306"
    print_message "$BLUE" "  • Database:  hospital_db"
    print_message "$BLUE" "  • User:      (check application.properties)"
    echo ""
    print_message "$GREEN" "Default Users:"
    print_message "$BLUE" "  • Admin:    admin / admin123"
    print_message "$BLUE" "  • Doctor:   doctor / doctor123"
    print_message "$BLUE" "  • Patient:  patient / patient123"
    echo ""
    print_message "$YELLOW" "For database setup, see: DATABASE_SETUP.md"
    echo ""
}

# Main execution
main() {
    print_header "🏥 Hospital Management System - Backend"

    # Run all checks
    print_header "🔍 Pre-flight Checks"
    check_java
    check_maven
    check_backend_directory
    check_config
    check_mysql
    check_database
    check_port

    # Optional: clean build
    read -p "Do you want to clean previous builds? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        clean_build
    fi

    # Display useful information
    display_info

    # Start the backend
    start_backend
}

# Run main function
main
