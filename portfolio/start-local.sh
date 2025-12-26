#!/bin/bash

# ============================================================
# Portfolio Application - Local Development Startup Script
# ============================================================
# This script builds and runs all containers for local development
# Frontend: http://localhost:3000
# Backend:  http://localhost:8080
# Database: postgres://localhost:5432
# ============================================================

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo -e "${BLUE}============================================================${NC}"
echo -e "${BLUE}       Portfolio Application - Local Development           ${NC}"
echo -e "${BLUE}============================================================${NC}"
echo ""

# Step 1: Build Backend
echo -e "${YELLOW}[1/4] Building Backend...${NC}"
cd portfolio-backend
if mvn clean package -DskipTests -q; then
    echo -e "${GREEN}✓ Backend build successful${NC}"
else
    echo -e "${RED}✗ Backend build failed${NC}"
    exit 1
fi
cd "$SCRIPT_DIR"

# Step 2: Stop any existing containers
echo -e "${YELLOW}[2/4] Stopping existing containers...${NC}"
docker-compose down --remove-orphans 2>/dev/null || true
echo -e "${GREEN}✓ Cleaned up existing containers${NC}"

# Step 3: Build Docker images
echo -e "${YELLOW}[3/4] Building Docker images...${NC}"
if docker-compose build --quiet; then
    echo -e "${GREEN}✓ Docker images built successfully${NC}"
else
    echo -e "${RED}✗ Docker build failed${NC}"
    exit 1
fi

# Step 4: Start all containers
echo -e "${YELLOW}[4/4] Starting all containers...${NC}"
docker-compose up -d

# Wait for services to be ready
echo ""
echo -e "${YELLOW}Waiting for services to start...${NC}"

# Wait for PostgreSQL
echo -n "  PostgreSQL: "
for i in {1..30}; do
    if docker exec portfolio-postgres pg_isready -U postgres -d portfolio_db > /dev/null 2>&1; then
        echo -e "${GREEN}Ready${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${RED}Timeout${NC}"
        echo -e "${RED}PostgreSQL failed to start. Check logs with: docker-compose logs postgres${NC}"
        exit 1
    fi
    sleep 1
done

# Wait for Backend
echo -n "  Backend: "
for i in {1..60}; do
    if curl -s http://localhost:8080/api/skills > /dev/null 2>&1; then
        echo -e "${GREEN}Ready${NC}"
        break
    fi
    if [ $i -eq 60 ]; then
        echo -e "${YELLOW}Still starting...${NC}"
        echo -e "${YELLOW}Backend may still be initializing. Check logs with: docker-compose logs backend${NC}"
    fi
    sleep 1
done

# Wait for Frontend
echo -n "  Frontend: "
for i in {1..30}; do
    if curl -s http://localhost:3000 > /dev/null 2>&1; then
        echo -e "${GREEN}Ready${NC}"
        break
    fi
    if [ $i -eq 30 ]; then
        echo -e "${YELLOW}Still starting...${NC}"
    fi
    sleep 1
done

echo ""
echo -e "${GREEN}============================================================${NC}"
echo -e "${GREEN}       Application Started Successfully!                   ${NC}"
echo -e "${GREEN}============================================================${NC}"
echo ""
echo -e "  ${BLUE}Frontend:${NC}  http://localhost:3000"
echo -e "  ${BLUE}Backend:${NC}   http://localhost:8080"
echo -e "  ${BLUE}API Docs:${NC}  http://localhost:8080/api/skills"
echo -e "  ${BLUE}Database:${NC}  postgresql://postgres:password@localhost:5432/portfolio_db"
echo ""
echo -e "${YELLOW}To stop the application, run:${NC} ./stop-local.sh"
echo ""
