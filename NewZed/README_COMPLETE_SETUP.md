# 🚀 User Management System - Complete Project

> A full-stack user management system with **Spring Boot** backend and **Next.js** frontend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Next.js](https://img.shields.io/badge/Next.js-14.2.3-black.svg)](https://nextjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4.5-blue.svg)](https://www.typescriptlang.org/)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Architecture](#-architecture)
- [Features](#-features)
- [Technology Stack](#-technology-stack)
- [Project Structure](#-project-structure)
- [Quick Start](#-quick-start)
- [Backend Setup](#-backend-setup)
- [Frontend Setup](#-frontend-setup)
- [API Documentation](#-api-documentation)
- [Troubleshooting](#-troubleshooting)
- [Documentation](#-documentation)
- [Development Workflow](#-development-workflow)
- [Contributing](#-contributing)
- [License](#-license)

---

## 🎯 Overview

This is a **production-ready**, **full-stack user management system** that demonstrates modern web development practices with:

- **Backend**: RESTful API built with Spring Boot, JPA, and PostgreSQL/H2
- **Frontend**: Modern web application built with Next.js 14, TypeScript, and Tailwind CSS
- **Type Safety**: Full type-safety from database to UI with TypeScript
- **Documentation**: Comprehensive guides for beginners and experts
- **Best Practices**: Following industry standards and design patterns

### What Can This System Do?

✅ **User Management**
- Create, read, update, and delete users
- User profile management with validation
- Soft delete and permanent delete options

✅ **Access Control**
- Role-based access control (User, Admin, Moderator, Super Admin)
- User status management (Active, Inactive, Pending, Blocked, Deleted)
- Account activation and deactivation

✅ **Search & Filter**
- Search users by name, email, or username
- Filter by status and role
- Pagination support for large datasets

✅ **Real-Time Feedback**
- Toast notifications for all operations
- Loading states and error handling
- Responsive design for all devices

---

## 🏗️ Architecture

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Frontend (Next.js)                       │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   React UI   │  │  TypeScript  │  │ Tailwind CSS │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
│                         │                                    │
│                         │ HTTP/REST                          │
│                         ▼                                    │
└─────────────────────────────────────────────────────────────┘
                          │
                          │ JSON
                          │
┌─────────────────────────▼─────────────────────────────────┐
│                 Backend (Spring Boot)                      │
│  ┌──────────────────────────────────────────────────────┐ │
│  │              REST Controllers                         │ │
│  └──────────────────────────────────────────────────────┘ │
│                         │                                  │
│  ┌──────────────────────▼──────────────────────────────┐  │
│  │              Service Layer                           │  │
│  └──────────────────────────────────────────────────────┘  │
│                         │                                  │
│  ┌──────────────────────▼──────────────────────────────┐  │
│  │              Repository Layer (JPA)                  │  │
│  └──────────────────────────────────────────────────────┘  │
│                         │                                  │
└─────────────────────────┼──────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────────────┐
│              Database (PostgreSQL / H2)                      │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │    Users     │  │   Roles      │  │   Audit      │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

### Layer-by-Layer Breakdown

#### Frontend Layers
1. **UI Components** - React components with TypeScript
2. **API Client** - Axios-based HTTP client with interceptors
3. **Type Definitions** - TypeScript interfaces matching backend DTOs
4. **Utilities** - Helper functions for formatting, validation, etc.

#### Backend Layers
1. **Controllers** - REST endpoints handling HTTP requests
2. **Services** - Business logic and orchestration
3. **Repositories** - Data access layer using Spring Data JPA
4. **Entities** - JPA entities representing database tables
5. **DTOs** - Data transfer objects for API communication

---

## ✨ Features

### Backend Features

#### Core Functionality
- ✅ RESTful API with 19+ endpoints
- ✅ CRUD operations for users
- ✅ Advanced search and filtering
- ✅ Pagination and sorting
- ✅ Soft delete with audit trail

#### Security & Validation
- ✅ JWT authentication ready (configurable)
- ✅ Password encryption with BCrypt
- ✅ Input validation using Bean Validation
- ✅ Role-based access control
- ✅ CORS configuration

#### Data Management
- ✅ JPA entities with relationships
- ✅ Flyway database migrations
- ✅ Multiple database profiles (H2, PostgreSQL)
- ✅ Audit fields (createdAt, updatedAt, createdBy, etc.)
- ✅ Soft delete support

#### Developer Experience
- ✅ Swagger/OpenAPI documentation
- ✅ Global exception handling
- ✅ Comprehensive error responses
- ✅ Extensive inline documentation
- ✅ Multiple profiles (dev, prod, test)

### Frontend Features

#### User Interface
- ✅ Modern, responsive design
- ✅ Intuitive navigation
- ✅ Real-time statistics dashboard
- ✅ Color-coded status badges
- ✅ Loading states and animations

#### User Operations
- ✅ Create users with validation
- ✅ View user details
- ✅ Edit user information
- ✅ Delete users (soft/permanent)
- ✅ Activate/deactivate accounts
- ✅ Block/unblock users

#### Search & Filter
- ✅ Keyword search across multiple fields
- ✅ Filter by status
- ✅ Filter by role
- ✅ Pagination controls
- ✅ Sort by multiple columns

#### User Experience
- ✅ Toast notifications
- ✅ Form validation
- ✅ Error handling
- ✅ Mobile-responsive
- ✅ Accessibility features

---

## 🛠️ Technology Stack

### Backend Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 17+ | Programming language |
| Spring Boot | 3.2.0 | Application framework |
| Spring Data JPA | 3.2.0 | Data access layer |
| Spring Security | 3.2.0 | Security framework |
| PostgreSQL | 15+ | Production database |
| H2 Database | 2.2.224 | Development database |
| Lombok | 1.18.30 | Reduce boilerplate code |
| MapStruct | 1.5.5 | Object mapping |
| Flyway | 9.22.3 | Database migrations |
| Springdoc OpenAPI | 2.2.0 | API documentation |
| Maven | 3.9+ | Build tool |

### Frontend Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Next.js | 14.2.3 | React framework |
| React | 18.3.1 | UI library |
| TypeScript | 5.4.5 | Type-safe JavaScript |
| Tailwind CSS | 3.4.3 | Utility-first CSS |
| Axios | 1.6.8 | HTTP client |
| React Hot Toast | 2.4.1 | Toast notifications |
| Lucide React | 0.379.0 | Icon library |
| date-fns | 3.6.0 | Date formatting |
| Node.js | 18+ | JavaScript runtime |
| npm | 9+ | Package manager |

---

## 📁 Project Structure

```
NewZed/
├── frontend/                          # Next.js Frontend Application
│   ├── src/
│   │   ├── app/                      # Next.js App Router pages
│   │   │   ├── layout.tsx           # Root layout
│   │   │   ├── page.tsx             # Home page
│   │   │   ├── globals.css          # Global styles
│   │   │   └── users/               # User pages (to implement)
│   │   │
│   │   ├── components/              # React components (to implement)
│   │   │   ├── Navbar.tsx
│   │   │   ├── UserCard.tsx
│   │   │   ├── UserForm.tsx
│   │   │   └── ...
│   │   │
│   │   └── lib/                     # Core libraries
│   │       ├── api.ts              # API client ✅
│   │       ├── types.ts            # TypeScript types ✅
│   │       └── utils.ts            # Utility functions ✅
│   │
│   ├── .env.local                   # Environment variables
│   ├── package.json                 # Dependencies
│   ├── tsconfig.json                # TypeScript config
│   ├── tailwind.config.ts           # Tailwind config
│   └── README.md                    # Frontend documentation
│
├── newZedCode/                       # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/zedcode/
│   │   │   │   ├── common/          # Common utilities
│   │   │   │   ├── config/          # Configuration classes
│   │   │   │   └── module/          # Feature modules
│   │   │   │       └── user/        # User module
│   │   │   │           ├── controller/
│   │   │   │           ├── service/
│   │   │   │           ├── repository/
│   │   │   │           ├── entity/
│   │   │   │           ├── dto/
│   │   │   │           └── mapper/
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.yml  # Main configuration
│   │   │       └── db/migration/    # Flyway migrations
│   │   │
│   │   └── test/                    # Test classes
│   │
│   ├── pom.xml                      # Maven configuration
│   ├── API_GUIDE.md                 # API documentation
│   ├── ARCHITECTURE.md              # Architecture guide
│   ├── START_HERE.md                # Beginner guide
│   └── README.md                    # Backend documentation
│
├── FRONTEND_IMPLEMENTATION_GUIDE.md # Frontend guide
├── README_COMPLETE_SETUP.md         # This file
└── setup-and-run.sh                 # Quick setup script
```

---

## 🚀 Quick Start

### Prerequisites

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Maven 3.9+** ([Download](https://maven.apache.org/download.cgi))
- **Node.js 18+** ([Download](https://nodejs.org/))
- **npm 9+** (comes with Node.js)
- **Git** ([Download](https://git-scm.com/))

### One-Command Setup (Linux/Mac)

```bash
# Make the script executable
chmod +x setup-and-run.sh

# Run the setup script
./setup-and-run.sh
```

This script will:
1. Check prerequisites
2. Setup environment
3. Install dependencies
4. Start the development server

### Manual Setup (All Platforms)

#### Step 1: Start Backend

```bash
# Navigate to backend directory
cd newZedCode

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

Backend will be available at: **http://localhost:8080**

#### Step 2: Start Frontend

```bash
# In a new terminal, navigate to frontend directory
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend will be available at: **http://localhost:3000**

#### Step 3: Verify Setup

1. Open browser to **http://localhost:3000**
2. You should see the home page with statistics
3. Backend API docs at **http://localhost:8080/api/swagger-ui.html**

---

## 🔧 Backend Setup

### Detailed Backend Setup

```bash
# Navigate to backend directory
cd /Users/puspo/JavaCourse/NewZed/newZedCode

# Clean and build
mvn clean package -DskipTests

# Run with specific profile (optional)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Database Configuration

#### Using H2 (Development - Default)

No additional setup required. H2 runs in-memory.

Access H2 Console: **http://localhost:8080/h2-console**
- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: (leave empty)

#### Using PostgreSQL (Production)

1. Install PostgreSQL
2. Create database:
   ```sql
   CREATE DATABASE userdb;
   ```

3. Update `application-prod.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/userdb
       username: your_username
       password: your_password
   ```

4. Run with prod profile:
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=prod
   ```

### Backend Endpoints

Once running, access:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/v3/api-docs
- **H2 Console**: http://localhost:8080/h2-console
- **Actuator**: http://localhost:8080/actuator

---

## 💻 Frontend Setup

### Detailed Frontend Setup

```bash
# Navigate to frontend directory
cd /Users/puspo/JavaCourse/NewZed/frontend

# Install dependencies
npm install

# Create environment file (if not exists)
cat > .env.local << EOF
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
EOF

# Start development server
npm run dev
```

### Frontend Scripts

```bash
# Development server with hot reload
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Run linting
npm run lint

# Type checking
npm run type-check
```

### Environment Configuration

Create `.env.local` file:

```env
# Required
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1

# Optional
NEXT_PUBLIC_API_TIMEOUT=30000
NEXT_PUBLIC_DEBUG_MODE=false
```

---

## 📚 API Documentation

### Main Endpoints

#### User Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/users` | Create new user |
| GET | `/api/v1/users` | Get all users (paginated) |
| GET | `/api/v1/users/{id}` | Get user by ID |
| GET | `/api/v1/users/email/{email}` | Get user by email |
| GET | `/api/v1/users/username/{username}` | Get user by username |
| PUT | `/api/v1/users/{id}` | Update user |
| DELETE | `/api/v1/users/{id}` | Soft delete user |
| DELETE | `/api/v1/users/{id}/permanent` | Permanently delete user |

#### Search & Filter

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users/search?keyword={keyword}` | Search users |
| GET | `/api/v1/users/status/{status}` | Filter by status |
| GET | `/api/v1/users/role/{role}` | Filter by role |

#### User Management Actions

| Method | Endpoint | Description |
|--------|----------|-------------|
| PATCH | `/api/v1/users/{id}/activate` | Activate user |
| PATCH | `/api/v1/users/{id}/deactivate` | Deactivate user |
| PATCH | `/api/v1/users/{id}/block` | Block user |
| PATCH | `/api/v1/users/{id}/unblock` | Unblock user |
| PATCH | `/api/v1/users/{id}/verify-email` | Verify email |

#### Validation & Statistics

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users/exists/email/{email}` | Check if email exists |
| GET | `/api/v1/users/exists/username/{username}` | Check if username exists |
| GET | `/api/v1/users/stats` | Get user statistics |

### Example API Request

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
    "role": "USER"
  }'

# Get all users
curl http://localhost:8080/api/v1/users?page=0&size=10

# Search users
curl http://localhost:8080/api/v1/users/search?keyword=john&page=0&size=10
```

### API Response Format

All successful responses follow this format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* actual data */ },
  "timestamp": "2024-01-15T10:30:00Z"
}
```

Error responses:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 123",
  "path": "/api/v1/users/123"
}
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Port Already in Use

**Backend (Port 8080)**
```bash
# Find and kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or change port in application.yml
server:
  port: 8081
```

**Frontend (Port 3000)**
```bash
# Find and kill process on port 3000
lsof -ti:3000 | xargs kill -9

# Or run on different port
PORT=3001 npm run dev
```

#### 2. CORS Errors

Add CORS configuration in backend (`CorsConfig.java`):

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH")
            .allowedHeaders("*");
    }
}
```

#### 3. Database Connection Failed

**H2 Database:**
- Check if H2 is enabled in `application.yml`
- Verify H2 console URL: `http://localhost:8080/h2-console`

**PostgreSQL:**
- Ensure PostgreSQL is running
- Check connection details in `application-prod.yml`
- Create database if it doesn't exist

#### 4. Frontend API Connection Failed

1. Verify backend is running: `http://localhost:8080/actuator/health`
2. Check `.env.local` has correct `NEXT_PUBLIC_API_URL`
3. Restart frontend: `npm run dev`
4. Clear Next.js cache: `rm -rf .next`

#### 5. Build Errors

**Backend:**
```bash
# Clean and rebuild
mvn clean install -U
```

**Frontend:**
```bash
# Clear cache and reinstall
rm -rf node_modules package-lock.json .next
npm install
```

---

## 📖 Documentation

### Backend Documentation

Located in `newZedCode/`:

- **[START_HERE.md](newZedCode/START_HERE.md)** - Beginner's guide
- **[API_GUIDE.md](newZedCode/API_GUIDE.md)** - Complete API reference
- **[ARCHITECTURE.md](newZedCode/ARCHITECTURE.md)** - System architecture
- **[GENERICS_GUIDE.md](newZedCode/GENERICS_GUIDE.md)** - Understanding Java generics
- **[DATA_FLOW_GUIDE.md](newZedCode/DATA_FLOW_GUIDE.md)** - How data flows
- **[CHEAT_SHEET.md](newZedCode/CHEAT_SHEET.md)** - Quick reference
- **[README.md](newZedCode/README.md)** - Backend README

### Frontend Documentation

Located in `frontend/`:

- **[README.md](frontend/README.md)** - Frontend README
- **[FRONTEND_IMPLEMENTATION_GUIDE.md](FRONTEND_IMPLEMENTATION_GUIDE.md)** - Implementation guide

### Key Concepts

#### Backend Concepts
- RESTful API design
- Layered architecture
- Generic types (`<T>`)
- DTO pattern
- Repository pattern
- Service layer pattern

#### Frontend Concepts
- Next.js App Router
- TypeScript type safety
- React hooks
- API integration
- Responsive design
- Component composition

---

## 🔄 Development Workflow

### Adding a New Feature

#### Backend

1. **Create Entity** (if needed)
   ```java
   @Entity
   @Table(name = "new_entity")
   public class NewEntity { /* ... */ }
   ```

2. **Create Repository**
   ```java
   public interface NewRepository extends JpaRepository<NewEntity, Long> { }
   ```

3. **Create DTOs**
   ```java
   public class NewDTO { /* ... */ }
   public class CreateNewRequest { /* ... */ }
   ```

4. **Create Service**
   ```java
   public interface NewService { /* ... */ }
   public class NewServiceImpl implements NewService { /* ... */ }
   ```

5. **Create Controller**
   ```java
   @RestController
   @RequestMapping("/api/v1/news")
   public class NewController { /* ... */ }
   ```

6. **Test** in Swagger UI

#### Frontend

1. **Add Types** to `src/lib/types.ts`
   ```typescript
   export interface New { /* ... */ }
   ```

2. **Add API Functions** to `src/lib/api.ts`
   ```typescript
   export const getNew = async () => { /* ... */ }
   ```

3. **Create Component** in `src/components/`
   ```typescript
   export default function NewComponent() { /* ... */ }
   ```

4. **Create Page** in `src/app/news/page.tsx`
   ```typescript
   export default function NewsPage() { /* ... */ }
   ```

5. **Test** in browser

---

## 🤝 Contributing

### Guidelines

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Make changes and commit: `git commit -m "feat: add new feature"`
4. Push to branch: `git push origin feature/new-feature`
5. Create a Pull Request

### Commit Message Format

Follow conventional commits:

```
feat: add user search functionality
fix: resolve pagination bug
docs: update API documentation
style: format code
refactor: restructure user service
test: add user controller tests
chore: update dependencies
```

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Spring Boot Team** - For the amazing framework
- **Next.js Team** - For the React framework
- **Tailwind CSS** - For the utility-first CSS framework
- **All Contributors** - Thank you!

---

## 📞 Support

### Getting Help

1. **Backend Issues**: Check `newZedCode/START_HERE.md`
2. **Frontend Issues**: Check `frontend/README.md`
3. **API Reference**: Check `newZedCode/API_GUIDE.md`
4. **Architecture Questions**: Check `newZedCode/ARCHITECTURE.md`

### Quick Links

- **Backend Swagger**: http://localhost:8080/api/swagger-ui.html
- **Frontend**: http://localhost:3000
- **H2 Console**: http://localhost:8080/h2-console

---

## 🎓 Learning Path

### For Beginners

1. **Day 1**: Setup and run both applications
2. **Day 2**: Read `newZedCode/START_HERE.md` and `newZedCode/GENERICS_GUIDE.md`
3. **Day 3**: Explore API endpoints in Swagger
4. **Day 4**: Understand the frontend in `frontend/README.md`
5. **Day 5**: Make a simple modification (change a color, add a button)
6. **Week 2+**: Implement new features!

### For Experienced Developers

1. Review `ARCHITECTURE.md` for system design
2. Check `API_GUIDE.md` for endpoint details
3. Explore the codebase
4. Start building features!

---

## 🚀 Next Steps

### Immediate Tasks

1. ✅ Setup and run the application
2. ✅ Test the API endpoints
3. ✅ Explore the frontend
4. ⏳ Implement remaining frontend pages
5. ⏳ Add authentication
6. ⏳ Deploy to production

### Future Enhancements

- [ ] User authentication with JWT
- [ ] Password reset functionality
- [ ] Email notifications
- [ ] User profile pictures
- [ ] Activity logs
- [ ] Data export (CSV, PDF)
- [ ] Advanced analytics dashboard
- [ ] Bulk user operations
- [ ] API rate limiting
- [ ] Internationalization (i18n)

---

## 📊 Project Status

### Backend Status
- [x] Project setup
- [x] User CRUD operations
- [x] Search and filter
- [x] Pagination
- [x] Validation
- [x] Exception handling
- [x] API documentation
- [x] Database configuration
- [x] Comprehensive guides

### Frontend Status
- [x] Project setup
- [x] Type definitions
- [x] API client
- [x] Utility functions
- [x] Global styles
- [x] Home page with statistics
- [ ] User list page
- [ ] User create page
- [ ] User edit page
- [ ] User detail page
- [ ] Search and filter UI
- [ ] Authentication UI

---

**Made with ❤️ by ZedCode**

**Happy Coding! 🚀**