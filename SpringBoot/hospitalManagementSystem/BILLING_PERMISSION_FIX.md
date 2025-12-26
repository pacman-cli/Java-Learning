# 🔧 Billing Permission Error - Complete Fix Guide

## Problem: "You don't have permission to perform this action" & "Failed to load billing information"

**Date:** November 21, 2025  
**Status:** ✅ Backend Working | ⚠️ Frontend Issue  

---

## Root Cause Analysis

### ✅ Backend is Working Correctly
Testing shows the backend API works fine:
```bash
# Patient can successfully access their billing info
curl -H "Authorization: Bearer <patient_token>" \
  http://localhost:8081/api/billings/patient/1
```

**Response:** Returns billing data successfully ✅

### ❌ Frontend Issue
The frontend is showing permission errors despite the backend working. Possible causes:
1. JWT token not being sent correctly
2. User ID mismatch or undefined
3. API call being made before authentication completes
4. Browser cache storing old authentication data

---

## Quick Fix Steps

### Step 1: Clear Browser Storage & Cache

**Option A: Clear All (Recommended)**
1. Open Developer Tools (`F12` or `Ctrl+Shift+I`)
2. Go to **Application** tab (Chrome/Edge) or **Storage** tab (Firefox)
3. Click **Clear site data** or **Clear storage**
4. Check all options (Cookies, Cache, Local Storage, Session Storage)
5. Click **Clear data**
6. **Hard refresh:** `Ctrl+Shift+R` (Windows/Linux) or `Cmd+Shift+R` (Mac)

**Option B: Clear Specific Items**
1. Open Developer Tools → Application/Storage
2. Clear:
   - **Cookies** → Delete `authToken` and `authUser`
   - **Local Storage** → Clear all
   - **Session Storage** → Clear all
3. Hard refresh

### Step 2: Logout and Login Again

1. Click **Logout** in your application
2. Clear cookies (see Step 1)
3. Login again with:
   - **Username:** `patient1`
   - **Password:** `password123`

### Step 3: Check Network Requests

1. Open Developer Tools (`F12`)
2. Go to **Network** tab
3. Filter by **XHR** or **Fetch**
4. Navigate to the billing page
5. Look for the request to `/api/billings/patient/{id}`
6. Check:
   - ✅ Request URL should be `http://localhost:8081/api/billings/patient/{number}`
   - ✅ Request Headers should include `Authorization: Bearer <token>`
   - ✅ Status should be `200 OK`
   - ❌ If `403` → Token issue
   - ❌ If `401` → Authentication issue
   - ❌ If `500` → Backend error

### Step 4: Restart Frontend Application

```bash
# Stop frontend (Ctrl+C)
cd hospitalManagementSystem/frontend

# Clear Next.js cache
rm -rf .next

# Restart
npm run dev
```

---

## Debugging Checklist

### Check 1: Verify Backend is Running
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"patient1","password":"password123"}'
```

**Expected:** JSON with token and user info  
**If Failed:** Backend not running → Start with `./mvnw spring-boot:run`

### Check 2: Verify Frontend API URL
```bash
cd hospitalManagementSystem/frontend
cat .env.local | grep API_BASE_URL
```

**Expected:** `NEXT_PUBLIC_API_BASE_URL=http://localhost:8081`  
**If Wrong:** Update to port 8081 and restart frontend

### Check 3: Check User ID in Frontend
Open browser console and run:
```javascript
// Check stored user
console.log(document.cookie);
// Should show authToken and authUser cookies

// Parse user cookie
const authUserCookie = document.cookie
  .split('; ')
  .find(row => row.startsWith('authUser='));
if (authUserCookie) {
  const user = JSON.parse(decodeURIComponent(authUserCookie.split('=')[1]));
  console.log('User ID:', user.id);
  console.log('User roles:', user.roles);
}
```

**Expected:**
- User ID should be a number (e.g., 2, 3, 4)
- Roles should include `ROLE_PATIENT`

### Check 4: Test Direct API Call from Browser Console
```javascript
// Get token from cookie
const token = document.cookie
  .split('; ')
  .find(row => row.startsWith('authToken='))
  ?.split('=')[1];

// Get user ID
const authUserCookie = document.cookie
  .split('; ')
  .find(row => row.startsWith('authUser='));
const user = JSON.parse(decodeURIComponent(authUserCookie.split('=')[1]));

// Test API call
fetch(`http://localhost:8081/api/billings/patient/${user.id}`, {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
})
.then(r => r.json())
.then(d => console.log('Billing data:', d))
.catch(e => console.error('Error:', e));
```

**Expected:** Array of billing records  
**If 403:** Token or role issue  
**If 401:** Token expired  
**If 404:** Wrong patient ID  

---

## Common Issues & Solutions

### Issue 1: "403 Forbidden" Error

**Cause:** Role mismatch or token doesn't contain proper roles

**Solution:**
```bash
# Re-login to get fresh token
1. Logout from application
2. Clear cookies
3. Login again
```

**Verify roles in token:**
```javascript
// Decode JWT token (paste in browser console)
const token = 'YOUR_TOKEN_HERE';
const base64Url = token.split('.')[1];
const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
}).join(''));

console.log(JSON.parse(jsonPayload));
```

Should show `authorities: ["ROLE_PATIENT"]`

### Issue 2: "401 Unauthorized" Error

**Cause:** Token expired or invalid

**Solution:**
1. Logout and login again
2. Check token expiration in cookies
3. Backend might have restarted (tokens invalidated)

### Issue 3: User ID is Undefined

**Cause:** Authentication state not properly loaded

**Solution:**
```typescript
// Add loading check in component
if (!user?.id) {
  return <div>Loading user information...</div>;
}

// Only fetch billings after user is loaded
useEffect(() => {
  if (user?.id) {
    fetchBillings();
  }
}, [user?.id]);
```

### Issue 4: CORS Error

**Cause:** Frontend and backend on different ports

**Solution:** Already configured in backend ✅
- CORS allows all origins
- Credentials allowed
- All methods allowed

If still seeing CORS:
1. Check backend logs for CORS errors
2. Verify CORS configuration in SecurityConfig
3. Restart backend

### Issue 5: Network Error (Connection Refused)

**Cause:** Backend not running or wrong port

**Solution:**
```bash
# Check if backend is running
curl http://localhost:8081/api/auth/login

# If failed, start backend
cd hospitalManagementSystem/hospital
./mvnw spring-boot:run
```

---

## Testing After Fix

### Test 1: Login as Patient
```
URL: http://localhost:3000/login
Username: patient1
Password: password123
```

### Test 2: Navigate to Billing
After login, go to: http://localhost:3000/my-billing

**Expected:** List of billing records  
**Not:** Permission error

### Test 3: Check Different Patients
Try logging in as:
- `patient2` / `password123`
- `patient3` / `password123`
- `patient4` / `password123`
- `patient5` / `password123`

Each should see their own billing records.

### Test 4: Check Admin Access
```
Username: admin
Password: password123
```

Admin should be able to access `/api/billings` (all billings)

---

## Manual Database Verification

If you want to verify the patient ID and billing records:

```sql
-- Connect to MySQL
mysql -u root -p hospital_db

-- Check users and their IDs
SELECT u.id, u.username, u.full_name, r.name as role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
WHERE r.name = 'ROLE_PATIENT';

-- Check billing records for a patient (e.g., patient ID 2)
SELECT * FROM billings WHERE patient_id = 2;

-- Check if patient ID 2 has user account
SELECT * FROM users WHERE id = 2;
```

---

## Code Fix (If Issue Persists)

If the issue persists after all above steps, add better error handling in the frontend component:

**File:** `frontend/src/app/my-billing/page.tsx`

```typescript
const fetchBillings = async () => {
  if (!user?.id) {
    console.error('User ID is undefined');
    toast.error('User not properly authenticated. Please login again.');
    return;
  }

  try {
    setLoading(true);
    console.log('Fetching billings for patient ID:', user.id);
    
    const data = await billingApi.getByPatient(user.id);
    console.log('Billings fetched successfully:', data);
    
    setBillings(data);
  } catch (error: any) {
    console.error('Error fetching billings:', error);
    
    if (error.response?.status === 403) {
      toast.error('You do not have permission to view billing information');
    } else if (error.response?.status === 401) {
      toast.error('Your session has expired. Please login again.');
      // Redirect to login
      router.push('/login');
    } else {
      toast.error('Failed to load billing information');
    }
  } finally {
    setLoading(false);
  }
};
```

---

## Prevention Tips

1. **Always use HTTPS in production** (avoids token interception)
2. **Set proper token expiration** (currently 24 hours)
3. **Implement token refresh** (auto-refresh before expiration)
4. **Handle 401 errors globally** (auto-redirect to login)
5. **Clear auth data on logout** (prevent stale tokens)

---

## Summary of Test Accounts

All accounts have access to their respective billing data:

| Username | Password | Patient ID | Has Billing Access |
|----------|----------|------------|-------------------|
| patient1 | password123 | 1 | ✅ Yes |
| patient2 | password123 | 2 | ✅ Yes |
| patient3 | password123 | 3 | ✅ Yes |
| patient4 | password123 | 4 | ✅ Yes |
| patient5 | password123 | 5 | ✅ Yes |
| admin | password123 | - | ✅ All billings |

---

## Quick Command Reference

```bash
# Backend status
curl http://localhost:8081/api/auth/login

# Frontend environment
cat frontend/.env.local | grep API

# Clear Next.js cache
cd frontend && rm -rf .next && npm run dev

# Restart backend
cd hospital && ./mvnw spring-boot:run

# Test patient login
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"patient1","password":"password123"}'

# Test billing access (replace TOKEN)
curl -H "Authorization: Bearer TOKEN" \
  http://localhost:8081/api/billings/patient/1
```

---

**Status:** ✅ Backend Working | ⚠️ Frontend Fix Required  
**Action:** Clear browser cache, logout/login, restart frontend  
**Expected Result:** Billing page loads successfully without errors  

🎉 **After following these steps, your billing page should work perfectly!**