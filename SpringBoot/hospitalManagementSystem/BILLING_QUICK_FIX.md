# 🚀 Billing Permission Error - QUICK FIX

## Problem
"You don't have permission to perform this action" when accessing billing page.

---

## ✅ QUICK FIX (Do These 3 Steps)

### Step 1: Clear Browser Data
1. Press `F12` to open Developer Tools
2. Go to **Application** tab (Chrome) or **Storage** tab (Firefox)
3. Under **Cookies**, delete:
   - `authToken`
   - `authUser`
4. Under **Local Storage**, click **Clear all**
5. Close Developer Tools

### Step 2: Hard Refresh Browser
- **Windows/Linux:** Press `Ctrl + Shift + R` or `Ctrl + F5`
- **Mac:** Press `Cmd + Shift + R`

### Step 3: Login Again
1. Go to login page: http://localhost:3000/login
2. Enter:
   - **Username:** `patient1`
   - **Password:** `password123`
3. Navigate to **My Billing** page

---

## ✅ ALTERNATIVE FIX (If above doesn't work)

### Restart Frontend Application

```bash
# Stop frontend (Ctrl+C in terminal)
cd hospitalManagementSystem/frontend

# Clear Next.js cache
rm -rf .next

# Restart
npm run dev
```

Then:
1. Clear browser cookies (Step 1 above)
2. Login again with `patient1` / `password123`

---

## 🔍 Why This Happens

The browser is caching old authentication data or the JWT token is stale. Clearing cookies and cache forces a fresh login with a new token.

---

## ✅ Test Credentials

Try these accounts (all have access to their billing):

| Username | Password |
|----------|----------|
| patient1 | password123 |
| patient2 | password123 |
| patient3 | password123 |
| admin    | password123 |

---

## 🐛 Still Not Working?

### Check Backend is Running
```bash
curl http://localhost:8081/api/auth/login
```

If you get "Connection refused", start backend:
```bash
cd hospitalManagementSystem/hospital
./mvnw spring-boot:run
```

### Check Frontend Port
Frontend `.env.local` should have:
```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8081
```

### Check Network Tab
1. Press `F12` → **Network** tab
2. Navigate to billing page
3. Look for `/api/billings/patient/X` request
4. Check response:
   - **200 OK** = Working ✅
   - **403 Forbidden** = Token issue (clear cookies)
   - **401 Unauthorized** = Login again
   - **500 Server Error** = Backend issue

---

## ✅ Quick Verification

After logging in, open browser console (`F12` → Console) and run:

```javascript
// Check if user is loaded
const authUserCookie = document.cookie.split('; ').find(row => row.startsWith('authUser='));
if (authUserCookie) {
  const user = JSON.parse(decodeURIComponent(authUserCookie.split('=')[1]));
  console.log('User ID:', user.id);
  console.log('Roles:', user.roles);
}
```

Should show:
- User ID: `2` (or another number)
- Roles: `["ROLE_PATIENT"]`

---

## 🎯 Expected Result

After the fix, you should see:
- ✅ List of billing records
- ✅ Bill amounts and dates
- ✅ Payment status
- ✅ No permission errors

---

**TL;DR:** Clear cookies → Hard refresh → Login again → Should work! 🎉