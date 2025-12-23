#!/bin/bash

# Build script for portfolio backend

echo "Building portfolio backend..."

# Navigate to backend directory
cd portfolio-backend

# Clean and build the project
mvn clean package

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "Jar file location: target/portfolio-backend-0.0.1-SNAPSHOT.jar"
else
    echo "Build failed!"
    exit 1
fi