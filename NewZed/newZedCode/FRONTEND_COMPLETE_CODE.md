# 🎨 Complete Next.js Frontend Code Repository

This document contains ALL the code you need to create a fully functional Next.js frontend that connects to your Spring Boot backend.

---

## 📋 Quick Setup Guide

### Step 1: Create Next.js Project

```bash
# Navigate to JavaCourse directory (parent of newZedCode)
cd /Users/puspo/JavaCourse

# Create Next.js app
npx create-next-app@latest frontend --typescript --tailwind --app --no-src-dir --import-alias "@/*" --use-npm

# When prompted, select:
# - Would you like to use ESLint? Yes
# - Would you like to use `src/` directory? No
# - Would you like to use App Router? Yes
# - Would you like to customize the default import alias? No

cd frontend

# Install dependencies
npm install axios react-hot-toast lucide-react date-fns
```

### Step 2: Replace/Create Files

Use the code below to create or replace files in your `frontend` directory.

### Step 3: Start Servers

```bash
# Terminal 1: Backend (in newZedCode directory)
cd /Users/puspo/JavaCourse/newZedCode
mvn spring-boot:run

# Terminal 2: Frontend (in frontend directory)
cd /Users/puspo/JavaCourse/frontend
npm run dev
```

### Step 4: Open Browser

Visit: http://localhost:3000

---

## 📁 Project Structure

```
frontend/
├── app/
│   ├── layout.tsx
│   ├── page.tsx
│   ├── globals.css
│   └── users/
│       ├── page.tsx
│       ├── create/
│       │   └── page.tsx
│       ├── edit/
│       │   └── [id]/
│       │       └── page.tsx
│       └── [id]/
│           └── page.tsx
├── components/
│   ├── Navbar.tsx
│   ├── UserCard.tsx
│   ├── UserForm.tsx
│   ├── Pagination.tsx
│   ├── SearchBar.tsx
│   └── Loading.tsx
├── lib/
│   ├── api.ts
│   ├── types.ts
│   └── utils.ts
├── public/
├── .env.local
├── next.config.js
├── tailwind.config.ts
├── tsconfig.json
└── package.json
```

---

## 📝 Complete File Contents

### 1. `package.json`

```json
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
```

---

### 2. `.env.local`

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
NEXT_PUBLIC_APP_NAME=User Management System
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
// TypeScript types matching the Spring Boot backend

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
```

---

### 7. `lib/api.ts`

```typescript
import axios from 'axios';
import type {
  User,
  CreateUserRequest,
  UpdateUserRequest,
  ApiResponse,
  PageResponse,
  UserStatus,
  UserRole,
} from './types';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
});

// API Functions
export const getAllUsers = async (
  page: number = 0,
  size: number = 10,
  sort: string = 'id,desc'
): Promise<ApiResponse<PageResponse<User>>> => {
  const response = await api.get('/users', { params: { page, size, sort } });
  return response.data;
};

export const getUserById = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.get(`/users/${id}`);
  return response.data;
};

export const createUser = async (userData: CreateUserRequest): Promise<ApiResponse<User>> => {
  const response = await api.post('/users', userData);
  return response.data;
};

export const updateUser = async (
  id: number,
  userData: UpdateUserRequest
): Promise<ApiResponse<User>> => {
  const response = await api.put(`/users/${id}`, userData);
  return response.data;
};

export const deleteUser = async (id: number): Promise<ApiResponse<void>> => {
  const response = await api.delete(`/users/${id}`);
  return response.data;
};

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

export const activateUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/activate`);
  return response.data;
};

export const deactivateUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/deactivate`);
  return response.data;
};

export const blockUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/block`);
  return response.data;
};

export const unblockUser = async (id: number): Promise<ApiResponse<User>> => {
  const response = await api.patch(`/users/${id}/unblock`);
  return response.data;
};

export default api;
```

---

### 8. `lib/utils.ts`

```typescript
import { format } from 'date-fns';
import { UserStatus, UserRole } from './types';

export const formatDate = (dateString: string): string => {
  try {
    return format(new Date(dateString), 'MMM dd, yyyy');
  } catch {
    return dateString;
  }
};

export const formatDateTime = (dateString: string): string => {
  try {
    return format(new Date(dateString), 'MMM dd, yyyy HH:mm');
  } catch {
    return dateString;
  }
};

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

export const getRoleColor = (role: UserRole): string => {
  const colors: Record<UserRole, string> = {
    [UserRole.USER]: 'bg-blue-100 text-blue-800',
    [UserRole.ADMIN]: 'bg-purple-100 text-purple-800',
    [UserRole.MODERATOR]: 'bg-indigo-100 text-indigo-800',
    [UserRole.SUPER_ADMIN]: 'bg-pink-100 text-pink-800',
  };
  return colors[role] || 'bg-gray-100 text-gray-800';
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
  description: "Modern user management with Next.js and Spring Boot",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
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
      <div className="container mx-auto px-4 py-20">
        <div className="text-center mb-16">
          <h1 className="text-5xl font-bold text-gray-900 mb-4">
            User Management System
          </h1>
          <p className="text-xl text-gray-600 mb-8">
            Modern, scalable user management with Next.js and Spring Boot
          </p>
          <div className="flex gap-4 justify-center">
            <Link href="/users" className="btn btn-primary px-8 py-3 text-lg">
              View Users
            </Link>
            <Link href="/users/create" className="btn btn-secondary px-8 py-3 text-lg">
              Create User
            </Link>
          </div>
        </div>

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

function FeatureCard({ icon, title, description }: {
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
          <Link href="/" className="flex items-center space-x-2">
            <Users className="w-8 h-8 text-blue-600" />
            <span className="text-xl font-bold text-gray-900">
              User Management
            </span>
          </Link>

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

function NavLink({ href, active, icon, children }: {
  href: string;
  active: boolean;
  icon: React.ReactNode;
  children: React.ReactNode;
}) {
  return (
    <Link
      href={href}
      className={`flex items-center space-x-1 px-4 py-2 rounded-lg font-medium transition-colors ${
        active ? 'bg-blue-600 text-white' : 'text-gray-700 hover:bg-gray-100'
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
import { User } from '@/lib/types';
import { getStatusColor, getRoleColor, formatDate } from '@/lib/utils';
import { Mail, Phone, Calendar, Edit, Trash2, Eye } from 'lucide-react';

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

      {user.bio && (
        <p className="text-gray-700 text-sm mb-4 line-clamp-2">{user.bio}</p>
      )}

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

import { ChevronLeft, ChevronRight } from 'lucide-react';

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
        <button
          onClick={() => onPageChange(currentPage - 1)}
          disabled={!hasPrevious}
          className="btn btn-secondary px-3"
        >
          <ChevronLeft className="w-4 h-4" />
        </button>

        {[...Array(totalPages)].map((_, i) => (
          <button
            key={i}
            onClick={() => onPageChange(i)}
            className={`px-4 py-2 rounded-lg font-medium transition-colors ${
              i === currentPage
                ? 'bg-blue-600 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            }`}
          >
            {i + 1}
          </button>
        )).slice(0, 5)}

        <button
          onClick={() => onPageChange(currentPage + 1)}
          disabled={!hasNext}
          className="btn btn-secondary px-3"
        >
          <ChevronRight className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
}
```

---

### 15. `components/SearchBar.tsx`

```typescript
'use client';

import { Search } from 'lucide-react';
import { useState } from 'react';

interface SearchBarProps {
  onSearch: (term: string) => void;
  placeholder?: string;
}

export default function SearchBar({ onSearch, placeholder = 'Search...' }: SearchBarProps) {
  const [searchTerm, setSearchTerm] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch(searchTerm);
  };

  return (
    <form onSubmit={handleSubmit} className="flex gap-2">
      <div className="relative flex-1">
        <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
        <input
          type="text"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          placeholder={placeholder}
          className="input pl-10"
        />
      </div>
      <button type="submit" className="btn btn-primary">
        Search
      </button>
    </form>
  );
}
```

---

### 16. `components/Loading.tsx`

```typescript
import { Loader2 } from 'lucide-react';

export default function Loading() {
  return (
    <div className="flex items-center justify-center min-h-[400px]">
      <Loader2 className="w-12 h-12 text-blue-600 animate-spin" />
    </div>
  );
}
```

---

### 17. `components/UserForm.tsx`

```typescript
'use client';

import { useState } from 'react';
import { CreateUserRequest, UpdateUserRequest, UserRole, UserStatus } from '@/lib/types';

interface UserFormProps {
  initialData?: Partial<CreateUserRequest | UpdateUserRequest>;
  onSubmit: (data: CreateUserRequest | UpdateUserRequest) => Promise<void>;
  isEdit?: boolean;
}

export default function UserForm({ initialData = {}, onSubmit, isEdit = false }: UserFormProps) {
  const [formData, setFormData] = useState({
    firstName: initialData.firstName || '',
    lastName: initialData.lastName || '',
    username: (initialData as CreateUserRequest).username || '',
    email: initialData.email || '',
    password: '',
    phoneNumber: initialData.phoneNumber || '',
    role: initialData.role || UserRole.USER,
    bio: initialData.bio || '',
  });

  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try {
      await onSubmit(formData);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="grid md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            First Name *
          </label>
          <input
            type="text"
            required
            value={formData.firstName}
            onChange={(e) => setFormData({ ...formData, firstName: e.target.value })}
            className="input"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Last Name *
          </label>
          <input
            type="text"
            required
            value={formData.lastName}
            onChange={(e) => setFormData({ ...formData, lastName: e.target.value })}
            className="input"
          />
        </div>
      </div>

      {!isEdit && (
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Username *
          </label>
          <input
            type="text"
            required
            value={formData.username}
            onChange={(e) => setFormData({ ...formData, username: e.target.value })}
            className="input"
          />
        </div>
      )}

      <div>
        <label className="block text-sm font-medium text-gray-700 mb-1">
          Email *
        </label>
        <input
          type="email"
          required
          value={formData.email}
          onChange={(e) => setFormData({ ...formData, email: e.target.value })}
          className="input"
        />
      </div>

      {!isEdit && (
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Password *
          </label>
          <input
            type="password"
            required
            value={formData.password}
            onChange={(e) => setFormData({ ...formData, password: e.target.value })}
            
