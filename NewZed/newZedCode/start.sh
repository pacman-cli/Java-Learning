#!/bin/bash

# ZedCode Backend Startup Script
# This script helps you start the Spring Boot application with various profiles

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Print banner
print_banner() {
    echo -e "${BLUE}"
    echo "╔════════════════════════════════════════════════════╗"
    echo "║        ZedCode Backend - Startup Script          ║"
    echo "║              Spring Boot Application              ║"
    echo "╚════════════════════════════════════════════════════╝"
    echo -e "${NC}"
}

# Print usage
print_usage() {
    echo -e "${YELLOW}Usage:${NC}"
    echo "  ./start.sh [OPTION]"
    echo ""
    echo -e "${YELLOW}Options:${NC}"
    echo "  dev         Start with development profile (default)"
    echo "  prod        Start with production profile"
    echo "  test        Start with test profile"
    echo "  build       Build the project without running"
    echo "  clean       Clean and build the project"
    echo "  help        Show this help message"
    echo ""
    echo -e "${YELLOW}Examples:${NC}"
    echo "  ./start.sh              # Start with dev profile"
    echo "  ./start.sh dev          # Start with dev profile"
    echo "  ./start.sh prod         # Start with prod profile"
    echo "  ./start.sh build        # Build only"
    echo ""
}

# Check if Java is installed
check_java() {
    if ! command -v java &> /dev/null; then
        echo -e "${RED}ERROR: Java is not installed!${NC}"
        echo "Please install Java 17 or higher and try again."
        exit 1
    fi

    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        echo -e "${RED}ERROR: Java 17 or higher is required!${NC}"
        echo "Current version: Java $JAVA_VERSION"
        exit 1
    fi

    echo -e "${GREEN}✓ Java version check passed${NC}"
}

# Check if Maven is installed
check_maven() {
    if ! command -v mvn &> /dev/null; then
        echo -e "${RED}ERROR: Maven is not installed!${NC}"
        echo "Please install Maven and try again."
        exit 1
    fi
    echo -e "${GREEN}✓ Maven check passed${NC}"
}

# Build the project
build_project() {
    echo -e "${YELLOW}Building project...${NC}"
    mvn clean package -DskipTests
    echo -e "${GREEN}✓ Build completed successfully${NC}"
}

# Clean and build the project
clean_build() {
    echo -e "${YELLOW}Cleaning and building project...${NC}"
    mvn clean install
    echo -e "${GREEN}✓ Clean build completed successfully${NC}"
}

# Start the application
start_app() {
    local profile=$1
    echo -e "${GREEN}Starting application with profile: ${profile}${NC}"
    echo -e "${YELLOW}Access points:${NC}"
    echo "  - API Base URL:    http://localhost:8080/api"
    echo "  - Swagger UI:      http://localhost:8080/api/swagger-ui.html"
    echo "  - H2 Console:      http://localhost:8080/api/h2-console (dev only)"
    echo "  - Actuator:        http://localhost:8080/api/actuator"
    echo ""
    echo -e "${YELLOW}Press Ctrl+C to stop the application${NC}"
    echo ""

    mvn spring-boot:run -Dspring-boot.run.profiles=$profile
}

# Main script
main() {
    print_banner

    # Parse command line arguments
    PROFILE="dev"

    if [ $# -eq 0 ]; then
        PROFILE="dev"
    else
        case "$1" in
            dev)
                PROFILE="dev"
                ;;
            prod)
                PROFILE="prod"
                ;;
            test)
                PROFILE="test"
                ;;
            build)
                check_java
                check_maven
                build_project
                exit 0
                ;;
            clean)
                check_java
                check_maven
                clean_build
                exit 0
                ;;
            help|-h|--help)
                print_usage
                exit 0
                ;;
            *)
                echo -e "${RED}ERROR: Unknown option: $1${NC}"
                echo ""
                print_usage
                exit 1
                ;;
        esac
    fi

    # Check prerequisites
    check_java
    check_maven

    # Start the application
    start_app $PROFILE
}

# Run main function
main "$@"
