#!/bin/bash

# =============================================================================
# Hospital Management System - Frontend Startup Script
# =============================================================================
# This script starts only the frontend application (Next.js)
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

# Function to check if Node.js is installed
check_nodejs() {
    if ! command -v node &> /dev/null; then
        print_message "$RED" "❌ Error: Node.js is not installed!"
        print_message "$YELLOW" "Please install Node.js 18+ from: https://nodejs.org/"
        exit 1
    fi

    local node_version=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
    if [ "$node_version" -lt 18 ]; then
        print_message "$RED" "❌ Error: Node.js version 18+ is required!"
        print_message "$YELLOW" "Current version: $(node -v)"
        print_message "$YELLOW" "Please upgrade Node.js from: https://nodejs.org/"
        exit 1
    fi

    print_message "$GREEN" "✓ Node.js version: $(node -v)"
}

# Function to check if npm is installed
check_npm() {
    if ! command -v npm &> /dev/null; then
        print_message "$RED" "❌ Error: npm is not installed!"
        exit 1
    fi
    print_message "$GREEN" "✓ npm version: $(npm -v)"
}

# Function to check if frontend directory exists
check_frontend_directory() {
    if [ ! -d "frontend" ]; then
        print_message "$RED" "❌ Error: Frontend directory not found!"
        print_message "$YELLOW" "Please run this script from the hospital management system root directory."
        exit 1
    fi
    print_message "$GREEN" "✓ Frontend directory found"
}

# Function to install dependencies if needed
install_dependencies() {
    cd frontend

    # Check if force reinstall is needed
    local force_reinstall=false
    if [ ! -d "node_modules" ]; then
        force_reinstall=true
    else
        # Check if Next.js is properly installed
        if [ ! -f "node_modules/next/dist/server/require-hook.js" ]; then
            print_message "$YELLOW" "⚠️  Warning: Next.js installation appears corrupted!"
            force_reinstall=true
        fi
    fi

    if [ "$force_reinstall" = true ]; then
        print_header "📦 Installing Frontend Dependencies"
        print_message "$YELLOW" "This may take a few minutes..."

        # Clean up corrupted installation
        if [ -d "node_modules" ]; then
            print_message "$YELLOW" "Cleaning up corrupted node_modules..."
            rm -rf node_modules package-lock.json .next
        fi

        npm install

        if [ $? -ne 0 ]; then
            print_message "$RED" "❌ Failed to install dependencies!"
            cd ..
            exit 1
        fi
        print_message "$GREEN" "✓ Dependencies installed successfully"
    else
        print_message "$GREEN" "✓ Dependencies already installed"
    fi

    cd ..
}

# Function to check environment file
check_env_file() {
    if [ ! -f "frontend/.env.local" ]; then
        print_message "$YELLOW" "⚠️  Warning: .env.local file not found!"
        print_message "$YELLOW" "Creating .env.local from example..."

        cat > frontend/.env.local << EOF
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
EOF

        print_message "$GREEN" "✓ Created .env.local with default settings"
        print_message "$YELLOW" "Please update the configuration if needed!"
    else
        print_message "$GREEN" "✓ Environment file found"
    fi
}

# Function to check if port 3000 is available
check_port() {
    if lsof -Pi :3000 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
        print_message "$YELLOW" "⚠️  Warning: Port 3000 is already in use!"
        print_message "$YELLOW" "Attempting to kill the process..."
        kill -9 $(lsof -t -i:3000) 2>/dev/null
        sleep 2

        if lsof -Pi :3000 -sTCP:LISTEN -t >/dev/null 2>&1 ; then
            print_message "$RED" "❌ Failed to free port 3000"
            print_message "$YELLOW" "Please manually stop the process using port 3000"
            exit 1
        fi
        print_message "$GREEN" "✓ Port 3000 is now available"
    else
        print_message "$GREEN" "✓ Port 3000 is available"
    fi
}

# Function to start the frontend
start_frontend() {
    print_header "🚀 Starting Frontend Application"

    cd frontend

    print_message "$GREEN" "Starting Next.js development server..."
    print_message "$BLUE" "Frontend will be available at: http://localhost:3000"
    print_message "$YELLOW" "Press Ctrl+C to stop the server"
    echo ""
    print_message "$YELLOW" "Note: You may see warnings about next.config.ts - these are safe to ignore"
    print_message "$YELLOW" "The server will still run correctly. Look for '✓ Ready in Xs' message."
    echo ""

    # Start the development server
    npm run dev

    # Capture exit code
    local exit_code=$?

    cd ..

    # Only show error if manually stopped (130) or actual error occurred
    # Note: Exit code 1 can occur from warnings but server might still be running
    if [ $exit_code -ne 0 ] && [ $exit_code -ne 130 ]; then
        echo ""
        print_message "$YELLOW" "⚠️  Server stopped with exit code: $exit_code"
        print_message "$YELLOW" "💡 If the server was running but showing warnings, that's normal"
        print_message "$YELLOW" "💡 If it didn't start at all, try: rm -rf frontend/node_modules frontend/package-lock.json frontend/.next"
    fi
}

# Main execution
main() {
    print_header "🏥 Hospital Management System - Frontend"

    # Run all checks
    print_header "🔍 Pre-flight Checks"
    check_nodejs
    check_npm
    check_frontend_directory
    check_env_file
    install_dependencies
    check_port

    # Start the frontend
    start_frontend
}

# Run main function
main
