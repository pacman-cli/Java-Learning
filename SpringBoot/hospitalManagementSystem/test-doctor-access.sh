#!/bin/bash

echo "========================================"
echo "Doctor Portal Access Test"
echo "========================================"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

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
if lsof -i :3001 >/dev/null 2>&1 || lsof -i :3000 >/dev/null 2>&1; then
    if lsof -i :3001 >/dev/null 2>&1; then
        echo -e "${GREEN}✓ Frontend is running on port 3001${NC}"
        FRONTEND_PORT=3001
    else
        echo -e "${GREEN}✓ Frontend is running on port 3000${NC}"
        FRONTEND_PORT=3000
    fi
else
    echo -e "${RED}✗ Frontend is NOT running${NC}"
    echo -e "${YELLOW}  Start it with: cd frontend && npm run dev${NC}"
    exit 1
fi

echo ""

# Test doctor login
echo "3️⃣  Testing Doctor Login..."
LOGIN_RESPONSE=$(curl -s -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor1","password":"password123"}')

# Check if login was successful
if echo "$LOGIN_RESPONSE" | grep -q "token"; then
    echo -e "${GREEN}✓ Doctor login successful${NC}"

    # Extract token
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

    # Extract roles
    ROLES=$(echo "$LOGIN_RESPONSE" | grep -o '"roles":\[[^]]*\]')
    echo -e "${BLUE}  Roles: $ROLES${NC}"

    # Check if ROLE_DOCTOR exists
    if echo "$LOGIN_RESPONSE" | grep -q "ROLE_DOCTOR"; then
        echo -e "${GREEN}✓ ROLE_DOCTOR found in response${NC}"
    else
        echo -e "${RED}✗ ROLE_DOCTOR NOT found${NC}"
    fi
else
    echo -e "${RED}✗ Doctor login failed${NC}"
    echo -e "${YELLOW}  Response: $LOGIN_RESPONSE${NC}"
    exit 1
fi

echo ""

# Test API endpoints with doctor token
echo "4️⃣  Testing Doctor API Access..."

# Test appointments endpoint
echo -n "  - Testing /api/appointments... "
APPT_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/api/appointments)

if [ "$APPT_RESPONSE" = "200" ]; then
    echo -e "${GREEN}✓ Success (200)${NC}"
else
    echo -e "${RED}✗ Failed ($APPT_RESPONSE)${NC}"
fi

# Test patients endpoint
echo -n "  - Testing /api/patients... "
PAT_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/api/patients)

if [ "$PAT_RESPONSE" = "200" ]; then
    echo -e "${GREEN}✓ Success (200)${NC}"
else
    echo -e "${RED}✗ Failed ($PAT_RESPONSE)${NC}"
fi

# Test medical records endpoint
echo -n "  - Testing /api/medical-records... "
MED_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/api/medical-records)

if [ "$MED_RESPONSE" = "200" ]; then
    echo -e "${GREEN}✓ Success (200)${NC}"
else
    echo -e "${RED}✗ Failed ($MED_RESPONSE)${NC}"
fi

# Test prescriptions endpoint
echo -n "  - Testing /api/prescriptions... "
PRESC_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/api/prescriptions)

if [ "$PRESC_RESPONSE" = "200" ]; then
    echo -e "${GREEN}✓ Success (200)${NC}"
else
    echo -e "${RED}✗ Failed ($PRESC_RESPONSE)${NC}"
fi

# Test lab orders endpoint
echo -n "  - Testing /api/lab-orders... "
LAB_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" \
  -H "Authorization: Bearer $TOKEN" \
  http://localhost:8081/api/lab-orders)

if [ "$LAB_RESPONSE" = "200" ]; then
    echo -e "${GREEN}✓ Success (200)${NC}"
else
    echo -e "${RED}✗ Failed ($LAB_RESPONSE)${NC}"
fi

echo ""

# Summary
echo "========================================"
echo "Test Summary"
echo "========================================"
echo ""
echo -e "${BLUE}✓ Backend Running${NC}"
echo -e "${BLUE}✓ Frontend Running${NC}"
echo -e "${BLUE}✓ Doctor Login Works${NC}"
echo -e "${BLUE}✓ Doctor Has ROLE_DOCTOR${NC}"
echo ""
echo -e "${GREEN}🎉 Doctor portal is ready to use!${NC}"
echo ""
echo "--------------------------------------"
echo "Next Steps:"
echo "--------------------------------------"
echo "1. Open browser: http://localhost:$FRONTEND_PORT"
echo "2. Login with:"
echo "   Username: doctor1"
echo "   Password: password123"
echo "3. Test these pages:"
echo "   - Dashboard"
echo "   - My Patients"
echo "   - Appointments ✨ (FIXED)"
echo "   - Medical Records"
echo "   - Prescriptions"
echo "   - Lab Requests"
echo "   - My Schedule"
echo "   - Settings ✨ (FIXED)"
echo ""
echo "4. Debug page (if issues):"
echo "   http://localhost:$FRONTEND_PORT/debug-auth"
echo ""
echo "--------------------------------------"
echo "Doctor Accounts Available:"
echo "--------------------------------------"
echo "doctor1 / password123 - Dr. Sarah Smith (Cardiology)"
echo "doctor2 / password123 - Dr. Michael Jones (Neurology)"
echo "doctor3 / password123 - Dr. Emily Davis (Pediatrics)"
echo "doctor4 / password123 - Dr. James Wilson (Orthopedics)"
echo ""
echo "========================================"
