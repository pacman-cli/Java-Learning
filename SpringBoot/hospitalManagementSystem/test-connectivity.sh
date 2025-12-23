#!/bin/bash

echo "========================================"
echo "Hospital Management System - Connectivity Test"
echo "========================================"
echo ""

# Colors
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Test Backend
echo "Testing Backend (http://localhost:8081)..."
BACKEND_STATUS=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8081/actuator/health 2>/dev/null || echo "000")

if [ "$BACKEND_STATUS" = "200" ]; then
    echo -e "${GREEN}✓ Backend is UP and running on port 8081${NC}"
elif [ "$BACKEND_STATUS" = "000" ]; then
    # Try basic connection
    if lsof -i :8081 >/dev/null 2>&1; then
        echo -e "${YELLOW}⚠ Backend is running on port 8081 but health check failed${NC}"
        echo -e "${YELLOW}  (This is normal if actuator is not configured)${NC}"
    else
        echo -e "${RED}✗ Backend is NOT running on port 8081${NC}"
        echo -e "${RED}  Please start backend: cd hospital && ./mvnw spring-boot:run${NC}"
    fi
else
    echo -e "${YELLOW}⚠ Backend returned status code: $BACKEND_STATUS${NC}"
fi

echo ""

# Test Frontend
echo "Testing Frontend (http://localhost:3000 or 3001)..."
FRONTEND_3000=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:3000 2>/dev/null || echo "000")
FRONTEND_3001=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:3001 2>/dev/null || echo "000")

if [ "$FRONTEND_3000" = "200" ] || [ "$FRONTEND_3000" = "304" ]; then
    echo -e "${GREEN}✓ Frontend is running on port 3000${NC}"
    echo -e "${GREEN}  Access at: http://localhost:3000${NC}"
elif [ "$FRONTEND_3001" = "200" ] || [ "$FRONTEND_3001" = "304" ]; then
    echo -e "${GREEN}✓ Frontend is running on port 3001${NC}"
    echo -e "${GREEN}  Access at: http://localhost:3001${NC}"
else
    if lsof -i :3000 >/dev/null 2>&1 || lsof -i :3001 >/dev/null 2>&1; then
        echo -e "${YELLOW}⚠ Frontend server is running but not responding${NC}"
        echo -e "${YELLOW}  Wait a few seconds for it to fully start...${NC}"
    else
        echo -e "${RED}✗ Frontend is NOT running${NC}"
        echo -e "${RED}  Please start frontend: cd frontend && npm run dev${NC}"
    fi
fi

echo ""

# Check Environment Variables
echo "Checking Frontend Environment..."
if [ -f "frontend/.env.local" ]; then
    API_URL=$(grep NEXT_PUBLIC_API_BASE_URL frontend/.env.local | cut -d'=' -f2)
    if [ "$API_URL" = "http://localhost:8081" ]; then
        echo -e "${GREEN}✓ API URL is correctly set to http://localhost:8081${NC}"
    else
        echo -e "${RED}✗ API URL is set to: $API_URL${NC}"
        echo -e "${RED}  Should be: http://localhost:8081${NC}"
    fi
else
    echo -e "${YELLOW}⚠ .env.local file not found${NC}"
fi

echo ""

# Test Credentials
echo "========================================"
echo "Test Login Credentials:"
echo "========================================"
echo "Admin:    admin / admin123"
echo "Doctor:   doctor1 / password123"
echo "Patient:  patient1 / password123"
echo ""

# Summary
echo "========================================"
echo "Summary:"
echo "========================================"

BACKEND_OK=false
FRONTEND_OK=false

if [ "$BACKEND_STATUS" = "200" ] || lsof -i :8081 >/dev/null 2>&1; then
    BACKEND_OK=true
fi

if [ "$FRONTEND_3000" = "200" ] || [ "$FRONTEND_3000" = "304" ] || [ "$FRONTEND_3001" = "200" ] || [ "$FRONTEND_3001" = "304" ]; then
    FRONTEND_OK=true
fi

if $BACKEND_OK && $FRONTEND_OK; then
    echo -e "${GREEN}✓ All systems are operational!${NC}"
    echo ""
    echo "Next steps:"
    echo "1. Open your browser and navigate to the frontend URL"
    echo "2. Login with one of the test credentials above"
    echo "3. Test the billing page at /my-billing (patient login required)"
elif $BACKEND_OK; then
    echo -e "${YELLOW}⚠ Backend is running, but frontend needs to be started${NC}"
elif $FRONTEND_OK; then
    echo -e "${YELLOW}⚠ Frontend is running, but backend needs to be started${NC}"
else
    echo -e "${RED}✗ Both backend and frontend need to be started${NC}"
fi

echo ""
