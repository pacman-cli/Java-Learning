#!/bin/bash

# Color codes for output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}========================================${NC}"
echo -e "${BLUE}  Spring Boot Pagination Example${NC}"
echo -e "${BLUE}  Startup Script${NC}"
echo -e "${BLUE}========================================${NC}"
echo ""

# Step 1: Start PostgreSQL Docker container
echo -e "${YELLOW}[1/4] Starting PostgreSQL database...${NC}"
docker-compose up -d

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Database started successfully${NC}"
else
    echo -e "${RED}✗ Failed to start database${NC}"
    exit 1
fi

# Wait for database to be ready
echo -e "${YELLOW}[2/4] Waiting for database to be ready...${NC}"
sleep 5

# Check if database is accessible
for i in {1..10}; do
    docker exec -it $(docker-compose ps -q db) pg_isready -U root > /dev/null 2>&1
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ Database is ready${NC}"
        break
    fi
    if [ $i -eq 10 ]; then
        echo -e "${RED}✗ Database failed to start after 10 attempts${NC}"
        exit 1
    fi
    echo -e "   Waiting... (attempt $i/10)"
    sleep 2
done

# Step 2: Build the application
echo -e "${YELLOW}[3/4] Building Spring Boot application...${NC}"
./mvnw clean package -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build successful${NC}"
else
    echo -e "${RED}✗ Build failed${NC}"
    exit 1
fi

# Step 3: Start the Spring Boot application
echo -e "${YELLOW}[4/4] Starting Spring Boot application...${NC}"
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
