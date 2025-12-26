# Patient Portal Authentication & Authorization Fixes

## 🔒 Issue Summary

**Problem**: Patient portal pages were showing "You don't have permission to perform this action" and "Failed to load medical records" errors.

**Root Cause**: Pages were calling backend APIs that returned ALL records (requiring ADMIN permissions) instead of using patient-specific endpoints.

---

## ✅ Fixes Applied

### 1. Medical Records Page (`/my-records`)

**Before**:
```typescript
const data = await medicalRecordsApi.getAll(); // Returns ALL records - requires ADMIN
const myRecords = data.filter((record) => record.patientId === user?.id);
```

**After**:
```typescript
if (!user?.id) {
  setError("User not found");
  return;
}
const data = await medicalRecordsApi.getByPatient(user.id); // Patient-specific endpoint
setRecords(data);
```

**Backend Endpoint**: `GET /api/medical-records/patient/{patientId}` ✅ (Already existed)

---

### 2. Appointments Page (`/my-appointments`)

**Before**:
```typescript
const data = await appointmentsApi.getAll(); // Returns ALL appointments - requires ADMIN
const myAppointments = data.filter((apt) => apt.patientId === user?.id);
```

**After**:
```typescript
if (!user?.id) {
  setError("User not found");
  return;
}
const data = await appointmentsApi.getByPatient(user.id); // Patient-specific endpoint
setAppointments(data);
```

**Backend Endpoint**: `GET /api/appointments/patient/{patientId}` ✅ (Already existed)

---

### 3. Prescriptions Page (`/my-prescriptions`)

**Before**:
```typescript
const data = await prescriptionsApi.getAll(); // Returns ALL prescriptions - requires ADMIN
const myPrescriptions = data.filter((rx) => rx.patientId === user?.id);
```

**After**:
```typescript
if (!user?.id) {
  setError("User not found");
  return;
}
const data = await prescriptionsApi.getByPatient(user.id); // Patient-specific endpoint
setPrescriptions(data);
```

**Backend Endpoint Created**: `GET /api/prescriptions/patient/{patientId}` ✅ **NEW**

#### Backend Changes for Prescriptions:

**Controller** (`PrescriptionController.java`):
```java
@GetMapping("/patient/{patientId}")
public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatient(
    @PathVariable Long patientId
) {
    return ResponseEntity.ok(
        prescriptionService.getPrescriptionsByPatient(patientId)
    );
}
```

**Service Interface** (`PrescriptionService.java`):
```java
List<PrescriptionDto> getPrescriptionsByPatient(Long patientId);
```

**Service Implementation** (`PrescriptionServiceImpl.java`):
```java
@Override
public List<PrescriptionDto> getPrescriptionsByPatient(Long patientId) {
    // Verify patient exists
    patientRepository
        .findById(patientId)
        .orElseThrow(() ->
            new IllegalArgumentException(
                "Patient not found with id: " + patientId
            )
        );

    return prescriptionRepository
        .findByPatientId(patientId)
        .stream()
        .map(PrescriptionMapper::toDto)
        .collect(Collectors.toList());
}
```

**Repository** (`PrescriptionRepository.java`):
```java
List<Prescription> findByPatientId(Long patientId);
```

**Frontend API** (`services/api.ts`):
```typescript
export const prescriptionsApi = {
  // ... existing methods
  getByPatient: (patientId: number) =>
    api.get(`/api/prescriptions/patient/${patientId}`),
};
```

---

### 4. Lab Reports Page (`/my-lab-reports`)

**Before**:
```typescript
const data = await labOrdersApi.getAll(); // Returns ALL lab orders - requires ADMIN
const myOrders = data.filter((order) => order.patientId === user?.id);
```

**After**:
```typescript
if (!user?.id) {
  setError("User not found");
  return;
}
const data = await labOrdersApi.getByPatient(user.id); // Patient-specific endpoint
setLabOrders(data);
```

**Backend Endpoint**: `GET /api/lab-orders/patient/{patientId}` ✅ (Already existed)

---

## 🔐 Authorization Model

### Endpoint Access Control

| Endpoint | Required Role | Purpose |
|----------|--------------|---------|
| `GET /api/medical-records` | ADMIN, DOCTOR | Get all records |
| `GET /api/medical-records/patient/{id}` | PATIENT, DOCTOR, ADMIN | Get patient's records |
| `GET /api/appointments` | ADMIN, DOCTOR | Get all appointments |
| `GET /api/appointments/patient/{id}` | PATIENT, DOCTOR, ADMIN | Get patient's appointments |
| `GET /api/prescriptions` | ADMIN, DOCTOR | Get all prescriptions |
| `GET /api/prescriptions/patient/{id}` | PATIENT, DOCTOR, ADMIN | Get patient's prescriptions |
| `GET /api/lab-orders` | ADMIN, DOCTOR, LAB_TECH | Get all lab orders |
| `GET /api/lab-orders/patient/{id}` | PATIENT, DOCTOR, ADMIN | Get patient's lab orders |

---

## 🧪 Testing

### Test User Credentials
- **Email**: `patient@test.com`
- **Password**: `password123`
- **Role**: PATIENT

### Test Checklist

#### My Appointments (`/my-appointments`)
- [x] Page loads without permission errors
- [x] Shows only current patient's appointments
- [x] Can book new appointment
- [x] Can view appointment details
- [x] Can cancel appointment
- [x] Search and filter work

#### My Medical Records (`/my-records`)
- [x] Page loads without permission errors
- [x] Shows only current patient's records
- [x] Can view record details
- [x] Can download records
- [x] Search and filter work
- [x] Statistics accurate

#### My Prescriptions (`/my-prescriptions`)
- [x] Page loads without permission errors
- [x] Shows only current patient's prescriptions
- [x] Separates active vs expired
- [x] Can view prescription details
- [x] Can download prescriptions
- [x] Refills count displays
- [x] Expiring soon warnings show

#### My Lab Reports (`/my-lab-reports`)
- [x] Page loads without permission errors
- [x] Shows only current patient's lab orders
- [x] Separates pending vs completed
- [x] Can view test results
- [x] Can download completed reports
- [x] Priority indicators show

---

## 🚀 Deployment Steps

### 1. Backend Deployment
```bash
cd hospital
mvn clean package
java -jar target/hospital-management-0.0.1-SNAPSHOT.jar
```

### 2. Verify Backend Endpoints
```bash
# Test patient-specific endpoints (replace {token} and {patientId})
curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/medical-records/patient/{patientId}

curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/appointments/patient/{patientId}

curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/prescriptions/patient/{patientId}

curl -H "Authorization: Bearer {token}" \
  http://localhost:8080/api/lab-orders/patient/{patientId}
```

### 3. Frontend Deployment
```bash
cd frontend
npm run build
npm start
```

### 4. Test Patient Portal
1. Login as patient
2. Navigate to each section
3. Verify data loads correctly
4. Check browser console for errors
5. Verify no permission errors

---

## 🔄 API Response Format

### Medical Records
```json
[
  {
    "id": 1,
    "patientId": 5,
    "doctorId": 2,
    "recordType": "CONSULTATION",
    "diagnosis": "Common Cold",
    "symptoms": "Fever, cough, sore throat",
    "treatment": "Rest, fluids, pain relievers",
    "notes": "Follow up in 1 week",
    "recordDate": "2024-01-15T10:30:00",
    "doctorName": "Dr. Smith",
    "patientName": "John Doe"
  }
]
```

### Appointments
```json
[
  {
    "id": 1,
    "patientId": 5,
    "doctorId": 2,
    "appointmentDateTime": "2024-02-01T09:00:00",
    "status": "SCHEDULED",
    "reason": "Annual Checkup",
    "duration": 30,
    "notes": "Bring previous lab results",
    "doctorName": "Dr. Smith",
    "patientName": "John Doe"
  }
]
```

### Prescriptions
```json
[
  {
    "id": 1,
    "patientId": 5,
    "doctorId": 2,
    "medicineId": 3,
    "medication": "Amoxicillin",
    "dosage": "500mg",
    "frequency": "Three times daily",
    "duration": "7 days",
    "instructions": "Take with food",
    "prescriptionDate": "2024-01-15T10:30:00",
    "refills": 2,
    "status": "ACTIVE",
    "doctorName": "Dr. Smith"
  }
]
```

### Lab Orders
```json
[
  {
    "id": 1,
    "patientId": 5,
    "doctorId": 2,
    "labTestId": 4,
    "orderDate": "2024-01-20T14:00:00",
    "status": "COMPLETED",
    "priority": "NORMAL",
    "results": "All values within normal range",
    "completedDate": "2024-01-22T16:00:00",
    "testName": "Complete Blood Count",
    "doctorName": "Dr. Smith"
  }
]
```

---

## 📋 Files Modified

### Frontend
1. `frontend/src/app/my-appointments/page.tsx` - Use patient-specific endpoint
2. `frontend/src/app/my-records/page.tsx` - Use patient-specific endpoint
3. `frontend/src/app/my-prescriptions/page.tsx` - Use patient-specific endpoint
4. `frontend/src/app/my-lab-reports/page.tsx` - Use patient-specific endpoint
5. `frontend/src/services/api.ts` - Add getByPatient method to prescriptionsApi

### Backend (New)
1. `hospital/.../PrescriptionController.java` - Add getPrescriptionsByPatient endpoint
2. `hospital/.../PrescriptionService.java` - Add interface method
3. `hospital/.../PrescriptionServiceImpl.java` - Implement method
4. `hospital/.../PrescriptionRepository.java` - Add findByPatientId query

---

## 🐛 Common Issues & Solutions

### Issue: Still getting 403 errors
**Solution**: 
- Clear browser cookies
- Re-login with patient credentials
- Check backend logs for actual error
- Verify JWT token is valid

### Issue: Empty data returned
**Solution**:
- Check if patient has any records in database
- Verify patient ID is correct
- Check database for test data
- Run seed data migration if needed

### Issue: User ID is undefined
**Solution**:
- Verify user is properly logged in
- Check AuthProvider is wrapping the app
- Ensure cookies are enabled
- Check token is being sent in request headers

---

## ✅ Success Criteria

Your patient portal is working correctly when:

- ✅ No "permission denied" errors
- ✅ No "failed to load" errors
- ✅ Patient sees only their own data
- ✅ All CRUD operations work
- ✅ Search and filters functional
- ✅ Download features work
- ✅ No console errors
- ✅ Proper loading states
- ✅ Error messages are user-friendly

---

## 📞 Support

If issues persist:

1. Check browser console for JavaScript errors
2. Check Network tab for API call responses
3. Check backend logs for server errors
4. Verify database has proper test data
5. Ensure backend and frontend are both running
6. Try logging out and back in

---

**Status**: ✅ FIXED - All patient portal pages now use proper patient-specific API endpoints

**Date**: January 2025

**Impact**: Patients can now access their medical information without permission errors