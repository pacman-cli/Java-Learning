# Swagger UI Testing Guide for Product Pagination API

## Table of Contents
1. [Getting Started](#getting-started)
2. [Accessing Swagger UI](#accessing-swagger-ui)
3. [API Endpoints Overview](#api-endpoints-overview)
4. [Detailed Testing Instructions](#detailed-testing-instructions)
5. [Common Use Cases](#common-use-cases)
6. [Troubleshooting](#troubleshooting)

---

## Getting Started

### Prerequisites
1. **Start PostgreSQL Database**
   ```bash
   docker-compose up -d
   ```

2. **Start Spring Boot Application**
   ```bash
   ./mvnw spring-boot:run
   ```
   Or if you're on Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. **Verify Application is Running**
   - The application should start on port 8080
   - Check console logs for "Started PaginationexampleApplication"

---

## Accessing Swagger UI

Once the application is running, open your browser and navigate to:

**Swagger UI URL:** `http://localhost:8080/swagger-ui.html`

Alternative URLs (all work the same):
- `http://localhost:8080/swagger-ui/index.html`
- `http://localhost:8080/swagger-ui/`

**API Documentation (JSON format):** `http://localhost:8080/v3/api-docs`

---

## API Endpoints Overview

The API provides 5 main endpoints for product management:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Create a new product |
| GET | `/api/products` | Get all products with pagination |
| GET | `/api/products/{id}` | Get a specific product by ID |
| GET | `/api/products/sort` | Get all products sorted by field |
| DELETE | `/api/products/{id}` | Delete a product by ID |

---

## Detailed Testing Instructions

### 1. Create a New Product (POST `/api/products`)

**Step-by-Step:**

1. **Expand the POST endpoint** by clicking on it
2. Click the **"Try it out"** button
3. **Edit the Request Body** with your product data

**Example Request Body:**
```json
{
  "name": "Laptop",
  "description": "High-performance laptop with 16GB RAM and 512GB SSD",
  "price": 999.99
}
```

**Important Notes:**
- **DO NOT include the `id` field** - it's auto-generated
- `name` is **required** (cannot be blank)
- `price` is **required** (must be greater than 0)
- `description` is **optional**

4. Click **"Execute"** button
5. **Expected Response:** Status Code `201 Created`

**Sample Response:**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop with 16GB RAM and 512GB SSD",
  "price": 999.99
}
```

**More Test Data Examples:**
```json
{
  "name": "Mouse",
  "description": "Wireless optical mouse",
  "price": 25.50
}
```

```json
{
  "name": "Keyboard",
  "description": "Mechanical keyboard with RGB lighting",
  "price": 89.99
}
```

```json
{
  "name": "Monitor",
  "description": "27-inch 4K display",
  "price": 399.00
}
```

```json
{
  "name": "Headphones",
  "description": "Noise-cancelling wireless headphones",
  "price": 249.99
}
```

---

### 2. Get All Products with Pagination (GET `/api/products`)

**Step-by-Step:**

1. **Expand the GET `/api/products` endpoint**
2. Click **"Try it out"**
3. **Configure pagination parameters:**

**Parameters:**

| Parameter | Description | Example Values |
|-----------|-------------|----------------|
| `page` | Page number (0-based) | 0, 1, 2, ... |
| `size` | Number of items per page | 5, 10, 20 |
| `sort` | Sort field and direction | name,asc or price,desc |

**Example 1: Basic Pagination**
- `page`: 0
- `size`: 10
- `sort`: Leave empty or default

4. Click **"Execute"**

**Expected Response Structure:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-performance laptop",
      "price": 999.99
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": false,
      "empty": true,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalPages": 1,
  "totalElements": 5,
  "last": true,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": false,
    "empty": true,
    "unsorted": true
  },
  "numberOfElements": 5,
  "first": true,
  "empty": false
}
```

**Example 2: Get Second Page with 5 Items**
- `page`: 1
- `size`: 5

**Example 3: Sort by Name (Ascending)**
- `page`: 0
- `size`: 10
- `sort`: `name,asc`

**Example 4: Sort by Price (Descending)**
- `page`: 0
- `size`: 10
- `sort`: `price,desc`

**Example 5: Multiple Sort Criteria**
- `sort`: `price,desc&sort=name,asc`

**Understanding the Response:**
- `content`: Array of products on current page
- `totalElements`: Total number of products in database
- `totalPages`: Total number of pages
- `number`: Current page number (0-based)
- `size`: Items per page
- `first`: True if this is the first page
- `last`: True if this is the last page
- `empty`: True if no content

---

### 3. Get Product by ID (GET `/api/products/{id}`)

**Step-by-Step:**

1. **Expand the GET `/api/products/{id}` endpoint**
2. Click **"Try it out"**
3. **Enter the product ID** in the `id` field
   - Example: `1`
4. Click **"Execute"**

**Expected Response (Success - 200 OK):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 999.99
}
```

**Expected Response (Not Found - 404):**
If you enter an ID that doesn't exist (e.g., 9999), you'll get:
- Status Code: `404 Not Found`
- Empty response body

---

### 4. Get All Products Sorted (GET `/api/products/sort`)

**Step-by-Step:**

1. **Expand the GET `/api/products/sort` endpoint**
2. Click **"Try it out"**
3. **Enter the sort field name**

**Available Sort Fields:**
- `name` - Sort by product name
- `price` - Sort by price
- `id` - Sort by ID
- `description` - Sort by description

**Examples:**

**Sort by Name:**
- `sortField`: `name`

**Sort by Price:**
- `sortField`: `price`

**Sort by ID:**
- `sortField`: `id`

4. Click **"Execute"**

**Expected Response:**
```json
[
  {
    "id": 2,
    "name": "Headphones",
    "description": "Noise-cancelling wireless headphones",
    "price": 249.99
  },
  {
    "id": 3,
    "name": "Keyboard",
    "description": "Mechanical keyboard with RGB lighting",
    "price": 89.99
  },
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 999.99
  }
]
```

**Note:** This endpoint returns ALL products (not paginated), sorted by the specified field in ascending order.

---

### 5. Delete Product by ID (DELETE `/api/products/{id}`)

**Step-by-Step:**

1. **Expand the DELETE `/api/products/{id}` endpoint**
2. Click **"Try it out"**
3. **Enter the product ID to delete**
   - Example: `1`
4. Click **"Execute"**

**Expected Response (Success - 204 No Content):**
- Status Code: `204 No Content`
- Empty response body
- The product is permanently deleted

**Verification:**
- Try to GET the same product by ID
- You should receive `404 Not Found`

---

## Common Use Cases

### Use Case 1: Create Multiple Products and View First Page

1. **Create 5 products** using POST endpoint (examples provided above)
2. **Get first page** with 3 items per page:
   - GET `/api/products?page=0&size=3`
3. **View response** - should show first 3 products
4. **Check pagination info:**
   - `totalElements`: 5
   - `totalPages`: 2
   - `first`: true
   - `last`: false

### Use Case 2: Navigate Through Pages

1. **Page 1 (first 3 items):**
   - `page=0`, `size=3`
2. **Page 2 (next 2 items):**
   - `page=1`, `size=3`
3. **Notice the `last` field** becomes `true` on page 2

### Use Case 3: Find Products by Price Range (Using Sorting)

1. **Sort by price (ascending):**
   - GET `/api/products/sort?sortField=price`
2. **View cheapest products first**
3. **Sort by price (descending) with pagination:**
   - GET `/api/products?page=0&size=10&sort=price,desc`
4. **View most expensive products first**

### Use Case 4: Search and Delete Workflow

1. **Get all products sorted by name:**
   - GET `/api/products/sort?sortField=name`
2. **Find the product ID** you want to delete
3. **Delete the product:**
   - DELETE `/api/products/{id}`
4. **Verify deletion:**
   - GET `/api/products/{id}` (should return 404)

---

## Troubleshooting

### Issue 1: Swagger UI Not Loading

**Problem:** Browser shows error or blank page

**Solutions:**
1. Verify application is running on port 8080
2. Check console logs for errors
3. Try alternative URLs:
   - `http://localhost:8080/swagger-ui/index.html`
   - `http://localhost:8080/swagger-ui/`
4. Clear browser cache and refresh
5. Check if port 8080 is already in use

### Issue 2: 400 Bad Request on POST

**Problem:** Creating product fails with 400 error

**Common Causes:**
1. **Including `id` field** in request body (remove it)
2. **Missing required fields** (`name` or `price`)
3. **Invalid price** (must be > 0)
4. **Blank name** (cannot be empty)

**Correct Request Body:**
```json
{
  "name": "Valid Product Name",
  "description": "Optional description",
  "price": 50.00
}
```

### Issue 3: Empty Response on GET

**Problem:** Getting empty array or no products

**Solutions:**
1. **Create some products first** using POST
2. **Check database connection:**
   - Ensure PostgreSQL is running via Docker
   - Verify `docker-compose up -d` was executed
3. **Check application logs** for database errors

### Issue 4: Database Connection Error

**Problem:** Application fails to start with connection errors

**Solutions:**
1. **Start PostgreSQL:**
   ```bash
   docker-compose up -d
   ```
2. **Verify database is running:**
   ```bash
   docker ps
   ```
3. **Check credentials** in `application.properties`
4. **Verify port** (should be 5432, not 5433)

### Issue 5: Pagination Not Working

**Problem:** Getting wrong page or wrong number of items

**Remember:**
1. **Page numbers are 0-based** (first page is 0, not 1)
2. **Default page size** may vary (check response)
3. **Parameters format:**
   - Correct: `?page=0&size=10&sort=name,asc`
   - Wrong: `?page=1&pageSize=10`

### Issue 6: Sort Direction Not Working

**Problem:** Items not sorting correctly

**Solutions:**
1. **Use correct format:** `fieldName,direction`
   - Examples: `name,asc` or `price,desc`
2. **Valid directions:** `asc` or `desc`
3. **For `/sort` endpoint:** Only field name, always ascending
4. **For `/products` endpoint with pagination:** Use `sort=field,direction`

---

## Advanced Tips

### Tip 1: Testing with cURL (Alternative to Swagger)

**Create Product:**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","description":"High-performance laptop","price":999.99}'
```

**Get Products with Pagination:**
```bash
curl "http://localhost:8080/api/products?page=0&size=10&sort=name,asc"
```

**Get Product by ID:**
```bash
curl http://localhost:8080/api/products/1
```

**Delete Product:**
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

### Tip 2: Using Swagger Schema

In Swagger UI, you can click the **"Schema"** tab to see the expected data structure with:
- Field types
- Required fields
- Validation rules
- Example values

### Tip 3: Export API Documentation

You can export the API specification:
1. Visit: `http://localhost:8080/v3/api-docs`
2. Copy the JSON
3. Import into tools like Postman or Insomnia

---

## Quick Reference Card

| Action | Endpoint | Method | Key Parameters |
|--------|----------|--------|----------------|
| Create | `/api/products` | POST | body: {name, description, price} |
| List (paginated) | `/api/products` | GET | page, size, sort |
| Get by ID | `/api/products/{id}` | GET | id (path) |
| Sort all | `/api/products/sort` | GET | sortField |
| Delete | `/api/products/{id}` | DELETE | id (path) |

---

## Example Testing Workflow

**Complete workflow to test all features:**

1. **Create 5 products** (POST x5)
2. **Get all products, page 1** (GET with page=0, size=3)
3. **Get all products, page 2** (GET with page=1, size=3)
4. **Sort by name** (GET /sort?sortField=name)
5. **Sort by price descending** (GET ?sort=price,desc)
6. **Get product by ID** (GET /{id})
7. **Delete a product** (DELETE /{id})
8. **Verify deletion** (GET /{id} - should return 404)
9. **Check remaining products** (GET with pagination)

---

## Need Help?

If you encounter issues not covered in this guide:

1. Check application console logs
2. Verify database is running: `docker ps`
3. Check application.properties configuration
4. Ensure all dependencies are installed: `./mvnw clean install`
5. Review Spring Boot startup logs for errors

---

**Happy Testing! 🚀**