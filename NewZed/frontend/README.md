# 🎨 User Management System - Frontend

A modern, responsive frontend application built with **Next.js 14**, **TypeScript**, and **Tailwind CSS** that connects seamlessly to the Spring Boot backend for comprehensive user management.

---

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Project Structure](#project-structure)
- [Configuration](#configuration)
- [API Integration](#api-integration)
- [Components](#components)
- [Pages](#pages)
- [Utilities](#utilities)
- [Styling](#styling)
- [Development](#development)
- [Building for Production](#building-for-production)
- [Deployment](#deployment)
- [Troubleshooting](#troubleshooting)
- [Best Practices](#best-practices)
- [Contributing](#contributing)

---

## 🎯 Overview

This is the frontend application for the User Management System. It provides a modern, intuitive user interface for managing users, with features like:

- **CRUD Operations**: Create, read, update, and delete users
- **Search & Filter**: Search users by keyword, filter by status and role
- **Pagination**: Efficient handling of large datasets
- **Real-time Feedback**: Toast notifications for all operations
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Type Safety**: Full TypeScript integration with backend DTOs
- **Modern UI**: Clean, professional design with Tailwind CSS

---

## ✨ Features

### User Management
- ✅ Create new users with validation
- ✅ View user details
- ✅ Edit user information
- ✅ Delete users (soft delete and permanent delete)
- ✅ Activate/deactivate user accounts
- ✅ Block/unblock users
- ✅ Verify user emails

### Search & Filter
- 🔍 Search users by name, email, or username
- 📊 Filter by user status (Active, Inactive, Pending, Blocked, Deleted)
- 👥 Filter by user role (User, Admin, Moderator, Super Admin)
- 📄 Paginated results with customizable page size

### Data Display
- 📊 User statistics dashboard
- 🏷️ Color-coded status and role badges
- 📅 Formatted dates and timestamps
- 📱 Responsive table and card views

### User Experience
- 🎨 Modern, clean UI
- 🌐 Responsive design for all screen sizes
- ⚡ Fast page loads with Next.js optimization
- 🔔 Real-time toast notifications
- ⌨️ Keyboard shortcuts support
- ♿ Accessibility features

---

## 🛠️ Tech Stack

### Core Framework
- **[Next.js 14](https://nextjs.org/)** - React framework with App Router
- **[React 18](https://react.dev/)** - UI library
- **[TypeScript](https://www.typescriptlang.org/)** - Type-safe JavaScript

### Styling
- **[Tailwind CSS](https://tailwindcss.com/)** - Utility-first CSS framework
- **[PostCSS](https://postcss.org/)** - CSS transformations
- **[Autoprefixer](https://github.com/postcss/autoprefixer)** - CSS vendor prefixes

### HTTP & State Management
- **[Axios](https://axios-http.com/)** - HTTP client for API calls
- **React Hooks** - Built-in state management

### UI Components
- **[React Hot Toast](https://react-hot-toast.com/)** - Toast notifications
- **[Lucide React](https://lucide.dev/)** - Beautiful icon library
- **[date-fns](https://date-fns.org/)** - Date formatting and manipulation

### Development Tools
- **[ESLint](https://eslint.org/)** - Code linting
- **[Prettier](https://prettier.io/)** - Code formatting (optional)

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

- **Node.js** (v18.0.0 or higher)
  ```bash
  node --version  # Should be v18+
  ```

- **npm** (v9.0.0 or higher)
  ```bash
  npm --version  # Should be v9+
  ```

- **Spring Boot Backend** running on `http://localhost:8080`
  - The backend must be running for the frontend to function
  - See backend README for setup instructions

---

## 🚀 Quick Start

### 1. Clone the Repository

```bash
cd /Users/puspo/JavaCourse/NewZed/frontend
```

### 2. Install Dependencies

```bash
npm install
```

This will install all required packages listed in `package.json`.

### 3. Configure Environment Variables

Create a `.env.local` file in the root directory:

```bash
# Copy the example file
cp .env.local.example .env.local
```

Edit `.env.local` and update the API URL if needed:

```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

### 4. Start the Development Server

```bash
npm run dev
```

The application will be available at **http://localhost:3000**

### 5. Open in Browser

Navigate to http://localhost:3000 and you should see the home page.

---

## 📁 Project Structure

```
frontend/
├── src/
│   ├── app/                    # Next.js App Router pages
│   │   ├── layout.tsx         # Root layout with toast provider
│   │   ├── page.tsx           # Home page
│   │   ├── globals.css        # Global styles
│   │   └── users/             # User management pages
│   │       ├── page.tsx       # User list page
│   │       ├── create/        # Create user page
│   │       ├── [id]/          # User detail page
│   │       └── edit/[id]/     # Edit user page
│   │
│   ├── components/            # Reusable React components
│   │   ├── Navbar.tsx         # Navigation bar
│   │   ├── UserCard.tsx       # User card component
│   │   ├── UserForm.tsx       # User form component
│   │   ├── Pagination.tsx     # Pagination component
│   │   ├── SearchBar.tsx      # Search input component
│   │   ├── StatusBadge.tsx    # Status badge component
│   │   ├── RoleBadge.tsx      # Role badge component
│   │   ├── Loading.tsx        # Loading spinner
│   │   ├── EmptyState.tsx     # Empty state component
│   │   └── ConfirmDialog.tsx  # Confirmation dialog
│   │
│   └── lib/                   # Utility libraries
│       ├── api.ts            # API client and functions
│       ├── types.ts          # TypeScript type definitions
│       └── utils.ts          # Helper functions
│
├── public/                    # Static assets
│   ├── favicon.ico
│   └── images/
│
├── .env.local                # Environment variables (not committed)
├── .env.local.example        # Example environment file
├── .gitignore               # Git ignore rules
├── next.config.js           # Next.js configuration
├── tailwind.config.ts       # Tailwind CSS configuration
├── tsconfig.json           # TypeScript configuration
├── postcss.config.js       # PostCSS configuration
├── package.json            # Dependencies and scripts
└── README.md              # This file
```

---

## ⚙️ Configuration

### Environment Variables

Environment variables are stored in `.env.local`:

```env
# API Configuration
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1

# Optional: API timeout in milliseconds
NEXT_PUBLIC_API_TIMEOUT=30000

# Optional: Enable debug mode
NEXT_PUBLIC_DEBUG_MODE=false
```

**Important Notes:**
- Variables prefixed with `NEXT_PUBLIC_` are exposed to the browser
- Never commit `.env.local` to version control
- Restart the dev server after changing environment variables

### Next.js Configuration

Edit `next.config.js` to customize:

```javascript
const nextConfig = {
  reactStrictMode: true,
  images: {
    remotePatterns: [/* ... */],
  },
  // Add custom configuration here
};
```

### Tailwind Configuration

Customize Tailwind in `tailwind.config.ts`:

```typescript
const config: Config = {
  theme: {
    extend: {
      colors: {
        // Custom colors
      },
    },
  },
};
```

---

## 🔌 API Integration

### API Client

The API client is configured in `src/lib/api.ts`:

```typescript
import axios from 'axios';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL,
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptors for authentication, error handling, etc.
```

### Available API Functions

#### User CRUD Operations
```typescript
import { getAllUsers, getUserById, createUser, updateUser, deleteUser } from '@/lib/api';

// Get all users with pagination
const response = await getAllUsers({ page: 0, size: 10, sort: 'id,desc' });

// Get single user
const user = await getUserById(1);

// Create user
const newUser = await createUser({
  firstName: 'John',
  lastName: 'Doe',
  username: 'johndoe',
  email: 'john@example.com',
  password: 'SecurePass123!',
});

// Update user
const updated = await updateUser(1, { firstName: 'Jane' });

// Delete user
await deleteUser(1);
```

#### Search and Filter
```typescript
import { searchUsers, getUsersByStatus, getUsersByRole } from '@/lib/api';

// Search users
const results = await searchUsers('john', { page: 0, size: 10 });

// Filter by status
const activeUsers = await getUsersByStatus('ACTIVE');

// Filter by role
const admins = await getUsersByRole('ADMIN');
```

#### User Management
```typescript
import { activateUser, deactivateUser, blockUser, unblockUser } from '@/lib/api';

// Activate user
await activateUser(1);

// Deactivate user
await deactivateUser(1);

// Block user
await blockUser(1);

// Unblock user
await unblockUser(1);
```

### Error Handling

All API functions throw errors that can be caught and handled:

```typescript
import { handleApiError } from '@/lib/api';

try {
  const user = await getUserById(1);
} catch (error) {
  const errorMessage = handleApiError(error);
  toast.error(errorMessage);
}
```

---

## 🧩 Components

### Core Components

#### Navbar
Navigation bar with logo and links.

```tsx
import Navbar from '@/components/Navbar';

<Navbar />
```

#### UserCard
Displays user information in a card format.

```tsx
import UserCard from '@/components/UserCard';

<UserCard 
  user={user} 
  onEdit={handleEdit}
  onDelete={handleDelete}
/>
```

#### UserForm
Form for creating or editing users.

```tsx
import UserForm from '@/components/UserForm';

<UserForm 
  initialData={user}
  onSubmit={handleSubmit}
  mode="edit" // or "create"
/>
```

#### Pagination
Pagination controls for lists.

```tsx
import Pagination from '@/components/Pagination';

<Pagination
  currentPage={page}
  totalPages={totalPages}
  onPageChange={setPage}
/>
```

#### StatusBadge
Badge showing user status with colors.

```tsx
import StatusBadge from '@/components/StatusBadge';

<StatusBadge status={user.status} />
```

#### Loading
Loading spinner component.

```tsx
import Loading from '@/components/Loading';

{isLoading && <Loading />}
```

---

## 📄 Pages

### Home Page (`/`)
Landing page with overview and quick actions.

### Users List (`/users`)
- Displays all users in a paginated table/grid
- Search and filter functionality
- Quick actions (view, edit, delete)

### Create User (`/users/create`)
- Form to create a new user
- Client-side validation
- Password strength indicator

### User Detail (`/users/[id]`)
- Detailed view of a single user
- All user information
- Action buttons (edit, delete, activate, etc.)

### Edit User (`/users/edit/[id]`)
- Form to edit existing user
- Pre-filled with current data
- Validation and error handling

---

## 🛠️ Utilities

### Type Definitions (`src/lib/types.ts`)

All TypeScript types matching the backend:

```typescript
export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  // ... other fields
}

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
```

### Helper Functions (`src/lib/utils.ts`)

#### Date Formatting
```typescript
import { formatDate, formatDateTime, formatRelativeTime } from '@/lib/utils';

formatDate('2024-01-15T10:30:00Z'); // 'Jan 15, 2024'
formatDateTime('2024-01-15T10:30:00Z'); // 'Jan 15, 2024 at 10:30 AM'
formatRelativeTime('2024-01-15T10:30:00Z'); // '2 hours ago'
```

#### Color Utilities
```typescript
import { getStatusColor, getRoleColor } from '@/lib/utils';

const colors = getStatusColor('ACTIVE');
// { bg: 'bg-green-100', text: 'text-green-800', border: 'border-green-200' }
```

#### Validation
```typescript
import { isValidEmail, isValidUsername, validatePassword } from '@/lib/utils';

if (!isValidEmail(email)) {
  // Show error
}

const passwordCheck = validatePassword(password);
if (!passwordCheck.valid) {
  console.log(passwordCheck.message);
}
```

---

## 🎨 Styling

### Tailwind CSS

This project uses Tailwind CSS for styling. Common patterns:

#### Buttons
```tsx
<button className="btn btn-primary">
  Primary Button
</button>

<button className="btn btn-secondary">
  Secondary Button
</button>

<button className="btn btn-danger">
  Danger Button
</button>
```

#### Cards
```tsx
<div className="card">
  <h3>Card Title</h3>
  <p>Card content...</p>
</div>

<div className="card-hover">
  <!-- Adds hover effect -->
</div>
```

#### Badges
```tsx
<span className="badge badge-success">Active</span>
<span className="badge badge-warning">Pending</span>
<span className="badge badge-danger">Blocked</span>
```

#### Forms
```tsx
<label className="label">Email</label>
<input className="input" type="email" />

<input className="input input-error" /> <!-- Error state -->
<span className="form-error">Error message</span>
```

### Custom Styles

Global styles are in `src/app/globals.css`. Add custom styles there:

```css
@layer components {
  .custom-class {
    @apply bg-blue-500 text-white p-4 rounded;
  }
}
```

---

## 💻 Development

### Available Scripts

```bash
# Start development server
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Run linting
npm run lint

# Type check
npm run type-check
```

### Development Server

```bash
npm run dev
```

- Runs on http://localhost:3000
- Hot reload enabled
- Fast refresh for React components
- Automatic TypeScript compilation

### Code Quality

#### ESLint
```bash
npm run lint
```

Fix linting issues automatically:
```bash
npm run lint -- --fix
```

#### Type Checking
```bash
npm run type-check
```

### Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)
- Mobile browsers (iOS Safari, Chrome Mobile)

---

## 🏗️ Building for Production

### Build the Application

```bash
npm run build
```

This creates an optimized production build in `.next/`.

### Test the Production Build Locally

```bash
npm run build
npm start
```

The production server runs on http://localhost:3000

### Build Output

The build process:
1. ✅ Type checks all TypeScript files
2. ✅ Lints the code
3. ✅ Optimizes images
4. ✅ Minifies JavaScript and CSS
5. ✅ Generates static assets
6. ✅ Creates optimized bundles

---

## 🚀 Deployment

### Vercel (Recommended)

1. **Push to GitHub**
   ```bash
   git push origin main
   ```

2. **Import to Vercel**
   - Go to [vercel.com](https://vercel.com)
   - Import your repository
   - Configure environment variables
   - Deploy!

3. **Environment Variables**
   Add in Vercel dashboard:
   ```
   NEXT_PUBLIC_API_URL=https://your-api-domain.com/api/v1
   ```

### Docker

Build Docker image:

```dockerfile
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM node:18-alpine AS runner
WORKDIR /app
COPY --from=builder /app/.next ./.next
COPY --from=builder /app/node_modules ./node_modules
COPY --from=builder /app/package.json ./package.json
EXPOSE 3000
CMD ["npm", "start"]
```

Build and run:
```bash
docker build -t user-management-frontend .
docker run -p 3000:3000 -e NEXT_PUBLIC_API_URL=http://api:8080/api/v1 user-management-frontend
```

### Static Export (Optional)

For static hosting:

```bash
npm run build
npm run export
```

Upload the `out/` directory to any static host (Netlify, AWS S3, etc.)

---

## 🐛 Troubleshooting

### Common Issues

#### 1. API Connection Failed

**Error:** `Network Error` or `ERR_CONNECTION_REFUSED`

**Solution:**
- Ensure backend is running on http://localhost:8080
- Check `.env.local` has correct `NEXT_PUBLIC_API_URL`
- Verify CORS is configured in backend

#### 2. CORS Errors

**Error:** `Access-Control-Allow-Origin` error

**Solution:**
Add CORS configuration in Spring Boot backend:

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

#### 3. Environment Variables Not Working

**Solution:**
- Restart the dev server after changing `.env.local`
- Ensure variables are prefixed with `NEXT_PUBLIC_`
- Clear `.next` cache: `rm -rf .next`

#### 4. Module Not Found Errors

**Solution:**
```bash
rm -rf node_modules package-lock.json
npm install
```

#### 5. Port 3000 Already in Use

**Solution:**
```bash
# Kill process on port 3000
lsof -ti:3000 | xargs kill -9

# Or use a different port
PORT=3001 npm run dev
```

### Debug Mode

Enable debug mode in `.env.local`:

```env
NEXT_PUBLIC_DEBUG_MODE=true
```

This shows detailed error messages in the console.

### Clear Cache

```bash
# Clear Next.js cache
rm -rf .next

# Clear node modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

---

## 📚 Best Practices

### Code Organization
- ✅ Keep components small and focused
- ✅ Use TypeScript for type safety
- ✅ Follow naming conventions (PascalCase for components, camelCase for functions)
- ✅ Extract reusable logic into custom hooks
- ✅ Use proper file structure

### State Management
- ✅ Use React hooks for local state
- ✅ Lift state up when needed
- ✅ Consider Context API for global state
- ✅ Use SWR or React Query for server state (future enhancement)

### Performance
- ✅ Use Next.js Image component for images
- ✅ Implement proper loading states
- ✅ Use React.memo for expensive components
- ✅ Lazy load components when appropriate
- ✅ Optimize bundle size

### Security
- ✅ Never expose sensitive data in client-side code
- ✅ Validate all user inputs
- ✅ Use HTTPS in production
- ✅ Implement proper authentication
- ✅ Sanitize user-generated content

### Accessibility
- ✅ Use semantic HTML
- ✅ Add proper ARIA labels
- ✅ Ensure keyboard navigation works
- ✅ Test with screen readers
- ✅ Maintain sufficient color contrast

---

## 🤝 Contributing

### Development Workflow

1. **Create a branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make changes**
   - Write code
   - Add tests if applicable
   - Update documentation

3. **Commit changes**
   ```bash
   git add .
   git commit -m "feat: add new feature"
   ```

4. **Push and create PR**
   ```bash
   git push origin feature/your-feature-name
   ```

### Commit Message Format

Follow conventional commits:

```
feat: add user search functionality
fix: resolve pagination bug
docs: update API documentation
style: format code with prettier
refactor: restructure user components
test: add user form tests
chore: update dependencies
```

---

## 📖 Additional Resources

### Documentation
- [Next.js Documentation](https://nextjs.org/docs)
- [React Documentation](https://react.dev)
- [TypeScript Documentation](https://www.typescriptlang.org/docs)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)

### Backend Integration
- Backend API Guide: `../newZedCode/API_GUIDE.md`
- Backend Architecture: `../newZedCode/ARCHITECTURE.md`
- Backend Setup: `../newZedCode/README.md`

### Learning Resources
- [Next.js Learn](https://nextjs.org/learn)
- [React Tutorial](https://react.dev/learn)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/handbook)
- [Tailwind UI Components](https://tailwindui.com)

---

## 📝 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

**ZedCode**

For questions or support, please refer to the documentation or create an issue in the repository.

---

## 🎉 Acknowledgments

- Next.js team for the amazing framework
- Vercel for hosting platform
- Tailwind CSS for the utility-first CSS framework
- All open-source contributors

---

**Happy Coding! 🚀**