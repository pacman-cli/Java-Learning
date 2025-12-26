# 🧪 Quick Test Guide: Patient Appointments

## 🎯 Objective
Test all fixes for the Patient Portal "My Appointments" section and button functionality.

---

## ⚡ Quick Start

### 1. Start Backend
```bash
cd hospitalManagementSystem/hospital
./mvnw spring-boot:run
```

Wait for: `Started HospitalApplication in X seconds`

### 2. Start Frontend
```bash
cd hospitalManagementSystem/frontend
npm run dev
```

Visit: `http://localhost:3000`

---

## 🧪 Test Cases

### Test 1: Login as Patient ✅

**Steps**:
1. Go to `http://localhost:3000/login`
2. Login with patient credentials:
   - Username: `patient1` (or your patient username)
   - Password: `password123` (or your password)
3. Click "Login"

**Expected Result**:
- ✅ Redirected to patient dashboard
- ✅ See "Welcome, [Patient Name]"
- ✅ Sidebar shows patient menu items

---

### Test 2: View My Appointments (Main Fix) ✅

**Steps**:
1. Click "My Appointments" in sidebar
2. Wait for page to load

**Expected Result**:
- ✅ Page loads without "Internal server error"
- ✅ See "My Appointments" heading
- ✅ See appointment cards (if any exist)
- ✅ Each card shows:
  - Doctor name (not just "Dr. undefined")
  - Appointment date and time
  - Status badge (Scheduled/Confirmed/Completed/Cancelled)
  - Reason for visit
  - Type (In Person/Video/Phone) - if set
  - Duration - if set

**If no appointments exist**:
- ✅ See "No upcoming appointments"
- ✅ See "Book your first appointment" link

---

### Test 3: Book New Appointment ✅

**Steps**:
1. Click "Book Appointment" button (top right)
2. Modal opens
3. Fill in form:
   - Doctor ID: `1` (or any valid doctor ID)
   - Date & Time: Select a future date/time
   - Appointment Type: Select "In Person"
   - Reason: `Regular checkup`
   - Notes: `First time visit` (optional)
4. Click "Book Appointment"

**Expected Result**:
- ✅ Success message: "Appointment scheduled successfully!"
- ✅ Modal closes
- ✅ New appointment appears in list
- ✅ Appointment shows all entered details

---

### Test 4: View Appointment Details ✅

**Steps**:
1. Find any appointment card
2. Click "View Details" button
3. Modal opens

**Expected Result**:
- ✅ Modal shows:
  - Status badge
  - Doctor name
  - Date & Time (formatted nicely)
  - Type
  - Reason
  - Notes (if any)
  - Duration (if set)
- ✅ "Close" button works

---

### Test 5: Cancel Appointment (Main Button Fix) ✅

**Steps**:
1. Find a SCHEDULED or CONFIRMED appointment
2. Click "Cancel" button
3. Browser confirms: "Are you sure you want to cancel this appointment?"
4. Click "OK"

**Expected Result**:
- ✅ Success message: "Appointment cancelled successfully!"
- ✅ Appointment status changes to "CANCELLED"
- ✅ Appointment **stays in the list** (not deleted)
- ✅ Cancel button disappears (can't cancel twice)
- ✅ Status badge turns red

**Important**: Appointment should be cancelled (status = CANCELLED), NOT deleted from database!

---

### Test 6: Search Appointments ✅

**Steps**:
1. Type in search box: doctor name or reason
2. Observe results

**Expected Result**:
- ✅ List filters in real-time
- ✅ Only matching appointments shown

---

### Test 7: Filter by Status ✅

**Steps**:
1. Open "All Status" dropdown
2. Select "Scheduled"
3. Observe results

**Expected Result**:
- ✅ Only SCHEDULED appointments shown
- ✅ Try other filters (Confirmed, Completed, Cancelled, All)
- ✅ Each filter works correctly

---

### Test 8: Upcoming vs Past Appointments ✅

**Expected Result**:
- ✅ "Upcoming Appointments" section shows future appointments
- ✅ "Past Appointments" section shows completed or past-date appointments
- ✅ Count is displayed correctly: "Upcoming Appointments (3)"

---

## 🔍 Backend Verification

### Check Appointment Status in Database

```sql
-- Connect to MySQL
mysql -u root -p

USE hospital_management_system;

-- View all appointments
SELECT id, patient_id, doctor_id, appointment_datetime, status, reason
FROM appointments
ORDER BY appointment_datetime DESC
LIMIT 10;

-- Check cancelled appointment (should exist, not deleted)
SELECT * FROM appointments WHERE status = 'CANCELLED';

-- Verify patient/doctor data loaded
SELECT 
    a.id,
    a.status,
    a.reason,
    p.full_name as patient_name,
    d.full_name as doctor_name
FROM appointments a
LEFT JOIN patients p ON a.patient_id = p.id
LEFT JOIN doctors d ON a.doctor_id = d.id
WHERE a.patient_id = 1
ORDER BY a.appointment_datetime DESC;
```

---

### Check Backend Logs

```bash
# Terminal where backend is running
# Look for:

# ✅ GOOD - No errors
2025-11-21 01:40:15.123  INFO ... : Fetching appointments for patient: 1
2025-11-21 01:40:15.456  INFO ... : Found 5 appointments

# ❌ BAD - LazyInitializationException (if still present)
org.hibernate.LazyInitializationException: failed to lazily initialize...

# ❌ BAD - AccessDeniedException (permission issue)
org.springframework.security.access.AccessDeniedException: Patients can only access their own appointments
```

---

## 🌐 API Testing (Optional)

### Test with cURL or Postman

**1. Get JWT Token** (login first in browser, check cookies)

**2. Test Get Appointments**:
```bash
curl -X GET http://localhost:8080/api/appointments/patient/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

**Expected Response**:
```json
[
  {
    "id": 1,
    "patientId": 1,
    "patientName": "John Doe",
    "doctorId": 2,
    "doctorName": "Dr. Sarah Smith",
    "appointmentDateTime": "2025-11-25T10:00:00",
    "status": "SCHEDULED",
    "reason": "Regular checkup",
    "notes": "First time visit",
    "type": "IN_PERSON",
    "duration": 30,
    "location": "Room 101"
  }
]
```

**3. Test Cancel Appointment**:
```bash
curl -X PATCH http://localhost:8080/api/appointments/1/cancel \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

**Expected Response**:
```json
{
  "id": 1,
  "status": "CANCELLED",
  "cancelledAt": "2025-11-21T01:45:00",
  ...
}
```

---

## ⚠️ Common Issues & Solutions

### Issue 1: "Internal server error" still appears

**Solution**:
1. Restart backend (may need to clear cache)
2. Check if JOIN FETCH queries are in repository
3. Check backend logs for LazyInitializationException
4. Verify Flyway migrations ran successfully

---

### Issue 2: "You don't have permission"

**Solution**:
1. Verify you're logged in as PATIENT role
2. Check JWT token in browser cookies (should exist)
3. Verify you're accessing YOUR OWN patient ID
4. Check SecurityConfig allows PATIENT access to /api/appointments/**

**Browser Console**:
```javascript
// Check user
const user = JSON.parse(localStorage.getItem('authUser'));
console.log('User ID:', user.id);
console.log('Roles:', user.roles);

// Check cookies
console.log('Token:', document.cookie);
```

---

### Issue 3: Doctor name shows as "Dr. undefined"

**Solution**:
1. Verify doctor exists in database
2. Check AppointmentMapper sets doctorName
3. Verify JOIN FETCH loads doctor entity
4. Check backend logs for mapping errors

---

### Issue 4: Cancel deletes instead of cancelling

**Solution**:
1. Verify frontend calls `appointmentsApi.cancel(id)`
2. Check it's NOT calling `appointmentsApi.delete(id)`
3. Verify backend has `/api/appointments/{id}/cancel` endpoint
4. Check database - appointment should have status='CANCELLED', not be deleted

---

### Issue 5: Type, Duration, Location not showing

**Solution**:
1. These fields might be NULL in database
2. When booking, make sure to fill them
3. Verify AppointmentDto has these fields
4. Verify AppointmentMapper maps these fields

---

## ✅ Success Criteria

All tests pass if:

- [x] No "Internal server error" in My Appointments
- [x] Appointments load and display correctly
- [x] Doctor names and patient names shown (not IDs)
- [x] Can book new appointment
- [x] Can view appointment details
- [x] Cancel button changes status to CANCELLED (doesn't delete)
- [x] Search and filter work
- [x] Upcoming/Past sections work
- [x] All buttons are functional
- [x] UI is responsive and looks good
- [x] Dark mode works (toggle in top right)

---

## 🎉 Final Verification

**Open browser console** (F12) and run:

```javascript
// Should NOT see any errors like:
// ❌ Failed to load appointments
// ❌ 500 Internal Server Error
// ❌ LazyInitializationException
// ❌ Cannot read property 'fullName' of null

// Should see:
// ✅ Appointments loaded successfully
// ✅ All data populated correctly
```

---

## 📊 Test Results Template

```
Date: ___________
Tester: ___________

Test 1 - Login as Patient:           [ ] Pass  [ ] Fail
Test 2 - View Appointments:           [ ] Pass  [ ] Fail
Test 3 - Book Appointment:            [ ] Pass  [ ] Fail
Test 4 - View Details:                [ ] Pass  [ ] Fail
Test 5 - Cancel Appointment:          [ ] Pass  [ ] Fail
Test 6 - Search:                      [ ] Pass  [ ] Fail
Test 7 - Filter:                      [ ] Pass  [ ] Fail
Test 8 - Upcoming/Past Split:         [ ] Pass  [ ] Fail

Overall: [ ] ALL PASS  [ ] SOME FAIL

Notes:
_______________________________________
_______________________________________
```

---

## 🚀 Ready for Production?

Before deploying:

- [ ] All test cases pass
- [ ] No console errors
- [ ] No backend errors in logs
- [ ] Database queries optimized (JOIN FETCH)
- [ ] Security checks in place
- [ ] Error handling works
- [ ] UI/UX is polished
- [ ] Mobile responsive
- [ ] Dark mode works
- [ ] Performance acceptable (page loads < 2 seconds)

---

**Happy Testing! 🎊**

If all tests pass, the Patient Appointments section is **FIXED** and **READY TO USE**! 🎉