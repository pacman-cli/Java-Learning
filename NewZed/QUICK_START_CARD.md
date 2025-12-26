# 🚀 Quick Start Card - User Management System

## ⚡ Essential Commands

### Backend (Spring Boot)
```bash
# Navigate to backend
cd newZedCode

# Build
mvn clean install

# Run
mvn spring-boot:run

# Run with profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Build without tests
mvn clean package -DskipTests
```

### Frontend (Next.js)
```bash
# Navigate to frontend
cd frontend

# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build

# Start production server
npm start

# Run linting
npm run lint
```

---

## 🌐 Important URLs

| Service | URL | Notes |
|---------|-----|-------|
| **Frontend** | http://localhost:3000 | Main application |
| **Backend API** | http://localhost:8080/api/v1 | REST API base |
| **Swagger UI** | http://localhost:8080/api/swagger-ui.html | API documentation |
| **H2 Console** | http://localhost:8080/h2-console | Database viewer |
| **Actuator** | http://localhost:8080/actuator/health | Health check |

**H2 Console Login:**
- JDBC URL: `jdbc:h2:mem:userdb`
- Username: `sa`
- Password: (empty)

---

## 📝 Common API Endpoints

```bash
# Get all users (paginated)
GET http://localhost:8080/api/v1/users?page=0&size=10

# Get user by ID
GET http://localhost:8080/api/v1/users/1

# Create user
POST http://localhost:8080/api/v1/users
Content-Type: application/json
{
  "firstName": "John",
  "lastName": "Doe",
  "username": "johndoe",
  "email": "john@example.com",
  "password": "SecurePass123!",
  "role": "USER"
}

# Update user
PUT http://localhost:8080/api/v1/users/1
Content-Type: application/json
{
  "firstName": "Jane"
}

# Delete user (soft)
DELETE http://localhost:8080/api/v1/users/1

# Search users
GET http://localhost:8080/api/v1/users/search?keyword=john

# Get user statistics
GET http://localhost:8080/api/v1/users/stats

# Activate user
PATCH http://localhost:8080/api/v1/users/1/activate

# Block user
PATCH http://localhost:8080/api/v1/users/1/block
```

---

## 🐛 Quick Troubleshooting

### Port Already in Use

**Backend (8080):**
```bash
lsof -ti:8080 | xargs kill -9
```

**Frontend (3000):**
```bash
lsof -ti:3000 | xargs kill -9
```

### CORS Errors
Add `CorsConfig.java` to backend:
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

### Frontend Not Connecting to Backend
```bash
# Check .env.local exists
cat frontend/.env.local

# Should contain:
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1

# Restart frontend
cd frontend
rm -rf .next
npm run dev
```

### Backend Build Fails
```bash
cd newZedCode
mvn clean install -U
```

### Frontend Build Fails
```bash
cd frontend
rm -rf node_modules package-lock.json .next
npm install
```

---

## 📁 Key Files

### Backend
- `src/main/resources/application.yml` - Main configuration
- `src/main/java/com/zedcode/module/user/controller/UserController.java` - User endpoints
- `src/main/java/com/zedcode/module/user/service/UserServiceImpl.java` - Business logic
- `src/main/java/com/zedcode/module/user/entity/User.java` - User entity
- `pom.xml` - Dependencies

### Frontend
- `src/lib/api.ts` - API client
- `src/lib/types.ts` - TypeScript types
- `src/lib/utils.ts` - Utility functions
- `src/app/page.tsx` - Home page
- `src/app/globals.css` - Global styles
- `.env.local` - Environment variables
- `package.json` - Dependencies

---

## 🎯 Quick Project Overview

```
Request Flow:
Browser → Next.js Frontend → Axios Client → Spring Boot Backend → JPA → Database

User Actions:
1. User visits http://localhost:3000
2. Frontend calls http://localhost:8080/api/v1/users
3. Backend processes request through Controller → Service → Repository
4. Data fetched from H2/PostgreSQL database
5. Response sent back as JSON with ApiResponse<T> wrapper
6. Frontend displays data with React components
```

---

## 📚 Documentation Quick Links

**Backend:**
- Start Here: `newZedCode/START_HERE.md`
- API Guide: `newZedCode/API_GUIDE.md`
- Architecture: `newZedCode/ARCHITECTURE.md`

**Frontend:**
- README: `frontend/README.md`
- Implementation Guide: `FRONTEND_IMPLEMENTATION_GUIDE.md`

**Complete Setup:**
- `README_COMPLETE_SETUP.md`

---

## 🔑 Environment Setup

### Backend Environment (.env or application.yml)
```yaml
spring:
  profiles:
    active: dev  # or prod, test
  datasource:
    url: jdbc:h2:mem:userdb  # or PostgreSQL URL
    username: sa
    password: 
```

### Frontend Environment (.env.local)
```env
NEXT_PUBLIC_API_URL=http://localhost:8080/api/v1
```

---

## 💡 Quick Tips

1. **Always start backend first**, then frontend
2. **Check Swagger UI** for API testing before frontend development
3. **Use H2 Console** to inspect database during development
4. **Check browser console** for frontend errors
5. **Check terminal logs** for backend errors
6. **Restart services** after changing environment variables
7. **Clear caches** if you see weird behavior

---

## 🚀 First Time Setup (5 Minutes)

```bash
# 1. Start Backend
cd newZedCode
mvn clean install
mvn spring-boot:run

# 2. In new terminal - Start Frontend
cd frontend
npm install
npm run dev

# 3. Open browser
# Frontend: http://localhost:3000
# Backend: http://localhost:8080/api/swagger-ui.html

# ✅ Done!
```

---

## 📊 Project Status

**Backend:** ✅ Complete and Production Ready
**Frontend:** ⚙️ Core infrastructure ready, pages need implementation

**What's Ready:**
- ✅ API Client with all endpoints
- ✅ TypeScript types matching backend
- ✅ Utility functions
- ✅ Global styles
- ✅ Home page with statistics

**What to Implement:**
- ⏳ User list page
- ⏳ Create user page
- ⏳ Edit user page
- ⏳ User detail page
- ⏳ Search and filter UI

---

## 🆘 Need Help?

1. **Backend issues?** Read `newZedCode/START_HERE.md`
2. **Frontend issues?** Read `frontend/README.md`
3. **API questions?** Check `newZedCode/API_GUIDE.md`
4. **Can't connect?** Verify both services are running

**Health Check:**
```bash
# Backend health
curl http://localhost:8080/actuator/health

# Frontend (should load page)
curl http://localhost:3000
```

---

**Keep this card handy for quick reference! 📌**