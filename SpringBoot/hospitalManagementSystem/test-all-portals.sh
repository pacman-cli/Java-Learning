#!/bin/bash

echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                                                                  ║"
echo "║       Hospital Management System - All Portals Test             ║"
echo "║                                                                  ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Test counters
TOTAL_TESTS=0
PASSED_TESTS=0
FAILED_TESTS=0

# Function to run a test
run_test() {
    local test_name=$1
    local test_command=$2
    local expected_result=$3

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "  Testing: $test_name... "

    result=$(eval "$test_command")

    if [ "$result" = "$expected_result" ]; then
        echo -e "${GREEN}✓ PASS${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ FAIL${NC} (Expected: $expected_result, Got: $result)"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
}

# Function to test login
test_login() {
    local username=$1
    local password=$2
    local role_name=$3

    echo ""
    echo -e "${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${CYAN}Testing $role_name Login${NC}"
    echo -e "${CYAN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"

    # Test login
    LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8081/api/auth/login \
      -H "Content-Type: application/json" \
      -d "{\"username\":\"$username\",\"password\":\"$password\"}")

    if echo "$LOGIN_RESPONSE" | grep -q "token"; then
        echo -e "${GREEN}✓ Login successful for $username${NC}"

        # Extract token
        TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

        # Extract roles
        ROLES=$(echo "$LOGIN_RESPONSE" | grep -o '"roles":\[[^]]*\]')
        echo -e "${BLUE}  Roles: $ROLES${NC}"

        # Return token for further tests
        echo "$TOKEN"
    else
        echo -e "${RED}✗ Login failed for $username${NC}"
        echo -e "${YELLOW}  Response: $LOGIN_RESPONSE${NC}"
        return 1
    fi
}

# Function to test API endpoint
test_api_endpoint() {
    local endpoint=$1
    local token=$2
    local endpoint_name=$3

    TOTAL_TESTS=$((TOTAL_TESTS + 1))
    echo -n "  - Testing $endpoint_name... "

    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
      -H "Authorization: Bearer $token" \
      http://localhost:8081$endpoint)

    if [ "$RESPONSE_CODE" = "200" ]; then
        echo -e "${GREEN}✓ Success (200)${NC}"
        PASSED_TESTS=$((PASSED_TESTS + 1))
    else
        echo -e "${RED}✗ Failed ($RESPONSE_CODE)${NC}"
        FAILED_TESTS=$((FAILED_TESTS + 1))
    fi
}

# Check if backend is running
echo "1️⃣  Checking Backend..."
if lsof -i :8081 >/dev/null 2>&1; then
    echo -e "${GREEN}✓ Backend is running on port 8081${NC}"
else
    echo -e "${RED}✗ Backend is NOT running${NC}"
    echo -e "${YELLOW}  Start it with: cd hospital && ./mvnw spring-boot:run${NC}"
    exit 1
fi

echo ""

# Check if frontend is running
echo "2️⃣  Checking Frontend..."
if lsof -i :3001 >/dev/null 2>&1; then
    echo -e "${GREEN}✓ Frontend is running on port 3001${NC}"
    FRONTEND_PORT=3001
elif lsof -i :3000 >/dev/null 2>&1; then
    echo -e "${GREEN}✓ Frontend is running on port 3000${NC}"
    FRONTEND_PORT=3000
else
    echo -e "${RED}✗ Frontend is NOT running${NC}"
    echo -e "${YELLOW}  Start it with: cd frontend && npm run dev${NC}"
    exit 1
fi

echo ""
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                     TESTING ALL PORTALS                          ║"
echo "╚══════════════════════════════════════════════════════════════════╝"

# ============================================================================
# ADMIN PORTAL TESTS
# ============================================================================
ADMIN_TOKEN=$(test_login "admin" "admin123" "ADMIN")

if [ -n "$ADMIN_TOKEN" ]; then
    echo ""
    echo "Testing Admin API Access..."
    test_api_endpoint "/api/appointments" "$ADMIN_TOKEN" "Appointments"
    test_api_endpoint "/api/patients" "$ADMIN_TOKEN" "Patients"
    test_api_endpoint "/api/doctors" "$ADMIN_TOKEN" "Doctors"
    test_api_endpoint "/api/billings" "$ADMIN_TOKEN" "Billings (should fail - needs BILLING_STAFF role)"
    test_api_endpoint "/api/medical-records" "$ADMIN_TOKEN" "Medical Records"
    test_api_endpoint "/api/prescriptions" "$ADMIN_TOKEN" "Prescriptions"
    test_api_endpoint "/api/lab-orders" "$ADMIN_TOKEN" "Lab Orders"

    echo ""
    echo "Admin Portal Pages to Test Manually:"
    echo "  ✓ Dashboard         - http://localhost:$FRONTEND_PORT/dashboard"
    echo "  ✓ Users             - http://localhost:$FRONTEND_PORT/users"
    echo "  ✓ Doctors           - http://localhost:$FRONTEND_PORT/doctors"
    echo "  ✓ Patients          - http://localhost:$FRONTEND_PORT/patients"
    echo "  ✓ Appointments      - http://localhost:$FRONTEND_PORT/appointments"
    echo "  ✓ Departments       - http://localhost:$FRONTEND_PORT/departments"
    echo "  ✓ Billing           - http://localhost:$FRONTEND_PORT/billing"
    echo "  ✓ Reports           - http://localhost:$FRONTEND_PORT/reports"
    echo "  ✓ Settings          - http://localhost:$FRONTEND_PORT/settings"
fi

# ============================================================================
# DOCTOR PORTAL TESTS
# ============================================================================
DOCTOR_TOKEN=$(test_login "doctor1" "password123" "DOCTOR")

if [ -n "$DOCTOR_TOKEN" ]; then
    echo ""
    echo "Testing Doctor API Access..."
    test_api_endpoint "/api/appointments" "$DOCTOR_TOKEN" "Appointments"
    test_api_endpoint "/api/patients" "$DOCTOR_TOKEN" "Patients"
    test_api_endpoint "/api/medical-records" "$DOCTOR_TOKEN" "Medical Records"
    test_api_endpoint "/api/prescriptions" "$DOCTOR_TOKEN" "Prescriptions"
    test_api_endpoint "/api/lab-orders" "$DOCTOR_TOKEN" "Lab Orders"
    test_api_endpoint "/api/doctors" "$DOCTOR_TOKEN" "Doctors (GET)"

    echo ""
    echo "Doctor Portal Pages to Test Manually:"
    echo "  ✓ Dashboard         - http://localhost:$FRONTEND_PORT/dashboard"
    echo "  ✓ My Patients       - http://localhost:$FRONTEND_PORT/my-patients"
    echo "  ✓ Appointments      - http://localhost:$FRONTEND_PORT/appointments"
    echo "  ✓ Medical Records   - http://localhost:$FRONTEND_PORT/records"
    echo "  ✓ Prescriptions     - http://localhost:$FRONTEND_PORT/prescriptions"
    echo "  ✓ Lab Requests      - http://localhost:$FRONTEND_PORT/lab-requests"
    echo "  ✓ My Schedule       - http://localhost:$FRONTEND_PORT/schedule"
    echo "  ✓ Settings          - http://localhost:$FRONTEND_PORT/settings"
fi

# ============================================================================
# PATIENT PORTAL TESTS
# ============================================================================
PATIENT_TOKEN=$(test_login "patient1" "password123" "PATIENT")

if [ -n "$PATIENT_TOKEN" ]; then
    echo ""
    echo "Testing Patient API Access..."
    test_api_endpoint "/api/appointments" "$PATIENT_TOKEN" "Appointments"
    test_api_endpoint "/api/patients" "$PATIENT_TOKEN" "Patients"
    test_api_endpoint "/api/medical-records" "$PATIENT_TOKEN" "Medical Records"
    test_api_endpoint "/api/prescriptions" "$PATIENT_TOKEN" "Prescriptions"
    test_api_endpoint "/api/lab-orders" "$PATIENT_TOKEN" "Lab Orders"
    test_api_endpoint "/api/doctors" "$PATIENT_TOKEN" "Doctors (GET)"

    # Get patient ID for specific tests
    PATIENT_DATA=$(curl -s -H "Authorization: Bearer $PATIENT_TOKEN" \
      http://localhost:8081/api/patients/by-user/$(echo "$PATIENT_TOKEN" | cut -d'.' -f2 | base64 -d 2>/dev/null | grep -o '"id":[0-9]*' | cut -d':' -f2 | head -1 || echo "1"))

    PATIENT_ID=$(echo "$PATIENT_DATA" | grep -o '"id":[0-9]*' | cut -d':' -f2 | head -1)

    if [ -n "$PATIENT_ID" ]; then
        test_api_endpoint "/api/billings/patient/$PATIENT_ID" "$PATIENT_TOKEN" "Patient Billing"
    fi

    echo ""
    echo "Patient Portal Pages to Test Manually:"
    echo "  ✓ Dashboard         - http://localhost:$FRONTEND_PORT/dashboard"
    echo "  ✓ My Appointments   - http://localhost:$FRONTEND_PORT/my-appointments"
    echo "  ✓ Medical Records   - http://localhost:$FRONTEND_PORT/my-records"
    echo "  ✓ Prescriptions     - http://localhost:$FRONTEND_PORT/my-prescriptions"
    echo "  ✓ Lab Reports       - http://localhost:$FRONTEND_PORT/my-lab-reports"
    echo "  ✓ Billing           - http://localhost:$FRONTEND_PORT/my-billing"
    echo "  ✓ Health Tracker    - http://localhost:$FRONTEND_PORT/health-tracker"
    echo "  ✓ Settings          - http://localhost:$FRONTEND_PORT/settings"
fi

# ============================================================================
# SUMMARY
# ============================================================================
echo ""
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                        TEST SUMMARY                              ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo ""
echo "Total Tests:  $TOTAL_TESTS"
echo -e "${GREEN}Passed:       $PASSED_TESTS${NC}"
echo -e "${RED}Failed:       $FAILED_TESTS${NC}"
echo ""

if [ $FAILED_TESTS -eq 0 ]; then
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${GREEN}🎉 ALL TESTS PASSED! All portals are working correctly!${NC}"
    echo -e "${GREEN}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
else
    echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
    echo -e "${YELLOW}⚠️  Some tests failed. Please review the errors above.${NC}"
    echo -e "${YELLOW}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
fi

echo ""
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                     MANUAL TESTING GUIDE                         ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo ""
echo "Open your browser: http://localhost:$FRONTEND_PORT"
echo ""
echo "Test each user type:"
echo ""
echo -e "${BLUE}ADMIN LOGIN:${NC}"
echo "  Username: admin"
echo "  Password: admin123"
echo "  Test: All 9 admin pages should be accessible"
echo ""
echo -e "${CYAN}DOCTOR LOGIN:${NC}"
echo "  Username: doctor1"
echo "  Password: password123"
echo "  Test: All 8 doctor pages should be accessible"
echo ""
echo -e "${YELLOW}PATIENT LOGIN:${NC}"
echo "  Username: patient1"
echo "  Password: password123"
echo "  Test: All 8 patient pages should be accessible"
echo ""
echo "Debug Page (if issues): http://localhost:$FRONTEND_PORT/debug-auth"
echo ""
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                         CREDENTIALS                              ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo ""
echo "Admin:    admin / admin123"
echo "Doctor 1: doctor1 / password123 (Dr. Sarah Smith - Cardiology)"
echo "Doctor 2: doctor2 / password123 (Dr. Michael Jones - Neurology)"
echo "Doctor 3: doctor3 / password123 (Dr. Emily Davis - Pediatrics)"
echo "Doctor 4: doctor4 / password123 (Dr. James Wilson - Orthopedics)"
echo "Patient:  patient1 / password123"
echo ""
echo "╔══════════════════════════════════════════════════════════════════╗"
echo "║                      TROUBLESHOOTING                             ║"
echo "╚══════════════════════════════════════════════════════════════════╝"
echo ""
echo "If you see permission errors:"
echo "  1. Visit: http://localhost:$FRONTEND_PORT/debug-auth"
echo "  2. Check if roles are correct"
echo "  3. Logout and login again"
echo "  4. Clear browser cookies/cache"
echo ""
echo "If network errors occur:"
echo "  1. Check backend: lsof -i :8081"
echo "  2. Check frontend: lsof -i :$FRONTEND_PORT"
echo "  3. Verify .env.local has: NEXT_PUBLIC_API_BASE_URL=http://localhost:8081"
echo ""
echo "View logs:"
echo "  Backend:  tail -f /tmp/backend.log"
echo "  Frontend: tail -f /tmp/frontend.log"
echo ""
echo "════════════════════════════════════════════════════════════════════"
echo ""
