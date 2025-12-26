# Authentication Service API Endpoints

## Base URL
```
http://localhost:8081
```

## Authentication Endpoints

### 1. Register User
- **URL**: `POST /api/auth/register`
- **Description**: Register a new user
- **Request Body**:
```json
{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe"
}
```
- **Response**:
```json
{
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "type": "Bearer",
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "USER"
}
```

### 2. Login User
- **URL**: `POST /api/auth/login`
- **Description**: Authenticate user and return JWT token
- **Request Body**:
```json
{
    "usernameOrEmail": "johndoe",
    "password": "password123"
}
```
- **Response**: Same as register response

## User Profile Endpoints

### 3. Get Current User Profile
- **URL**: `GET /api/user/profile`
- **Description**: Get current authenticated user's profile
- **Headers**: `Authorization: Bearer <token>`
- **Response**:
```json
{
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "role": "USER",
    "isEnabled": true,
    "isAccountNonExpired": true,
    "isAccountNonLocked": true,
    "isCredentialsNonExpired": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00",
    "lastLogin": "2024-01-01T10:05:00"
}
```

### 4. Get User by ID
- **URL**: `GET /api/user/{id}`
- **Description**: Get user by ID (Admin/Moderator only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Same as profile response

### 5. Get User by Username
- **URL**: `GET /api/user/username/{username}`
- **Description**: Get user by username (Admin/Moderator only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Same as profile response

## User Management Endpoints (Admin/Moderator)

### 6. Get All Users (Paginated)
- **URL**: `GET /api/user/all`
- **Description**: Get all users with pagination (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Query Parameters**:
  - `page` (optional): Page number (default: 0)
  - `size` (optional): Page size (default: 10)
  - `sortBy` (optional): Sort field (default: createdAt)
  - `sortDir` (optional): Sort direction (default: desc)
- **Response**:
```json
{
    "users": [
        {
            "id": 1,
            "username": "johndoe",
            "email": "john@example.com",
            "firstName": "John",
            "lastName": "Doe",
            "role": "USER",
            "isEnabled": true,
            "createdAt": "2024-01-01T10:00:00",
            "updatedAt": "2024-01-01T10:00:00",
            "lastLogin": "2024-01-01T10:05:00"
        }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 25,
    "totalPages": 3,
    "first": true,
    "last": false,
    "hasNext": true,
    "hasPrevious": false
}
```

### 7. Get Users by Role
- **URL**: `GET /api/user/role/{role}`
- **Description**: Get users by role (Admin/Moderator only)
- **Headers**: `Authorization: Bearer <token>`
- **Path Parameters**: `role` (USER, ADMIN, MODERATOR)
- **Response**: Array of user objects

### 8. Get Active Users
- **URL**: `GET /api/user/active`
- **Description**: Get all active users (Admin/Moderator only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Array of user objects

### 9. Get Recent Users
- **URL**: `GET /api/user/recent`
- **Description**: Get recently registered users (Admin/Moderator only)
- **Headers**: `Authorization: Bearer <token>`
- **Query Parameters**: `limit` (optional, default: 10)
- **Response**: Array of user objects

### 10. Get User Statistics
- **URL**: `GET /api/user/stats`
- **Description**: Get user statistics (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**:
```json
{
    "totalUsers": 100,
    "adminCount": 2,
    "moderatorCount": 5,
    "userCount": 93
}
```

## User Administration Endpoints (Admin Only)

### 11. Update User Role
- **URL**: `PUT /api/user/{id}/role`
- **Description**: Update user role (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
```json
{
    "role": "MODERATOR"
}
```
- **Response**: Updated user object

### 12. Enable User
- **URL**: `PUT /api/user/{id}/enable`
- **Description**: Enable user account (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Updated user object

### 13. Disable User
- **URL**: `PUT /api/user/{id}/disable`
- **Description**: Disable user account (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Updated user object

### 14. Lock User
- **URL**: `PUT /api/user/{id}/lock`
- **Description**: Lock user account (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Updated user object

### 15. Unlock User
- **URL**: `PUT /api/user/{id}/unlock`
- **Description**: Unlock user account (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: Updated user object

### 16. Change User Password
- **URL**: `PUT /api/user/{id}/password`
- **Description**: Change user password (Admin or own account)
- **Headers**: `Authorization: Bearer <token>`
- **Request Body**:
```json
{
    "password": "newPassword123"
}
```
- **Response**: Updated user object

### 17. Delete User
- **URL**: `DELETE /api/user/{id}`
- **Description**: Delete user account (Admin only)
- **Headers**: `Authorization: Bearer <token>`
- **Response**: 204 No Content

## Health Check

### 18. Health Check
- **URL**: `GET /api/health`
- **Description**: Check service health
- **Response**:
```json
{
    "status": "UP",
    "service": "auth-service",
    "timestamp": "2024-01-01T10:00:00",
    "version": "1.0.0"
}
```

## Seed Users

The following users are available for testing (password: `password123`):

| Username | Email | Role | Description |
|----------|--------|------|-------------|
| admin | admin@codearena.com | ADMIN | System administrator |
| moderator | moderator@codearena.com | MODERATOR | Moderator user |
| testuser | testuser@codearena.com | USER | Test user |
| alice.smith | alice.smith@example.com | USER | Sample user |
| bob.johnson | bob.johnson@example.com | USER | Sample user |
| charlie.brown | charlie.brown@example.com | USER | Sample user |
| diana.prince | diana.prince@example.com | MODERATOR | Sample moderator |
| eve.adams | eve.adams@example.com | USER | Sample user |

## Authorization Levels

### Public Endpoints
- POST `/api/auth/register`
- POST `/api/auth/login`
- GET `/api/health`

### User Level (Authenticated)
- GET `/api/user/profile`
- PUT `/api/user/{own-id}/password`

### Moderator Level
- GET `/api/user/{id}`
- GET `/api/user/username/{username}`
- GET `/api/user/role/{role}`
- GET `/api/user/active`
- GET `/api/user/recent`

### Admin Level
- All moderator endpoints plus:
- GET `/api/user/all`
- GET `/api/user/stats`
- PUT `/api/user/{id}/role`
- PUT `/api/user/{id}/enable`
- PUT `/api/user/{id}/disable`
- PUT `/api/user/{id}/lock`
- PUT `/api/user/{id}/unlock`
- PUT `/api/user/{id}/password`
- DELETE `/api/user/{id}`

## Error Responses

All endpoints return standardized error responses:

```json
{
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "details": {
        "username": "Username is required",
        "email": "Email should be valid"
    },
    "timestamp": "2024-01-01T10:00:00"
}
```

### Common Error Codes
- **400 Bad Request**: Invalid request format or validation errors
- **401 Unauthorized**: Invalid or missing JWT token
- **403 Forbidden**: Insufficient permissions for the requested operation
- **404 Not Found**: Requested resource not found
- **409 Conflict**: Resource already exists (duplicate username/email)
- **500 Internal Server Error**: Server-side error

## Testing with curl

### Register a user:
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "firstName": "Test",
    "lastName": "User"
  }'
```

### Login with admin user:
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "usernameOrEmail": "admin",
    "password": "password123"
  }'
```

### Get profile (replace TOKEN with actual token):
```bash
curl -X GET http://localhost:8081/api/user/profile \
  -H "Authorization: Bearer TOKEN"
```

### Get all users (Admin only):
```bash
curl -X GET "http://localhost:8081/api/user/all?page=0&size=10&sortBy=createdAt&sortDir=desc" \
  -H "Authorization: Bearer TOKEN"
```

### Update user role (Admin only):
```bash
curl -X PUT http://localhost:8081/api/user/2/role \
  -H "Authorization: Bearer TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"role": "MODERATOR"}'
```

### Disable user (Admin only):
```bash
curl -X PUT http://localhost:8081/api/user/2/disable \
  -H "Authorization: Bearer TOKEN"
```

### Get user statistics (Admin only):
```bash
curl -X GET http://localhost:8081/api/user/stats \
  -H "Authorization: Bearer TOKEN"
```

