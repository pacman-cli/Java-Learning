# Portfolio Backend

A Spring Boot backend for a portfolio website with RESTful APIs and MySQL database integration.

## Features

- 🚀 RESTful API endpoints
- 🗄️ MySQL database integration
- 🔄 CRUD operations for portfolio data
- 🛡️ Data validation
- 🌐 CORS enabled for frontend integration
- 📦 Modular architecture with services, repositories, and controllers

## Tech Stack

- **Spring Boot 3.2** - Java framework for building web applications
- **Spring Data JPA** - ORM for database operations
- **MySQL** - Relational database
- **Maven** - Dependency management
- **Hibernate** - Object-relational mapping
- **Jakarta Validation** - Data validation

## API Endpoints

### Skills

- `GET /api/skills` - Get all skills
- `GET /api/skills/{id}` - Get skill by ID
- `POST /api/skills` - Create a new skill
- `PUT /api/skills/{id}` - Update a skill
- `DELETE /api/skills/{id}` - Delete a skill

### Projects

- `GET /api/projects` - Get all projects
- `GET /api/projects/featured` - Get featured projects
- `GET /api/projects/{id}` - Get project by ID
- `POST /api/projects` - Create a new project
- `PUT /api/projects/{id}` - Update a project
- `DELETE /api/projects/{id}` - Delete a project

### Experiences

- `GET /api/experiences` - Get all experiences
- `GET /api/experiences/{id}` - Get experience by ID
- `POST /api/experiences` - Create a new experience
- `PUT /api/experiences/{id}` - Update an experience
- `DELETE /api/experiences/{id}` - Delete an experience

### Educations

- `GET /api/educations` - Get all educations
- `GET /api/educations/{id}` - Get education by ID
- `POST /api/educations` - Create a new education
- `PUT /api/educations/{id}` - Update an education
- `DELETE /api/educations/{id}` - Delete an education

### Contact Messages

- `GET /api/contact-messages` - Get all messages
- `GET /api/contact-messages/{id}` - Get message by ID
- `POST /api/contact-messages` - Create a new message
- `PUT /api/contact-messages/{id}` - Update a message
- `DELETE /api/contact-messages/{id}` - Delete a message

### Portfolio Data

- `GET /api/portfolio/data` - Get all portfolio data in a single response

## Project Structure

```
portfolio-backend/
├── src/
│   ├── main/
│   │   ├── java/com/portfolio/
│   │   │   ├── PortfolioBackendApplication.java
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/portfolio/
├── pom.xml
└── README.md
```

## Entity Relationships

- **Skill** - Represents technical skills with categories
- **Project** - Portfolio projects with technologies and URLs
- **Experience** - Work experiences with technologies used
- **Education** - Educational background with courses
- **ContactMessage** - Messages from the contact form

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

### Database Setup

1. Create a MySQL database:

```sql
CREATE DATABASE portfolio_db;
```

2. Update database credentials in `src/main/resources/application.properties`

### Installation

1. Clone the repository:

```bash
git clone <repository-url>
```

2. Navigate to the backend directory:

```bash
cd portfolio-backend
```

3. Build the project:

```bash
mvn clean install
```

4. Run the application:

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## Configuration

Update the following properties in `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/portfolio_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=password

# Server Configuration
server.port=8080
```

## Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a pull request

## License

This project is licensed under the MIT License.
