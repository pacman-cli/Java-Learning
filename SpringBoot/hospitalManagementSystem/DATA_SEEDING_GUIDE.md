# 🌱 Data Seeding Guide - Hospital Management System

## 📋 Overview

The Hospital Management System now includes **automatic data seeding** that clears and repopulates the database with comprehensive test data every time the backend starts.

This ensures:
- ✅ Clean state on every restart
- ✅ Consistent test data
- ✅ Proper relationships between all entities
- ✅ Ready-to-test environment
- ✅ No more "Patient not found with id: 65" errors!

---

## 🚀 How It Works

### Automatic Seeding on Startup

When you start the backend:
```bash
cd hospitalManagementSystem/hospital
./mvnw spring-boot:run
```

The `DataLoader` component automatically:
1. **Checks** if data exists
2. **Clears** all existing data (except roles)
3. **Seeds** comprehensive test data
4. **Prints** a summary of seeded data

---

## 📊 What Gets Seeded

### 1. **Roles** (Standard System Roles)
- `ROLE_ADMIN`
- `ROLE_DOCTOR`
- `ROLE_NURSE`
- `ROLE_PATIENT`
- `ROLE_RECEPTIONIST`
- `ROLE_PHARMACIST`
- `ROLE_LAB_TECHNICIAN`
- `ROLE_BILLING_STAFF`

### 2. **Users** (10 Users)
- 1 Admin
- 5 Patients
- 4 Doctors

### 3. **Patients** (5 Patients)
| ID | Name | Email | Contact | Gender |
|----|------|-------|---------|--------|
| 1 | John Doe | john.doe@email.com | +8801712345671 | MALE |
| 2 | Jane Smith | jane.smith@email.com | +8801712345672 | FEMALE |
| 3 | Bob Johnson | bob.johnson@email.com | +8801712345673 | MALE |
| 4 | Alice Williams | alice.williams@email.com | +8801712345674 | FEMALE |
| 5 | Charlie Brown | charlie.brown@email.com | +8801712345675 | MALE |

### 4. **Doctors** (4 Doctors)
| ID | Name | Specialization | Department | License | Fee |
|----|------|----------------|------------|---------|-----|
| 1 | Dr. Sarah Smith | Cardiology | Cardiology | MED12345 | 1000 BDT |
| 2 | Dr. Michael Jones | Neurology | Neurology | MED12346 | 1500 BDT |
| 3 | Dr. Emily Davis | Pediatrics | Pediatrics | MED12347 | 2000 BDT |
| 4 | Dr. James Wilson | Orthopedics | Orthopedics | MED12348 | 2500 BDT |

### 5. **Appointments** (10 Appointments)
- 2 appointments per patient
- 1 past completed appointment
- 1 future scheduled appointment
- Mix of IN_PERSON and VIDEO types
- Properly linked to patients and doctors

### 6. **Medicines** (8 Medicines)
| Name | Description | Price |
|------|-------------|-------|
| Paracetamol 500mg | Pain relief and fever reducer | 50 BDT |
| Amoxicillin 250mg | Antibiotic | 120 BDT |
| Omeprazole 20mg | Reduces stomach acid | 80 BDT |
| Metformin 500mg | Diabetes medication | 60 BDT |
| Atorvastatin 10mg | Cholesterol medication | 150 BDT |
| Aspirin 75mg | Blood thinner | 40 BDT |
| Vitamin D3 1000IU | Vitamin supplement | 100 BDT |
| Cetirizine 10mg | Allergy relief | 45 BDT |

### 7. **Prescriptions** (10 Prescriptions)
- 2 prescriptions per patient
- Linked to patients, doctors, and medicines
- Includes dosage and instructions

### 8. **Lab Tests** (8 Tests)
| Test Name | Description | Cost |
|-----------|-------------|------|
| Complete Blood Count (CBC) | Basic blood test | 500 BDT |
| Lipid Profile | Cholesterol test | 800 BDT |
| Blood Sugar (Fasting) | Diabetes screening | 300 BDT |
| Liver Function Test (LFT) | Liver health | 1200 BDT |
| Kidney Function Test (KFT) | Kidney health | 1000 BDT |
| Thyroid Profile | Thyroid function | 1500 BDT |
| Urine Analysis | Basic urine test | 200 BDT |
| X-Ray Chest | Chest radiography | 600 BDT |

### 9. **Lab Orders** (5 Orders)
- 1 lab order per patient
- Mix of ORDERED, IN_PROGRESS, COMPLETED statuses
- Completed orders have report paths

### 10. **Billings** (5 Billings)
- Linked to completed appointments
- Mix of PAID, PENDING, OVERDUE statuses
- Paid bills include payment method and timestamp

### 11. **Medical Records** (10 Records)
- 2 records per patient
- Types: DIAGNOSIS, TREATMENT, LAB_RESULT, PRESCRIPTION, NOTE
- Includes title, content, and timestamps

---

## 🔑 Test Credentials

### Admin Account
```
Username: admin
Password: password123
Email: admin@hospital.com
```

### Patient Accounts
```
Username: patient1 | Password: password123
Username: patient2 | Password: password123
Username: patient3 | Password: password123
Username: patient4 | Password: password123
Username: patient5 | Password: password123
```

### Doctor Accounts
```
Username: doctor1 | Password: password123 (Cardiology)
Username: doctor2 | Password: password123 (Neurology)
Username: doctor3 | Password: password123 (Pediatrics)
Username: doctor4 | Password: password123 (Orthopedics)
```

**Note**: All passwords are `password123` for easy testing!

---

## 📈 Startup Logs

When the backend starts, you'll see:

```
================================================================================
🚀 Starting Data Seeding Process...
================================================================================
⚠️  Data already exists. Clearing all data...
🗑️  Clearing existing data...
✅ All data cleared successfully!
📋 Seeding Roles...
✅ Roles seeded. Total: 8
👥 Seeding Users...
   ✓ Created user: admin (ROLE_ADMIN)
   ✓ Created user: patient1 (ROLE_PATIENT)
   ✓ Created user: patient2 (ROLE_PATIENT)
   ...
✅ Users seeded. Total: 10
🏥 Seeding Patients...
   ✓ Created patient: John Doe
   ✓ Created patient: Jane Smith
   ...
✅ Patients seeded. Total: 5
👨‍⚕️ Seeding Doctors...
   ✓ Created doctor: Dr. Sarah Smith (Cardiology)
   ✓ Created doctor: Dr. Michael Jones (Neurology)
   ...
✅ Doctors seeded. Total: 4
💊 Seeding Medicines...
   ✓ Created medicine: Paracetamol 500mg
   ...
✅ Medicines seeded. Total: 8
🔬 Seeding Lab Tests...
   ✓ Created lab test: Complete Blood Count (CBC)
   ...
✅ Lab Tests seeded. Total: 8
📅 Seeding Appointments...
   ✓ Created appointments for patient: John Doe
   ...
✅ Appointments seeded. Total: 10
💉 Seeding Prescriptions...
   ✓ Created prescriptions for patient: John Doe
   ...
✅ Prescriptions seeded. Total: 10
🧪 Seeding Lab Orders...
   ✓ Created lab order for patient: John Doe (ORDERED)
   ...
✅ Lab Orders seeded. Total: 5
💰 Seeding Billings...
   ✓ Created billing for patient: John Doe (PAID)
   ...
✅ Billings seeded. Total: 5
📋 Seeding Medical Records...
   ✓ Created medical records for patient: John Doe
   ...
✅ Medical Records seeded. Total: 10
================================================================================
✅ Data Seeding Completed Successfully!
================================================================================
📊 Database Summary:
   - Roles: 8
   - Users: 10
   - Patients: 5
   - Doctors: 4
   - Appointments: 10
   - Medicines: 8
   - Prescriptions: 10
   - Lab Tests: 8
   - Lab Orders: 5
   - Billings: 5
   - Medical Records: 10

🔑 Test Credentials:
   Admin: admin / password123
   Patient: patient1 / password123
   Doctor: doctor1 / password123

✨ Ready to test! Visit http://localhost:3000
```

---

## 🧪 Testing the Seeded Data

### Step 1: Start Backend
```bash
cd hospitalManagementSystem/hospital
./mvnw spring-boot:run
```

Wait for seeding to complete.

### Step 2: Start Frontend
```bash
cd hospitalManagementSystem/frontend
npm run dev
```

### Step 3: Test as Patient
1. Go to http://localhost:3000
2. Login: `patient1` / `password123`
3. Test all sections:
   - ✅ **My Appointments** - See 2 appointments (1 past, 1 upcoming)
   - ✅ **My Prescriptions** - See 2 prescriptions
   - ✅ **My Lab Reports** - See 1 lab order
   - ✅ **My Billing** - See 1 billing record
   - ✅ **My Medical Records** - See 2 records

### Step 4: Test as Doctor
1. Logout and login: `doctor1` / `password123`
2. Test:
   - ✅ View appointments
   - ✅ View patients
   - ✅ Create prescriptions
   - ✅ Order lab tests

### Step 5: Test as Admin
1. Logout and login: `admin` / `password123`
2. Test:
   - ✅ View all patients
   - ✅ View all doctors
   - ✅ View all appointments
   - ✅ Manage system

---

## 🔄 How to Disable Auto-Seeding (Optional)

If you want to disable automatic seeding:

### Option 1: Comment out the DataLoader
Edit `hospital/src/main/java/com/pacman/hospital/config/DataLoader.java`:
```java
// @Component  // Comment this line
@Order(1)
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    // ...
}
```

### Option 2: Use Spring Profile
Add to `application.properties`:
```properties
spring.profiles.active=production
```

And add profile check in DataLoader:
```java
@Profile("!production")
@Component
public class DataLoader implements CommandLineRunner {
    // ...
}
```

---

## 🐛 Troubleshooting

### Issue 1: "Patient not found with id: X"
**Solution**: Restart the backend. Data will be reseeded automatically.

### Issue 2: Foreign key constraint errors
**Solution**: The seeder clears data in the correct order. If you see this, check database manually:
```sql
SHOW PROCESSLIST;
-- Kill any hanging transactions
```

### Issue 3: Data not clearing
**Solution**: 
1. Stop backend
2. Manually clear database:
   ```sql
   USE hospital_management_system;
   SET FOREIGN_KEY_CHECKS = 0;
   TRUNCATE TABLE medical_records;
   TRUNCATE TABLE billings;
   TRUNCATE TABLE lab_orders;
   TRUNCATE TABLE prescriptions;
   TRUNCATE TABLE appointments;
   TRUNCATE TABLE lab_tests;
   TRUNCATE TABLE medicines;
   TRUNCATE TABLE doctors;
   TRUNCATE TABLE patients;
   TRUNCATE TABLE user_roles;
   TRUNCATE TABLE users;
   SET FOREIGN_KEY_CHECKS = 1;
   ```
3. Restart backend

### Issue 4: Seeding takes too long
**Solution**: The seeder creates ~60+ records. Should take <5 seconds. If longer:
- Check database connection
- Check for database locks
- Restart MySQL

---

## 🎯 Benefits of Auto-Seeding

### For Development
- ✅ No manual data entry needed
- ✅ Consistent test environment
- ✅ Easy to reset and start fresh
- ✅ All relationships properly set up

### For Testing
- ✅ Known data state
- ✅ Reproducible tests
- ✅ Full coverage of scenarios
- ✅ Edge cases included (past/future dates, different statuses)

### For Demos
- ✅ Professional data
- ✅ Realistic scenarios
- ✅ Complete user journey
- ✅ Impressive showcase

---

## 📝 Customizing Seed Data

Want to add your own test data? Edit `DataLoader.java`:

### Add More Patients
```java
private List<Patient> seedPatients(List<User> users) {
    // ... existing code ...
    
    // Add your custom patient
    Patient customPatient = Patient.builder()
        .fullName("Your Name")
        .dateOfBirth(LocalDate.of(1990, 1, 1))
        .gender(Gender.MALE)
        .contactInfo("+8801712345678")
        .email("your.email@example.com")
        .address("Your Address")
        .build();
    
    patients.add(patientRepository.save(customPatient));
    
    return patients;
}
```

### Add More Medicines
```java
String[][] medicineData = {
    // ... existing medicines ...
    {"Your Medicine", "Description", "N/A", "99.00"},
};
```

---

## 🔒 Security Notes

### Production Deployment
**⚠️ IMPORTANT**: Disable data seeding in production!

1. Use environment-specific profiles
2. Never seed production databases
3. Change all default passwords
4. Use secure password policies

### Development Best Practices
- Keep default passwords simple for testing
- Don't commit real user data
- Use fake data generators for larger datasets
- Document all test credentials

---

## 📊 Database Schema

The seeding respects all database constraints:
- Foreign key relationships
- Not null constraints
- Unique constraints
- Indexes
- Default values

All seeded data is **referentially consistent** and **production-ready**.

---

## 🎉 Summary

With automatic data seeding:
1. ✅ **No more empty database errors**
2. ✅ **No more "ID not found" errors**
3. ✅ **Consistent test environment**
4. ✅ **Ready to test immediately**
5. ✅ **Professional demo data**

Just start the backend and everything is ready! 🚀

---

**Last Updated**: November 21, 2025  
**Status**: ✅ ACTIVE AND WORKING  
**Next Restart**: Data will be cleared and reseeded automatically!