# JWT Authentication System for Hospital Management

## Overview
This document describes the comprehensive JWT (JSON Web Token) authentication system implemented for the Hospital Management System, supporting multiple user types with role-based access control.

## User Roles and Permissions

### Patient Roles
- **ROLE_PATIENT**: Basic patient access to their own records and appointments

### Medical Staff Roles
- **ROLE_DOCTOR**: Access to patient records, appointments, medical records
- **ROLE_NURSE**: Access to patient records, appointments, medical records
- **ROLE_PHARMACIST**: Access to pharmacy and prescription management
- **ROLE_LAB_TECHNICIAN**: Access to laboratory tests and results

### Administrative Roles
- **ROLE_ADMIN**: Full system access
- **ROLE_RECEPTIONIST**: Patient registration, appointment scheduling
- **ROLE_BILLING_STAFF**: Billing and payment management
- **ROLE_INSURANCE_STAFF**: Insurance claim processing

### Management Roles
- **ROLE_HOSPITAL_MANAGER**: Hospital-wide management access
- **ROLE_DEPARTMENT_HEAD**: Department-specific management
- **ROLE_SUPER_ADMIN**: System administration

## API Endpoints

### Authentication Endpoints
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration
- `GET /api/auth/me` - Get current user info

### Protected Endpoints by Role

#### Patient Endpoints
- `/api/patients/**` - Accessible by: PATIENT, DOCTOR, NURSE, ADMIN, RECEPTIONIST

#### Doctor Endpoints
- `/api/doctors/**` - Accessible by: DOCTOR, ADMIN, DEPARTMENT_HEAD

#### Appointment Endpoints
- `/api/appointments/**` - Accessible by: PATIENT, DOCTOR, NURSE, ADMIN, RECEPTIONIST

#### Pharmacy Endpoints
- `/api/pharmacy/**` - Accessible by: PHARMACIST, ADMIN

#### Laboratory Endpoints
- `/api/laboratory/**` - Accessible by: LAB_TECHNICIAN, DOCTOR, ADMIN

#### Billing Endpoints
- `/api/billing/**` - Accessible by: BILLING_STAFF, ADMIN

#### Insurance Endpoints
- `/api/insurance/**` - Accessible by: INSURANCE_STAFF, ADMIN

#### Medical Records
- `/api/medical-records/**` - Accessible by: DOCTOR, NURSE, ADMIN

#### Payment Endpoints
- `/api/payments/**` - Accessible by: BILLING_STAFF, ADMIN

#### Admin Endpoints
- `/api/admin/**` - Accessible by: ADMIN only

## Default Users

The system creates default users for testing:

1. **Admin User**
   - Username: `admin`
   - Password: `admin123`
   - Role: ROLE_ADMIN

2. **Doctor User**
   - Username: `doctor`
   - Password: `doctor123`
   - Role: ROLE_DOCTOR

3. **Patient User**
   - Username: `patient`
   - Password: `patient123`
   - Role: ROLE_PATIENT

## JWT Token Structure

### Header
```json
{
  "alg": "HS512",
  "typ": "JWT"
}
```

### Payload
```json
{
  "sub": "username",
  "iat": 1234567890,
  "exp": 1234654290,
  "authorities": ["ROLE_DOCTOR", "ROLE_USER"]
}
```

### Signature
HMACSHA512(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)

## Configuration

### JWT Settings (application.properties)
```properties
app.jwt.secret=mySecretKey123456789012345678901234567890
app.jwt.expiration=86400000
```

### Security Configuration
- CORS enabled for all origins
- CSRF disabled for stateless API
- Session management: STATELESS
- Password encoding: BCrypt

## Usage Examples

### Login Request
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "doctor",
    "password": "doctor123"
  }'
```

### Login Response
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 2,
  "username": "doctor",
  "fullName": "Dr. John Smith",
  "email": "doctor@hospital.com",
  "roles": ["ROLE_DOCTOR"],
  "expiresAt": "2024-10-11T01:38:39"
}
```

### Using Token in Requests
```bash
curl -X GET http://localhost:8080/api/patients \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

### Register New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "fullName": "New User",
    "email": "newuser@hospital.com",
    "roles": ["ROLE_PATIENT"]
  }'
```

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    email VARCHAR(100),
    enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Roles Table
```sql
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
```

### User Roles Junction Table
```sql
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);
```

## Security Features

1. **Password Encryption**: BCrypt hashing
2. **JWT Token Security**: HMAC-SHA512 signing
3. **Token Expiration**: Configurable (default 24 hours)
4. **Role-Based Access Control**: Fine-grained permissions
5. **CORS Support**: Cross-origin requests enabled
6. **Input Validation**: Request validation with Bean Validation
7. **Error Handling**: Comprehensive error responses

## Frontend Integration

### Setting Authorization Header
```javascript
// Add to all API requests
const token = localStorage.getItem('authToken');
const config = {
  headers: {
    'Authorization': `Bearer ${token}`
  }
};
```

### Handling Login Response
```javascript
const login = async (credentials) => {
  const response = await fetch('/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(credentials)
  });
  
  const data = await response.json();
  localStorage.setItem('authToken', data.token);
  return data;
};
```

## Error Handling

### Common Error Responses
- **401 Unauthorized**: Invalid or missing token
- **403 Forbidden**: Insufficient permissions
- **400 Bad Request**: Invalid request data
- **500 Internal Server Error**: Server-side errors

### Token Expiration
When a token expires, the client should:
1. Redirect to login page
2. Clear stored token
3. Show appropriate message

## Testing

### Test with Default Users
1. Use admin credentials for full access
2. Use doctor credentials for medical staff access
3. Use patient credentials for patient access

### Test Role-Based Access
1. Try accessing admin endpoints with patient role
2. Verify proper 403 Forbidden responses
3. Test different role combinations

## Maintenance

### Adding New Roles
1. Add role to `UserRole` enum
2. Update database migration
3. Update security configuration if needed

### Token Management
- Monitor token expiration
- Implement refresh token if needed
- Consider token blacklisting for logout

## Security Best Practices

1. **Use HTTPS in production**
2. **Rotate JWT secrets regularly**
3. **Implement rate limiting**
4. **Monitor failed login attempts**
5. **Use strong password policies**
6. **Regular security audits**
