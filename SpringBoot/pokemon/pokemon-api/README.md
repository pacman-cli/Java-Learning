# Pokemon API - Full Stack Application

A comprehensive full-stack Pokemon management application built with **Spring Boot** (backend) and **React** (frontend), featuring JWT authentication, CRUD operations, and modern UI design.

## 🏗️ Architecture Overview

```
pokemon-api/
├── src/main/java/com/pacman/pokemonapi/     # Spring Boot Backend
│   ├── adapter/persistence/                # JPA Entities
│   ├── config/                            # Configuration Classes
│   ├── controller/                        # REST Controllers
│   ├── dto/                               # Data Transfer Objects
│   ├── enums/                            # Enums
│   ├── mapper/                           # Entity-DTO Mappers
│   ├── repository/                       # JPA Repositories
│   ├── security/                         # Security Configuration
│   ├── service/                          # Business Logic
│   └── util/                             # Utility Classes
├── frontend/                              # React Frontend
│   ├── src/components/                    # React Components
│   ├── src/services/                      # API Services
│   └── src/                              # Main App Files
└── build.gradle                          # Project Dependencies
```

## � Features Implemented

### Backend Features (Spring Boot 3.5.5)

#### 🔐 Authentication & Security

- **JWT-based Authentication** with secure token generation and validation
- **BCrypt Password Encryption** for secure password storage
- **Role-based Access Control** (USER/ADMIN roles)
- **CORS Configuration** for frontend-backend communication
- **Spring Security** integration with custom JWT filter

#### 📊 Pokemon Management

- **Complete CRUD Operations** (Create, Read, Update, Delete)
- **Pokemon Types** using enum (NORMAL, FIRE, WATER, ELECTRIC, etc.)
- **Image URL Support** for Pokemon avatars
- **HP System** with validation (1-999)
- **Search and Filter** capabilities

#### 🗄️ Database Integration

- **MySQL Database** connection with JPA/Hibernate
- **Entity Mapping** with proper relationships
- **Auto DDL Updates** for schema management
- **Connection Pool** configuration

#### 🏗️ Clean Architecture

- **Adapter Pattern** for persistence layer
- **DTO Pattern** for data transfer
- **Mapper Classes** for entity-DTO conversion
- **Service Layer** for business logic
- **Repository Pattern** for data access

### Frontend Features (React 19.1.1)

#### 🎨 User Interface

- **Modern, Responsive Design** with gradient backgrounds
- **Pokemon Cards** with type-specific color coding
- **Interactive Forms** with validation
- **Loading States** and error handling
- **Mobile-Friendly** responsive design

#### 🔒 Authentication UI

- **Login/Signup Forms** with form validation
- **JWT Token Management** with localStorage
- **Auto-logout** on token expiration
- **Role Selection** during registration

#### 📱 Pokemon Management UI

- **Pokemon Grid View** with cards
- **Add/Edit Pokemon Forms** with all fields
- **Search Functionality** by name/description
- **Type Filtering** dropdown
- **Image Preview** with fallback placeholders
- **Confirmation Dialogs** for deletions

#### 🔧 Technical Features

- **Axios HTTP Client** with interceptors
- **Auto Token Injection** for authenticated requests
- **Error Handling** with user-friendly messages

## 🛠️ Technology Stack

### Backend

- **Java 17** - Programming language
- **Spring Boot 3.5.5** - Framework
- **Spring Security** - Authentication & authorization
- **Spring Data JPA** - Database access
- **MySQL** - Database
- **JWT (JJWT 0.13.0)** - Token-based authentication
- **BCrypt** - Password encryption
- **Lombok** - Code generation
- **MapStruct 1.6.3** - Object mapping
- **Gradle** - Build tool

### Frontend

- **React 19.1.1** - UI library
- **Axios 1.12.2** - HTTP client
- **React Router DOM 7.9.1** - Routing (installed, ready for use)
- **CSS3** - Styling with modern features
- **JavaScript ES6+** - Programming language

## 📁 Database Schema

### Users Table

```sql
users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
)
```

### Pokemon Table

```sql
pokemon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    description TEXT,
    type VARCHAR(50),
    hp INTEGER,
    image_url VARCHAR(500)
)
```

## 🔌 API Endpoints

### Authentication Endpoints

```
POST /api/auth/signup    # Register new user
POST /api/auth/login     # Authenticate user
```

### Pokemon Endpoints (Protected)

```
GET    /api/pokemons     # Get all Pokemon
GET    /api/pokemons/{id} # Get Pokemon by ID
POST   /api/pokemons     # Create new Pokemon
PUT    /api/pokemons/{id} # Update Pokemon
DELETE /api/pokemons/{id} # Delete Pokemon
```

## 🏃‍♂️ Getting Started

### Prerequisites

- Java 17+
- MySQL Server
- Node.js 16+
- npm or yarn

### Backend Setup

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd pokemon-api
   ```

2. **Configure Database**

   - Create MySQL database: `pokemon_db`
   - Update `application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/pokemon_db
     spring.datasource.username=root
     spring.datasource.password=your_password
     ```

3. **Run Backend**
   ```bash
   ./gradlew bootRun
   ```
   Backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to frontend directory**

   ```bash
   cd frontend
   ```

2. **Install dependencies**

   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm start
   ```
   Frontend will start on `http://localhost:3000`

## 🔒 Security Implementation

### JWT Token Structure
```json
{
  "sub": "username",
  "role": "ROLE_USER",
  "iat": 1234567890,
  "exp": 1234567890
}
```

### Security Features
- **Token Expiration**: 10 hours
- **Secure Headers**: Authorization Bearer token
- **Password Hashing**: BCrypt with salt
- **CORS Protection**: Configured for localhost:3000
- **Input Validation**: Both frontend and backend validation

## 📊 Component Architecture

### Backend Components
```
Controllers → Services → Repositories → Database
     ↓            ↓
   DTOs ←→ Mappers ←→ Entities
```

### Frontend Components
```
App → AuthPage/PokemonList
        ↓
   LoginForm/SignupForm → PokemonCard/PokemonForm
        ↓
   Services → API → Backend
```

## 🔧 Configuration Files

### Backend Configuration
- `application.properties` - Database and server config
- `build.gradle` - Dependencies and build configuration
- `SecurityConfig.java` - Security rules and JWT config
- `CorsConfig.java` - Cross-origin request configuration

### Frontend Configuration
- `package.json` - Dependencies and scripts
- `api.js` - Axios configuration with interceptors
- Component-specific CSS files for styling

## 📝 Key Implementation Details

### Backend Architecture
1. **Clean Architecture**: Adapter pattern with clear separation of concerns
2. **Entity-DTO Mapping**: Manual mappers for data transformation
3. **JWT Integration**: Custom filter for token validation
4. **Security Configuration**: Spring Security with CORS and JWT
5. **Database Design**: MySQL with JPA/Hibernate ORM

### Frontend Architecture
1. **Component Structure**: Reusable React components
2. **State Management**: React hooks for local state
3. **API Integration**: Axios with interceptors for token management
4. **Responsive Design**: Mobile-first CSS with modern layouts
5. **Form Validation**: Client-side validation with user feedback

## 🏆 Best Practices Implemented

### Backend Best Practices
- Clean code architecture with separation of concerns
- Proper exception handling and error messages
- Secure password storage with BCrypt
- JWT token validation and expiration
- Input validation and sanitization
- Proper HTTP status codes

### Frontend Best Practices
- Component-based architecture
- State management with React hooks
- Responsive design principles
- Error boundary implementation
- Performance optimization
- Code reusability

---

## 👨‍💻 Developer Information

**Framework**: Spring Boot 3.5.5 + React 19.1.1  
**Database**: MySQL 8.0+  
**Security**: JWT + Spring Security + BCrypt  
**Architecture**: Clean Architecture with Adapter Pattern  

---

*This README documents a complete full-stack Pokemon management application with modern development practices and comprehensive feature set.*

## API Endpoints

### Authentication

- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/login` - Login user

### Pokemon Management (Protected)

- `GET /api/pokemons` - Get all Pokemon
- `GET /api/pokemons/{id}` - Get Pokemon by ID
- `POST /api/pokemons` - Create new Pokemon
- `PUT /api/pokemons/{id}` - Update Pokemon
- `DELETE /api/pokemons/{id}` - Delete Pokemon

## Project Structure

```
pokemon-api/
├── src/main/java/com/pacman/pokemonapi/
│   ├── adapter/persistence/     # JPA Entities
│   ├── config/                  # Configuration classes
│   ├── controller/              # REST Controllers
│   ├── dto/                     # Data Transfer Objects
│   ├── enums/                   # Enums (PokemonType)
│   ├── mapper/                  # Entity-DTO mappers
│   ├── repository/              # JPA Repositories
│   ├── security/                # Security components
│   ├── service/                 # Business logic
│   └── util/                    # Utility classes
├── frontend/
│   ├── src/
│   │   ├── components/          # React components
│   │   ├── services/            # API services
│   │   └── App.js               # Main App component
│   └── public/                  # Static files
└── README.md
```

## Features in Detail

### Authentication System

- JWT-based authentication with secure token storage
- Role-based access control (USER/ADMIN roles)
- Protected routes and API endpoints

### Pokemon Management

- Complete CRUD operations for Pokemon
- Pokemon types based on official Pokemon types
- Image URL support for Pokemon avatars
- HP tracking and statistics

### User Interface

- Responsive grid layout for Pokemon cards
- Real-time search and filtering
- Form validation and error handling
- Loading states and user feedback
- Pokemon type color coding

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is open source and available under the [MIT License](LICENSE).
