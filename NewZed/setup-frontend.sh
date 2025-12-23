#!/bin/bash

echo "🚀 Setting up Next.js Frontend..."

# Navigate to parent directory
cd "$(dirname "$0")"

# Remove existing frontend if it exists
if [ -d "frontend" ]; then
    echo "Removing existing frontend directory..."
    rm -rf frontend
fi

# Create Next.js app
echo "Creating Next.js app with TypeScript and Tailwind..."
npx create-next-app@latest frontend --typescript --tailwind --app --no-src-dir --import-alias "@/*" --use-npm --eslint --no-git

cd frontend

# Install additional dependencies
echo "Installing additional dependencies..."
npm install axios react-hot-toast lucide-react date-fns

echo "✅ Frontend setup complete!"
echo ""
echo "Next steps:"
echo "1. cd frontend"
echo "2. Update configuration files (see FRONTEND_SETUP_GUIDE.md)"
echo "3. npm run dev"
