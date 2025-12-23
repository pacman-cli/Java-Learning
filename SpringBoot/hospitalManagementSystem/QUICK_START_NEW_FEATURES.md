# Quick Start Guide - New Features

Welcome! This guide will help you quickly get started with the three newly implemented features in the Hospital Management System.

## 🚀 Quick Navigation

- [Medical Records](#-medical-records) - `/records`
- [Lab Requests](#-lab-requests) - `/lab-requests`
- [Doctor Schedule](#-doctor-schedule) - `/schedule`

---

## 📋 Prerequisites

### 1. Start the Backend
```bash
cd hospital
./mvnw spring-boot:run
```
Wait for: `Started HospitalApplication in X seconds`

### 2. Start the Frontend
```bash
cd frontend
npm run dev
```
Open: http://localhost:3000

### 3. Login
```
Username: doctor
Password: doctor123
```

---

## 📁 Medical Records

**What it does:** Manage patient medical records, diagnoses, treatment plans, and clinical documentation.

### Quick Actions

#### Create a New Record
1. Click **"Add Record"** button (top right)
2. Fill in the form:
   - Select Patient
   - Choose Record Type (e.g., "Diagnosis")
   - Enter Title (e.g., "Annual Physical Exam")
   - Add Content (detailed notes)
3. Click **"Create Record"**

#### View a Record
- Click the **👁️ Eye icon** on any record card
- See all details including patient info and creation date

#### Edit a Record
- Click the **✏️ Edit icon**
- Modify title, type, or content
- Click **"Update Record"**

#### Upload Files
- Click the **📤 Upload icon** on a record
- Select file from your computer
- Confirmation when upload completes

#### Search & Filter
- **Search box:** Type patient name, title, or keywords
- **Filter dropdown:** Choose record type (Diagnosis, Lab Result, etc.)

### Record Types Available
- Diagnosis
- Treatment Plan
- Progress Note
- Lab Result
- Radiology Report
- Surgical Report
- Consultation Note
- Discharge Summary
- Prescription
- Other

### Tips
✅ Use descriptive titles for easy searching
✅ Add detailed content for better patient care
✅ Upload relevant files (lab reports, x-rays, etc.)
✅ Use appropriate record types for organization

---

## 🧪 Lab Requests

**What it does:** Order laboratory tests, track status, and upload results.

### Dashboard Overview

Four status cards show:
- 🟡 **Pending** - Awaiting lab processing
- 🔵 **In Progress** - Currently being tested
- 🟢 **Completed** - Results available
- 🔴 **Cancelled** - Request cancelled

### Quick Actions

#### Create a Lab Request
1. Click **"New Lab Request"** button
2. Fill in:
   - Select Patient
   - Choose Lab Test (e.g., "Complete Blood Count")
   - (Optional) Link to Appointment ID
   - (Optional) Add Notes
3. Click **"Create Request"**

#### Change Status
- Use the **status dropdown** on each card
- Select new status: Pending → In Progress → Completed
- Status updates immediately

#### Upload Lab Report
- Click the **📤 Upload icon**
- Select PDF/image of lab results
- File uploads to server
- "Report Available" badge appears

#### View Request Details
- Click **👁️ Eye icon**
- See complete information:
  - Test name
  - Patient details
  - Order date
  - Current status
  - Notes
  - Report (if uploaded)

#### Edit Request
- Click **✏️ Edit icon**
- Modify test, patient, or notes
- Cannot change status from edit (use status dropdown)

### Workflow Example

1. **Doctor orders test:**
   - Create request → Status: PENDING
   - Add notes: "Fasting required"

2. **Lab receives order:**
   - Change status → IN_PROGRESS
   
3. **Results ready:**
   - Upload report file
   - Change status → COMPLETED
   
4. **Doctor reviews results:**
   - View request details
   - Download report
   - Add to medical record

### Tips
✅ Use notes field for special instructions
✅ Link to appointments for context
✅ Update status promptly for tracking
✅ Upload clear, readable reports

---

## 📅 Doctor Schedule

**What it does:** Manage your working hours, availability, and time-off requests.

### Calendar View

**Color Key:**
- 🟦 **Teal border** = Today
- 🟥 **Red background** = Blocked (unavailable)
- 🟩 **Green background** = Available for appointments
- ⬜ **Gray** = No schedule set

**Navigation:**
- **⬅️ Previous** - Go to previous month
- **Today** - Jump to current month
- **➡️ Next** - Go to next month

### Quick Actions

#### Add Time Slot
1. Click **"Add Time Slot"** button
2. Select:
   - Day of Week (Monday-Sunday)
   - Start Time (e.g., 09:00)
   - End Time (e.g., 12:00)
   - Max Appointments (e.g., 6)
3. Click **"Add Time Slot"**

#### Manage Availability
- Click **green "Available"** button → Toggle to "Unavailable"
- Click **gray "Unavailable"** button → Toggle to "Available"
- Use this for temporary changes (don't delete the slot)

#### Delete Time Slot
- Click **🗑️ Trash icon** on any slot
- Confirm deletion
- Slot removed from schedule

#### Block a Date
1. Click **"Block Date"** button
2. Choose date (future dates only)
3. Enter reason (e.g., "Conference", "Personal Leave")
4. Click **"Block Date"**
5. Date shows as red on calendar

#### Unblock a Date
- Find blocked date in "Blocked Dates" section
- Click **"Unblock"** button
- Confirm action
- Date returns to normal

### Default Schedule

Pre-configured schedule (you can modify):
```
Monday:    09:00-12:00 (6 max), 14:00-17:00 (6 max)
Wednesday: 09:00-12:00 (6 max), 14:00-17:00 (6 max)
Friday:    09:00-13:00 (8 max)
```

### Use Cases

#### Regular Weekly Schedule
1. Add time slots for your typical work days
2. Set realistic max appointments per slot
3. Keep availability ON for normal operation

#### Vacation Planning
1. Use "Block Date" for each day off
2. Reason: "Vacation"
3. Patients won't see these dates when booking

#### Conference Day
1. Block the specific date
2. Reason: "Medical Conference"
3. Or keep slots but toggle to "Unavailable"

#### Half Day
1. Don't delete afternoon slots
2. Toggle them to "Unavailable" temporarily
3. Toggle back when schedule returns to normal

### Tips
✅ Add slots before accepting appointments
✅ Block holidays and personal days in advance
✅ Set realistic max appointments (consider time per patient)
✅ Use "Unavailable" for temporary changes
✅ Use "Block Date" for full-day unavailability

---

## 🎯 Common Workflows

### New Patient Visit

1. **Before Appointment:**
   - Check your schedule (green = available)
   - Ensure time slot exists for the day

2. **During Appointment:**
   - Create medical record (Consultation Note)
   - Order lab tests if needed (Lab Requests)
   
3. **After Appointment:**
   - Complete medical record with findings
   - Update appointment status
   - Review lab results when ready

### Lab Test Process

1. **Order Phase:**
   - Create lab request during appointment
   - Status: PENDING
   - Add special instructions in notes

2. **Processing:**
   - Lab changes status to IN_PROGRESS
   - Patient gets blood drawn/test done

3. **Results:**
   - Lab uploads report
   - Changes status to COMPLETED
   - Doctor reviews and adds to medical record

### Schedule Management

**Weekly Setup:**
1. Set regular working hours (Monday-Friday)
2. Define max appointments per slot
3. Keep all slots "Available"

**Vacation:**
1. Block all dates you'll be away
2. Provide reason for staff
3. Patients automatically see unavailability

**Returning from Leave:**
1. Unblock dates
2. Verify time slots are still "Available"
3. Review any pending appointments

---

## 🔍 Troubleshooting

### Can't See Records/Requests
**Problem:** Empty page or no data  
**Solution:**
- Verify backend is running (check http://localhost:8080)
- Login with correct role (doctor/admin)
- Try refreshing the page (F5)
- Check browser console for errors (F12)

### Upload Failed
**Problem:** File upload doesn't work  
**Solution:**
- Check file size (may have limit)
- Ensure backend has write permissions
- Verify storage directory exists
- Check network connection

### Schedule Not Saving
**Problem:** Changes disappear after refresh  
**Solution:**
- This is expected! Schedule uses LocalStorage (temporary)
- Data persists in your browser
- Clear browser cache will reset schedule
- Backend integration coming soon

### Status Won't Change
**Problem:** Lab request status stuck  
**Solution:**
- Refresh the page
- Check backend logs for errors
- Verify you have permission (doctor/admin)
- Try from view modal instead

### Search Not Working
**Problem:** Search returns no results  
**Solution:**
- Check spelling
- Try partial words (e.g., "dia" for "diagnosis")
- Clear filter (set to "All")
- Case-insensitive search is supported

---

## 💡 Pro Tips

### Efficiency Tips

1. **Batch Operations:**
   - Order multiple lab tests together
   - Create multiple time slots at once
   - Bulk block dates (use date picker)

2. **Use Filters:**
   - Filter by status to focus on pending items
   - Filter records by type for quick access
   - Combine search + filter for precision

3. **Keyboard Shortcuts:**
   - Enter to submit forms
   - Escape to close modals
   - Tab to navigate fields

### Organization Tips

1. **Naming Conventions:**
   - Medical Records: "Type - Brief Description - Date"
   - Example: "Diagnosis - Annual Physical - 2024-12-10"

2. **Status Management:**
   - Update lab request status promptly
   - Use CANCELLED only when necessary
   - Complete records before changing status

3. **File Management:**
   - Upload files immediately when available
   - Use descriptive filenames
   - Include date in filename if relevant

---

## 📱 Mobile Use

All features work on mobile devices!

### Best Practices on Mobile

1. **Portrait Mode:**
   - Better for forms and lists
   - Easier scrolling

2. **Landscape Mode:**
   - Better for calendar view
   - More content visible

3. **Touch Gestures:**
   - Tap to select
   - Swipe to scroll
   - Pinch to zoom (calendar)

---

## 🆘 Getting Help

### Resources

1. **Detailed Documentation:**
   - `COMPLETED_FEATURES.md` - Complete feature specs
   - `FIXES_AND_IMPROVEMENTS_SUMMARY.md` - Technical details

2. **Backend API:**
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - API Docs: http://localhost:8080/api-docs

3. **Console Logs:**
   - Press F12 to open developer tools
   - Check Console tab for errors
   - Check Network tab for API calls

### Common Issues

| Issue | Quick Fix |
|-------|-----------|
| Can't login | Check credentials (doctor/doctor123) |
| Page blank | Restart backend |
| Changes not saving | Check backend connection |
| Upload fails | Check file size and type |
| Schedule resets | Expected (LocalStorage only) |

---

## ✅ Checklist

### First Time Setup
- [ ] Backend running (port 8080)
- [ ] Frontend running (port 3000)
- [ ] Logged in as doctor
- [ ] Can see dashboard

### Test Each Feature
- [ ] Created a medical record
- [ ] Uploaded a file to record
- [ ] Created a lab request
- [ ] Changed lab request status
- [ ] Added a time slot to schedule
- [ ] Blocked a date
- [ ] Searched and filtered records

### Ready to Use
- [ ] Understand all three features
- [ ] Know how to create/edit/delete
- [ ] Know how to search and filter
- [ ] Know how to upload files
- [ ] Know where to get help

---

## 🎓 Training Scenarios

### Scenario 1: New Patient Consultation
**Goal:** Document a new patient visit

1. Go to `/records`
2. Create record: "Consultation Note"
3. Patient: Select from dropdown
4. Title: "Initial Consultation - Chief Complaint"
5. Content: Document findings
6. Upload: Patient's previous records (if any)
7. Save record

### Scenario 2: Order Lab Tests
**Goal:** Order blood work for patient

1. Go to `/lab-requests`
2. Create new request
3. Patient: Select patient
4. Test: "Complete Blood Count"
5. Notes: "Fasting required - Morning collection"
6. Save request
7. Status remains PENDING until lab processes

### Scenario 3: Vacation Planning
**Goal:** Block dates for 1-week vacation

1. Go to `/schedule`
2. Navigate to vacation month
3. Click "Block Date" for each day
4. Reason: "Annual Leave"
5. Verify dates show as red
6. Check "Blocked Dates" list

---

## 🔐 Security Notes

- **Sessions:** Auto-logout after inactivity
- **Permissions:** Only doctors/admins can access these features
- **Data:** All operations logged (audit trail)
- **Files:** Uploaded files are stored securely
- **Passwords:** Never share your credentials

---

## 📞 Support

**Issues?** Check the console (F12)
**Questions?** See `COMPLETED_FEATURES.md`
**Bugs?** Report with steps to reproduce

---

**Happy Managing! 🏥**

Last Updated: December 2024