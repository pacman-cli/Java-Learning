# 🚀 Frontend Quick Start Guide

**Simple 3-step guide to set up your Next.js frontend**

---

## ✅ Prerequisites

- Node.js 18+ installed ([Download](https://nodejs.org/))
- Spring Boot backend running on `http://localhost:8080`
- Terminal/Command Prompt

---

## 📝 Step-by-Step Setup

### Step 1: Create Next.js Project

```bash
# Navigate to JavaCourse directory (parent of newZedCode)
cd /Users/puspo/JavaCourse

# Create Next.js app with all features
npx create-next-app@latest frontend

# When prompted, answer:
✔ Would you like to use TypeScript? › Yes
✔ Would you like to use ESLint? › Yes
✔ Would you like to use Tailwind CSS? › Yes
✔ Would you like to use `src/` directory? › No
✔ Would you like to use App Router? › Yes
✔ Would you like to customize the default import alias? › No

# Navigate to frontend
cd frontend

# Install additional dependencies
npm install axios react-hot-toast lucide-react date-fns
```

---

### Step 2: Create Project Files

Create these files in your `frontend` directory:

#### A. Create `.env.local` file:
```bash
# In frontend directory
cat > .env.local << 'EOF'
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
EOF
```

#### B. Update `next.config.js`:
```bash
cat > next.config.js << 'EOF'
/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
}
module.exports = nextConfig
EOF
```

#### C. Create `lib/types.ts`:
```bash
mkdir -p lib
cat > lib/types.ts << 'EOF'
export enum UserRole {
  USER = 'USER',
  ADMIN = 'ADMIN',
  MODERATOR = 'MODERATOR',
  SUPER_ADMIN = 'SUPER_ADMIN',
}

export enum UserStatus {
  ACTIVE = 'ACTIVE',
  INACTIVE = 'INACTIVE',
  PENDING = 'PENDING',
  BLOCKED = 'BLOCKED',
  DELETED = 'DELETED',
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  fullName: string;
  username: string;
  email: string;
  phoneNumber?: string;
  role: UserRole;
  status: UserStatus;
  emailVerified: boolean;
  createdAt: string;
  updatedAt?: string;
}

export interface CreateUserRequest {
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  phoneNumber?: string;
  role?: UserRole;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}
EOF
```

#### D. Create `lib/api.ts`:
```bash
cat > lib/api.ts << 'EOF'
import axios from 'axios';
import type { User, CreateUserRequest, ApiResponse, PageResponse } from './types';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
  headers: { 'Content-Type': 'application/json' },
});

export const getAllUsers = async (page = 0, size = 10) => {
  const response = await api.get<ApiResponse<PageResponse<User>>>('/users', {
    params: { page, size, sort: 'id,desc' }
  });
  return response.data;
};

export const getUserById = async (id: number) => {
  const response = await api.get<ApiResponse<User>>(`/users/${id}`);
  return response.data;
};

export const createUser = async (userData: CreateUserRequest) => {
  const response = await api.post<ApiResponse<User>>('/users', userData);
  return response.data;
};

export const updateUser = async (id: number, userData: Partial<CreateUserRequest>) => {
  const response = await api.put<ApiResponse<User>>(`/users/${id}`, userData);
  return response.data;
};

export const deleteUser = async (id: number) => {
  const response = await api.delete<ApiResponse<void>>(`/users/${id}`);
  return response.data;
};
EOF
```

#### E. Update `app/globals.css`:
```bash
cat > app/globals.css << 'EOF'
@tailwind base;
@tailwind components;
@tailwind utilities;

@layer components {
  .btn {
    @apply px-4 py-2 rounded-lg font-medium transition-colors;
  }
  .btn-primary {
    @apply bg-blue-600 text-white hover:bg-blue-700;
  }
  .btn-secondary {
    @apply bg-gray-200 text-gray-800 hover:bg-gray-300;
  }
  .card {
    @apply bg-white rounded-lg shadow-md p-6;
  }
}
EOF
```

#### F. Update `app/layout.tsx`:
```bash
cat > app/layout.tsx << 'EOF'
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import { Toaster } from 'react-hot-toast';
import "./globals.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "User Management System",
  description: "Built with Next.js and Spring Boot",
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body className={inter.className}>
        {children}
        <Toaster position="top-right" />
      </body>
    </html>
  );
}
EOF
```

#### G. Update `app/page.tsx`:
```bash
cat > app/page.tsx << 'EOF'
'use client';

import { useEffect, useState } from 'react';
import { getAllUsers } from '@/lib/api';
import { User } from '@/lib/types';
import toast from 'react-hot-toast';
import Link from 'next/link';

export default function Home() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const response = await getAllUsers(0, 10);
      setUsers(response.data.content);
    } catch (error) {
      toast.error('Failed to load users');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow">
        <div className="container mx-auto px-4 py-6">
          <h1 className="text-3xl font-bold text-gray-900">User Management System</h1>
        </div>
      </header>

      {/* Main Content */}
      <main className="container mx-auto px-4 py-8">
        <div className="flex justify-between items-center mb-6">
          <h2 className="text-2xl font-semibold">Users</h2>
          <Link href="/users/create" className="btn btn-primary">
            + Create User
          </Link>
        </div>

        {loading ? (
          <p className="text-center py-8">Loading users...</p>
        ) : users.length === 0 ? (
          <p className="text-center py-8 text-gray-500">No users found</p>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
            {users.map((user) => (
              <div key={user.id} className="card">
                <h3 className="text-xl font-bold mb-2">{user.fullName}</h3>
                <p className="text-gray-600 text-sm mb-1">@{user.username}</p>
                <p className="text-gray-600 text-sm mb-3">{user.email}</p>
                <div className="flex gap-2">
                  <span className="inline-block px-2 py-1 text-xs font-semibold rounded bg-blue-100 text-blue-800">
                    {user.role}
                  </span>
                  <span className="inline-block px-2 py-1 text-xs font-semibold rounded bg-green-100 text-green-800">
                    {user.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}
EOF
```

---

### Step 3: Run the Application

```bash
# Make sure you're in the frontend directory
cd /Users/puspo/JavaCourse/frontend

# Start the development server
npm run dev
```

---

## 🎯 Testing the Setup

1. **Start Backend** (if not already running):
   ```bash
   # In a separate terminal
   cd /Users/puspo/JavaCourse/newZedCode
   mvn spring-boot:run
   ```

2. **Start Frontend**:
   ```bash
   cd /Users/puspo/JavaCourse/frontend
   npm run dev
   ```

3. **Open Browser**:
   - Frontend: http://localhost:3000
   - Backend Swagger: http://localhost:8080/api/swagger-ui.html

4. **Test the Integration**:
   - You should see users loaded from the backend
   - Try creating a user through Swagger
   - Refresh the frontend to see the new user

---

## 🔧 Troubleshooting

### CORS Issues?
If you get CORS errors, make sure your Spring Boot backend has CORS configured in `CorsConfig.java`:

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
            .allowedHeaders("*");
    }
}
```

### Connection Refused?
- Make sure backend is running on port 8080
- Check `.env.local` has correct API URL
- Verify `NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1`

### TypeScript Errors?
Run: `npm install --save-dev @types/node @types/react @types/react-dom`

---

## 📚 What Was Created

Your frontend now has:
- ✅ Next.js 14 with App Router
- ✅ TypeScript for type safety
- ✅ Tailwind CSS for styling
- ✅ Axios for API calls
- ✅ React Hot Toast for notifications
- ✅ Full type definitions matching backend
- ✅ User list display
- ✅ Integration with Spring Boot backend

---

## 🚀 Next Steps

1. **Add more pages**:
   - Create user form (`app/users/create/page.tsx`)
   - User detail page (`app/users/[id]/page.tsx`)
   - Edit user page (`app/users/edit/[id]/page.tsx`)

2. **Add more components**:
   - Navbar component
   - UserCard component
   - Pagination component
   - Search bar

3. **Enhance features**:
   - Add search functionality
   - Implement pagination
   - Add user filtering by role/status
   - Add loading states and error handling

---

## 📖 Full Code Examples

For complete code examples including all pages and components, see:
- `FRONTEND_COMPLETE_CODE.md` - Complete file contents
- `FRONTEND_SETUP_GUIDE.md` - Detailed setup guide

---

## 🎉 Success!

Your Next.js frontend is now connected to your Spring Boot backend!

**Test it**:
1. Open http://localhost:3000
2. You should see users from your database
3. Backend API is available at http://localhost:8080/api/v1

**Need help?** Check:
- Backend logs: Look for CORS or connection errors
- Frontend console: Press F12 in browser
- Network tab: See if API calls are working

---

**Happy Coding! 🚀**