# 🎨 Next.js Frontend Setup Guide

Complete guide to set up a Next.js 14 + TypeScript + Tailwind CSS frontend that connects to your Spring Boot backend.

---

## 📋 Table of Contents

1. [Quick Setup (5 Minutes)](#quick-setup)
2. [Manual Setup (Detailed)](#manual-setup)
3. [Project Structure](#project-structure)
4. [Complete Code Files](#complete-code-files)
5. [Running the Application](#running-the-application)
6. [Testing the Integration](#testing-the-integration)

---

## 🚀 Quick Setup (5 Minutes)

### Step 1: Create Next.js Project

```bash
# Navigate to the parent directory
cd /Users/puspo/JavaCourse

# Create Next.js app with TypeScript and Tailwind
npx create-next-app@latest frontend --typescript --tailwind --app --no-src-dir --import-alias "@/*" --use-npm --eslint

# Navigate to frontend
cd frontend
```

### Step 2: Install Dependencies

```bash
npm install axios react-hot-toast lucide-react date-fns
npm install -D @types/node
```

### Step 3: Update Configuration Files

Replace the contents of these files with the code provided in the [Complete Code Files](#complete-code-files) section below.

### Step 4: Start Development Servers

```bash
# Terminal 1: Start Backend (in newZedCode directory)
cd /Users/puspo/JavaCourse/newZedCode
mvn spring-boot:run

# Terminal 2: Start Frontend (in frontend directory)
cd /Users/puspo/JavaCourse/frontend
npm run dev
```

### Step 5: Open Browser

Visit: `http://localhost:3000`

---

## 🛠️ Manual Setup (Detailed)

### Prerequisites

- Node.js 18+ installed
- npm or yarn installed
- Backend running on `http://localhost:8080`

---

## 📁 Project Structure

After setup, your `frontend` directory should look like this:

```
frontend/
├── app/
│   ├── layout.tsx                 # Root layout
│   ├── page.tsx                   # Home page
│   ├── users/
│   │   ├── page.tsx              # Users list page
│   │   ├── [id]/
│   │   │   └── page.tsx          # User detail page
│   │   ├── create/
│   │   │   └── page.tsx          # Create user page
│   │   └── edit/
│   │       └── [id]/
│   │           └── page.tsx      # Edit user page
│   └── globals.css               # Global styles
├── components/
│   ├── UserCard.tsx              # User card component
│   ├── UserForm.tsx              # User form component
│   ├── Pagination.tsx            # Pagination component
│   ├── SearchBar.tsx             # Search component
│   ├── Navbar.tsx                # Navigation component
│   └── Loading.tsx               # Loading component
├── lib/
│   ├── api.ts                    # API client
│   ├── types.ts                  # TypeScript types
│   └── utils.ts                  # Utility functions
├── public/
│   └── (static assets)
├── .env.local                    # Environment variables
├── next.config.js                # Next.js configuration
├── tailwind.config.ts            # Tailwind configuration
├── tsconfig.json                 # TypeScript configuration
└── package.json                  # Dependencies
```

---

## 📝 Complete Code Files

### 1. `package.json`

```json
{
  "name": "frontend",
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
```

---

### 2. `.env.local`

```env
# Backend API URL
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1

# App Configuration
NEXT_PUBLIC_APP_NAME=User Management System
NEXT_PUBLIC_APP_VERSION=1.0.0
```

---

### 3. `next.config.js`

```javascript
/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  async rewrites() {
    return [
      {
        source: '/api/:path*',
        destination: 'http://localhost:8080/api/:path*',
      },
    ]
  },
  images: {
    domains: ['localhost'],
  },
}

module.exports = nextConfig
```

---

### 4. `tailwind.config.ts`

```typescript
import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          100: '#dbeafe',
          200: '#bfdbfe',
          300: '#93c5fd',
          400: '#60a5fa',
          500: '#3b82f6',
          600: '#2563eb',
          700: '#1d4ed8',
          800: '#1e40af',
          900: '#1e3a8a',
        },
      },
    },
  },
  plugins: [],
};

export default config;
```

---

### 5. `tsconfig.json`

```json
{
  "compilerOptions": {
    "target": "ES2017",
    "lib": ["dom", "dom.iterable", "esnext"],
    "allowJs": true,
    "skipLibCheck": true,
    "strict": true,
    "noEmit": true,
    "esModuleInterop": true,
    "module": "esnext",
    "moduleResolution": "bundler",
    "resolveJsonModule": true,
    "isolatedModules": true,
    "jsx": "preserve",
    "incremental": true,
    "plugins": [
      {
        "name": "next"
      }
    ],
    "paths": {
      "@/*": ["./*"]
    }
  },
  "include": ["next-env.d.ts", "**/*.ts", "**/*.tsx", ".next/types/**/*.ts"],
  "exclude": ["node_modules"]
}
```

---

### 6. `lib/types.ts`

```typescript
// TypeScript types matching the backend API

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
  enabled: boolean;
  accountNonLocked: boolean;
  lastLoginAt?: string;
  profileImageUrl?: string;
  bio?: string;
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
  bio?: string;
}

export interface UpdateUserRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  role?: UserRole;
  status?: UserStatus;
  bio?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  timestamp: string;
  metadata?: any;
}

export interface PageResponse<T> {
  content: T[];
  pageNumber: number;
  pageSize: number;
  totalElements: number;
  totalPages: number;
  last: boolean;
  first: boolean;
  hasNext: boolean;
  hasPrevious: boolean;
  numberOfElements: number;
  empty: boolean;
}

export interface ErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  validationErrors?: Record<string, string>;
}
```

---

### 7. `lib/api.ts`

```typescript
import axios, { AxiosError } from 'axios';
import type {
  User,
  CreateUserRequest,
  UpdateUserRequest,
  ApiResponse,
  PageResponse,
  ErrorResponse,
  UserStatus,
  UserRole,
} from './types';

// Create axios instance
const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor (for adding auth token if needed)
api.interceptors.request.use(
  (config) => {
    // You can add auth token here if needed
    // const token = localStorage.getItem('token');
    // if (token) {
    //   config.headers.Authorization = `Bearer ${token}`;
    // }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor (for error handling)
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ErrorResponse>) => {
    console.error('API Error:', error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// API Functions

/**
 * Get all users with pagination
 */
export const getAllUsers = async (
  page: number = 0,
  size: number = 10,
  sort: string = 'id,desc'
): Promise<ApiResponse<PageResponse<User>>> => {
  const response = await api.get('/users', {
    params: { page, size, sort },
  });
  return response.data;
};

/**
 * Get user by ID
 */
export const getUserById = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.get(`/users/${id}`);
  return response.data;
};

/**
 * Create new user
 */
export const createUser = async (
  userData: CreateUserRequest
): Promise<ApiResponse<User>> => {
  const response = await api.post('/users', userData);
  return response.data;
};

/**
 * Update user
 */
export const updateUser = async (
  id: number,
  userData: UpdateUserRequest
): Promise<ApiResponse<User>> => {
  const response = await api.put(`/users/${id}`, userData);
  return response.data;
};

/**
 * Delete user (soft delete)
 */
export const deleteUser = async (id: number): Promise<ApiResponse<void>> => {
  const response = await api.delete(`/users/${id}`);
  return response.data;
};

/**
 * Search users
 */
export const searchUsers = async (
  searchTerm: string,
  page: number = 0,
  size: number = 10
): Promise<ApiResponse<PageResponse<User>>> => {
  const response = await api.get('/users/search', {
    params: { searchTerm, page, size },
  });
  return response.data;
};

/**
 * Get users by status
 */
export const getUsersByStatus = async (
  status: UserStatus,
  page: number = 0,
  size: number = 10
): Promise<ApiResponse<PageResponse<User>>> => {
  const response = await api.get(`/users/status/${status}`, {
    params: { page, size },
  });
  return response.data;
};

/**
 * Get users by role
 */
export const getUsersByRole = async (
  role: UserRole,
  page: number = 0,
  size: number = 10
): Promise<ApiResponse<PageResponse<User>>> => {
  const response = await api.get(`/users/role/${role}`, {
    params: { page, size },
  });
  return response.data;
};

/**
 * Activate user
 */
export const activateUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/activate`);
  return response.data;
};

/**
 * Deactivate user
 */
export const deactivateUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/deactivate`);
  return response.data;
};

/**
 * Block user
 */
export const blockUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/block`);
  return response.data;
};

/**
 * Unblock user
 */
export const unblockUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/unblock`);
  return response.data;
};

/**
 * Check if email exists
 */
export const checkEmailExists = async (
  email: string
): Promise<ApiResponse<boolean>> => {
  const response = await api.get('/users/exists/email', {
    params: { email },
  });
  return response.data;
};

/**
 * Check if username exists
 */
export const checkUsernameExists = async (
  username: string
): Promise<ApiResponse<boolean>> => {
  const response = await api.get('/users/exists/username', {
    params: { username },
  });
  return response.data;
};

export default api;
```

---

### 8. `lib/utils.ts`

```typescript
import { format } from 'date-fns';
import { UserStatus, UserRole } from './types';

/**
 * Format date to readable string
 */
export const formatDate = (dateString: string): string => {
  try {
    return format(new Date(dateString), 'MMM dd, yyyy');
  } catch {
    return dateString;
  }
};

/**
 * Format date to datetime string
 */
export const formatDateTime = (dateString: string): string => {
  try {
    return format(new Date(dateString), 'MMM dd, yyyy HH:mm');
  } catch {
    return dateString;
  }
};

/**
 * Get status badge color
 */
export const getStatusColor = (status: UserStatus): string => {
  const colors: Record<UserStatus, string> = {
    [UserStatus.ACTIVE]: 'bg-green-100 text-green-800',
    [UserStatus.INACTIVE]: 'bg-gray-100 text-gray-800',
    [UserStatus.PENDING]: 'bg-yellow-100 text-yellow-800',
    [UserStatus.BLOCKED]: 'bg-red-100 text-red-800',
    [UserStatus.DELETED]: 'bg-red-100 text-red-800',
  };
  return colors[status] || 'bg-gray-100 text-gray-800';
};

/**
 * Get role badge color
 */
export const getRoleColor = (role: UserRole): string => {
  const colors: Record<UserRole, string> = {
    [UserRole.USER]: 'bg-blue-100 text-blue-800',
    [UserRole.ADMIN]: 'bg-purple-100 text-purple-800',
    [UserRole.MODERATOR]: 'bg-indigo-100 text-indigo-800',
    [UserRole.SUPER_ADMIN]: 'bg-pink-100 text-pink-800',
  };
  return colors[role] || 'bg-gray-100 text-gray-800';
};

/**
 * Truncate text
 */
export const truncate = (text: string, length: number): string => {
  if (text.length <= length) return text;
  return text.substring(0, length) + '...';
};
```

---

### 9. `app/globals.css`

```css
@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  html {
    @apply scroll-smooth;
  }
  
  body {
    @apply bg-gray-50 text-gray-900 antialiased;
  }
}

@layer components {
  .btn {
    @apply px-4 py-2 rounded-lg font-medium transition-colors duration-200 disabled:opacity-50 disabled:cursor-not-allowed;
  }
  
  .btn-primary {
    @apply bg-blue-600 text-white hover:bg-blue-700;
  }
  
  .btn-secondary {
    @apply bg-gray-200 text-gray-800 hover:bg-gray-300;
  }
  
  .btn-danger {
    @apply bg-red-600 text-white hover:bg-red-700;
  }
  
  .input {
    @apply w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent;
  }
  
  .card {
    @apply bg-white rounded-lg shadow-md p-6;
  }
  
  .badge {
    @apply inline-block px-3 py-1 text-sm font-semibold rounded-full;
  }
}
```

---

### 10. `app/layout.tsx`

```typescript
import type { Metadata } from "next";
import { Inter } from "next/font/google";
import { Toaster } from 'react-hot-toast';
import Navbar from '@/components/Navbar';
import "./globals.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "User Management System",
  description: "Modern user management system built with Next.js and Spring Boot",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <Navbar />
        <main className="min-h-screen">
          {children}
        </main>
        <Toaster position="top-right" />
      </body>
    </html>
  );
}
```

---

### 11. `app/page.tsx`

```typescript
import Link from 'next/link';
import { Users, Shield, Zap, Database } from 'lucide-react';

export default function Home() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
      {/* Hero Section */}
      <div className="container mx-auto px-4 py-20">
        <div className="text-center mb-16">
          <h1 className="text-5xl font-bold text-gray-900 mb-4">
            User Management System
          </h1>
          <p className="text-xl text-gray-600 mb-8">
            Modern, scalable user management with Next.js and Spring Boot
          </p>
          <div className="flex gap-4 justify-center">
            <Link
              href="/users"
              className="btn btn-primary px-8 py-3 text-lg"
            >
              View Users
            </Link>
            <Link
              href="/users/create"
              className="btn btn-secondary px-8 py-3 text-lg"
            >
              Create User
            </Link>
          </div>
        </div>

        {/* Features */}
        <div className="grid md:grid-cols-2 lg:grid-cols-4 gap-8 mt-20">
          <FeatureCard
            icon={<Users className="w-12 h-12 text-blue-600" />}
            title="User Management"
            description="Complete CRUD operations for managing users"
          />
          <FeatureCard
            icon={<Shield className="w-12 h-12 text-green-600" />}
            title="Role-Based Access"
            description="Flexible role and permission system"
          />
          <FeatureCard
            icon={<Zap className="w-12 h-12 text-yellow-600" />}
            title="Real-Time Updates"
            description="Instant synchronization with backend"
          />
          <FeatureCard
            icon={<Database className="w-12 h-12 text-purple-600" />}
            title="Pagination & Search"
            description="Efficient data handling and filtering"
          />
        </div>

        {/* Tech Stack */}
        <div className="mt-20 text-center">
          <h2 className="text-3xl font-bold mb-8">Built With Modern Tech</h2>
          <div className="flex flex-wrap justify-center gap-6">
            <TechBadge name="Next.js 14" color="bg-black" />
            <TechBadge name="TypeScript" color="bg-blue-600" />
            <TechBadge name="Tailwind CSS" color="bg-cyan-500" />
            <TechBadge name="Spring Boot" color="bg-green-600" />
            <TechBadge name="PostgreSQL" color="bg-blue-700" />
            <TechBadge name="REST API" color="bg-orange-500" />
          </div>
        </div>
      </div>
    </div>
  );
}

function FeatureCard({
  icon,
  title,
  description,
}: {
  icon: React.ReactNode;
  title: string;
  description: string;
}) {
  return (
    <div className="card text-center hover:shadow-xl transition-shadow">
      <div className="flex justify-center mb-4">{icon}</div>
      <h3 className="text-xl font-semibold mb-2">{title}</h3>
      <p className="text-gray-600">{description}</p>
    </div>
  );
}

function TechBadge({ name, color }: { name: string; color: string }) {
  return (
    <span className={`${color} text-white px-6 py-2 rounded-full font-semibold`}>
      {name}
    </span>
  );
}
```

---

### 12. `components/Navbar.tsx`

```typescript
'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import { Users, Home, Plus } from 'lucide-react';

export default function Navbar() {
  const pathname = usePathname();

  const isActive = (path: string) => pathname === path;

  return (
    <nav className="bg-white shadow-md">
      <div className="container mx-auto px-4">
        <div className="flex items-center justify-between h-16">
          {/* Logo */}
          <Link href="/" className="flex items-center space-x-2">
            <Users className="w-8 h-8 text-blue-600" />
            <span className="text-xl font-bold text-gray-900">
              User Management
            </span>
          </Link>

          {/* Navigation Links */}
          <div className="flex items-center space-x-4">
            <NavLink href="/" active={isActive('/')} icon={<Home className="w-4 h-4" />}>
              Home
            </NavLink>
            <NavLink href="/users" active={isActive('/users')} icon={<Users className="w-4 h-4" />}>
              Users
            </NavLink>
            <NavLink href="/users/create" active={isActive('/users/create')} icon={<Plus className="w-4 h-4" />}>
              Create User
            </NavLink>
          </div>
        </div>
      </div>
    </nav>
  );
}

function NavLink({
  href,
  active,
  icon,
  children,
}: {
  href: string;
  active: boolean;
  icon: React.ReactNode;
  children: React.ReactNode;
}) {
  return (
    <Link
      href={href}
      className={`flex items-center space-x-1 px-4 py-2 rounded-lg font-medium transition-colors ${
        active
          ? 'bg-blue-600 text-white'
          : 'text-gray-700 hover:bg-gray-100'
      }`}
    >
      {icon}
      <span>{children}</span>
    </Link>
  );
}
```

---

### 13. `components/UserCard.tsx`

```typescript
'use client';

import Link from 'next/link';
import { User, UserStatus } from '@/lib/types';
import { getStatusColor, getRoleColor, formatDate } from '@/lib/utils';
import { Mail, Phone, Calendar, Edit, Trash2, Eye } from 'lucide-react';
import toast from 'react-hot-toast';

interface UserCardProps {
  user: User;
  onDelete?: (id: number) => void;
}

export default function UserCard({ user, onDelete }: UserCardProps) {
  const handleDelete = () => {
    if (confirm(`Are you sure you want to delete ${user.fullName}?`)) {
      onDelete?.(user.id);
    }
  };

  return (
    <div className="card hover:shadow-xl transition-shadow">
      {/* Header */}
      <div className="flex items-start justify-between mb-4">
        <div>
          <h3 className="text-xl font-bold text-gray-900">{user.fullName}</h3>
          <p className="text-gray-600">@{user.username}</p>
        </div>
        <div className="flex gap-2">
          <span className={`badge ${getStatusColor(user.status)}`}>
            {user.status}
          </span>
          <span className={`badge ${getRoleColor(user.role)}`}>
            {user.role}
          </span>
        </div>
      </div>

      {/* Info */}
      <div className="space-y-2 mb-4">
        <div className="flex items-center text-gray-600">
          <Mail className="w-4 h-4 mr-2" />
          <span className="text-sm">{user.email}</span>
        </div>
        {user.phoneNumber && (
          <div className="flex items-center text-gray-600">
            <Phone className="w-4 h-4 mr-2" />
            <span className="text-sm">{user.phoneNumber}</span>
          </div>
        )}
        <div className="flex items-center text-gray-600">
          <Calendar className="w-4 h-4 mr-2" />
          <span className="text-sm">Joined {formatDate(user.createdAt)}</span>
        </div>
      </div>

      {/* Bio */}
      {user.bio && (
        <p className="text-gray-700 text-sm mb-4 line-clamp-2">{user.bio}</p>
      )}

      {/* Actions */}
      <div className="flex gap-2 pt-4 border-t border-gray-200">
        <Link
          href={`/users/${user.id}`}
          className="btn btn-secondary flex-1 flex items-center justify-center gap-2"
        >
          <Eye className="w-4 h-4" />
          View
        </Link>
        <Link
          href={`/users/edit/${user.id}`}
          className="btn btn-primary flex-1 flex items-center justify-center gap-2"
        >
          <Edit className="w-4 h-4" />
          Edit
        </Link>
        <button
          onClick={handleDelete}
          className="btn btn-danger flex items-center justify-center gap-2 px-4"
        >
          <Trash2 className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
}
```

---

### 14. `components/Pagination.tsx`

```typescript
'use client';

import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-react';

interface PaginationProps {
  currentPage: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  hasNext: boolean;
  hasPrevious: boolean;
}

export default function Pagination({
  currentPage,
  totalPages,
  onPageChange,
  hasNext,
  hasPrevious,
}: PaginationProps) {
  return (
    <div className="flex items-center justify-between mt-6">
      <div className="text-sm text-gray-600">
        Page {currentPage + 1} of {totalPages}
      </div>

      <div className="flex gap-2">
        {/* First Page */}
        <button
          onClick={() => onPageChange(0)}
          disabled={!hasPrevious}
          className="btn btn-secondary px-3"
          title="First Page"
        >
          <ChevronsLeft className="w-4 h-4" />
        </button>

        {/* Previous Page */}
        <button
          onClick={() => onPageChange(currentPage - 1)}
          disabled={!hasPrevious}
          className="btn btn-secondary px-3"
          title="Previous Page"
        >
          <ChevronLeft className="w-4 h-4" />
        </button>

        {/* Page Numbers */}
        {getPageNumbers(currentPage, totalPages).map((page, index) =>
          page === -1 ? (
            <span key={index} className="px-4 py-2 text-gray-400">
              ...
            </span>
          ) : (
            <button
              key={index}
              onClick={() => onPageChange(page)}
              className={`px-4 py-2 rounded-lg font-medium transition-colors