# API Quick Reference Card

## 🚀 Base URL
```
http://localhost:8080
```

## 📍 Endpoints Summary

### 1. CREATE Product
```
POST /api/products
```
**Request Body:**
```json
{
  "name": "Product Name",
  "description": "Optional description",
  "price": 99.99
}
```
**Response:** `201 Created`

---

### 2. GET All Products (Paginated)
```
GET /api/products
```
**Query Parameters:**
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `page` | integer | 0 | Page number (0-based) |
| `size` | integer | 20 | Items per page |
| `sort` | string | - | Sort by field (e.g., `name,asc` or `price,desc`) |

**Examples:**
```
GET /api/products?page=0&size=10
GET /api/products?page=1&size=5&sort=name,asc
GET /api/products?sort=price,desc
```

**Response:** `200 OK`

---

### 3. GET Product by ID
```
GET /api/products/{id}
```
**Path Parameter:**
- `id` - Product ID (Long)

**Example:**
```
GET /api/products/1
```

**Responses:**
- `200 OK` - Product found
- `404 Not Found` - Product doesn't exist

---

### 4. GET Products Sorted
```
GET /api/products/sort
```
**Query Parameter:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `sortField` | string | Yes | Field to sort by (`name`, `price`, `id`) |

**Examples:**
```
GET /api/products/sort?sortField=name
GET /api/products/sort?sortField=price
```

**Response:** `200 OK` - Returns ALL products (not paginated)

---

### 5. DELETE Product
```
DELETE /api/products/{id}
```
**Path Parameter:**
- `id` - Product ID to delete (Long)

**Example:**
```
DELETE /api/products/1
```

**Response:** `204 No Content`

---

## 🔍 Sort Options

### Available Sort Fields
- `name` - Product name
- `price` - Product price
- `id` - Product ID
- `description` - Product description

### Sort Directions
- `asc` - Ascending order (A→Z, 0→9)
- `desc` - Descending order (Z→A, 9→0)

### Examples
```
sort=name,asc          # Sort by name A→Z
sort=price,desc        # Sort by price high→low
sort=price,asc         # Sort by price low→high
sort=id,desc           # Sort by newest first
```

---

## ✅ Validation Rules

### Product Fields

| Field | Required | Type | Constraints |
|-------|----------|------|-------------|
| `id` | Auto-generated | Long | Read-only, don't include in POST |
| `name` | ✅ Yes | String | Cannot be blank |
| `description` | ❌ No | String | Optional, can be null |
| `price` | ✅ Yes | BigDecimal | Must be > 0 |

---

## 📊 Response Format

### Paginated Response
```json
{
  "content": [
    {
      "id": 1,
      "name": "Product Name",
      "description": "Description",
      "price": 99.99
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "last": false,
  "empty": false
}
```

### Single Product Response
```json
{
  "id": 1,
  "name": "Product Name",
  "description": "Description",
  "price": 99.99
}
```

---

## 🔢 HTTP Status Codes

| Code | Description | When |
|------|-------------|------|
| `200` | OK | Successful GET request |
| `201` | Created | Product created successfully |
| `204` | No Content | Product deleted successfully |
| `400` | Bad Request | Invalid input data |
| `404` | Not Found | Product not found |
| `500` | Server Error | Internal server error |

---

## 🧪 cURL Examples

### Create Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Laptop","description":"Gaming laptop","price":1299.99}'
```

### Get Products (Page 1, 10 items, sorted by name)
```bash
curl "http://localhost:8080/api/products?page=0&size=10&sort=name,asc"
```

### Get Product by ID
```bash
curl http://localhost:8080/api/products/1
```

### Get Sorted Products
```bash
curl "http://localhost:8080/api/products/sort?sortField=price"
```

### Delete Product
```bash
curl -X DELETE http://localhost:8080/api/products/1
```

---

## 🎯 Common Use Cases

### Use Case 1: Get First Page (10 Items)
```
GET /api/products?page=0&size=10
```

### Use Case 2: Get Cheapest Products First
```
GET /api/products?page=0&size=10&sort=price,asc
```

### Use Case 3: Get Most Expensive Products First
```
GET /api/products?page=0&size=10&sort=price,desc
```

### Use Case 4: Get Products Alphabetically
```
GET /api/products?page=0&size=10&sort=name,asc
```

### Use Case 5: Navigate to Next Page
```
GET /api/products?page=1&size=10&sort=name,asc
```

---

## 🌐 Swagger UI Access

**Swagger UI URL:**
```
http://localhost:8080/swagger-ui.html
```

**OpenAPI Docs (JSON):**
```
http://localhost:8080/v3/api-docs
```

---

## ⚡ Quick Testing Workflow

1. **Create 3 products** (POST x3)
2. **View first page** (GET ?page=0&size=2)
3. **View second page** (GET ?page=1&size=2)
4. **Sort by price** (GET ?sort=price,desc)
5. **Get specific product** (GET /{id})
6. **Delete a product** (DELETE /{id})

---

## 🛠️ Pagination Tips

### Remember:
- **Pages are 0-based**: First page = 0, Second page = 1
- **Default page size**: 20 (if not specified)
- **Sort format**: `field,direction` (e.g., `name,asc`)
- **Multiple sorts**: Use `&sort=` multiple times

### Pagination Math:
```
Total Items: 25
Page Size: 10

Total Pages = ceil(25 / 10) = 3 pages
Pages: 0, 1, 2

Page 0: Items 1-10
Page 1: Items 11-20
Page 2: Items 21-25
```

---

## ❗ Common Errors

### Error: 400 Bad Request (POST)
**Cause:** Missing required fields or invalid data
**Fix:** Ensure `name` and `price` are provided, price > 0

### Error: 404 Not Found (GET/DELETE)
**Cause:** Product ID doesn't exist
**Fix:** Verify product exists, check ID value

### Error: Page is empty
**Cause:** Page number too high
**Fix:** Check `totalPages` in response, use valid page number

---

## 📱 Postman Collection

Import into Postman:
1. Create new collection
2. Add requests using the cURL examples above
3. Set `{{baseUrl}}` variable to `http://localhost:8080`
4. Use `{{baseUrl}}/api/products` in your requests

---

## 🔐 Headers (Optional)

Currently no authentication required. For future versions:
```
Authorization: Bearer <token>
Content-Type: application/json
Accept: application/json
```

---

**Last Updated:** 2024
**API Version:** 1.0.0

---

**Need detailed help?** See [SWAGGER_TESTING_GUIDE.md](SWAGGER_TESTING_GUIDE.md)

**Full Documentation:** See [README.md](README.md)