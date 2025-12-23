## Blogify

> A secure, JWT-authenticated blogging REST API built with Spring Boot 3, MySQL, and Spring Security. Includes Swagger UI, validation, and a minimal static UI.

---

### ✨ Features

- JWT-based stateless authentication (login/register)
- Role-ready security (default `ROLE_USER`, easily extend to `ROLE_ADMIN`)
- CRUD for blog posts and comments with ownership checks
- DTO validation with helpful error responses
- OpenAPI/Swagger UI with bearer auth support
- MySQL persistence via Spring Data JPA
- Global exception handling
- Optimized JPA queries to avoid N+1 issues

---

### 📑 Table of Contents

- [Features](#-features)
- [Project Structure](#%EF%B8%8F-project-structure)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Setup](#environment-setup)
- [Authentication Flow](#-authentication-flow)
- [API Overview](#-api-overview)
- [Quick Test Scripts](#-quick-test-scripts-curl)
- [Configuration Details](#%EF%B8%8F-configuration-details)
- [Data Model](#%EF%B8%8F-data-model)
- [Development](#%EF%B8%8F-development)
- [Dark UI](#-dark-ui-swagger--static)
- [Security Notes](#-security-notes)
- [Known Improvements](#-known-improvements)
- [Fixes Applied](#-fixes-applied-in-this-repo)
- [License](#-license)

---

### 🗂️ Project Structure

```
src/main/java/com/pacman/blogify/
  config/           # Security, OpenAPI, JWT properties
  controller/       # REST controllers for auth, posts, comments
  dto/              # Request/response DTOs
  exception/        # Global exception handler + custom exceptions
  mapper/           # DTO <-> Entity mappers
  model/            # JPA entities (User, BlogPost, Comment)
  repository/       # Spring Data repositories
  security/         # JWT filter + response wrapper
  service/          # Business logic services
  util/             # JwtUtil (generate/validate tokens)
src/main/resources/
  application.properties
  static/           # Minimal static UI
```

---

### 🚀 Getting Started

#### Prerequisites

- Java 17+
- Maven 3.9+
- MySQL 8+ running locally with a schema named `blogify`

#### Environment Setup

0) Clone the repository:

```bash
git clone https://github.com/your-user/blogify.git
cd blogify
```

1) Configure database and JWT in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blogify?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASS

# JWT
app.jwt.secret=change-me-in-prod-32+chars
app.jwt.expirationMs=86400000
```

2) Start MySQL and ensure the `blogify` database exists.

3) Build and run:

```bash
./mvnw spring-boot:run
```

App runs on [`http://localhost:8080`](http://localhost:8080).

---

### 🔐 Authentication Flow

- Register: `POST /api/auth/register` with `{ username, email, password }` (password 8–20 chars)
- Login: `POST /api/auth/login` with `{ username, password }` → returns `{ token }`
- Use token: Add header `Authorization: Bearer <token>` for protected endpoints

Token generation/validation: `util/JwtUtil` using `app.jwt.secret` and `app.jwt.expirationMs`.

---

### 📚 API Overview

Base URL: `http://localhost:8080`

### Auth
- `POST /api/auth/register` → 200 OK on success
- `POST /api/auth/login` → 200 OK `{ token }`

### Blog Posts
- `GET /api/posts` → public list of posts
- `GET /api/posts/{id}` → public single post
- `POST /api/posts` → create (requires Bearer token)
- `PUT /api/posts/{id}` → update if you’re the author
- `DELETE /api/posts/{id}` → delete if you’re the author

### Comments
- `GET /api/comments/posts/{postId}` → public list of comments for a post
- `POST /api/comments/posts/{postId}` → create (requires Bearer token)
- `GET /api/comments/{commentId}` → get a comment
- `PUT /api/comments/{commentId}` → update if you’re the author
- `DELETE /api/comments/{commentId}` → delete if you’re the author

Full interactive docs: [Swagger UI](http://localhost:8080/swagger-ui.html).

When using Swagger for protected routes, click “Authorize” and paste `Bearer <token>`.

---

### 🧪 Quick Test Scripts (curl)

Note: These examples use `jq` to extract JSON fields. Install with `brew install jq` (macOS) or adjust to your environment.

```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"alice","email":"alice@example.com","password":"P@ssword1"}'

# Login (capture token)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"alice","password":"P@ssword1"}' | jq -r .token)

# Create a post
curl -X POST http://localhost:8080/api/posts \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"title":"Hello","content":"My first post content..."}'

# List posts (public)
curl http://localhost:8080/api/posts

# Comment on a post (assumes id=1)
curl -X POST http://localhost:8080/api/comments/posts/1 \
  -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' \
  -d '{"content":"Nice!"}'
```

Without `jq`, you can copy the token from the login response manually and export it:

```bash
export TOKEN=PASTE_YOUR_JWT_HERE
```

---

### ⚙️ Configuration Details

- Security rules: `config/SecurityConfig`
  - Public: `/`, static assets, Swagger, `/api/auth/**`, `/api/posts`, `/api/posts/*`, `/api/comments/posts/*`
  - Protected: `/api/posts/**`, `/api/comments/**`, `/api/admin/**`
- JWT filter: `security/JwtRequestFilter` added before `AnonymousAuthenticationFilter`
- OpenAPI: `config/OpenApiConfig` with scheme `Bearer Authentication`

---

### 🧱 Data Model

- `User(id, username, email, password, blogPosts, comments)`
- `BlogPost(id, title, content, author, comments, createdAt, updatedAt)`
- `Comment(id, content, post, author, createdAt)`

Timestamps are set via JPA lifecycle hooks in `BlogPost` and `Comment`.

---

### 🛠️ Development

Build:
```bash
./mvnw clean package
```

Run tests:
```bash
./mvnw test
```

Hot reload (DevTools included as runtime optional).

---

### 🎨 Dark UI (Swagger + Static)

Swagger UI uses your browser/system theme. To force a dark look while browsing docs, use a dark browser theme or extensions such as “Dark Reader”. The static `static/` pages are minimal; pair them with a dark system/browser theme for a dark experience.

---

### 🔒 Security Notes

- Change `app.jwt.secret` for any non-local environment (use 32+ random bytes)
- Do not log raw JWTs; the filter uses debug-level minimal logging
- CORS is wide-open for development; restrict origins in production (see `corsConfigurationSource` in `SecurityConfig`)

---

### ✅ Known Improvements

- Add refresh tokens and token revocation
- Add roles/authorities on `User` and admin endpoints
- Add rate limiting for auth endpoints
- Add pagination for posts/comments
- Add request/response examples to Swagger annotations
- Replace runtime `RuntimeException` in registration with dedicated exceptions

---

### 🐛 Fixes Applied in This Repo

- Align Swagger security name to `Bearer Authentication` across controllers
- Avoid logging raw JWT values; use debug-level minimal logs
- Correct password validation to `@Size(min=8, max=20)`
- Public matchers updated to include `/api/posts/*` and `/api/comments/posts/*`

---

### 📜 License

This project is provided as-is for learning/demo purposes. Add a proper license file before production use.


