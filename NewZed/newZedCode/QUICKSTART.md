# Quick Start Guide - ZedCode Backend

Get up and running with the ZedCode Backend in under 5 minutes!

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher ([Download here](https://adoptium.net/))
- **Maven 3.6+** ([Download here](https://maven.apache.org/download.cgi))
- **Git** (optional, for cloning)
- **Your favorite IDE** (IntelliJ IDEA, Eclipse, or VS Code recommended)

### Verify Installation

```bash
# Check Java version
java -version

# Check Maven version
mvn -version
```

## 🚀 Quick Start (3 Steps)

### Step 1: Get the Code

```bash
cd newZedCode
```

### Step 2: Run the Application

**Option A: Using the startup script (Recommended)**

For Mac/Linux:
```bash
chmod +x start.sh
./start.sh
```

For Windows:
```bash
start.bat
```

**Option B: Using Maven directly**

```bash
mvn spring-boot:run
```

**Option C: Build and run JAR**

```bash
mvn clean package -DskipTests
java -jar target/backend-1.0.0.jar
```

### Step 3: Verify It's Running

Open your browser and navigate to:

```
http://localhost:8080/api/swagger-ui.html
```

You should see the interactive API documentation!

## 🎯 Access Points

Once the application is running, you can access:

| Endpoint | URL | Description |
|----------|-----|-------------|
| **API Base** | `http://localhost:8080/api` | Base API URL |
| **Swagger UI** | `http://localhost:8080/api/swagger-ui.html` | Interactive API docs |
| **API Docs** | `http://localhost:8080/api/api-docs` | OpenAPI JSON |
| **H2 Console** | `http://localhost:8080/api/h2-console` | Database console (dev only) |
| **Health Check** | `http://localhost:8080/api/actuator/health` | Application health |
| **Metrics** | `http://localhost:8080/api/actuator/metrics` | Application metrics |

## 🧪 Test the API

### Using curl

```bash
# Create a user
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "username": "johndoe",
    "email": "john@example.com",
    "password": "SecurePass123!",
    "phoneNumber": "+1234567890"
  }'

# Get all users (requires admin role)
curl http://localhost:8080/api/v1/users

# Get user by ID
curl http://localhost:8080/api/v1/users/1

# Check if email exists
curl http://localhost:8080/api/v1/users/exists/email?email=john@example.com
```

### Using Postman

1. Import the OpenAPI spec from: `http://localhost:8080/api/api-docs`
2. Create a new request
3. Test the endpoints

### Using Swagger UI

1. Go to `http://localhost:8080/api/swagger-ui.html`
2. Expand any endpoint
3. Click "Try it out"
4. Fill in the parameters
5. Click "Execute"

## 💾 Database Access (Development)

### H2 Console

1. Navigate to: `http://localhost:8080/api/h2-console`
2. Use these credentials:
   - **JDBC URL**: `jdbc:h2:mem:zedcodedb`
   - **Username**: `sa`
   - **Password**: (leave empty)
3. Click "Connect"

## 🔧 Configuration

### Switch Profiles

**Development (default)**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

**Production**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

**Test**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### Using PostgreSQL (Production)

1. Install PostgreSQL
2. Create a database:
   ```sql
   CREATE DATABASE zedcode_db;
   ```
3. Update `src/main/resources/application-dev.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/zedcode_db
       username: your_username
       password: your_password
   ```
4. Run with dev profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=dev
   ```

## 📝 Common Tasks

### Run Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=UserServiceTest

# Run tests with coverage
mvn clean test jacoco:report
```

### Build for Production

```bash
# Build JAR file
mvn clean package -DskipTests

# The JAR will be in target/backend-1.0.0.jar
```

### Clean Build

```bash
mvn clean install
```

### Format Code (if configured)

```bash
mvn spotless:apply
```

## 🎨 IDE Setup

### IntelliJ IDEA

1. Open IntelliJ IDEA
2. File → Open → Select `newZedCode` folder
3. Wait for Maven to import dependencies
4. Run configuration:
   - Main class: `com.zedcode.ZedCodeApplication`
   - VM options: `-Dspring.profiles.active=dev`
5. Click Run

### Eclipse

1. File → Import → Maven → Existing Maven Projects
2. Browse to `newZedCode` folder
3. Right-click project → Run As → Spring Boot App

### VS Code

1. Install Java Extension Pack
2. Install Spring Boot Extension Pack
3. Open `newZedCode` folder
4. Press F5 or click "Run" in the Spring Boot Dashboard

## 🔐 Authentication & Security

### Current Setup

By default, security is configured but permissive for development:

- Most endpoints are **open** in development mode
- Some admin endpoints require `ROLE_ADMIN`
- JWT authentication is ready but needs implementation

### Test with Security (Future)

```bash
# Login (when auth is implemented)
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@example.com",
    "password": "admin123"
  }'

# Use the returned token
curl http://localhost:8080/api/v1/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

## 📚 API Endpoints Overview

### User Management

```
POST   /api/v1/users                    - Create user
GET    /api/v1/users                    - Get all users (paginated)
GET    /api/v1/users/{id}               - Get user by ID
GET    /api/v1/users/email/{email}      - Get user by email
GET    /api/v1/users/username/{username} - Get user by username
PUT    /api/v1/users/{id}               - Update user
DELETE /api/v1/users/{id}               - Delete user (soft)
GET    /api/v1/users/search             - Search users
GET    /api/v1/users/status/{status}    - Get users by status
GET    /api/v1/users/role/{role}        - Get users by role
PATCH  /api/v1/users/{id}/activate      - Activate user
PATCH  /api/v1/users/{id}/deactivate    - Deactivate user
PATCH  /api/v1/users/{id}/block         - Block user
PATCH  /api/v1/users/{id}/unblock       - Unblock user
GET    /api/v1/users/stats              - Get user statistics
```

## 🐛 Troubleshooting

### Port Already in Use

If port 8080 is occupied:

**Option 1: Change port in `application.yml`**
```yaml
server:
  port: 8081
```

**Option 2: Kill the process using port 8080**
```bash
# Mac/Linux
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Database Connection Error

- **H2**: Check that H2 is not disabled in application.yml
- **PostgreSQL**: Verify database exists and credentials are correct

### Maven Build Fails

```bash
# Clear Maven cache
mvn clean
rm -rf ~/.m2/repository

# Rebuild
mvn clean install
```

### Can't Access Swagger UI

1. Verify application is running
2. Check URL: `http://localhost:8080/api/swagger-ui.html`
3. Check logs for errors
4. Ensure port 8080 is not blocked by firewall

### Java Version Issues

```bash
# Check Java version
java -version

# Set JAVA_HOME (Mac/Linux)
export JAVA_HOME=/path/to/java17

# Set JAVA_HOME (Windows)
set JAVA_HOME=C:\Path\To\Java17
```

## 📖 Next Steps

Now that you have the application running:

1. **Explore the API**: Use Swagger UI to test endpoints
2. **Read the Architecture**: Check `ARCHITECTURE.md` for design details
3. **Add New Features**: Create new modules following the user module example
4. **Configure Security**: Implement JWT authentication
5. **Add More Endpoints**: Build out your business logic
6. **Write Tests**: Add unit and integration tests
7. **Deploy**: Follow deployment guide in `README.md`

## 🆘 Getting Help

- Check the [README.md](README.md) for detailed documentation
- Review [ARCHITECTURE.md](ARCHITECTURE.md) for architecture details
- Browse the code - it's well-commented!
- Check logs in `logs/application.log`

## 🎉 Success!

If you've made it this far, you should have:

- ✅ Application running on http://localhost:8080
- ✅ Swagger UI accessible
- ✅ Database configured
- ✅ API endpoints responding

**Happy Coding! 🚀**

---

**Quick Reference Card**

```bash
# Start app
./start.sh                    # Mac/Linux
start.bat                     # Windows

# Run tests
mvn test

# Build
mvn clean package

# Access Swagger
http://localhost:8080/api/swagger-ui.html

# Access H2 Console
http://localhost:8080/api/h2-console
```
