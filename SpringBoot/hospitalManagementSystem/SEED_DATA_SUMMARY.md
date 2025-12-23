# Seed Data Summary - Hospital Management System

## Overview
Comprehensive seed data has been added to the database via Flyway migration V9 for thorough testing of the doctor portal and entire hospital management system.

**Migration File**: `V9__seed_data_for_testing.sql`

---

## Data Summary

### Current Database State
| Category | Count | Description |
|----------|-------|-------------|
| **Doctors** | 6 | 3 new seed doctors + 3 existing |
| **Patients** | 14 | 6 new seed patients + 8 existing |
| **Appointments** | 19 | 15 new appointments + 4 existing |
| **Lab Tests** | 15 | 12 new test types + 3 existing |
| **Lab Orders** | 11 | 8 new orders + 3 existing |
| **Users** | 14 | 9 new users + 5 existing |
| **Roles** | 5 | All standard roles |

---

## Seed Data Details

### 1. Doctors (3 Added)

#### Dr. John Smith - Cardiologist
- **Specialization**: Cardiology
- **License**: LIC-CARD-001
- **Email**: dr.smith@hospital.com
- **Contact**: +1-555-0101
- **Experience**: 15 years
- **Consultation Fee**: $200.00
- **Schedule**: 9:00 AM - 5:00 PM
- **Qualifications**: MD, FACC, Board Certified Cardiologist
- **Bio**: Experienced cardiologist specializing in interventional cardiology and heart disease prevention
- **Languages**: English, Spanish

#### Dr. Sarah Johnson - Pediatrician
- **Specialization**: Pediatrics
- **License**: LIC-PEDI-002
- **Email**: dr.johnson@hospital.com
- **Contact**: +1-555-0102
- **Experience**: 12 years
- **Consultation Fee**: $150.00
- **Schedule**: 8:00 AM - 4:00 PM
- **Qualifications**: MD, FAAP, Pediatric Specialist
- **Bio**: Dedicated pediatrician with expertise in child development and adolescent medicine
- **Languages**: English, French

#### Dr. Michael Williams - Orthopedic Surgeon
- **Specialization**: Orthopedics
- **License**: LIC-ORTH-003
- **Email**: dr.williams@hospital.com
- **Contact**: +1-555-0103
- **Experience**: 18 years
- **Consultation Fee**: $250.00
- **Schedule**: 10:00 AM - 6:00 PM
- **Qualifications**: MD, FAAOS, Orthopedic Surgeon
- **Bio**: Renowned orthopedic surgeon specializing in sports medicine and joint replacement
- **Languages**: English, German

---

### 2. Patients (6 Added)

#### James Anderson
- **DOB**: 1985-03-12 (Age: 40)
- **Gender**: Male
- **Blood Type**: A+
- **Contact**: +1-555-0201
- **Email**: james@email.com
- **Medical History**: Hypertension diagnosed 2020, well controlled with medication
- **Allergies**: Penicillin, Peanuts
- **Insurance**: Blue Cross Blue Shield (BCBS-123456789)
- **Emergency Contact**: Jennifer Anderson (Spouse) - +1-555-0301

#### Mary Taylor
- **DOB**: 1990-07-25 (Age: 35)
- **Gender**: Female
- **Blood Type**: O+
- **Contact**: +1-555-0202
- **Email**: mary@email.com
- **Medical History**: Type 2 Diabetes diagnosed 2019, managed with diet and metformin
- **Allergies**: Latex
- **Insurance**: Aetna (AETNA-987654321)
- **Emergency Contact**: David Taylor (Spouse) - +1-555-0302

#### Robert Brown
- **DOB**: 1978-11-08 (Age: 47)
- **Gender**: Male
- **Blood Type**: B+
- **Contact**: +1-555-0203
- **Email**: robert@email.com
- **Medical History**: Previous MI in 2021, currently on aspirin and statin therapy
- **Allergies**: None known
- **Insurance**: United Healthcare (UHC-456789123)
- **Emergency Contact**: Linda Brown (Sister) - +1-555-0303

#### Patricia Davis
- **DOB**: 2010-04-18 (Age: 15)
- **Gender**: Female
- **Blood Type**: AB+
- **Contact**: +1-555-0204
- **Email**: patricia@email.com
- **Medical History**: Asthma since age 5, uses albuterol inhaler as needed
- **Allergies**: Dust mites
- **Insurance**: Cigna (CIGNA-789123456)
- **Emergency Contact**: Susan Davis (Mother) - +1-555-0304

#### William Martinez
- **DOB**: 1995-09-30 (Age: 30)
- **Gender**: Male
- **Blood Type**: O-
- **Contact**: +1-555-0205
- **Email**: william@email.com
- **Medical History**: Recent knee injury from sports, undergoing physical therapy
- **Allergies**: Shellfish
- **Insurance**: Blue Cross Blue Shield (BCBS-321654987)
- **Emergency Contact**: Maria Martinez (Mother) - +1-555-0305

#### Jennifer Wilson
- **DOB**: 1988-12-15 (Age: 37)
- **Gender**: Female
- **Blood Type**: A-
- **Contact**: +1-555-0206
- **Email**: jennifer@email.com
- **Medical History**: Pregnant (second trimester), no complications
- **Allergies**: Sulfa drugs
- **Insurance**: Aetna (AETNA-654987321)
- **Emergency Contact**: Thomas Wilson (Spouse) - +1-555-0306

---

### 3. Appointments (15 Added)

#### Today's Appointments
1. **Dr. Smith → Robert Brown** - 10:00 AM
   - Type: Follow-up for cardiac health
   - Status: CONFIRMED
   - Duration: 30 min | Fee: $200

2. **Dr. Johnson → Patricia Davis** - 11:00 AM
   - Type: Asthma check-up
   - Status: CONFIRMED
   - Duration: 30 min | Fee: $150

3. **Dr. Williams → William Martinez** - 2:00 PM
   - Type: Knee pain evaluation
   - Status: SCHEDULED
   - Duration: 45 min | Fee: $250

#### Tomorrow's Appointments
4. **Dr. Smith → James Anderson** - 9:00 AM
   - Type: Blood pressure check
   - Status: SCHEDULED
   - Duration: 30 min | Fee: $200

5. **Dr. Johnson → Patricia Davis** - 10:30 AM
   - Type: Vaccination (flu shot)
   - Status: SCHEDULED
   - Duration: 20 min | Fee: $150

6. **Dr. Williams → William Martinez** - 3:00 PM
   - Type: Physical therapy consultation
   - Status: SCHEDULED
   - Duration: 45 min | Fee: $250

#### Past Appointments (Completed)
7-9. Various completed appointments from 1-3 months ago
10. **Cancelled** - Mary Taylor's diabetes consultation (3 days ago)

#### Next Week Appointments
11-13. Follow-ups scheduled for various patients
14-15. Additional historical appointments for testing

---

### 4. Lab Tests (12 Added)

Complete list of available lab tests:

| Test Name | Description | Cost |
|-----------|-------------|------|
| Complete Blood Count (CBC) | Measures RBC, WBC, platelets | $45.00 |
| Lipid Panel | Cholesterol and triglyceride levels | $65.00 |
| HbA1c Test | Average blood sugar over 3 months | $55.00 |
| Thyroid Function Test (TSH) | Thyroid hormone levels | $75.00 |
| Comprehensive Metabolic Panel | Glucose, electrolytes, kidney function | $85.00 |
| Urinalysis | Urine health indicators | $30.00 |
| Chest X-Ray | Radiographic chest imaging | $150.00 |
| ECG/EKG | Heart electrical activity | $100.00 |
| Blood Glucose Test | Current blood sugar level | $25.00 |
| Vitamin D Test | Vitamin D blood levels | $80.00 |
| Liver Function Test | Liver enzymes and function | $70.00 |
| Kidney Function Test | Creatinine and BUN levels | $60.00 |

---

### 5. Lab Orders (8 Added)

Various lab orders with different statuses for comprehensive testing:

1. **CBC** for James Anderson - **COMPLETED** (2 days ago)
   - Purpose: Routine annual physical exam

2. **Lipid Panel** for Robert Brown - **IN_PROGRESS** (1 day ago)
   - Purpose: Cardiac health monitoring
   - Linked to appointment

3. **HbA1c Test** for Mary Taylor - **ORDERED** (today)
   - Purpose: Diabetes management follow-up

4. **Blood Glucose** for Mary Taylor - **COMPLETED** (5 days ago)
   - Purpose: Fasting blood sugar check

5. **ECG/EKG** for Robert Brown - **COMPLETED** (7 days ago)
   - Purpose: Post-MI cardiac evaluation
   - Linked to completed appointment

6. **Thyroid Function** for Jennifer Wilson - **SAMPLE_COLLECTED** (3 days ago)
   - Purpose: Prenatal thyroid screening

7. **Liver Function** for James Anderson - **COMPLETED** (10 days ago)
   - Purpose: Annual health screening

8. **CBC** for Patricia Davis - **ORDERED** (today)
   - Purpose: Routine pediatric checkup
   - Linked to appointment

---

### 6. Users & Authentication (9 Added)

All users use the password: **password123**

#### Doctor Accounts
| Username | Email | Role | Full Name |
|----------|-------|------|-----------|
| dr.smith | dr.smith@hospital.com | ROLE_DOCTOR | Dr. John Smith |
| dr.johnson | dr.johnson@hospital.com | ROLE_DOCTOR | Dr. Sarah Johnson |
| dr.williams | dr.williams@hospital.com | ROLE_DOCTOR | Dr. Michael Williams |

#### Patient Accounts
| Username | Email | Role | Full Name |
|----------|-------|------|-----------|
| patient.james | james@email.com | ROLE_PATIENT | James Anderson |
| patient.mary | mary@email.com | ROLE_PATIENT | Mary Taylor |
| patient.robert | robert@email.com | ROLE_PATIENT | Robert Brown |
| patient.patricia | patricia@email.com | ROLE_PATIENT | Patricia Davis |
| patient.william | william@email.com | ROLE_PATIENT | William Martinez |
| patient.jennifer | jennifer@email.com | ROLE_PATIENT | Jennifer Wilson |

#### Admin Account
| Username | Email | Role | Full Name |
|----------|-------|------|-----------|
| admin.user | admin@hospital.com | ROLE_ADMIN | Admin User |

---

## Testing Scenarios Covered

### 1. Appointment Management
- ✅ View today's appointments
- ✅ View upcoming appointments (tomorrow, next week)
- ✅ View past/completed appointments
- ✅ View cancelled appointments
- ✅ Different appointment types (routine, follow-up, consultation, vaccination, diagnostic)
- ✅ Different appointment statuses (scheduled, confirmed, completed, cancelled)
- ✅ Appointments with/without notes

### 2. Lab Order Management
- ✅ Orders in various statuses (ORDERED, SAMPLE_COLLECTED, IN_PROGRESS, COMPLETED)
- ✅ Orders linked to appointments
- ✅ Standalone lab orders (not linked to appointments)
- ✅ Orders with detailed notes
- ✅ Multiple orders for same patient
- ✅ Historical completed orders

### 3. Patient Records
- ✅ Diverse patient demographics (age 15-47, various genders)
- ✅ Different blood types
- ✅ Various medical conditions (hypertension, diabetes, asthma, pregnancy, cardiac, orthopedic)
- ✅ Multiple allergies
- ✅ Different insurance providers
- ✅ Emergency contacts

### 4. Doctor Portal Features
- ✅ Multiple doctors with different specializations
- ✅ Different consultation fees
- ✅ Various working hours/schedules
- ✅ Multiple appointments per doctor
- ✅ Doctor-patient relationships
- ✅ Multiple languages spoken

### 5. Authentication & Authorization
- ✅ Doctor role access
- ✅ Patient role access
- ✅ Admin role access
- ✅ Multiple users per role

---

## How to Use This Seed Data

### Login Credentials

**Format**: `username / password`

**Doctors**:
- `dr.smith / password123` - Cardiologist
- `dr.johnson / password123` - Pediatrician  
- `dr.williams / password123` - Orthopedic Surgeon

**Patients**:
- `patient.james / password123` - Male, 40, Hypertension
- `patient.mary / password123` - Female, 35, Diabetes
- `patient.robert / password123` - Male, 47, Post-MI
- `patient.patricia / password123` - Female, 15, Asthma
- `patient.william / password123` - Male, 30, Knee injury
- `patient.jennifer / password123` - Female, 37, Pregnant

**Admin**:
- `admin.user / password123` - Full system access

---

## Testing Workflows

### Workflow 1: Doctor Daily Schedule
1. Login as **dr.smith**
2. View today's appointments (should see Robert Brown at 10:00 AM)
3. Check upcoming appointments for tomorrow
4. Review completed appointments from past weeks

### Workflow 2: Patient Portal
1. Login as **patient.robert**
2. View upcoming appointments with Dr. Smith
3. Check lab order status (Lipid Panel - IN_PROGRESS)
4. Review completed ECG results

### Workflow 3: Lab Order Management
1. Login as **dr.smith**
2. Create new lab order for a patient
3. View pending lab orders
4. Update lab order status to SAMPLE_COLLECTED → IN_PROGRESS → COMPLETED

### Workflow 4: Appointment Management
1. Login as **dr.johnson**
2. View Patricia Davis's appointment today
3. Mark appointment as COMPLETED
4. Add diagnosis and prescription notes

### Workflow 5: Multi-Patient Management
1. Login as **dr.williams**
2. View all appointments (scheduled, confirmed, completed)
3. Filter by date range
4. Export/review appointment summary

---

## Database Verification

To verify seed data was loaded correctly:

```sql
-- Check doctors
SELECT full_name, specialization, license_number FROM doctors;

-- Check patients
SELECT full_name, date_of_birth, medical_history FROM patients;

-- Check appointments
SELECT a.id, d.full_name as doctor, p.full_name as patient, 
       a.appointment_datetime, a.status
FROM appointments a
JOIN doctors d ON a.doctor_id = d.id
JOIN patients p ON a.patient_id = p.id
ORDER BY a.appointment_datetime;

-- Check lab orders
SELECT lo.id, lt.test_name, p.full_name as patient, lo.status, lo.ordered_at
FROM lab_orders lo
JOIN lab_tests lt ON lo.lab_test_id = lt.id
JOIN patients p ON lo.patient_id = p.id
ORDER BY lo.ordered_at DESC;

-- Check users and roles
SELECT u.username, u.full_name, r.name as role
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON ur.role_id = r.id
ORDER BY r.name, u.username;
```

---

## Cleanup Script

If you need to remove all seed data:

**File**: `cleanup_test_data.sql`

```bash
cd hospital
mysql -u root -p'MdAshikur123+' hospital_db < cleanup_test_data.sql
```

This will:
- Remove all seed users, patients, doctors
- Remove all seed appointments and lab orders
- Remove seed lab tests
- Keep existing data intact
- Reset Flyway V9 migration if needed

---

## Notes & Best Practices

### Important Notes
1. **Password Security**: All users have the same password (password123) for testing only. Change in production!
2. **Data Relationships**: All appointments are properly linked to doctors and patients with foreign keys
3. **Realistic Dates**: Appointments use CURDATE() so they're always relative to today
4. **Statuses**: Various appointment and lab order statuses for comprehensive testing
5. **Idempotent**: Migration uses ON DUPLICATE KEY UPDATE to avoid errors on re-runs

### Best Practices
1. Use seed data only in development/testing environments
2. Never deploy seed data to production
3. Regularly refresh seed data to test with current dates
4. Document any custom test scenarios you create
5. Keep passwords secure in production environments

---

## Migration Status

✅ **V9 Migration Successfully Applied**

- Version: 9
- Description: seed data for testing
- Status: SUCCESS
- Execution Time: ~27ms
- Applied: November 21, 2025

---

## Support & Troubleshooting

### Common Issues

**Issue**: Migration fails with duplicate key error
**Solution**: Run cleanup script first, then restart backend

**Issue**: Dates are in the past
**Solution**: Seed data uses relative dates (CURDATE(), NOW()), restart backend to refresh

**Issue**: Cannot login with seed credentials
**Solution**: Verify users were created: `SELECT * FROM users WHERE username LIKE 'dr.%';`

**Issue**: No appointments showing
**Solution**: Check date filters - seed data creates appointments for today/tomorrow/next week

---

## Future Enhancements

Potential additions to seed data:

1. **Medical Records** - Add sample medical record files/documents
2. **Prescriptions** - Add detailed prescription data
3. **Billing** - Add invoices and payment records
4. **Notifications** - Add sample notifications/messages
5. **Audit Logs** - Add historical audit trail data
6. **Reports** - Add generated report data
7. **Schedules** - Add recurring appointment schedules
8. **More Specializations** - Add more doctor types (neurology, dermatology, etc.)

---

**Last Updated**: November 21, 2025  
**Migration File**: V9__seed_data_for_testing.sql  
**Status**: ✅ Active & Ready for Testing