# Role-Based Dashboard System

## Overview

The Hospital Management System now features three distinct, role-based dashboards tailored to the specific needs of different user types: **Admin**, **Doctor**, and **Patient**. Each dashboard provides unique features and interfaces optimized for their respective workflows.

---

## 🎯 Dashboard Routing System

The system automatically routes users to the appropriate dashboard based on their role upon login:

```
/dashboard → Role Detection → Appropriate Dashboard
```

**Priority Order:**
1. Admin/Super Admin → Admin Dashboard
2. Doctor → Doctor Dashboard  
3. Patient → Patient Dashboard

---

## 🛡️ Admin Dashboard

### Purpose
Complete system oversight and management for hospital administrators.

### Key Features

#### 1. **Comprehensive Statistics**
- Total Patients (with growth trends)
- Total Doctors (with growth trends)
- Today's Appointments
- Monthly Revenue (with growth percentage)
- Total Staff count
- Pending Appointments
- Occupancy Rate
- Active Users

#### 2. **Quick Actions**
- Manage Users (Add, edit, remove users)
- Manage Doctors (View and manage doctor profiles)
- Manage Departments (Configure hospital departments)
- View Reports (Access system reports and analytics)
- Financial Overview (Billing and revenue management)
- System Settings (Configure system preferences)
- Database Management (Backup and restore operations)
- Audit Logs (View system activity logs)

#### 3. **Department Performance**
- Real-time performance metrics for each department
- Patient count per department
- Revenue by department
- Growth trends

#### 4. **Recent Activity**
- User registration events
- Appointment changes
- System operations
- Payment activities
- Department updates

#### 5. **System Status**
- Server status monitoring
- Database health check
- Last backup timestamp
- Real-time system metrics

### Color Scheme
- Primary: Blue gradient (Blue-600 to Blue-800)
- Accent: Various colors for different metrics

---

## 🩺 Doctor Dashboard

### Purpose
Medical-focused interface for healthcare professionals to manage patient care and appointments.

### Key Features

#### 1. **Today's Overview**
- Today's Appointments (with pending count)
- Completed Consultations
- Active Patients under care
- Pending Reviews (lab reports & tests)

#### 2. **Quick Actions**
- New Prescription
- Lab Request
- Write Notes
- Patient Records
- Video Call
- Schedule Management

#### 3. **Appointments Management**
- **Tabs:**
  - Today (current appointments)
  - Upcoming (future appointments)
  - Past (completed appointments)
- **For Each Appointment:**
  - Patient name and urgency indicator
  - Time and appointment type
  - Reason for visit
  - Status (Scheduled/In Progress/Completed)
  - Quick actions (View Details, Start Consultation, Complete)

#### 4. **Recent Patients**
- Patient profiles with priority indicators
- Last visit information
- Current conditions
- Quick access to records
- Direct phone/email contact buttons

#### 5. **Upcoming Tasks**
- Review lab results
- Prepare reports
- Team meetings
- Administrative tasks
- Timestamps for each task

#### 6. **Performance Metrics** (Monthly)
- Total Consultations (with trends)
- Patient Satisfaction rating
- Average Consultation Time
- Follow-up Rate

### Color Scheme
- Primary: Teal gradient (Teal-600 to Teal-800)
- Accent: Medical green and blue tones

---

## 👤 Patient Dashboard

### Purpose
Patient-centric interface for managing personal health, appointments, and medical records.

### Key Features

#### 1. **Personal Health Overview**
- Upcoming Appointments count
- Active Prescriptions count
- Medical Records count
- Completed Visits (yearly)

#### 2. **Quick Actions**
- Book Appointment
- View Records
- Prescriptions
- Lab Reports
- Pay Bills
- Contact Support

#### 3. **Appointments Section**
- **Tabs:**
  - Upcoming (scheduled appointments)
  - Past (appointment history)
- **For Each Appointment:**
  - Doctor name and specialty
  - Date, time, and location
  - Appointment type
  - Status badges (Scheduled/Confirmed/Completed/Cancelled)
  - Action buttons:
    - Join Video Call
    - Reschedule
    - Cancel
    - View Summary
    - Download report

#### 4. **Health Metrics**
- Blood Pressure
- Heart Rate
- Blood Sugar
- Weight
- Status indicators (Normal/Abnormal)
- Quick update functionality

#### 5. **Active Prescriptions**
- Medication name and dosage
- Frequency and duration
- Prescribing doctor
- Start and end dates
- Active/Completed status

#### 6. **Recent Medical Records**
- Record type (Blood Test, X-Ray, ECG, etc.)
- Date and attending doctor
- Summary of findings
- Quick access to full reports

#### 7. **Emergency Contact**
- Emergency hotline number
- Primary care doctor information
- Quick-dial buttons

### Color Scheme
- Primary: Purple gradient (Purple-600 to Purple-800)
- Accent: Patient-friendly purple and pink tones

---

## 🎨 Common Layout Features

### Navigation Sidebar
- Role-based menu items
- Active route highlighting
- User profile section
- Quick logout button
- Collapsible on mobile

### Top Header
- Mobile menu toggle
- Global search bar
- Theme toggle (Light/Dark mode)
- Notifications bell (with badge)
- Responsive design

### Design System
- Consistent card layouts
- Hover effects and transitions
- Loading states
- Empty states
- Error handling
- Responsive grid layouts
- Dark mode support

---

## 🔐 Security & Access Control

### Role Hierarchy
```
SUPER_ADMIN > ADMIN > DOCTOR > PATIENT
```

### Access Rules
- **Admin:** Full system access
- **Doctor:** Medical operations, own patients, appointments
- **Patient:** Personal data only (appointments, records, prescriptions)

### Protected Routes
All dashboard routes are protected with authentication:
```typescript
export default withAuth(DashboardPage);
```

---

## 📱 Responsive Design

### Breakpoints
- **Mobile:** < 768px (Single column, hamburger menu)
- **Tablet:** 768px - 1024px (Two columns, collapsible sidebar)
- **Desktop:** > 1024px (Multi-column, fixed sidebar)

### Mobile Optimizations
- Touch-friendly buttons
- Collapsible sidebar with backdrop
- Stacked card layouts
- Simplified navigation
- Bottom action bars

---

## 🎯 User Experience Highlights

### Admin Dashboard
- **Focus:** System management and oversight
- **Layout:** Dense information display with multiple metrics
- **Actions:** System-wide operations
- **Colors:** Professional blue theme

### Doctor Dashboard  
- **Focus:** Patient care and clinical workflow
- **Layout:** Appointment-centric with task management
- **Actions:** Clinical operations and consultations
- **Colors:** Medical teal theme

### Patient Dashboard
- **Focus:** Personal health management
- **Layout:** Simplified, easy-to-understand interface
- **Actions:** Booking, viewing, and managing personal health data
- **Colors:** Friendly purple theme

---

## 🚀 Future Enhancements

### Planned Features
1. **Real-time Updates:** WebSocket integration for live appointment updates
2. **Advanced Analytics:** Interactive charts and graphs
3. **Notifications System:** In-app and email notifications
4. **Telemedicine:** Integrated video consultation
5. **AI Assistant:** Chatbot for common queries
6. **Mobile Apps:** Native iOS and Android applications
7. **Multi-language Support:** Internationalization (i18n)
8. **Accessibility:** WCAG 2.1 AA compliance

---

## 📂 File Structure

```
frontend/src/
├── app/
│   └── dashboard/
│       └── page.tsx                 # Main dashboard router
├── components/
│   ├── dashboards/
│   │   ├── AdminDashboard.tsx      # Admin-specific dashboard
│   │   ├── DoctorDashboard.tsx     # Doctor-specific dashboard
│   │   └── PatientDashboard.tsx    # Patient-specific dashboard
│   └── layout/
│       └── DashboardLayout.tsx     # Common layout wrapper
└── providers/
    ├── AuthProvider.tsx            # Authentication & role management
    └── ThemeProvider.tsx           # Dark/Light theme management
```

---

## 🔧 Technical Implementation

### Role Detection
```typescript
const getDashboard = () => {
  if (hasRole("ADMIN") || hasRole("SUPER_ADMIN")) {
    return <AdminDashboard />;
  }
  if (hasRole("DOCTOR")) {
    return <DoctorDashboard />;
  }
  if (hasRole("PATIENT")) {
    return <PatientDashboard />;
  }
  return <PatientDashboard />; // Default fallback
};
```

### Navigation Customization
Each role receives a customized navigation menu based on their permissions:
```typescript
const getNavigationItems = () => {
  // Returns role-specific menu items
};
```

### Theme Integration
All dashboards support dark mode through the ThemeProvider:
```typescript
const { actualTheme, toggleTheme } = useTheme();
```

---

## ✅ Testing Checklist

### Admin Dashboard
- [ ] All statistics display correctly
- [ ] Quick actions are functional
- [ ] Department performance loads
- [ ] Activity feed updates
- [ ] System status is accurate

### Doctor Dashboard
- [ ] Appointments load by tab
- [ ] Patient cards display correctly
- [ ] Quick actions work
- [ ] Tasks list updates
- [ ] Performance metrics calculate

### Patient Dashboard
- [ ] Appointments show with correct status
- [ ] Health metrics are editable
- [ ] Prescriptions list loads
- [ ] Medical records accessible
- [ ] Emergency contacts display

### Common Features
- [ ] Theme toggle works
- [ ] Search functionality
- [ ] Notifications appear
- [ ] Sidebar navigation
- [ ] Mobile responsive
- [ ] Logout successful

---

## 📞 Support & Maintenance

### Issue Reporting
For bugs or feature requests related to dashboards, please include:
1. User role (Admin/Doctor/Patient)
2. Browser and device information
3. Steps to reproduce
4. Expected vs actual behavior
5. Screenshots if applicable

### Updates
Dashboard features and improvements are released regularly. Check the changelog for the latest updates.

---

**Last Updated:** November 20, 2024  
**Version:** 1.0.0  
**Maintainer:** Development Team