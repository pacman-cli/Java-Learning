# Auth Service Frontend

A modern React TypeScript frontend application for the Auth Service backend, featuring user authentication, role-based access control, and comprehensive user management.

## Features

### 🔐 Authentication & Authorization
- User login and registration
- JWT token-based authentication
- Role-based access control (User, Moderator, Admin)
- Persistent login sessions
- Secure logout functionality

### 👥 User Management
- User profile management
- Password change functionality
- User listing with pagination and search
- Role management (Admin only)
- User status control (enable/disable/lock/unlock)
- User statistics and analytics

### 🎨 User Interface
- Modern, responsive design with Tailwind CSS
- Mobile-friendly interface
- Dark/light theme support
- Real-time notifications
- Loading states and error handling
- Accessible components

### 🛡️ Security Features
- Protected routes based on user roles
- Automatic token refresh handling
- Secure API communication
- Input validation and sanitization
- CSRF protection

## Tech Stack

- **React 18** - Frontend framework
- **TypeScript** - Type safety
- **Tailwind CSS** - Styling
- **React Router** - Navigation
- **React Hook Form** - Form management
- **React Query** - Data fetching and caching
- **Axios** - HTTP client
- **React Toastify** - Notifications
- **Heroicons** - Icon library

## Prerequisites

- Node.js 16.x or higher
- npm or yarn package manager
- Auth Service backend running on http://localhost:8081

## Installation

1. **Clone the repository** (if not already done):
   ```bash
   git clone <repository-url>
   cd codeArene/auth-frontend
   ```

2. **Install dependencies**:
   ```bash
   npm install
   # or
   yarn install
   ```

3. **Configure environment variables**:
   ```bash
   cp .env.example .env
   ```
   
   Edit `.env` file:
   ```env
   REACT_APP_API_URL=http://localhost:8081/api
   REACT_APP_ENV=development
   REACT_APP_DEBUG=true
   ```

## Development

### Start the development server:
```bash
npm start
# or
yarn start
```

The application will open at [http://localhost:3000](http://localhost:3000).

### Available Scripts

- `npm start` - Start development server
- `npm build` - Build for production
- `npm test` - Run test suite
- `npm run eject` - Eject from Create React App (not recommended)

## Project Structure

```
src/
├── components/          # Reusable UI components
│   ├── Layout.tsx      # Main layout with navigation
│   ├── LoadingSpinner.tsx
│   └── ProtectedRoute.tsx
├── context/            # React context providers
│   └── AuthContext.tsx # Authentication state management
├── hooks/              # Custom React hooks
├── pages/              # Page components
│   ├── Login.tsx
│   ├── Register.tsx
│   ├── Dashboard.tsx
│   ├── Profile.tsx
│   ├── Users.tsx
│   ├── Admin.tsx
│   ├── Settings.tsx
│   ├── Unauthorized.tsx
│   └── NotFound.tsx
├── services/           # API services
│   └── api.ts         # HTTP client and API calls
├── types/              # TypeScript type definitions
│   └── auth.ts        # Authentication types
├── utils/              # Utility functions
│   └── formatters.ts  # Date, text, and data formatters
├── App.tsx            # Main application component
├── index.tsx          # Application entry point
└── index.css          # Global styles and Tailwind imports
```

## User Roles & Permissions

### 🟢 User (Default Role)
- View own profile
- Update own password
- Access dashboard
- Manage personal settings

### 🟡 Moderator
- All User permissions
- View all users
- Access user management page
- View user statistics

### 🔴 Admin
- All Moderator permissions
- Manage user roles
- Enable/disable users
- Lock/unlock user accounts
- Delete users
- Access admin panel
- View system statistics

## Demo Accounts

For testing purposes, the following accounts are available:

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| admin | password123 | ADMIN | System administrator |
| moderator | password123 | MODERATOR | Content moderator |
| testuser | password123 | USER | Regular user |

## API Integration

The frontend communicates with the Auth Service backend via REST API:

- **Base URL**: `http://localhost:8081/api`
- **Authentication**: Bearer JWT tokens
- **Error Handling**: Automatic token refresh and error notifications
- **Request Interceptors**: Automatic token attachment
- **Response Interceptors**: Error handling and redirects

### Key API Endpoints Used:
- `POST /auth/login` - User authentication
- `POST /auth/register` - User registration
- `GET /user/profile` - Get current user
- `GET /user/all` - List all users (paginated)
- `PUT /user/{id}/role` - Update user role
- `GET /user/stats` - User statistics

## Styling & Theming

The application uses Tailwind CSS with a custom configuration:

### Color Scheme
- **Primary**: Blue (#3B82F6)
- **Gray Scale**: Custom gray palette
- **Status Colors**: Green (success), Red (error), Yellow (warning)

### Responsive Design
- Mobile-first approach
- Breakpoints: sm (640px), md (768px), lg (1024px), xl (1280px)
- Responsive navigation and layouts

### Components
- Custom form components with validation styles
- Consistent button variants (primary, secondary, danger)
- Badge components for roles and status
- Card layouts for content sections

## State Management

### Authentication State
- Managed by `AuthContext` using React Context API
- Persistent storage in localStorage
- Automatic token validation and refresh

### Form State
- React Hook Form for form validation and submission
- Real-time validation feedback
- Error handling and display

### API State
- React Query for server state management
- Caching and background updates
- Loading and error states

## Error Handling

### Client-Side Errors
- Form validation errors with field-specific messages
- Network error handling with retry mechanisms
- User-friendly error messages via toast notifications

### Server-Side Errors
- HTTP status code handling (401, 403, 404, 500)
- Automatic redirect to login on authentication errors
- Detailed error messages from API responses

## Security Considerations

### Authentication
- JWT tokens stored securely in localStorage
- Automatic token cleanup on logout
- Protected routes with role-based access

### Input Validation
- Client-side form validation with React Hook Form
- TypeScript type safety
- Sanitized data display

### API Security
- HTTPS in production (configure reverse proxy)
- CORS configuration on backend
- Request/response interceptors for security headers

## Deployment

### Development Deployment
```bash
npm run build
serve -s build -l 3000
```

### Production Deployment

1. **Build the application**:
   ```bash
   npm run build
   ```

2. **Configure environment variables** for production:
   ```env
   REACT_APP_API_URL=https://api.yourdomain.com/api
   REACT_APP_ENV=production
   REACT_APP_DEBUG=false
   ```

3. **Deploy to static hosting**:
   - **Nginx**: Serve the `build` folder
   - **Apache**: Configure with proper rewrites for SPA
   - **Netlify/Vercel**: Direct deployment from repository
   - **AWS S3 + CloudFront**: Static site hosting

### Nginx Configuration Example
```nginx
server {
    listen 80;
    server_name yourdomain.com;
    root /path/to/build;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://backend:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## Environment Configuration

### Development (.env)
```env
REACT_APP_API_URL=http://localhost:8081/api
REACT_APP_ENV=development
REACT_APP_DEBUG=true
REACT_APP_API_TIMEOUT=10000
```

### Production (.env.production)
```env
REACT_APP_API_URL=https://api.yourdomain.com/api
REACT_APP_ENV=production
REACT_APP_DEBUG=false
REACT_APP_API_TIMEOUT=30000
```

## Browser Support

- **Modern Browsers**: Chrome 90+, Firefox 88+, Safari 14+, Edge 90+
- **Mobile**: iOS Safari 14+, Chrome Mobile 90+
- **Features**: ES6+, CSS Grid, Flexbox

## Performance Optimization

### Build Optimizations
- Code splitting with React.lazy()
- Tree shaking for unused code elimination
- Asset optimization and compression
- Service worker for caching (can be enabled)

### Runtime Optimizations
- React Query for efficient data fetching and caching
- Debounced search and filtering
- Virtualized lists for large datasets (future enhancement)
- Image lazy loading (if applicable)

## Accessibility

- ARIA labels and roles
- Keyboard navigation support
- Screen reader compatibility
- High contrast mode support
- Focus management

## Testing

### Unit Testing (Future Enhancement)
```bash
npm test
```

### End-to-End Testing (Future Enhancement)
```bash
npm run test:e2e
```

## Troubleshooting

### Common Issues

1. **API Connection Failed**
   - Check if backend is running on port 8081
   - Verify REACT_APP_API_URL in .env file
   - Check browser console for CORS errors

2. **Authentication Issues**
   - Clear browser localStorage
   - Check JWT token expiration
   - Verify backend JWT configuration

3. **Build Errors**
   - Update Node.js to version 16+
   - Clear node_modules and reinstall dependencies
   - Check TypeScript errors in terminal

4. **Styling Issues**
   - Ensure Tailwind CSS is properly configured
   - Check for conflicting CSS classes
   - Verify PostCSS configuration

### Debug Mode
Enable debug mode by setting `REACT_APP_DEBUG=true` in .env file for additional console logging.

## Contributing

1. Follow the existing code structure and naming conventions
2. Use TypeScript for all new components
3. Add proper error handling and loading states
4. Test with different user roles
5. Ensure responsive design on mobile devices
6. Add JSDoc comments for complex functions

## License

This project is part of the CodeArena Auth Service and follows the same license terms.

## Support

For technical support or questions:
- Check the troubleshooting section above
- Review the backend API documentation
- Contact the development team

---

**Note**: This frontend application requires the Auth Service backend to be running. Please refer to the backend documentation for setup instructions.