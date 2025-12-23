# Quick Start Guide

## 🚀 Getting Started in 30 Seconds

### Prerequisites
- Java 17 or higher
- PostgreSQL database running on port 5432
- Maven (or use included Maven wrapper)

### Start the Application

```bash
# Option 1: Use the startup script
./run-app.sh

# Option 2: Manual start
./mvnw spring-boot:run
```

Once the application starts, open your browser to:
- **Frontend Dashboard:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/v3/api-docs

---

## 📋 What's Happening?

### Your Problem: Frontend Isn't Fetching Data

**Root Cause:** The Spring Boot application wasn't running!

**What We Fixed:**
1. ✅ Added **CORS configuration** (`WebConfig.java`) - allows frontend to access the API
2. ✅ Created **startup scripts** - easy one-command launch
3. ✅ Your PostgreSQL database is already running and configured correctly

---

## 🎯 Testing the Application

### 1. Using the Frontend Dashboard

Open http://localhost:8080 in your browser:

1. **Create a Product:**
   - Fill in the form (Name & Price are required)
   - Click "Create Product"
   - See the success message

2. **View Products:**
   - Products appear in the table automatically
   - Use pagination controls (Previous/Next)
   - Change page size (5, 10, 20, 50, 100 items)

3. **Sort Products:**
   - Select sort field (Name, Price, ID)
   - Click "Apply" to reload with sorting

4. **Delete Products:**
   - Click "Delete" button next to any product
   - Confirm the deletion

5. **View Product Details:**
   - Click "View" button to see full product info

6. **API Response Viewer:**
   - Click "Toggle API Response" to see raw JSON responses
   - Great for debugging and learning

### 2. Using Swagger UI

Open http://localhost:8080/swagger-ui.html

1. Expand any endpoint (e.g., "POST /api/products")
2. Click "Try it out"
3. Fill in the request body/parameters
4. Click "Execute"
5. View the response

**Example - Create Product:**
```json
{
  "name": "Laptop",
  "price": 999.99,
  "description": "High-performance laptop"
}
```

**Example - Get Products with Pagination:**
- Parameters: `page=0`, `size=10`, `sort=name,asc`

### 3. Using cURL

```bash
# Get all products (paginated)
curl http://localhost:8080/api/products

# Get products with specific page and size
curl "http://localhost:8080/api/products?page=0&size=5"

# Create a new product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Smartphone",
    "price": 699.99,
    "description": "Latest model"
  }'

# Get product by ID
curl http://localhost:8080/api/products/1

# Delete product
curl -X DELETE http://localhost:8080/api/products/1

# Get products sorted
curl "http://localhost:8080/api/products?page=0&size=10&sort=price,desc"
```

---

## 🛠️ Troubleshooting

### Frontend Shows "Failed to fetch"

**Check 1:** Is the application running?
```bash
lsof -i :8080
# Should show Java process on port 8080
```

**Check 2:** Is the database running?
```bash
psql -U root -h localhost -p 5432 -d pagination_db -c "\dt"
# Should show 'products' table
```

**Check 3:** Browser console errors?
- Press F12 to open Developer Tools
- Check the Console tab for errors
- Check the Network tab for failed requests

### Application Won't Start

**Port 8080 already in use:**
```bash
# Find what's using port 8080
lsof -i :8080

# Kill the process (replace PID)
kill -9 <PID>
```

**Database connection error:**
```bash
# Verify database exists
psql -U root -h localhost -p 5432 -l

# Create database if missing
psql -U root -h localhost -p 5432 -c "CREATE DATABASE pagination_db;"
```

### Can't Connect to Database

Make sure your PostgreSQL is running and configured:
- Database name: `pagination_db`
- Username: `root`
- Password: `MdAshikur123+`
- Port: `5432`

Check `src/main/resources/application.properties` for these settings.

---

## 📁 Project Structure

```
paginationexample/
├── src/main/java/.../
│   ├── config/
│   │   ├── SwaggerConfig.java      # Swagger/OpenAPI configuration
│   │   └── WebConfig.java          # CORS configuration (NEW!)
│   ├── controller/
│   │   └── ProductController.java  # REST API endpoints
│   ├── dto/
│   │   └── ProductDto.java         # Data Transfer Object
│   ├── entity/
│   │   └── Product.java            # JPA Entity
│   ├── mapper/
│   │   └── ProductMapper.java      # DTO ↔ Entity mapping
│   ├── repository/
│   │   └── ProductRepository.java  # Database access
│   └── service/
│       └── ProductService.java     # Business logic
├── src/main/resources/
│   ├── static/
│   │   └── index.html              # Frontend dashboard
│   └── application.properties      # Configuration
├── run-app.sh                      # Quick start script (NEW!)
├── QUICK_START.md                  # This guide (NEW!)
└── TROUBLESHOOTING.md              # Detailed troubleshooting (NEW!)
```

---

## 🎓 API Endpoints Summary

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | Get paginated products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/sort` | Get all products sorted |
| POST | `/api/products` | Create new product |
| DELETE | `/api/products/{id}` | Delete product by ID |

### Pagination Parameters

- `page` - Page number (0-based, default: 0)
- `size` - Items per page (default: 10)
- `sort` - Sort criteria (format: `field,direction`)
  - Examples: `name,asc` or `price,desc`

---

## ✅ Verification Checklist

Run through this checklist to verify everything works:

- [ ] Application starts without errors: `./run-app.sh`
- [ ] Frontend loads: http://localhost:8080
- [ ] Can create a product via frontend form
- [ ] Products display in the table
- [ ] Pagination works (next/previous buttons)
- [ ] Can delete a product
- [ ] Can view product details
- [ ] Swagger UI loads: http://localhost:8080/swagger-ui.html
- [ ] Can execute API calls from Swagger
- [ ] API response viewer shows JSON data

---

## 🔄 Start/Stop Commands

### Start Application
```bash
./run-app.sh
```

### Stop Application
Press `Ctrl + C` in the terminal where the app is running

### Restart Application
```bash
# Stop with Ctrl+C, then:
./run-app.sh
```

### Check if Running
```bash
# Check application
lsof -i :8080

# Check database
psql -U root -h localhost -p 5432 -d pagination_db -c "SELECT COUNT(*) FROM products;"
```

---

## 📚 Next Steps

Now that your application is running:

1. **Add more products** - Use the frontend to create test data
2. **Explore pagination** - Try different page sizes and sorting
3. **Test with Swagger** - Execute API calls directly
4. **Check the code** - Review the source files to understand the implementation
5. **Read documentation** - Check `SWAGGER_TESTING_GUIDE.md` and `FRONTEND_GUIDE.md`

---

## 🆘 Need Help?

If you're still having issues:

1. Check `TROUBLESHOOTING.md` for detailed solutions
2. Review application logs in the terminal
3. Check browser console (F12) for frontend errors
4. Verify all prerequisites are installed:
   ```bash
   java -version    # Should be 17+
   psql --version   # Should be installed
   ```

---

## 🎉 Success!

If you can see products in the frontend dashboard at http://localhost:8080, you're all set! The frontend is now successfully fetching data from your Spring Boot API.

**What was the issue?**
The application simply wasn't running. Now it is, and the CORS configuration ensures the frontend can communicate with the backend API without any issues.

Enjoy testing your pagination example! 🚀