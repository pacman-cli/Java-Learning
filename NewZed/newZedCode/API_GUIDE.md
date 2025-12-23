# API Guide - ZedCode Backend

Complete guide to using the ZedCode Backend REST API with practical examples.

## 📋 Table of Contents

- [Getting Started](#getting-started)
- [Authentication](#authentication)
- [Response Format](#response-format)
- [Error Handling](#error-handling)
- [User Management API](#user-management-api)
- [Pagination & Sorting](#pagination--sorting)
- [Search & Filtering](#search--filtering)
- [Status Codes](#status-codes)
- [Rate Limiting](#rate-limiting)
- [Best Practices](#best-practices)

---

## 🚀 Getting Started

### Base URL

```
Local Development: http://localhost:8080/api
Production: https://api.yourdomain.com/api
```

### API Versioning

All endpoints are versioned:
```
/api/v1/users
/api/v1/products
```

### Content Type

All requests and responses use JSON:
```
Content-Type: application/json
```

---

## 🔐 Authentication

### JWT Token Authentication

Most endpoints require authentication using JWT tokens.

#### Login (To be implemented)
```bash
POST /api/v1/auth/login

Request:
{
  "email": "user@example.com",
  "password": "SecurePass123!"
}

Response:
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

#### Using the Token

Include the token in the Authorization header:

```bash
curl -H "Authorization: Bearer YOUR_TOKEN_HERE" \
     http://localhost:8080/api/v1/users
```

---

## 📦 Response Format

### Success Response

All successful responses follow this structure:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

### Error Response

All error responses follow this structure:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 123",
  "path": "/api/v1/users/123"
}
```

### Validation Error Response

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/users",
  "validationErrors": {
    "email": "Email should be valid",
    "password": "Password must be at least 8 characters"
  }
}
```

---

## 👤 User Management API

### 1. Create User

**Endpoint:** `POST /api/v1/users`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "password": "SecurePass123!",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "bio": "Software developer and tech enthusiast"
  }'
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "User created successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "status": "PENDING",
    "emailVerified": false,
    "enabled": true,
    "accountNonLocked": true,
    "bio": "Software developer and tech enthusiast",
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**Validation Rules:**
- `firstName`: Required, 2-50 characters
- `lastName`: Required, 2-50 characters
- `username`: Required, 3-20 characters, alphanumeric
- `email`: Required, valid email format
- `password`: Required, minimum 8 characters, must contain uppercase, lowercase, number, and special character
- `phoneNumber`: Optional, 10-15 digits
- `role`: Optional, defaults to USER
- `bio`: Optional, max 500 characters

---

### 2. Get All Users

**Endpoint:** `GET /api/v1/users`

**Permission:** `ROLE_ADMIN`

**Parameters:**
- `page` (optional): Page number, default 0
- `size` (optional): Page size, default 10
- `sort` (optional): Sort field and direction, default "id,desc"

**Request:**
```bash
curl "http://localhost:8080/api/v1/users?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [
      {
        "id": 1,
        "firstName": "John",
        "lastName": "Doe",
        "fullName": "John Doe",
        "username": "johndoe",
        "email": "john.doe@example.com",
        "role": "USER",
        "status": "ACTIVE"
      }
    ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "hasNext": false,
    "hasPrevious": false,
    "numberOfElements": 1,
    "empty": false
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 3. Get User by ID

**Endpoint:** `GET /api/v1/users/{id}`

**Permission:** Authenticated user

**Request:**
```bash
curl http://localhost:8080/api/v1/users/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "phoneNumber": "+1234567890",
    "role": "USER",
    "status": "ACTIVE",
    "emailVerified": true,
    "enabled": true,
    "accountNonLocked": true,
    "lastLoginAt": "2024-01-15T09:00:00Z",
    "bio": "Software developer",
    "createdAt": "2024-01-10T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 4. Get User by Email

**Endpoint:** `GET /api/v1/users/email/{email}`

**Request:**
```bash
curl http://localhost:8080/api/v1/users/email/john.doe@example.com \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK` (same format as Get User by ID)

---

### 5. Get User by Username

**Endpoint:** `GET /api/v1/users/username/{username}`

**Request:**
```bash
curl http://localhost:8080/api/v1/users/username/johndoe \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK` (same format as Get User by ID)

---

### 6. Update User

**Endpoint:** `PUT /api/v1/users/{id}`

**Permission:** Owner or Admin

**Request:**
```bash
curl -X PUT http://localhost:8080/api/v1/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "firstName": "John Updated",
    "lastName": "Doe Updated",
    "phoneNumber": "+9876543210",
    "bio": "Updated bio information"
  }'
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "User updated successfully",
  "data": {
    "id": 1,
    "firstName": "John Updated",
    "lastName": "Doe Updated",
    "fullName": "John Updated Doe Updated",
    "username": "johndoe",
    "email": "john.doe@example.com",
    "phoneNumber": "+9876543210",
    "bio": "Updated bio information",
    "updatedAt": "2024-01-15T11:00:00Z"
  },
  "timestamp": "2024-01-15T11:00:00Z"
}
```

**Notes:**
- Only non-null fields in the request will be updated
- Username cannot be changed
- Password must be updated through a separate endpoint
- Email change will reset email verification status

---

### 7. Delete User (Soft Delete)

**Endpoint:** `DELETE /api/v1/users/{id}`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/v1/users/1 \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "User deleted successfully",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 8. Permanently Delete User

**Endpoint:** `DELETE /api/v1/users/{id}/permanent`

**Permission:** `ROLE_SUPER_ADMIN`

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/v1/users/1/permanent \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "User permanently deleted",
  "timestamp": "2024-01-15T10:30:00Z"
}
```

**⚠️ Warning:** This action cannot be undone!

---

### 9. Search Users

**Endpoint:** `GET /api/v1/users/search`

**Permission:** `ROLE_ADMIN`

**Parameters:**
- `searchTerm` (required): Search term for name, email, or username
- `page` (optional): Page number
- `size` (optional): Page size
- `sort` (optional): Sort criteria

**Request:**
```bash
curl "http://localhost:8080/api/v1/users/search?searchTerm=john&page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK` (paginated response)

---

### 10. Get Users by Status

**Endpoint:** `GET /api/v1/users/status/{status}`

**Permission:** `ROLE_ADMIN`

**Valid Statuses:** `ACTIVE`, `INACTIVE`, `PENDING`, `BLOCKED`, `SUSPENDED`, `DELETED`

**Request:**
```bash
curl "http://localhost:8080/api/v1/users/status/ACTIVE?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK` (paginated response)

---

### 11. Get Users by Role

**Endpoint:** `GET /api/v1/users/role/{role}`

**Permission:** `ROLE_ADMIN`

**Valid Roles:** `USER`, `ADMIN`, `MODERATOR`, `SUPER_ADMIN`

**Request:**
```bash
curl "http://localhost:8080/api/v1/users/role/ADMIN?page=0&size=10" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK` (paginated response)

---

### 12. Activate User

**Endpoint:** `PATCH /api/v1/users/{id}/activate`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/activate \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "User activated successfully",
  "data": {
    "id": 1,
    "status": "ACTIVE",
    "enabled": true
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 13. Deactivate User

**Endpoint:** `PATCH /api/v1/users/{id}/deactivate`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/deactivate \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`

---

### 14. Block User

**Endpoint:** `PATCH /api/v1/users/{id}/block`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/block \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "User blocked successfully",
  "data": {
    "id": 1,
    "status": "BLOCKED",
    "enabled": false,
    "accountNonLocked": false
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 15. Unblock User

**Endpoint:** `PATCH /api/v1/users/{id}/unblock`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/unblock \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`

---

### 16. Verify User Email

**Endpoint:** `PATCH /api/v1/users/{id}/verify-email`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl -X PATCH http://localhost:8080/api/v1/users/1/verify-email \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Email verified successfully",
  "data": {
    "id": 1,
    "emailVerified": true,
    "status": "ACTIVE"
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 17. Check Email Exists

**Endpoint:** `GET /api/v1/users/exists/email`

**Parameters:**
- `email` (required): Email to check

**Request:**
```bash
curl "http://localhost:8080/api/v1/users/exists/email?email=john@example.com"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Operation successful",
  "data": true,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 18. Check Username Exists

**Endpoint:** `GET /api/v1/users/exists/username`

**Parameters:**
- `username` (required): Username to check

**Request:**
```bash
curl "http://localhost:8080/api/v1/users/exists/username?username=johndoe"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Operation successful",
  "data": false,
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

### 19. Get User Statistics

**Endpoint:** `GET /api/v1/users/stats`

**Permission:** `ROLE_ADMIN`

**Request:**
```bash
curl http://localhost:8080/api/v1/users/stats \
  -H "Authorization: Bearer YOUR_TOKEN"
```

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "totalUsers": 1250,
    "activeUsers": 980,
    "inactiveUsers": 150,
    "blockedUsers": 20,
    "pendingUsers": 100,
    "adminCount": 5,
    "userCount": 1200,
    "moderatorCount": 45
  },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

---

## 📄 Pagination & Sorting

### Pagination Parameters

- `page`: Page number (0-indexed), default: 0
- `size`: Number of items per page, default: 10, max: 100
- `sort`: Sort field and direction

### Sort Format

```
sort=field,direction

Examples:
sort=createdAt,desc      # Sort by creation date, newest first
sort=firstName,asc       # Sort by first name, A-Z
sort=email,asc          # Sort by email
```

### Example Request

```bash
# Get page 2 (third page), 20 items per page, sorted by creation date descending
curl "http://localhost:8080/api/v1/users?page=2&size=20&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Pagination Response Fields

- `pageNumber`: Current page number (0-indexed)
- `pageSize`: Number of items per page
- `totalElements`: Total number of items across all pages
- `totalPages`: Total number of pages
- `first`: Is this the first page?
- `last`: Is this the last page?
- `hasNext`: Is there a next page?
- `hasPrevious`: Is there a previous page?
- `numberOfElements`: Number of items in current page
- `empty`: Is the page empty?

---

## 🔍 Search & Filtering

### Search Users

Search across multiple fields: firstName, lastName, email, username

```bash
curl "http://localhost:8080/api/v1/users/search?searchTerm=john" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Filter by Status

```bash
curl "http://localhost:8080/api/v1/users/status/ACTIVE" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Filter by Role

```bash
curl "http://localhost:8080/api/v1/users/role/ADMIN" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Combine Filters with Pagination

```bash
curl "http://localhost:8080/api/v1/users/status/ACTIVE?page=0&size=20&sort=createdAt,desc" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## 📊 Status Codes

| Code | Meaning | Description |
|------|---------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 204 | No Content | Request successful, no content to return |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Authentication required |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists |
| 422 | Unprocessable Entity | Validation failed |
| 429 | Too Many Requests | Rate limit exceeded |
| 500 | Internal Server Error | Server error |

---

## ⚡ Rate Limiting

(To be implemented)

Rate limits apply per API key:
- **Default**: 100 requests per minute
- **Authenticated**: 1000 requests per minute
- **Admin**: 5000 requests per minute

Response headers:
```
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1610708400
```

---

## ✅ Best Practices

### 1. Always Use HTTPS in Production

```bash
# ❌ Bad
http://api.example.com/users

# ✅ Good
https://api.example.com/users
```

### 2. Handle Errors Gracefully

```javascript
try {
  const response = await fetch('/api/v1/users/1');
  const data = await response.json();
  
  if (!response.ok) {
    console.error('Error:', data.message);
    // Handle error based on status code
  }
} catch (error) {
  console.error('Network error:', error);
}
```

### 3. Use Pagination for Large Datasets

```bash
# ❌ Bad - might return too much data
curl http://localhost:8080/api/v1/users

# ✅ Good - paginated request
curl "http://localhost:8080/api/v1/users?page=0&size=20"
```

### 4. Validate Input on Client Side

```javascript
// Validate before sending request
if (!email.includes('@')) {
  return 'Invalid email';
}

if (password.length < 8) {
  return 'Password too short';
}
```

### 5. Cache When Appropriate

```javascript
// Cache user data for 5 minutes
const cacheKey = `user_${userId}`;
const cached = cache.get(cacheKey);

if (cached) {
  return cached;
}

const user = await fetchUser(userId);
cache.set(cacheKey, user, 300); // 5 minutes
return user;
```

### 6. Use Appropriate HTTP Methods

- `GET`: Retrieve data (idempotent)
- `POST`: Create new resource
- `PUT`: Update entire resource
- `PATCH`: Partial update
- `DELETE`: Remove resource

### 7. Include Request IDs for Debugging

```bash
curl -H "X-Request-ID: unique-request-id-123" \
     http://localhost:8080/api/v1/users/1
```

---

## 🧪 Testing with Different Tools

### cURL

```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe",...}'
```

### HTTPie

```bash
http POST localhost:8080/api/v1/users \
  firstName=John lastName=Doe email=john@example.com
```

### JavaScript (Fetch)

```javascript
const response = await fetch('http://localhost:8080/api/v1/users/1', {
  method: 'GET',
  headers: {
    'Authorization': 'Bearer YOUR_TOKEN',
    'Content-Type': 'application/json'
  }
});
const data = await response.json();
```

### Python (Requests)

```python
import requests

headers = {
    'Authorization': 'Bearer YOUR_TOKEN',
    'Content-Type': 'application/json'
}

response = requests.get(
    'http://localhost:8080/api/v1/users/1',
    headers=headers
)
user = response.json()
```

---

## 📚 Additional Resources

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI Spec**: http://localhost:8080/api/api-docs
- **Health Check**: http://localhost:8080/api/actuator/health
- **Documentation**: See README.md and ARCHITECTURE.md

---

**Last Updated**: January 2024  
**API Version**: v1  
**Base URL**: http://localhost:8080/api