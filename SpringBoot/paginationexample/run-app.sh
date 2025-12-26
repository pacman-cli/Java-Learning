#!/bin/bash

# Color codes for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Spring Boot Pagination Example${NC}"
echo -e "${BLUE}  Starting Application...${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Check if database is accessible
echo -e "${YELLOW}[1/2] Checking database connection...${NC}"
psql -U root -h localhost -p 5432 -d pagination_db -c "\dt" > /dev/null 2>&1

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Database is accessible${NC}"
else
    echo -e "${RED}✗ Cannot connect to database${NC}"
    echo -e "${YELLOW}Please ensure PostgreSQL is running with:${NC}"
    echo -e "  - Database: pagination_db"
    echo -e "  - User: root"
    echo -e "  - Port: 5432"
    exit 1
fi

# Start the Spring Boot application
echo -e "${YELLOW}[2/2] Starting Spring Boot application...${NC}"
echo ""
echo -e "${BLUE}========================================${NC}"
echo -e "${GREEN}  Application is starting...${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""
echo -e "Once started, you can access:"
echo -e "  ${GREEN}Frontend Dashboard:${NC} http://localhost:8080"
echo -e "  ${GREEN}Swagger UI:${NC}         http://localhost:8080/swagger-ui.html"
echo -e "  ${GREEN}API Docs:${NC}           http://localhost:8080/v3/api-docs"
echo ""
echo -e "${YELLOW}Press Ctrl+C to stop the application${NC}"
echo ""

./mvnw spring-boot:run
