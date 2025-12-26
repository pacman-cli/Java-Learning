#!/bin/bash

# Start Pokemon Manager Application
echo "🚀 Starting Pokemon Manager Application..."

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 11 or higher."
    exit 1
fi

# Check if Node.js is installed
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node.js 14 or higher."
    exit 1
fi

echo "✅ Prerequisites check passed"

# Function to cleanup background processes
cleanup() {
    echo "🛑 Shutting down application..."
    kill $BACKEND_PID 2>/dev/null
    kill $FRONTEND_PID 2>/dev/null
    exit 0
}

# Setup signal handlers
trap cleanup SIGINT SIGTERM

# Start Backend
echo "📦 Starting Spring Boot backend..."
./gradlew bootRun > backend.log 2>&1 &
BACKEND_PID=$!

# Wait a bit for backend to start
sleep 5

# Start Frontend
echo "⚛️  Starting React frontend..."
cd frontend
npm start > ../frontend.log 2>&1 &
FRONTEND_PID=$!
cd ..

echo "✅ Application started successfully!"
echo "🌐 Frontend: http://localhost:3000"
echo "🔧 Backend:  http://localhost:8080"
echo "📋 Logs: backend.log, frontend.log"
echo ""
echo "Press Ctrl+C to stop the application"

# Wait for processes
wait $BACKEND_PID
wait $FRONTEND_PID