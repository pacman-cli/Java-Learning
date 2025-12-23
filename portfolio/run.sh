#!/bin/bash

# Run script for portfolio application

echo "Starting portfolio application..."

# Build backend
echo "Building backend..."
./build-backend.sh

if [ $? -ne 0 ]; then
    echo "Failed to build backend"
    exit 1
fi

# Build frontend
echo "Building frontend..."
./build-frontend.sh

if [ $? -ne 0 ]; then
    echo "Failed to build frontend"
    exit 1
fi

# Start docker-compose
echo "Starting application with docker-compose..."
docker-compose up -d

if [ $? -eq 0 ]; then
    echo "Application started successfully!"
    echo "Frontend: http://localhost:3000"
    echo "Backend: http://localhost:8080"
    echo "Database: mysql://localhost:3306"
else
    echo "Failed to start application"
    exit 1
fi