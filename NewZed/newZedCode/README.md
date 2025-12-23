# ZedCode Backend - Scalable Spring Boot Application

A production-ready, scalable Spring Boot backend application with a modular architecture designed for enterprise-level applications.

## 📋 Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [🎓 Learning Resources (For Beginners)](#-learning-resources-for-beginners)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [Database](#database)
- [Security](#security)
- [Testing](#testing)
- [Contributing](#contributing)

## ✨ Features

- **Modular Architecture**: Organized by feature modules for scalability
- **RESTful API**: Well-structured REST endpoints
- **JWT Authentication**: Secure token-based authentication
- **Role-Based Access Control**: Fine-grained permissions
- **Input Validation**: Comprehensive request validation
- **Global Exception Handling**: Centralized error management
- **Pagination & Sorting**: Built-in support for paginated responses
- **API Documentation**: Interactive Swagger/OpenAPI documentation
- **Database Migration**: Flyway for version-controlled database changes
- **Logging**: Structured logging with SLF4J and Logback
- **Caching**: Redis support for improved performance
- **Health Checks**: Spring Actuator endpoints
- **Soft Delete**: Data retention with soft delete functionality

## 🎓 Learning Resources (For Beginners)

**New to Spring Boot or struggling with generic types (`<T>`)? Start here!**

This project includes comprehensive beginner-friendly guides:

### 📖 Essential Guides

1. **[GENERICS_GUIDE.md](GENERICS_GUIDE.md)** - Complete Guide to Generic Types
   - What are generics and why use them?
   - Understanding `<T>`, `<E>`, `<K, V>` notation
   - Real examples from this project explained step-by-step
   - Common patterns and best practices
   - **Start here if you see `<T>` and don't understand it!**

2. **[DATA_FLOW_GUIDE.md](DATA_FLOW_GUIDE.md)** - How Data Flows Through The App
   - Visual diagrams showing request → response flow
   - Step-by-step code walkthrough with explanations
   - Understanding Controller → Service → Repository → Database
   - How generic types transform at each layer
   - **Perfect for understanding the big picture!**

3. **[CHEAT_SHEET.md](CHEAT_SHEET.md)** - Quick Reference Guide
   - Common code patterns and snippets
   - API response examples
   - Controller patterns (CRUD operations)
   - Service patterns with generics
   - Copy-paste ready code examples
   - **Use this for quick lookups!**

### 📚 Additional Documentation

- **[API_GUIDE.md](API_GUIDE.md)** - Complete API endpoint documentation
- **[ARCHITECTURE.md](ARCHITECTURE.md)** - Project architecture and design decisions
- **[QUICKSTART.md](QUICKSTART.md)** - Get up and running quickly
- **[PROJECT_SUMMARY.md](PROJECT_SUMMARY.md)** - High-level project overview

### 💡 Key Concepts Explained

The code includes **extensive inline comments** explaining:
- Generic types (`ApiResponse<T>`, `PageResponse<T>`)
- How data flows between layers
- Why we use DTOs vs Entities
- Pagination mechanics
- Response wrapper patterns

### 🎯 Learning Path Recommendation

**For absolute beginners:**
1. Read `GENERICS_GUIDE.md` (30 min) - Understand the foundation
2. Read `DATA_FLOW_GUIDE.md` (20 min) - See how it all works together
3. Explore the actual code with new understanding
4. Keep `CHEAT_SHEET.md` open as reference

**For quick learners:**
1. Skim `CHEAT_SHEET.md` (10 min) - Get the patterns
2. Jump into the code
3. Reference `DATA_FLOW_GUIDE.md` when confused

**All code comments are beginner-friendly** - look for detailed explanations in:
- `src/main/java/com/zedcode/common/dto/ApiResponse.java`
- `src/main/java/com/zedcode/common/dto/PageResponse.java`
- `src/main/java/com/zedcode/module/user/controller/UserController.java`
- `src/main/java/com/zedcode/module/user/service/UserServiceImpl.java`

---

## 🛠 Technology Stack
</text>


- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** - Authentication & Authorization
- **Spring Data JPA** - Data persistence
- **PostgreSQL** - Production database
- **H2 Database** - Development/Testing
- **JWT (JJWT)** - Token-based authentication
- **MapStruct** - Object mapping
- **Lombok** - Boilerplate code reduction
- **Flyway** - Database migrations
- **Springdoc OpenAPI** - API documentation
- **Maven** - Dependency management

## 📁 Project Structure

```
newZedCode/
├── src/
│   ├── main/
│   │   ├── java/com/zedcode/
│   │   │   ├── ZedCodeApplication.java          # Main application class
│   │   │   ├── config/                          # Configuration classes
│   │   │   │   ├── CorsConfig.java             # CORS configuration
│   │   │   │   ├── SecurityConfig.java         # Security configuration
│   │   │   │   └── OpenApiConfig.java          # Swagger/OpenAPI config
│   │   │   ├── common/                         # Shared components
│   │   │   │   ├── constants/                  # Application constants
│   │   │   │   │   └── AppConstants.java
│   │   │   │   ├── dto/                        # Common DTOs
│   │   │   │   │   ├── ApiResponse.java        # Standard API response
│   │   │   │   │   ├── ErrorResponse.java      # Error response
│   │   │   │   │   └── PageResponse.java       # Paginated response
│   │   │   │   ├── exception/                  # Custom exceptions
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   │   ├── BadRequestException.java
│   │   │   │   │   ├── UnauthorizedException.java
│   │   │   │   │   ├── ForbiddenException.java
│   │   │   │   │   └── ConflictException.java
│   │   │   │   └── util/                       # Utility classes
│   │   │   │       ├── DateUtils.java
│   │   │   │       └── StringUtils.java
│   │   │   └── module/                         # Feature modules
│   │   │       └── user/                       # User module (example)
│   │   │           ├── controller/             # REST controllers
│   │   │           │   └── UserController.java
│   │   │           ├── service/                # Business logic
│   │   │           │   ├── UserService.java
│   │   │           │   └── UserServiceImpl.java
│   │   │           ├── repository/             # Data access
│   │   │           │   └── UserRepository.java
│   │   │           ├── entity/                 # JPA entities
│   │   │           │   ├── User.java
│   │   │           │   ├── BaseEntity.java
│   │   │           │   ├── Role.java
│   │   │           │   └── UserStatus.java
│   │   │           ├── dto/                    # Data Transfer Objects
│   │   │           │   ├── UserDTO.java
│   │   │           │   ├── CreateUserRequest.java
│   │   │           │   └── UpdateUserRequest.java
│   │   │           └── mapper/                 # MapStruct mappers
│   │   │               └── UserMapper.java
│   │   └── resources/
│   │       ├── application.yml                 # Main configuration
│   │       ├── application-dev.yml             # Development profile
│   │       ├── application-prod.yml            # Production profile
│   │       └── db/migration/                   # Flyway migrations
│   └── test/
│       └── java/com/zedcode/                   # Test classes
├── pom.xml                                      # Maven dependencies
└── README.md                                    # This file
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (for production)
- Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd newZedCode
   ```

2. **Configure the database**
   
   For development (H2 in-memory database is pre-configured):
   ```bash
   # No additional configuration needed
   ```

   For PostgreSQL (production):
   ```bash
   # Create database
   createdb zedcode_db
   
   # Update application-dev.yml or application-prod.yml with your credentials
   ```

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   # Using Maven
   mvn spring-boot:run
   
   # Or using the JAR file
   java -jar target/backend-1.0.0.jar
   
   # With specific profile
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

5. **Access the application**
   - API Base URL: `http://localhost:8080/api`
   - Swagger UI: `http://localhost:8080/api/swagger-ui.html`
   - H2 Console: `http://localhost:8080/api/h2-console` (dev only)
   - Actuator: `http://localhost:8080/api/actuator`

## ⚙️ Configuration

### Environment Variables

Create environment-specific configuration:

**Development (application-dev.yml)**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/zedcode_db
    username: postgres
    password: postgres
```

**Production (application-prod.yml)**
```yaml
# Use environment variables for sensitive data
spring:
  datasource:
    url: ${DATABASE_URL}
    username: ${DATABASE_USERNAME}
    password: ${DATABASE_PASSWORD}

app:
  jwt:
    secret: ${JWT_SECRET}
```

### Profiles

- **dev**: Development profile (default)
- **prod**: Production profile
- **test**: Test profile

Activate a profile:
```bash
# Via command line
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Via environment variable
export SPRING_PROFILES_ACTIVE=prod

# Via application.yml
spring:
  profiles:
    active: prod
```

## 📚 API Documentation

### Swagger/OpenAPI

Access interactive API documentation at:
```
http://localhost:8080/api/swagger-ui.html
```

### Authentication

Most endpoints require JWT authentication. Include the token in the Authorization header:

```bash
Authorization: Bearer <your-jwt-token>
```

### Example API Endpoints

#### User Management

```http
# Create a new user
POST /api/v1/users
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "role": "USER"
}

# Get all users (paginated)
GET /api/v1/users?page=0&size=10&sort=createdAt,desc

# Get user by ID
GET /api/v1/users/{id}

# Update user
PUT /api/v1/users/{id}

# Delete user (soft delete)
DELETE /api/v1/users/{id}

# Search users
GET /api/v1/users/search?query=john
```

## 🗄️ Database

### Schema Management

Database schema is managed using Flyway migrations located in `src/main/resources/db/migration/`.

**Migration naming convention:**
```
V{version}__{description}.sql

Example:
V1__create_users_table.sql
V2__add_user_roles.sql
```

### H2 Console (Development)

Access H2 console at: `http://localhost:8080/api/h2-console`

Credentials:
- JDBC URL: `jdbc:h2:mem:zedcodedb`
- Username: `sa`
- Password: (leave empty)

## 🔒 Security

### JWT Configuration

Configure JWT settings in `application.yml`:

```yaml
app:
  jwt:
    secret: your-256-bit-secret-key
    expiration: 86400000  # 24 hours
    refresh-expiration: 604800000  # 7 days
```

### Password Encryption

Passwords are encrypted using BCrypt with a strength of 10.

### CORS

CORS is configured to allow requests from specified origins. Update `application.yml`:

```yaml
app:
  cors:
    allowed-origins: http://localhost:3000,http://localhost:4200
    allowed-methods: GET,POST,PUT,DELETE,PATCH,OPTIONS
```

## 🧪 Testing

### Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run with coverage
mvn clean test jacoco:report
```

### Test Structure

```
src/test/java/com/zedcode/
├── module/
│   └── user/
│       ├── UserControllerTest.java
│       ├── UserServiceTest.java
│       └── UserRepositoryTest.java
```

## 📝 Adding New Modules

To add a new module (e.g., Product module):

1. **Create module structure:**
   ```
   src/main/java/com/zedcode/module/product/
   ├── controller/
   ├── service/
   ├── repository/
   ├── entity/
   ├── dto/
   └── mapper/
   ```

2. **Create Entity:**
   ```java
   @Entity
   @Table(name = "products")
   public class Product extends BaseEntity {
       // fields, getters, setters
   }
   ```

3. **Create Repository:**
   ```java
   @Repository
   public interface ProductRepository extends JpaRepository<Product, Long> {
       // custom queries
   }
   ```

4. **Create Service:**
   ```java
   @Service
   public class ProductServiceImpl implements ProductService {
       // business logic
   }
   ```

5. **Create Controller:**
   ```java
   @RestController
   @RequestMapping("/api/v1/products")
   public class ProductController {
       // REST endpoints
   }
   ```

## 🔄 Best Practices

### Code Style
- Follow Java naming conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public APIs
- Keep methods small and focused

### Error Handling
- Use custom exceptions for specific errors
- Return standardized error responses
- Log exceptions appropriately

### Security
- Never commit sensitive data
- Use environment variables for secrets
- Implement proper authentication/authorization
- Validate all input data

### Performance
- Use pagination for large datasets
- Implement caching where appropriate
- Optimize database queries
- Use connection pooling

## 📊 Monitoring & Health Checks

### Actuator Endpoints

```bash
# Health check
GET /api/actuator/health

# Application info
GET /api/actuator/info

# Metrics
GET /api/actuator/metrics

# All endpoints
GET /api/actuator
```

## 🚢 Deployment

### Build for Production

```bash
mvn clean package -DskipTests
```

### Docker (Optional)

Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

Build and run:
```bash
docker build -t zedcode-backend .
docker run -p 8080:8080 zedcode-backend
```

## 📄 License

This project is licensed under the MIT License.

## 👥 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📧 Contact

For questions or support, please contact: support@zedcode.com

---

**Happy Coding! 🚀**