# Spring Boot Pagination Example API

A comprehensive REST API demonstrating pagination, sorting, and CRUD operations for product management using Spring Boot, PostgreSQL, and Swagger/OpenAPI documentation.

## 📋 Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Prerequisites](#prerequisites)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Testing the API](#testing-the-api)
- [Project Structure](#project-structure)
- [Common Issues](#common-issues)

---

## ✨ Features

- **CRUD Operations**: Create, Read, Update, and Delete products
- **Pagination Support**: Efficient data retrieval with customizable page size
- **Sorting Capabilities**: Sort by any field (name, price, id) in ascending or descending order
- **Input Validation**: Comprehensive validation using Bean Validation API
- **Swagger UI Integration**: Interactive API documentation and testing interface
- **PostgreSQL Database**: Reliable relational database with Docker support
- **RESTful Design**: Following REST API best practices
- **DTO Pattern**: Separation of entity and data transfer objects
- **Lombok Integration**: Reduced boilerplate code

---

## 🛠️ Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17 | Programming Language |
| Spring Boot | 3.5.7 | Application Framework |
| Spring Data JPA | 3.5.7 | Data Access Layer |
| PostgreSQL | 15 | Database |
| Lombok | Latest | Code Generation |
| SpringDoc OpenAPI | 2.8.0 | API Documentation |
| Maven | Latest | Build Tool |
| Docker | Latest | Database Containerization |

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

1. **Java Development Kit (JDK) 17 or higher**
   ```bash
   java -version
   ```

2. **Maven 3.6+** (or use the included Maven wrapper)
   ```bash
   mvn -version
   ```

3. **Docker & Docker Compose**
   ```bash
   docker --version
   docker-compose --version
   ```

4. **Git** (for cloning the repository)
   ```bash
   git --version
   ```

---

## 🚀 Installation & Setup

### 1. Clone the Repository

```bash
git clone <repository-url>
cd paginationexample
```

### 2. Start PostgreSQL Database

The project includes a `docker-compose.yml` file for easy database setup:

```bash
docker-compose up -d
```

This will start PostgreSQL with the following configuration:
- **Host**: localhost
- **Port**: 5432
- **Database**: pagination_db
- **Username**: root
- **Password**: MdAshikur123+

**Verify database is running:**
```bash
docker ps
```

You should see a container named `paginationexample-db-1` or similar.

### 3. Install Dependencies

Using Maven wrapper (recommended):
```bash
./mvnw clean install
```

Or using system Maven:
```bash
mvn clean install
```

---

## 🏃 Running the Application

### Option 1: Using Maven Wrapper (Recommended)

```bash
./mvnw spring-boot:run
```

### Option 2: Using System Maven

```bash
mvn spring-boot:run
```

### Option 3: Run JAR File

```bash
./mvnw clean package
java -jar target/paginationexample-0.0.1-SNAPSHOT.jar
```

### Verify Application is Running

You should see console output similar to:
```
Started PaginationexampleApplication in X.XXX seconds
```

The application will be available at: **http://localhost:8080**

---

## 📚 API Documentation

### Accessing Swagger UI

Once the application is running, access the interactive API documentation:

**Swagger UI:** [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

**OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

### Available Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/products` | Create a new product |
| `GET` | `/api/products` | Get paginated list of products |
| `GET` | `/api/products/{id}` | Get a specific product by ID |
| `GET` | `/api/products/sort` | Get all products sorted by field |
| `DELETE` | `/api/products/{id}` | Delete a product by ID |

---

## 🗄️ Database Schema

### Products Table

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(19,2) NOT NULL
);
```

**Fields:**
- `id`: Auto-generated unique identifier
- `name`: Product name (required)
- `description`: Product description (optional)
- `price`: Product price (required, must be > 0)

---

## 🧪 Testing the API

### Quick Start Testing Guide

#### 1. Create a Product

**Request:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "description": "High-performance laptop with 16GB RAM",
    "price": 999.99
  }'
```

**Response (201 Created):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop with 16GB RAM",
  "price": 999.99
}
```

#### 2. Get All Products (Paginated)

**Request:**
```bash
curl "http://localhost:8080/api/products?page=0&size=10&sort=name,asc"
```

**Query Parameters:**
- `page`: Page number (0-based, default: 0)
- `size`: Items per page (default: 20)
- `sort`: Sort criteria (format: `field,direction`)

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-performance laptop with 16GB RAM",
      "price": 999.99
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true
}
```

#### 3. Get Product by ID

**Request:**
```bash
curl http://localhost:8080/api/products/1
```

**Response (200 OK):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop with 16GB RAM",
  "price": 999.99
}
```

#### 4. Get Sorted Products

**Request:**
```bash
curl "http://localhost:8080/api/products/sort?sortField=price"
```

**Response:** Array of all products sorted by price (ascending)

#### 5. Delete Product

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

**Response (204 No Content):** Empty body, product deleted

---

## 📁 Project Structure

```
paginationexample/
├── src/
│   ├── main/
│   │   ├── java/com/puspo/codearena/paginationexample/
│   │   │   ├── config/
│   │   │   │   └── SwaggerConfig.java          # Swagger/OpenAPI configuration
│   │   │   ├── controller/
│   │   │   │   └── ProductController.java      # REST API endpoints
│   │   │   ├── dto/
│   │   │   │   └── ProductDto.java             # Data Transfer Object
│   │   │   ├── entity/
│   │   │   │   └── Product.java                # JPA Entity
│   │   │   ├── mapper/
│   │   │   │   └── ProductMapper.java          # DTO-Entity mapper
│   │   │   ├── repository/
│   │   │   │   └── ProductRepository.java      # Data access layer
│   │   │   ├── service/
│   │   │   │   ├── ProductService.java         # Service interface
│   │   │   │   └── IProductService.java        # Service implementation
│   │   │   └── PaginationexampleApplication.java
│   │   └── resources/
│   │       └── application.properties          # Application configuration
│   └── test/
│       └── java/com/puspo/codearena/paginationexample/
│           └── PaginationexampleApplicationTests.java
├── docker-compose.yml                          # PostgreSQL setup
├── pom.xml                                     # Maven dependencies
├── README.md                                   # This file
└── SWAGGER_TESTING_GUIDE.md                   # Detailed Swagger testing guide
```

---

## 🔧 Configuration

### Application Properties

Key configuration in `src/main/resources/application.properties`:

```properties
# Application Name
spring.application.name=paginationexample

# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/pagination_db
spring.datasource.username=root
spring.datasource.password=MdAshikur123+

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server Configuration
server.port=8080

# Swagger/OpenAPI Configuration
springdoc.api-docs.enabled=true
springdoc.swagger-ui.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

### Customization

**Change Server Port:**
```properties
server.port=8081
```

**Change Database:**
Update `spring.datasource.*` properties in `application.properties`

**Disable Swagger:**
```properties
springdoc.swagger-ui.enabled=false
```

---

## ❗ Common Issues

### Issue 1: Port 8080 Already in Use

**Error:** `Port 8080 is already in use`

**Solution:**
1. Stop the application using port 8080
2. Or change the port in `application.properties`:
   ```properties
   server.port=8081
   ```

### Issue 2: Database Connection Failed

**Error:** `Connection refused` or `Unknown database`

**Solutions:**
1. Ensure Docker is running: `docker ps`
2. Start database: `docker-compose up -d`
3. Check database logs: `docker-compose logs db`
4. Verify credentials in `application.properties`

### Issue 3: Lombok Not Working

**Error:** Getters/Setters not found

**Solutions:**
1. Enable annotation processing in your IDE
2. Install Lombok plugin (IntelliJ IDEA, Eclipse, VS Code)
3. Rebuild project: `./mvnw clean install`

### Issue 4: Maven Wrapper Permission Denied

**Error:** `Permission denied: ./mvnw`

**Solution:**
```bash
chmod +x mvnw
./mvnw spring-boot:run
```

---

## 📖 Additional Documentation

For detailed Swagger UI testing instructions with examples and screenshots, see:
- **[SWAGGER_TESTING_GUIDE.md](SWAGGER_TESTING_GUIDE.md)** - Comprehensive guide for testing APIs using Swagger UI

---

## 🎯 API Examples

### Pagination Examples

**Get first page (10 items):**
```
GET /api/products?page=0&size=10
```

**Get second page (5 items):**
```
GET /api/products?page=1&size=5
```

### Sorting Examples

**Sort by name (ascending):**
```
GET /api/products?sort=name,asc
```

**Sort by price (descending):**
```
GET /api/products?sort=price,desc
```

**Multiple sort criteria:**
```
GET /api/products?sort=price,desc&sort=name,asc
```

**Combined pagination and sorting:**
```
GET /api/products?page=0&size=10&sort=price,desc
```

---

## 🔍 Validation Rules

### Product Creation/Update

- **name**: 
  - Required (cannot be null or blank)
  - Example: "Laptop"

- **description**: 
  - Optional
  - Can be null or empty
  - Example: "High-performance laptop"

- **price**: 
  - Required (cannot be null)
  - Must be greater than 0
  - Format: Decimal with up to 2 decimal places
  - Example: 999.99

---

## 🚀 Future Enhancements

- [ ] Add update (PUT/PATCH) endpoint
- [ ] Implement product search/filter functionality
- [ ] Add product categories
- [ ] Implement security (Spring Security)
- [ ] Add unit and integration tests
- [ ] Implement caching (Redis)
- [ ] Add audit logging
- [ ] Implement soft delete
- [ ] Add product images support
- [ ] Implement API versioning

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📝 License

This project is for educational purposes.

---

## 👥 Author

**Puspo** - Code Arena

---

## 🙏 Acknowledgments

- Spring Boot Documentation
- Spring Data JPA
- SpringDoc OpenAPI
- PostgreSQL
- Lombok Project

---

## 📞 Support

For issues, questions, or contributions:
- Check the [SWAGGER_TESTING_GUIDE.md](SWAGGER_TESTING_GUIDE.md)
- Review application logs
- Check Docker container status
- Verify database connection

---

**Happy Coding! 🎉**