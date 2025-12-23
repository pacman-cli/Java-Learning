# 🚀 Quick Test Guide - Start Here!

## ⚡ 3-Minute Setup

### Step 1: Start Backend (Terminal 1)
```bash
cd hospital
./mvnw spring-boot:run
```
Wait for: `Started HospitalApplication`

### Step 2: Start Frontend (Terminal 2)
```bash
cd frontend
npm run dev
```
Wait for: `✓ Ready in X ms`

### Step 3: Open Browser
Go to: `http://localhost:3000/login`

---

## 👥 Test Accounts

### Test Admin Features 🛡️
- Username: `admin`
- Password: `admin123`
- **What to see**: Blue dashboard, system management features

### Test Doctor Features 🩺
- Username: `doctor`  
- Password: `doctor123`
- **What to see**: Teal dashboard, patient appointments

### Test Patient Features 👤
- Username: `patient`
- Password: `patient123`
- **What to see**: Purple dashboard, personal health info

---

## ✅ Quick Verification

After logging in with each account, verify:

1. **Color Changes** - Admin=Blue, Doctor=Teal, Patient=Purple
2. **Different Stats** - Each role shows different numbers
3. **Unique Actions** - Different quick action buttons
4. **Sidebar Menu** - Each role has different navigation items
5. **Theme Toggle** - Click 🌙/☀️ button (top-right)

---

## 🎉 Success Indicators

You'll know it's working when you see:

✅ Backend console shows "Started HospitalApplication"
✅ Frontend console shows "✓ Ready"
✅ Login page displays with "Use" buttons for demo accounts
✅ After login, dashboard matches the user role
✅ Different colors for different roles
✅ All cards and buttons display properly

---

## 🐛 If Something Goes Wrong

**Backend won't start:**
- Check if MySQL is running
- Check port 8080 is free

**Frontend won't start:**
- Run `npm install` first
- Check port 3000 is free

**Login doesn't work:**
- Make sure backend fully started
- Check browser console (F12) for errors

---

## 📚 More Information

- **Full Documentation**: See `ROLE_BASED_DASHBOARDS.md`
- **Testing Guide**: See `QUICK_START_DASHBOARDS.md`  
- **Visual Guide**: See `DASHBOARD_VISUAL_GUIDE.md`

---

**That's it! You're ready to explore the new dashboards! 🎊**
