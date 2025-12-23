#!/bin/bash

# Stop script for portfolio application

echo "Stopping portfolio application..."

# Stop docker-compose
docker-compose down

if [ $? -eq 0 ]; then
    echo "Application stopped successfully!"
else
    echo "Failed to stop application"
    exit 1
fi