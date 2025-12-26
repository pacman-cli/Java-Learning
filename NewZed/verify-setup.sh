#!/bin/bash

# =============================================================================
# User Management System - Complete Verification Script
# =============================================================================
# This script checks the entire setup for errors and verifies everything works
#
# Usage:
#   chmod +x verify-setup.sh
#   ./verify-setup.sh
#
# Author: ZedCode
# =============================================================================

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color
BOLD='\033[1m'

# Counters
TOTAL_CHECKS=0
PASSED_CHECKS=0
FAILED_CHECKS=0
WARNING_CHECKS=0

# =============================================================================
# UTILITY FUNCTIONS
# =============================================================================

print_header() {
    echo -e "\n${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${BOLD}${BLUE}  $1${NC}"
    echo -e "${BOLD}${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}\n"
}

print_section() {
    echo -e "\n${BOLD}${CYAN}▶ $1${NC}"
}

print_check() {
    echo -n "  Checking $1... "
    TOTAL_CHECKS=$((TOTAL_CHECKS + 1))
}

print_pass() {
    echo -e "${GREEN}✓ PASS${NC}"
    if [ ! -z "$1" ]; then
        echo -e "    ${GREEN}→ $1${NC}"
    fi
    PASSED_CHECKS=$((PASSED_CHECKS + 1))
}

print_fail() {
    echo -e "${RED}✗ FAIL${NC}"
    if [ ! -z "$1" ]; then
        echo -e "    ${RED}→ $1${NC}"
    fi
    FAILED_CHECKS=$((FAILED_CHECKS + 1))
}

print_warning() {
    echo -e "${YELLOW}⚠ WARNING${NC}"
    if [ ! -z "$1" ]; then
        echo -e "    ${YELLOW}→ $1${NC}"
    fi
    WARNING_CHECKS=$((WARNING_CHECKS + 1))
}

print_info() {
    echo -e "    ${BLUE}ℹ $1${NC}"
}

# =============================================================================
# PREREQUISITE CHECKS
# =============================================================================

check_prerequisites() {
    print_header "1. CHECKING PREREQUISITES"

    # Check Node.js
    print_check "Node.js installation"
    if command -v node &> /dev/null; then
        NODE_VERSION=$(node --version)
        NODE_MAJOR=$(echo $NODE_VERSION | cut -d'.' -f1 | sed 's/v//')
        if [ "$NODE_MAJOR" -ge 18 ]; then
            print_pass "Node.js $NODE_VERSION (✓ v18+)"
        else
            print_fail "Node.js $NODE_VERSION (requires v18+)"
        fi
    else
        print_fail "Node.js not installed"
        print_info "Install from: https://nodejs.org/"
    fi

    # Check npm
    print_check "npm installation"
    if command -v npm &> /dev/null; then
        NPM_VERSION=$(npm --version)
        print_pass "npm v$NPM_VERSION"
    else
        print_fail "npm not installed"
    fi

    # Check Java
    print_check "Java installation"
    if command -v java &> /dev/null; then
        JAVA_VERSION=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}')
        print_pass "Java $JAVA_VERSION"
    else
        print_fail "Java not installed"
        print_info "Install from: https://www.oracle.com/java/technologies/downloads/"
    fi

    # Check Maven
    print_check "Maven installation"
    if command -v mvn &> /dev/null; then
        MVN_VERSION=$(mvn --version | head -n 1 | awk '{print $3}')
        print_pass "Maven $MVN_VERSION"
    else
        print_fail "Maven not installed"
        print_info "Install from: https://maven.apache.org/download.cgi"
    fi

    # Check Git
    print_check "Git installation"
    if command -v git &> /dev/null; then
        GIT_VERSION=$(git --version | awk '{print $3}')
        print_pass "Git $GIT_VERSION"
    else
        print_warning "Git not installed (optional but recommended)"
    fi
}

# =============================================================================
# BACKEND VERIFICATION
# =============================================================================

check_backend() {
    print_header "2. CHECKING BACKEND (Spring Boot)"

    # Check backend directory
    print_check "Backend directory structure"
    if [ -d "newZedCode" ]; then
        print_pass "Directory exists"
    else
        print_fail "Backend directory 'newZedCode' not found"
        return
    fi

    # Check pom.xml
    print_check "Maven configuration (pom.xml)"
    if [ -f "newZedCode/pom.xml" ]; then
        print_pass "pom.xml exists"
    else
        print_fail "pom.xml not found"
    fi

    # Check application.yml
    print_check "Application configuration"
    if [ -f "newZedCode/src/main/resources/application.yml" ]; then
        print_pass "application.yml exists"
    else
        print_fail "application.yml not found"
    fi

    # Check main application class
    print_check "Main application class"
    if [ -f "newZedCode/src/main/java/com/zedcode/ZedCodeApplication.java" ]; then
        print_pass "ZedCodeApplication.java exists"
    else
        print_fail "Main application class not found"
    fi

    # Check if backend is running
    print_check "Backend server status"
    if curl -s -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        print_pass "Backend is running on port 8080"
    else
        print_warning "Backend is not running"
        print_info "Start with: cd newZedCode && mvn spring-boot:run"
    fi

    # Check backend API endpoint
    if curl -s -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        print_check "Backend health check"
        HEALTH=$(curl -s http://localhost:8080/actuator/health)
        if echo $HEALTH | grep -q '"status":"UP"'; then
            print_pass "Health status: UP"
        else
            print_warning "Health status: Unknown"
        fi
    fi
}

# =============================================================================
# FRONTEND VERIFICATION
# =============================================================================

check_frontend() {
    print_header "3. CHECKING FRONTEND (Next.js)"

    # Check frontend directory
    print_check "Frontend directory structure"
    if [ -d "frontend" ]; then
        print_pass "Directory exists"
    else
        print_fail "Frontend directory not found"
        return
    fi

    cd frontend

    # Check package.json
    print_check "Package configuration (package.json)"
    if [ -f "package.json" ]; then
        print_pass "package.json exists"
    else
        print_fail "package.json not found"
        cd ..
        return
    fi

    # Check node_modules
    print_check "Dependencies installation"
    if [ -d "node_modules" ]; then
        print_pass "node_modules directory exists"
    else
        print_warning "Dependencies not installed"
        print_info "Run: npm install"
    fi

    # Check TypeScript config
    print_check "TypeScript configuration (tsconfig.json)"
    if [ -f "tsconfig.json" ]; then
        print_pass "tsconfig.json exists"
    else
        print_fail "tsconfig.json not found"
    fi

    # Check Tailwind config
    print_check "Tailwind CSS configuration"
    if [ -f "tailwind.config.ts" ]; then
        print_pass "tailwind.config.ts exists"
    else
        print_fail "tailwind.config.ts not found"
    fi

    # Check Next.js config
    print_check "Next.js configuration (next.config.js)"
    if [ -f "next.config.js" ]; then
        print_pass "next.config.js exists"
    else
        print_fail "next.config.js not found"
    fi

    # Check .env.local
    print_check "Environment configuration (.env.local)"
    if [ -f ".env.local" ]; then
        print_pass ".env.local exists"

        # Check if API URL is configured
        if grep -q "NEXT_PUBLIC_API_URL" .env.local; then
            API_URL=$(grep "NEXT_PUBLIC_API_URL" .env.local | cut -d'=' -f2)
            print_info "API URL: $API_URL"
        fi
    else
        print_warning ".env.local not found"
        print_info "Copy from .env.example or create with API URL"
    fi

    # Check source files
    print_check "Core source files"
    MISSING_FILES=()
    [ ! -f "src/lib/api.ts" ] && MISSING_FILES+=("api.ts")
    [ ! -f "src/lib/types.ts" ] && MISSING_FILES+=("types.ts")
    [ ! -f "src/lib/utils.ts" ] && MISSING_FILES+=("utils.ts")
    [ ! -f "src/app/layout.tsx" ] && MISSING_FILES+=("layout.tsx")
    [ ! -f "src/app/page.tsx" ] && MISSING_FILES+=("page.tsx")
    [ ! -f "src/app/globals.css" ] && MISSING_FILES+=("globals.css")

    if [ ${#MISSING_FILES[@]} -eq 0 ]; then
        print_pass "All core files present"
    else
        print_fail "Missing files: ${MISSING_FILES[*]}"
    fi

    # Check TypeScript compilation
    if [ -d "node_modules" ] && command -v npm &> /dev/null; then
        print_check "TypeScript type checking"
        if npm run type-check > /dev/null 2>&1; then
            print_pass "No TypeScript errors"
        else
            print_fail "TypeScript errors found"
            print_info "Run 'npm run type-check' for details"
        fi
    fi

    # Check if frontend is running
    print_check "Frontend server status"
    if curl -s -f http://localhost:3000 > /dev/null 2>&1; then
        print_pass "Frontend is running on port 3000"
    else
        print_warning "Frontend is not running"
        print_info "Start with: npm run dev"
    fi

    cd ..
}

# =============================================================================
# API CONNECTIVITY CHECK
# =============================================================================

check_api_connectivity() {
    print_header "4. CHECKING API CONNECTIVITY"

    # Check if backend is running
    print_check "Backend API availability"
    if curl -s -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
        print_pass "Backend API is accessible"
    else
        print_warning "Backend API is not accessible"
        print_info "Start backend first: cd newZedCode && mvn spring-boot:run"
        return
    fi

    # Test user stats endpoint
    print_check "User stats endpoint"
    RESPONSE=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/v1/users/stats 2>/dev/null)
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

    if [ "$HTTP_CODE" = "200" ]; then
        print_pass "Endpoint responding (HTTP 200)"
    else
        print_fail "Endpoint error (HTTP $HTTP_CODE)"
    fi

    # Test users list endpoint
    print_check "Users list endpoint"
    RESPONSE=$(curl -s -w "\n%{http_code}" http://localhost:8080/api/v1/users?page=0&size=1 2>/dev/null)
    HTTP_CODE=$(echo "$RESPONSE" | tail -n1)

    if [ "$HTTP_CODE" = "200" ]; then
        print_pass "Endpoint responding (HTTP 200)"
    else
        print_fail "Endpoint error (HTTP $HTTP_CODE)"
    fi

    # Check CORS configuration
    print_check "CORS configuration"
    CORS_HEADER=$(curl -s -I -H "Origin: http://localhost:3000" http://localhost:8080/api/v1/users/stats 2>/dev/null | grep -i "access-control-allow-origin")

    if [ ! -z "$CORS_HEADER" ]; then
        print_pass "CORS headers present"
    else
        print_warning "CORS headers not found"
        print_info "Frontend may have connection issues"
    fi

    # Check Swagger documentation
    print_check "Swagger documentation"
    if curl -s -f http://localhost:8080/api/swagger-ui.html > /dev/null 2>&1; then
        print_pass "Swagger UI accessible"
        print_info "Visit: http://localhost:8080/api/swagger-ui.html"
    else
        print_warning "Swagger UI not accessible"
    fi
}

# =============================================================================
# DOCUMENTATION CHECK
# =============================================================================

check_documentation() {
    print_header "5. CHECKING DOCUMENTATION"

    DOCS=(
        "README_COMPLETE_SETUP.md:Complete Setup Guide"
        "FRONTEND_IMPLEMENTATION_GUIDE.md:Frontend Implementation Guide"
        "QUICK_START_CARD.md:Quick Start Card"
        "newZedCode/API_GUIDE.md:API Guide"
        "newZedCode/ARCHITECTURE.md:Architecture Guide"
        "newZedCode/START_HERE.md:Beginner's Guide"
        "frontend/README.md:Frontend README"
    )

    for doc in "${DOCS[@]}"; do
        FILE=$(echo $doc | cut -d':' -f1)
        NAME=$(echo $doc | cut -d':' -f2)

        print_check "$NAME"
        if [ -f "$FILE" ]; then
            print_pass "Available"
        else
            print_warning "Not found: $FILE"
        fi
    done
}

# =============================================================================
# SUMMARY REPORT
# =============================================================================

print_summary() {
    print_header "VERIFICATION SUMMARY"

    echo -e "${BOLD}Total Checks: $TOTAL_CHECKS${NC}"
    echo -e "${GREEN}✓ Passed: $PASSED_CHECKS${NC}"
    echo -e "${RED}✗ Failed: $FAILED_CHECKS${NC}"
    echo -e "${YELLOW}⚠ Warnings: $WARNING_CHECKS${NC}"

    echo ""

    if [ $FAILED_CHECKS -eq 0 ]; then
        echo -e "${GREEN}${BOLD}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo -e "${GREEN}${BOLD}  ✓ ALL CRITICAL CHECKS PASSED!${NC}"
        echo -e "${GREEN}${BOLD}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        echo -e "${GREEN}Your setup is ready to use! 🎉${NC}"
        echo ""
        echo -e "${CYAN}Quick Start:${NC}"
        echo -e "  1. Start backend: ${YELLOW}cd newZedCode && mvn spring-boot:run${NC}"
        echo -e "  2. Start frontend: ${YELLOW}cd frontend && npm run dev${NC}"
        echo -e "  3. Open browser: ${YELLOW}http://localhost:3000${NC}"
    else
        echo -e "${RED}${BOLD}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo -e "${RED}${BOLD}  ✗ SOME CHECKS FAILED${NC}"
        echo -e "${RED}${BOLD}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
        echo ""
        echo -e "${RED}Please fix the failed checks above.${NC}"
        echo ""
        echo -e "${CYAN}Common Solutions:${NC}"
        echo -e "  • Missing Node.js: ${YELLOW}https://nodejs.org/${NC}"
        echo -e "  • Missing Java: ${YELLOW}https://www.oracle.com/java/technologies/downloads/${NC}"
        echo -e "  • Missing dependencies: ${YELLOW}cd frontend && npm install${NC}"
        echo -e "  • Backend not running: ${YELLOW}cd newZedCode && mvn spring-boot:run${NC}"
    fi

    if [ $WARNING_CHECKS -gt 0 ]; then
        echo ""
        echo -e "${YELLOW}Note: Warnings indicate non-critical issues that should be addressed.${NC}"
    fi

    echo ""
    echo -e "${CYAN}Documentation:${NC}"
    echo -e "  • Complete Setup: ${YELLOW}README_COMPLETE_SETUP.md${NC}"
    echo -e "  • Quick Start: ${YELLOW}QUICK_START_CARD.md${NC}"
    echo -e "  • API Guide: ${YELLOW}newZedCode/API_GUIDE.md${NC}"
    echo ""
}

# =============================================================================
# MAIN EXECUTION
# =============================================================================

main() {
    clear

    echo -e "${CYAN}${BOLD}"
    echo "╔═══════════════════════════════════════════════════════════════════╗"
    echo "║                                                                   ║"
    echo "║        USER MANAGEMENT SYSTEM - VERIFICATION SCRIPT               ║"
    echo "║                                                                   ║"
    echo "╚═══════════════════════════════════════════════════════════════════╝"
    echo -e "${NC}"

    echo -e "${CYAN}This script will verify your complete setup and check for errors.${NC}"
    echo ""

    # Run all checks
    check_prerequisites
    check_backend
    check_frontend
    check_api_connectivity
    check_documentation

    # Print summary
    print_summary

    echo ""
    echo -e "${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${CYAN}Verification completed at: $(date)${NC}"
    echo -e "${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo ""
}

# =============================================================================
# SCRIPT ENTRY POINT
# =============================================================================

# Check if script is being run from the correct directory
if [ ! -d "newZedCode" ] && [ ! -d "frontend" ]; then
    echo -e "${RED}Error: This script must be run from the NewZed project root directory.${NC}"
    echo -e "${YELLOW}Current directory: $(pwd)${NC}"
    echo -e "${CYAN}Expected structure: NewZed/newZedCode and NewZed/frontend${NC}"
    exit 1
fi

# Run main function
main "$@"
