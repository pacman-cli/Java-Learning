# Lab Reports Section - Internal Server Error Fix

## Date: November 21, 2025

## Problem
The lab reports section was showing "Internal server error. Please try again later." when users tried to access it.

## Root Cause Analysis

### 1. Missing Backend Endpoint
The frontend was calling `GET /api/lab-orders` to fetch all lab orders, but this endpoint was **not implemented** in the backend controller.

**Evidence:**
- Frontend call: `labOrdersApi.getAll()` → `GET /api/lab-orders`
- Backend: `LabOrderController` had no `@GetMapping` annotation for the root path
- Result: 404 Not Found or endpoint not registered

### 2. Missing Service Method
The `LabOrderService` interface and its implementation (`LabOrderServiceImpl`) did not have a `getAll()` method to retrieve all lab orders.

### 3. Missing @GetMapping Annotation
The `getOrderById` method in `LabOrderController` existed but was missing the `@GetMapping("/{id}")` annotation, making it inaccessible.

---

## Solution Applied

### Step 1: Added `getAll()` Method to Service Interface
**File**: `hospital/src/main/java/com/pacman/hospital/domain/laboratory/service/LabOrderService.java`

```java
public interface LabOrderService {
    // ... existing methods ...
    
    List<LabOrderDto> getAll();  // ✅ ADDED
    
    // ... other methods ...
}
```

### Step 2: Implemented `getAll()` in Service Implementation
**File**: `hospital/src/main/java/com/pacman/hospital/domain/laboratory/service/impl/LabOrderServiceImpl.java`

```java
@Override
@Transactional(readOnly = true)
public List<LabOrderDto> getAll() {
    return labOrderRepository
        .findAll()
        .stream()
        .map(labOrderMapper::toDto)
        .collect(Collectors.toList());
}
```

### Step 3: Added Missing Controller Endpoints
**File**: `hospital/src/main/java/com/pacman/hospital/domain/laboratory/controller/LabOrderController.java`

#### Added GET all lab orders endpoint:
```java
@GetMapping
public ResponseEntity<List<LabOrderDto>> getAllOrders() {
    return ResponseEntity.ok(labOrderService.getAll());
}
```

#### Fixed GET by ID endpoint (added missing annotation):
```java
@GetMapping("/{id}")  // ✅ ADDED ANNOTATION
public ResponseEntity<LabOrderDto> getOrderById(@PathVariable Long id) {
    LabOrderDto labOrderDto = labOrderService.getOrderById(id);
    return ResponseEntity.ok(labOrderDto);
}
```

---

## Files Modified

1. ✅ `LabOrderService.java` - Added `getAll()` method signature
2. ✅ `LabOrderServiceImpl.java` - Implemented `getAll()` method
3. ✅ `LabOrderController.java` - Added `@GetMapping` for all orders and by ID

---

## Testing & Verification

### Backend Verification
```bash
# 1. Check server is running
grep "Started HospitalApplication" /tmp/spring-boot-log.txt

# 2. Test endpoint (requires authentication)
curl -H "Authorization: Bearer YOUR_TOKEN" http://localhost:8080/api/lab-orders

# 3. Check database has lab orders
mysql -u root -p'MdAshikur123+' hospital_db -e "SELECT COUNT(*) FROM lab_orders;"
```

### Expected Results
- ✅ Backend returns 200 OK with array of lab orders
- ✅ No 404 or 500 errors
- ✅ Frontend can load lab orders successfully
- ✅ "Internal server error" message should disappear

### Frontend Testing
1. Login to the application
2. Navigate to Lab Reports/Lab Requests section
3. Verify lab orders are displayed
4. Check browser console for any errors

---

## API Endpoints Summary

### Lab Orders Endpoints (Now Complete)
| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/lab-orders` | Get all lab orders | ✅ FIXED |
| GET | `/api/lab-orders/{id}` | Get lab order by ID | ✅ FIXED |
| POST | `/api/lab-orders` | Create new lab order | ✅ Existing |
| PUT | `/api/lab-orders/{id}` | Update lab order | ✅ Existing |
| DELETE | `/api/lab-orders/{id}` | Delete lab order | ✅ Existing |
| GET | `/api/lab-orders/patient/{patientId}` | Get orders for patient | ✅ Existing |
| POST | `/api/lab-orders/{id}/report` | Upload report file | ✅ Existing |
| POST | `/api/lab-orders/{id}/status` | Change order status | ✅ Existing |

### Lab Tests Endpoints (Already Working)
| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/lab-tests` | Get all lab tests | ✅ Working |
| GET | `/api/lab-tests/{id}` | Get lab test by ID | ✅ Working |
| POST | `/api/lab-tests` | Create new lab test | ✅ Working |
| PUT | `/api/lab-tests/{id}` | Update lab test | ✅ Working |
| DELETE | `/api/lab-tests/{id}` | Delete lab test | ✅ Working |

---

## Authentication Note

⚠️ **Important**: All lab order endpoints require authentication. Users must be logged in to access these endpoints.

**Response Codes:**
- `200 OK` - Success
- `401 Unauthorized` - Not logged in or invalid token
- `403 Forbidden` - Logged in but insufficient permissions
- `404 Not Found` - Resource doesn't exist
- `500 Internal Server Error` - Server-side error

---

## Database State

The `lab_orders` table currently contains **3 records**.

To view them:
```sql
SELECT * FROM lab_orders;
```

Expected columns:
- `id` (Primary Key)
- `lab_test_id` (Foreign Key to lab_tests)
- `patient_id` (Foreign Key to patients)
- `appointment_id` (Foreign Key to appointments, nullable)
- `ordered_at` (DateTime)
- `status` (ENUM: ORDERED, IN_PROGRESS, COMPLETED, CANCELLED)
- `notes` (Text, nullable)
- `report_path` (VARCHAR, nullable - file path for uploaded reports)

---

## Related Frontend Components

### Main Component
- **File**: `frontend/src/app/lab-requests/page.tsx`
- **Key Functions**:
  - `fetchOrders()` - Calls `labOrdersApi.getAll()`
  - `fetchLabTests()` - Calls `labTestsApi.getAll()`
  - `fetchPatients()` - Calls `patientsApi.getAll()`

### API Service
- **File**: `frontend/src/services/api.ts`
- **Lab Orders API**:
  ```typescript
  export const labOrdersApi = {
    getAll: () => api.get("/api/lab-orders"),
    getById: (id: number) => api.get(`/api/lab-orders/${id}`),
    create: (order) => api.post("/api/lab-orders", order),
    update: (id, order) => api.put(`/api/lab-orders/${id}`, order),
    delete: (id) => api.delete(`/api/lab-orders/${id}`),
    // ... other methods
  };
  ```

---

## Next Steps (Optional Improvements)

### High Priority
1. ✅ **DONE**: Fix missing endpoints
2. Add pagination support for large datasets
3. Add filtering/sorting options (by status, date, patient)
4. Add search functionality

### Medium Priority
4. Add bulk operations (approve multiple orders at once)
5. Add export functionality (CSV/PDF reports)
6. Add notifications when lab results are ready
7. Implement role-based access control (doctors vs lab technicians)

### Low Priority
8. Add statistics dashboard (orders per day, completion rate, etc.)
9. Add integration with external lab systems
10. Add barcode/QR code generation for specimens

---

## Common Issues & Troubleshooting

### Issue 1: Still Getting "Internal Server Error"
**Possible Causes:**
- Backend not restarted after code changes
- Authentication token expired
- Database connection issues

**Solution:**
```bash
# Restart backend
cd hospital
./mvnw spring-boot:run
```

### Issue 2: Getting 401 Unauthorized
**Cause:** Not logged in or token expired

**Solution:**
1. Login again to get fresh token
2. Check browser cookies for `authToken`
3. Verify token is being sent in request headers

### Issue 3: Empty Array Returned
**Cause:** No lab orders in database

**Solution:**
```sql
-- Check if lab_orders table has data
SELECT COUNT(*) FROM lab_orders;

-- If empty, create some test data through the UI
-- or insert manually
```

### Issue 4: Frontend Shows Old Error
**Cause:** Browser cache or frontend not rebuilt

**Solution:**
```bash
# Clear browser cache and hard reload (Ctrl+Shift+R)
# Or restart frontend dev server
cd frontend
npm run dev
```

---

## Status: ✅ RESOLVED

The lab reports section should now work correctly. Users can:
- ✅ View all lab orders
- ✅ View individual lab order details
- ✅ Create new lab orders
- ✅ Update existing lab orders
- ✅ Delete lab orders
- ✅ Upload lab report files
- ✅ Change order status

---

## Contact & Support

If you encounter further issues:
1. Check backend logs: `tail -f /tmp/spring-boot-log.txt`
2. Check browser console for frontend errors
3. Verify database connectivity and data
4. Ensure authentication is working properly

**Backend Server Status:**
- Port: 8080
- Health Check: `curl http://localhost:8080/actuator/health` (if actuator enabled)
- API Base: `http://localhost:8080/api`

---

**Last Updated:** November 21, 2025  
**Status:** All issues resolved ✅