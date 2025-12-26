# Quick Start Guide - Role-Based Dashboards

## 🚀 Getting Started

### Prerequisites
- Node.js v18+ installed
- MySQL server running
- Backend API running on port 8080

### Step 1: Start the Backend

```bash
# Navigate to hospital directory
cd hospital

# Start Spring Boot backend
./mvnw spring-boot:run
```

Wait for the message: `Started HospitalApplication in X seconds`

### Step 2: Start the Frontend

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies (if not already done)
npm install

# Start development server
npm run dev
```

Frontend will be available at: `http://localhost:3000`

---

## 👥 Demo User Accounts

### Admin Account
- **Username:** `admin`
- **Password:** `admin123`
- **Access:** Full system management
- **Dashboard:** Blue-themed admin dashboard

### Doctor Account
- **Username:** `doctor`
- **Password:** `doctor123`
- **Access:** Patient care and appointments
- **Dashboard:** Teal-themed doctor dashboard

### Patient Account
- **Username:** `patient`
- **Password:** `patient123`
- **Access:** Personal health records
- **Dashboard:** Purple-themed patient dashboard

---

## 🎯 Testing Each Dashboard

### Test Admin Dashboard

1. Navigate to `http://localhost:3000/login`
2. Click "Use" button for Admin credentials
3. Click "Sign in"
4. You should see:
   - Blue gradient header with "Admin Dashboard"
   - 8 statistics cards
   - 8 quick action buttons
   - Department performance metrics
   - Recent activity feed
   - System status panel

**Key Features to Test:**
- View total patients, doctors, staff
- Check monthly revenue statistics
- Explore quick actions (all 8 buttons)
- Review department performance
- Monitor system activity
- Check system status indicators

### Test Doctor Dashboard

1. Log out from admin account
2. Return to login page
3. Click "Use" button for Doctor credentials
4. Click "Sign in"
5. You should see:
   - Teal gradient header with "Doctor Dashboard"
   - 4 statistics cards
   - 6 quick action buttons
   - Today's appointments list
   - Recent patients sidebar
   - Upcoming tasks list
   - Monthly performance metrics

**Key Features to Test:**
- View today's appointments (5 mock appointments)
- Check appointment tabs (Today, Upcoming, Past)
- Explore quick actions (prescriptions, lab requests, notes)
- Review recent patients with priority indicators
- Check upcoming tasks
- View performance metrics

### Test Patient Dashboard

1. Log out from doctor account
2. Return to login page
3. Click "Use" button for Patient credentials
4. Click "Sign in"
5. You should see:
   - Purple gradient header with "My Health Dashboard"
   - 4 health statistics cards
   - 6 quick action buttons
   - Appointments section (Upcoming/Past tabs)
   - Health metrics sidebar
   - Active prescriptions
   - Recent medical records

**Key Features to Test:**
- View upcoming appointments (2 mock)
- Check appointment details (doctor, time, location)
- Review active prescriptions (3 active meds)
- Check health metrics (BP, heart rate, etc.)
- View medical records
- Test appointment action buttons

---

## 🎨 Dashboard Features by Role

### Admin Dashboard Features
✅ Complete system overview
✅ User and staff management
✅ Department performance tracking
✅ Financial metrics and revenue
✅ Real-time activity monitoring
✅ System health status
✅ Audit logs access
✅ Database management

### Doctor Dashboard Features
✅ Appointment management (Today/Upcoming/Past)
✅ Patient consultation tracking
✅ Medical notes and prescriptions
✅ Lab request management
✅ Patient priority system (High/Medium/Low)
✅ Task management
✅ Performance metrics
✅ Video call integration (UI ready)

### Patient Dashboard Features
✅ Personal appointment booking
✅ Medical records access
✅ Active prescriptions tracking
✅ Health metrics monitoring
✅ Lab reports viewing
✅ Billing and payments
✅ Emergency contact information
✅ Appointment rescheduling/cancellation

---

## 🔄 Quick Navigation Guide

### Common Navigation Items
All roles have access to:
- **Dashboard** - Main dashboard (role-specific)
- **Settings** - User preferences

### Admin-Only Navigation
- Users
- Doctors
- Patients
- Appointments
- Departments
- Billing
- Reports

### Doctor-Only Navigation
- My Patients
- Appointments
- Medical Records
- Prescriptions
- Lab Requests
- My Schedule

### Patient-Only Navigation
- My Appointments
- Medical Records
- Prescriptions
- Lab Reports
- Billing
- Health Tracker

---

## 🌓 Theme Switching

Both Light and Dark modes are supported:

1. Look for the theme toggle button (🌙/☀️) in the top-right corner
2. Click to switch between themes
3. Theme preference is saved automatically
4. All dashboards support both themes

---

## 📱 Mobile Testing

### Test Responsive Design

1. Open browser developer tools (F12)
2. Toggle device toolbar (Ctrl+Shift+M)
3. Select different device sizes:
   - iPhone 12/13 (390x844)
   - iPad (768x1024)
   - Desktop (1920x1080)

**Mobile Features:**
- Hamburger menu (≡) for navigation
- Collapsible sidebar with backdrop
- Stacked card layouts
- Touch-friendly buttons
- Responsive grid system

---

## 🐛 Troubleshooting

### Issue: "Cannot connect to backend"
**Solution:** Ensure backend is running on port 8080
```bash
cd hospital
./mvnw spring-boot:run
```

### Issue: "Login credentials not working"
**Solution:** Backend must be fully started with demo users seeded
- Wait for backend startup completion
- Check console for "Started HospitalApplication"

### Issue: "Page shows blank/white screen"
**Solution:** 
1. Check browser console for errors (F12)
2. Clear browser cache and cookies
3. Restart frontend: `npm run dev`

### Issue: "Theme not switching"
**Solution:**
1. Clear localStorage: `localStorage.clear()`
2. Refresh page
3. Try theme toggle again

### Issue: "Sidebar not showing on mobile"
**Solution:**
1. Click hamburger menu (≡) in top-left
2. Ensure screen width < 1024px for mobile view

---

## ✅ Feature Checklist

Use this checklist to verify all features:

### Authentication
- [ ] Login with admin credentials
- [ ] Login with doctor credentials
- [ ] Login with patient credentials
- [ ] Logout functionality
- [ ] Session persistence

### Admin Dashboard
- [ ] Statistics cards load
- [ ] Quick actions visible
- [ ] Department performance displays
- [ ] Activity feed updates
- [ ] System status shows

### Doctor Dashboard
- [ ] Appointments list loads
- [ ] Tab switching works (Today/Upcoming/Past)
- [ ] Patient cards display
- [ ] Quick actions visible
- [ ] Tasks list shows
- [ ] Performance metrics calculate

### Patient Dashboard
- [ ] Appointments display correctly
- [ ] Health metrics visible
- [ ] Prescriptions list loads
- [ ] Medical records accessible
- [ ] Emergency contacts show

### Common Features
- [ ] Theme toggle works (light/dark)
- [ ] Sidebar navigation functional
- [ ] Search bar visible
- [ ] Notifications bell shows
- [ ] Mobile responsive
- [ ] User profile displays

---

## 🎓 Understanding the Mock Data

All dashboards currently use **mock data** for demonstration:

### Mock Statistics
- Patient counts: 1247 total, 856 active
- Doctor counts: 45 total, 38 active
- Appointments: Varies by dashboard
- Revenue: $125,000 monthly (admin only)

### Mock Appointments
- **Admin:** 4 sample appointments
- **Doctor:** 5 today's appointments
- **Patient:** 2 upcoming, 2 past

### Mock Prescriptions (Patient)
- Aspirin 75mg
- Lisinopril 10mg
- Vitamin D3 1000 IU

**Note:** To connect to real backend data, update API calls in service files.

---

## 🔗 API Integration Points

When backend is ready, these endpoints will be used:

### Admin Endpoints
```
GET /api/admin/stats
GET /api/admin/users
GET /api/admin/departments
GET /api/admin/activities
```

### Doctor Endpoints
```
GET /api/doctor/appointments
GET /api/doctor/patients
GET /api/doctor/tasks
POST /api/doctor/prescriptions
```

### Patient Endpoints
```
GET /api/patient/appointments
GET /api/patient/records
GET /api/patient/prescriptions
GET /api/patient/health-metrics
```

---

## 📞 Need Help?

### Documentation
- [Role-Based Dashboards](./ROLE_BASED_DASHBOARDS.md) - Complete feature documentation
- [Backend Setup](./DATABASE_SETUP.md) - Database configuration
- [Startup Guide](./STARTUP_GUIDE.md) - General startup instructions

### Common Commands
```bash
# Frontend
npm run dev          # Start development server
npm run build        # Build for production
npm run start        # Start production server

# Backend
./mvnw spring-boot:run              # Start backend
./mvnw clean compile                # Compile only
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev  # Dev profile
```

---

## 🎉 Success Indicators

You'll know everything is working when:

✅ Backend shows: "Started HospitalApplication"
✅ Frontend shows: "✓ Ready in X ms"
✅ Login page displays with demo credentials
✅ After login, appropriate dashboard loads based on role
✅ All statistics and cards display data
✅ Navigation sidebar is functional
✅ Theme toggle switches colors
✅ No console errors in browser (F12)

---

**Happy Testing! 🚀**

If you encounter any issues not covered here, check the browser console (F12) for error messages and refer to the troubleshooting section.