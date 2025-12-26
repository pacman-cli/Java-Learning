#!/bin/bash

# =============================================================================
# User Management Frontend - Setup and Run Script
# =============================================================================
# This script automates the setup and launch of the Next.js frontend
#
# Usage:
#   chmod +x setup-and-run.sh
#   ./setup-and-run.sh
#
# Author: ZedCode
# =============================================================================

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# =============================================================================
# UTILITY FUNCTIONS
# =============================================================================

print_header() {
    echo -e "\n${BLUE}============================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}============================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ $1${NC}"
}

# =============================================================================
# CHECK PREREQUISITES
# =============================================================================

check_prerequisites() {
    print_header "Checking Prerequisites"

    local all_ok=true

    # Check Node.js
    if command -v node &> /dev/null; then
        NODE_VERSION=$(node --version)
        print_success "Node.js is installed: $NODE_VERSION"

        # Extract major version
        NODE_MAJOR=$(echo $NODE_VERSION | cut -d'.' -f1 | sed 's/v//')
        if [ "$NODE_MAJOR" -lt 18 ]; then
            print_error "Node.js version must be 18 or higher (current: $NODE_VERSION)"
            all_ok=false
        fi
    else
        print_error "Node.js is not installed"
        print_info "Please install Node.js from https://nodejs.org/"
        all_ok=false
    fi

    # Check npm
    if command -v npm &> /dev/null; then
        NPM_VERSION=$(npm --version)
        print_success "npm is installed: v$NPM_VERSION"
    else
        print_error "npm is not installed"
        all_ok=false
    fi

    # Check if backend is running
    print_info "Checking if backend is running..."
    if curl -s http://localhost:8080/api/v1/users/stats > /dev/null 2>&1; then
        print_success "Backend is running on http://localhost:8080"
    else
        print_warning "Backend is not running on http://localhost:8080"
        print_info "Please start the Spring Boot backend before accessing the frontend"
        print_info "cd ../newZedCode && mvn spring-boot:run"
    fi

    if [ "$all_ok" = false ]; then
        print_error "Prerequisites check failed. Please install missing dependencies."
        exit 1
    fi

    print_success "All prerequisites are satisfied!"
}

# =============================================================================
# SETUP ENVIRONMENT
# =============================================================================

setup_environment() {
    print_header "Setting Up Environment"

    # Navigate to frontend directory
    cd "$(dirname "$0")/frontend"

    # Check if .env.local exists
    if [ ! -f .env.local ]; then
        print_info "Creating .env.local file..."
        cat > .env.local << 'EOF'
# Environment Configuration for User Management Frontend

# Backend API Base URL
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1

# Optional configurations
# NEXT_PUBLIC_API_TIMEOUT=30000
# NEXT_PUBLIC_DEBUG_MODE=false
EOF
        print_success "Created .env.local"
    else
        print_success ".env.local already exists"
    fi
}

# =============================================================================
# INSTALL DEPENDENCIES
# =============================================================================

install_dependencies() {
    print_header "Installing Dependencies"

    if [ -d "node_modules" ]; then
        print_info "node_modules directory exists"
        read -p "Do you want to reinstall dependencies? (y/N): " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            print_info "Removing node_modules and package-lock.json..."
            rm -rf node_modules package-lock.json
            print_info "Installing dependencies (this may take a few minutes)..."
            npm install
            print_success "Dependencies installed successfully"
        else
            print_info "Skipping dependency installation"
        fi
    else
        print_info "Installing dependencies (this may take a few minutes)..."
        npm install
        print_success "Dependencies installed successfully"
    fi
}

# =============================================================================
# BUILD CHECK
# =============================================================================

run_build_check() {
    print_header "Running Build Check (Optional)"

    read -p "Do you want to check if the project builds correctly? (y/N): " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        print_info "Running build check..."
        if npm run build; then
            print_success "Build successful!"
        else
            print_error "Build failed. Please check the errors above."
            exit 1
        fi
    else
        print_info "Skipping build check"
    fi
}

# =============================================================================
# START DEVELOPMENT SERVER
# =============================================================================

start_dev_server() {
    print_header "Starting Development Server"

    print_info "Starting Next.js development server..."
    print_info ""
    print_info "The application will be available at:"
    print_info "  ${GREEN}http://localhost:3000${NC}"
    print_info ""
    print_info "Backend API should be running at:"
    print_info "  ${GREEN}http://localhost:8080${NC}"
    print_info ""
    print_info "Press ${YELLOW}Ctrl+C${NC} to stop the server"
    print_info ""

    sleep 2

    # Start the development server
    npm run dev
}

# =============================================================================
# MAIN EXECUTION
# =============================================================================

main() {
    clear

    print_header "User Management Frontend - Setup & Run"

    echo -e "${BLUE}"
    echo "  _   _                 __  __                                                   _   "
    echo " | | | |___  ___ _ __  |  \/  | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ "
    echo " | | | / __|/ _ \ '__| | |\/| |/ _\` | '_ \ / _\` |/ _\` |/ _ \ '_ \` _ \ / _ \ '_ \| __|"
    echo " | |_| \__ \  __/ |    | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ "
    echo "  \___/|___/\___|_|    |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_| |_| |_|\___|_| |_|\__|"
    echo "                                                  |___/                               "
    echo -e "${NC}"

    # Run setup steps
    check_prerequisites
    setup_environment
    install_dependencies
    run_build_check

    # Start server
    start_dev_server
}

# =============================================================================
# SCRIPT ENTRY POINT
# =============================================================================

# Check if script is being sourced or executed
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi
