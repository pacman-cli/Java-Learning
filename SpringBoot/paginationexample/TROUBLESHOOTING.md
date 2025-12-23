# Troubleshooting Guide

## Frontend Not Fetching Data

### Problem
The frontend dashboard loads but shows no products or shows connection errors.

### Common Causes & Solutions

#### 1. Application Not Running
**Symptom:** Browser console shows `Failed to fetch` or `net::ERR_CONNECTION_REFUSED`

**Solution:**
```bash
# Check if the application is running
lsof -i :8080

# If nothing is running, start the application
./start.sh

# Or manually:
docker-compose up -d
./mvnw spring-boot:run
```

#### 2. Database Not Running
**Symptom:** Application fails to start with database connection errors

**Solution:**
```bash
# Start the database
docker-compose up -d

# Verify database is running
docker ps

# Check database logs
docker-compose logs db

# Test database connection
docker exec -it $(docker-compose ps -q db) psql -U root -d pagination_db
```

#### 3. CORS Issues
**Symptom:** Browser console shows CORS policy errors

**Solution:**
- Verify `WebConfig.java` exists and has CORS configuration
- Make sure you're accessing the frontend at `http://localhost:8080` (not a different port)
- Clear browser cache and reload

#### 4. Port Already in Use
**Symptom:** Application fails to start with "Port 8080 is already in use"

**Solution:**
```bash
# Find what's using port 8080
lsof -i :8080

# Kill the process (replace PID with actual process ID)
kill -9 <PID>

# Or use a different port in application.properties
# server.port=8081
```

---

## API Endpoints Not Responding

### Problem
API calls return 404 or unexpected errors.

### Solutions

#### 1. Check API Base URL
The API base URL should be: `http://localhost:8080/api/products`

#### 2. Verify Endpoints
```bash
# Test GET all products (paginated)
curl http://localhost:8080/api/products

# Test GET with pagination
curl "http://localhost:8080/api/products?page=0&size=10"

# Test POST (create product)
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","price":99.99}'

# Test GET by ID
curl http://localhost:8080/api/products/1

# Test DELETE
curl -X DELETE http://localhost:8080/api/products/1
```

---

## Database Issues

### Problem
Data not persisting or database errors.

### Solutions

#### 1. Reset Database
```bash
# Stop and remove containers
docker-compose down -v

# Start fresh
docker-compose up -d

# Wait for database to be ready
sleep 5

# Restart application
./mvnw spring-boot:run
```

#### 2. Check Database Configuration
Verify `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pagination_db
spring.datasource.username=root
spring.datasource.password=MdAshikur123+
```

#### 3. Load Sample Data
```bash
# Connect to database
docker exec -it $(docker-compose ps -q db) psql -U root -d pagination_db

# Run sample data (if available)
\i /path/to/sample-data.sql
```

---

## Build Issues

### Problem
Maven build fails or dependencies can't be resolved.

### Solutions

#### 1. Clean and Rebuild
```bash
# Clean build
./mvnw clean

# Full rebuild
./mvnw clean install

# Skip tests if needed
./mvnw clean package -DskipTests
```

#### 2. Update Dependencies
```bash
# Force update dependencies
./mvnw clean install -U
```

#### 3. Clear Maven Cache
```bash
# Remove local repository (be careful!)
rm -rf ~/.m2/repository

# Rebuild
./mvnw clean install
```

---

## Swagger UI Issues

### Problem
Swagger UI not loading or showing errors.

### Solutions

#### 1. Verify Swagger URL
- Main UI: `http://localhost:8080/swagger-ui.html`
- Alternative: `http://localhost:8080/swagger-ui/index.html`
- API Docs JSON: `http://localhost:8080/v3/api-docs`

#### 2. Check Dependencies
Verify `pom.xml` has:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

---

## Frontend Display Issues

### Problem
Frontend looks broken or styles not loading.

### Solutions

#### 1. Clear Browser Cache
- Chrome/Edge: `Ctrl+Shift+Delete` (Windows/Linux) or `Cmd+Shift+Delete` (Mac)
- Firefox: `Ctrl+Shift+Delete` (Windows/Linux) or `Cmd+Shift+Delete` (Mac)
- Hard refresh: `Ctrl+F5` (Windows/Linux) or `Cmd+Shift+R` (Mac)

#### 2. Verify Static Files
```bash
# Check if index.html exists
ls -la src/main/resources/static/index.html

# Rebuild to ensure static files are packaged
./mvnw clean package -DskipTests
```

#### 3. Check Browser Console
Open Developer Tools (F12) and check:
- Console tab for JavaScript errors
- Network tab for failed requests

---

## Performance Issues

### Problem
Application is slow or times out.

### Solutions

#### 1. Check Database Performance
```bash
# Monitor database
docker stats $(docker-compose ps -q db)
```

#### 2. Increase Memory
Update `docker-compose.yml`:
```yaml
services:
  db:
    image: postgres:15
    environment:
      # ... existing config
    deploy:
      resources:
        limits:
          memory: 2G
```

#### 3. Tune Application
Update `application.properties`:
```properties
# Connection pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5

# JPA optimization
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
```

---

## Common Error Messages

### "Connection refused"
- **Cause:** Application or database not running
- **Fix:** Start services with `./start.sh`

### "404 Not Found"
- **Cause:** Wrong URL or endpoint doesn't exist
- **Fix:** Check URL matches API endpoints

### "500 Internal Server Error"
- **Cause:** Database connection issue or bug in code
- **Fix:** Check application logs: `./mvnw spring-boot:run` output

### "Access to fetch at ... has been blocked by CORS policy"
- **Cause:** CORS not configured or accessing from wrong origin
- **Fix:** Verify `WebConfig.java` exists and access from `http://localhost:8080`

### "Validation failed"
- **Cause:** Invalid input data (empty name, negative price, etc.)
- **Fix:** Ensure name is not blank and price is positive

---

## Getting Help

### Application Logs
```bash
# View live logs
./mvnw spring-boot:run

# Or if running as JAR
java -jar target/*.jar
```

### Database Logs
```bash
docker-compose logs -f db
```

### Check Application Status
```bash
# Check if port 8080 is in use
lsof -i :8080

# Check running Docker containers
docker ps

# Check application health (if actuator enabled)
curl http://localhost:8080/actuator/health
```

---

## Quick Start Commands

### Start Everything
```bash
./start.sh
```

### Stop Everything
```bash
# Stop Spring Boot: Ctrl+C
# Stop database
docker-compose down
```

### Reset Everything
```bash
# Stop all
docker-compose down -v

# Start fresh
./start.sh
```

### Test API
```bash
# Quick test
curl http://localhost:8080/api/products

# Expected: JSON response with pagination data
```

---

## Still Having Issues?

1. Check all services are running:
   ```bash
   docker ps                    # Database should be running
   lsof -i :8080               # Application should be on port 8080
   ```

2. Test each component:
   - Database: `docker exec -it $(docker-compose ps -q db) pg_isready -U root`
   - API: `curl http://localhost:8080/api/products`
   - Frontend: Open `http://localhost:8080` in browser

3. Review logs for errors:
   - Application: Check console output from `./mvnw spring-boot:run`
   - Database: `docker-compose logs db`
   - Browser: Open Developer Tools (F12) → Console tab

4. Try a complete reset:
   ```bash
   docker-compose down -v
   ./mvnw clean
   ./start.sh
   ```
