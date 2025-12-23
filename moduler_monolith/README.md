# Modular Monolith Application

A Spring Boot application demonstrating a modular monolith architecture with JWT authentication and product management.

## Fixed Issues

### 1. **AuthenticationService**

- ✅ Added missing `AuthenticationManager` bean in SecurityConfig
- ✅ Completed `login()` method with proper return statement
- ✅ Fixed `UserRepository.findByEmail()` to return `Optional<User>`
- ✅ Added password encoding using BCryptPasswordEncoder
- ✅ User entity now directly implements UserDetails (no need for wrapper)

### 2. **ProductRepository**

- ✅ Fixed to properly extend `JpaRepository<Product, Long>`
- ✅ Removed incorrect generic type parameters

### 3. **UserRepository**

- ✅ Changed ID type from `Long` to `Integer` to match User entity
- ✅ Changed `findByEmail()` return type to `Optional<User>`

### 4. **SecurityConfig**

- ✅ Added `AuthenticationManager` bean
- ✅ Added `PasswordEncoder` bean (BCrypt)
- ✅ Added `UserDetailsService` bean
- ✅ Added `AuthenticationProvider` bean (DaoAuthenticationProvider)
- ✅ Configured authentication provider in security filter chain

### 5. **ProductService**

- ✅ Removed unnecessary cast in save operation

### 6. **Application**

- ✅ Added `@EnableJpaAuditing` for BaseEntity timestamp auditing

### 7. **Dependencies**

- ✅ Added missing JWT implementation dependencies (jjwt-impl, jjwt-jackson)

### 8. **Validation**

- ✅ Fixed `ProductRequest` - changed `@NotBlank` to `@NotNull` for BigDecimal price field

### 9. **Configuration**

- ✅ Added database configuration (PostgreSQL)
- ✅ Added JPA/Hibernate configuration
- ✅ Added JWT configuration
- ✅ Added Redis configuration

### 10. **Controllers**

- ✅ Created `AuthenticationController` for `/api/v1/auth` endpoints
- ✅ Created `ProductController` for `/api/v1/products` endpoints

### 11. **Exception Handling**

- ✅ Implemented proper `GlobalExceptionHandler` with handlers for:
    - Validation exceptions
    - Username not found exceptions
    - Bad credentials exceptions
    - Runtime exceptions
    - Generic exceptions

## Architecture

```
moduler_monolith/
├── config/              # Configuration classes
│   ├── AsyncConfig      # Async processing configuration
│   └── SecurityConfig   # Security & authentication configuration
├── core/                # Core/shared components
│   ├── exceptions/      # Global exception handlers
│   ├── models/          # Base entities
│   └── utils/           # Utility classes
├── modules/             # Feature modules
│   ├── auth/            # Authentication module
│   │   ├── controller/
│   │   ├── dto/
│   │   └── service/
│   └── product/         # Product module
│       ├── controller/
│       ├── dto/
│       ├── entity/
│       ├── mapper/
│       ├── repository/
│       └── service/
└── security/            # Security components (JWT)
```

## API Endpoints

### Authentication

- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login and get JWT token

### Products (Requires Authentication)

- `POST /api/v1/products` - Create a new product

## Technology Stack

- **Spring Boot 4.0.0**
- **Java 17**
- **PostgreSQL** - Database
- **Spring Security** - Authentication & Authorization
- **JWT (JJWT 0.11.5)** - Token-based authentication
- **Spring Data JPA** - Data persistence
- **Lombok** - Reduce boilerplate code
- **MapStruct** - Object mapping
- **Redis** - Caching (optional)
- **Bean Validation** - Input validation

## Setup Instructions

### Prerequisites

- Java 17 or higher
- PostgreSQL database
- Redis (optional)
- Maven

### Database Setup

1. Create a PostgreSQL database named `moduler_monolith`
2. Update credentials in `application.properties` if needed:
   ```properties
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

### Running the Application

```bash
# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## Testing the API

### Register a new user

```bash
curl -X POST http://localhost:8080/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

### Create a product (with JWT token)

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "name": "Product Name",
    "description": "Product Description",
    "price": 99.99,
    "stockQuantity": 100
  }'
```

## Security Features

- JWT-based stateless authentication
- BCrypt password hashing
- Role-based access control (USER, ADMIN)
- Session management: STATELESS
- CSRF protection disabled (suitable for API)

## Future Enhancements

- Add CRUD operations for products
- Implement refresh token mechanism
- Add email verification
- Add caching with Redis
- Add API documentation (Swagger/OpenAPI)
- Add comprehensive unit and integration tests
- Add Docker support

