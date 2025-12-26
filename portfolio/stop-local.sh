#!/bin/bash

# ============================================================
# Portfolio Application - Stop Script
# ============================================================

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Get script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

echo -e "${BLUE}Stopping Portfolio Application...${NC}"

# Check for --clean flag to remove volumes
if [ "$1" == "--clean" ]; then
    echo -e "${YELLOW}Removing containers and volumes...${NC}"
    docker-compose down -v --remove-orphans
    echo -e "${GREEN}✓ Containers and volumes removed${NC}"
else
    echo -e "${YELLOW}Stopping containers...${NC}"
    docker-compose down --remove-orphans
    echo -e "${GREEN}✓ Containers stopped${NC}"
    echo ""
    echo -e "${YELLOW}Note:${NC} Database volume preserved. Use ${BLUE}./stop-local.sh --clean${NC} to remove all data."
fi

echo ""
echo -e "${GREEN}Application stopped.${NC}"
