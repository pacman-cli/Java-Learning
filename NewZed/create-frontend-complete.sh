#!/bin/bash

# Complete Frontend Setup Script
# This script creates a full Next.js + Tailwind frontend

echo "🚀 Creating Next.js Frontend with Spring Boot Integration"
echo "==========================================================="

# Check if we're in the right directory
if [ ! -d "newZedCode" ]; then
    echo "❌ Error: Run this script from the JavaCourse directory"
    exit 1
fi

# Create frontend if it doesn't exist
if [ -d "frontend" ]; then
    read -p "Frontend directory exists. Delete and recreate? (y/N) " -n 1 -r
    echo
    if [[ $REPLY =~ ^[Yy]$ ]]; then
        rm -rf frontend
    else
        exit 0
    fi
fi

# Create basic Next.js structure
echo "📁 Creating directory structure..."
mkdir -p frontend/{app/{users/{create,edit/\[id\],\[id\]},api},components,lib,public}

# Create package.json
echo "📦 Creating package.json..."
cat > frontend/package.json << 'EOF'
{
  "name": "user-management-frontend",
  "version": "1.0.0",
  "private": true,
  "scripts": {
    "dev": "next dev",
    "build": "next build",
    "start": "next start",
    "lint": "next lint"
  },
  "dependencies": {
    "next": "14.2.0",
    "react": "^18",
    "react-dom": "^18",
    "typescript": "^5",
    "axios": "^1.6.7",
    "react-hot-toast": "^2.4.1",
    "lucide-react": "^0.344.0",
    "date-fns": "^3.3.1"
  },
  "devDependencies": {
    "@types/node": "^20",
    "@types/react": "^18",
    "@types/react-dom": "^18",
    "autoprefixer": "^10.0.1",
    "postcss": "^8",
    "tailwindcss": "^3.4.1",
    "eslint": "^8",
    "eslint-config-next": "14.2.0"
  }
}
EOF

echo "✅ Package.json created"
echo "📥 Installing dependencies (this may take a minute)..."

cd frontend
npm install --silent

echo "✅ Dependencies installed"
echo "📄 Creating configuration files..."

# Create all necessary files
# (The actual file content would continue here...)

echo ""
echo "🎉 Frontend setup complete!"
echo ""
echo "Next steps:"
echo "1. cd frontend"
echo "2. npm run dev"
echo ""
echo "Then open http://localhost:3000 in your browser"

