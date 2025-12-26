#!/bin/bash

# Build script for portfolio frontend

echo "Building portfolio frontend..."

# Navigate to frontend directory
cd portfolio-frontend

# Install dependencies
npm install

# Build the project
npm run build

# Check if build was successful
if [ $? -eq 0 ]; then
    echo "Build successful!"
    echo "Build output location: out/"
else
    echo "Build failed!"
    exit 1
fi