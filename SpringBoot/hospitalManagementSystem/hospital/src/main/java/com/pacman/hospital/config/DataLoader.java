package com.pacman.hospital.config;

import com.pacman.hospital.core.security.model.Role;
import com.pacman.hospital.core.security.model.User;
import com.pacman.hospital.core.security.model.UserRole;
import com.pacman.hospital.core.security.repository.RoleRepository;
import com.pacman.hospital.core.security.repository.UserRepository;
import com.pacman.hospital.domain.appointment.model.Appointment;
import com.pacman.hospital.domain.appointment.model.AppointmentStatus;
import com.pacman.hospital.domain.appointment.repository.AppointmentRepository;
import com.pacman.hospital.domain.billing.model.Billing;
import com.pacman.hospital.domain.billing.model.BillingStatus;
import com.pacman.hospital.domain.billing.repository.BillingRepository;
import com.pacman.hospital.domain.doctor.model.Doctor;
import com.pacman.hospital.domain.doctor.repository.DoctorRepository;
import com.pacman.hospital.domain.laboratory.model.LabOrder;
import com.pacman.hospital.domain.laboratory.model.LabOrderStatus;
import com.pacman.hospital.domain.laboratory.model.LabTest;
import com.pacman.hospital.domain.laboratory.repository.LabOrderRepository;
import com.pacman.hospital.domain.laboratory.repository.LabTestRepository;
import com.pacman.hospital.domain.medicalrecord.model.MedicalRecord;
import com.pacman.hospital.domain.medicalrecord.respository.MedicalRecordRepository;
import com.pacman.hospital.domain.patient.model.Gender;
import com.pacman.hospital.domain.patient.model.Patient;
import com.pacman.hospital.domain.patient.repository.PatientRepository;
import com.pacman.hospital.domain.pharmacy.model.Medicine;
import com.pacman.hospital.domain.pharmacy.model.Prescription;
import com.pacman.hospital.domain.pharmacy.repository.MedicineRepository;
import com.pacman.hospital.domain.pharmacy.repository.PrescriptionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Comprehensive DataLoader component that seeds the database with test data.
 * This ensures a complete testing environment with all relationships properly set up.
 */
@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicineRepository medicineRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final LabTestRepository labTestRepository;
    private final LabOrderRepository labOrderRepository;
    private final BillingRepository billingRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("=".repeat(80));
        log.info("🚀 Starting Data Seeding Process...");
        log.info("=".repeat(80));

        // Check if data already exists
        if (userRepository.count() > 0) {
            log.info("⚠️  Data already exists. Clearing all data...");
            clearAllData();
        }

        // Seed in order to maintain referential integrity
        seedRoles();
        List<User> users = seedUsers();
        List<Patient> patients = seedPatients(users);
        List<Doctor> doctors = seedDoctors(users);
        List<Medicine> medicines = seedMedicines();
        List<LabTest> labTests = seedLabTests();
        List<Appointment> appointments = seedAppointments(patients, doctors);
        seedPrescriptions(patients, doctors, medicines);
        seedLabOrders(patients, labTests);
        seedBillings(patients, appointments);
        seedMedicalRecords(patients);

        log.info("=".repeat(80));
        log.info("✅ Data Seeding Completed Successfully!");
        log.info("=".repeat(80));
        printSummary();
    }

    private void clearAllData() {
        log.info("🗑️  Clearing existing data...");

        // Clear in reverse order of dependencies
        try {
            medicalRecordRepository.deleteAll();
            billingRepository.deleteAll();
            labOrderRepository.deleteAll();
            prescriptionRepository.deleteAll();
            appointmentRepository.deleteAll();
            labTestRepository.deleteAll();
            medicineRepository.deleteAll();
            doctorRepository.deleteAll();
            patientRepository.deleteAll();
            userRepository.deleteAll();
            // Don't delete roles as they are standard

            log.info("✅ All data cleared successfully!");
        } catch (Exception e) {
            log.error("❌ Error clearing data: {}", e.getMessage());
        }
    }

    private void seedRoles() {
        log.info("📋 Seeding Roles...");

        for (UserRole userRole : UserRole.values()) {
            String roleName = userRole.getAuthority();

            if (!roleRepository.existsByName(roleName)) {
                Role role = Role.builder().name(roleName).build();
                roleRepository.save(role);
                log.info("   ✓ Created role: {}", roleName);
            }
        }

        log.info("✅ Roles seeded. Total: {}", roleRepository.count());
    }

    private List<User> seedUsers() {
        log.info("👥 Seeding Users...");

        List<User> users = new ArrayList<>();

        // Admin user
        users.add(
            createUser(
                "admin",
                "admin@hospital.com",
                "Admin User",
                "ROLE_ADMIN"
            )
        );

        // Patient users
        users.add(
            createUser(
                "patient1",
                "john.doe@email.com",
                "John Doe",
                "ROLE_PATIENT"
            )
        );
        users.add(
            createUser(
                "patient2",
                "jane.smith@email.com",
                "Jane Smith",
                "ROLE_PATIENT"
            )
        );
        users.add(
            createUser(
                "patient3",
                "bob.johnson@email.com",
                "Bob Johnson",
                "ROLE_PATIENT"
            )
        );
        users.add(
            createUser(
                "patient4",
                "alice.williams@email.com",
                "Alice Williams",
                "ROLE_PATIENT"
            )
        );
        users.add(
            createUser(
                "patient5",
                "charlie.brown@email.com",
                "Charlie Brown",
                "ROLE_PATIENT"
            )
        );

        // Doctor users
        users.add(
            createUser(
                "doctor1",
                "dr.sarah.smith@hospital.com",
                "Dr. Sarah Smith",
                "ROLE_DOCTOR"
            )
        );
        users.add(
            createUser(
                "doctor2",
                "dr.michael.jones@hospital.com",
                "Dr. Michael Jones",
                "ROLE_DOCTOR"
            )
        );
        users.add(
            createUser(
                "doctor3",
                "dr.emily.davis@hospital.com",
                "Dr. Emily Davis",
                "ROLE_DOCTOR"
            )
        );
        users.add(
            createUser(
                "doctor4",
                "dr.james.wilson@hospital.com",
                "Dr. James Wilson",
                "ROLE_DOCTOR"
            )
        );

        log.info("✅ Users seeded. Total: {}", users.size());
        return users;
    }

    private User createUser(
        String username,
        String email,
        String fullName,
        String roleName
    ) {
        Role role = roleRepository
            .findByName(roleName)
            .orElseThrow(() ->
                new RuntimeException("Role not found: " + roleName)
            );

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = User.builder()
            .username(username)
            .email(email)
            .fullName(fullName)
            .password(passwordEncoder.encode("password123"))
            .roles(roles)
            .enabled(true)
            .build();

        user = userRepository.save(user);
        log.info("   ✓ Created user: {} ({})", username, roleName);
        return user;
    }

    private List<Patient> seedPatients(List<User> users) {
        log.info("🏥 Seeding Patients...");

        List<Patient> patients = new ArrayList<>();

        // Get patient users (indices 1-5)
        for (int i = 1; i <= 5 && i < users.size(); i++) {
            User user = users.get(i);
            Patient patient = Patient.builder()
                .userId(user.getId()) // Link patient with user account
                .fullName(user.getFullName())
                .dateOfBirth(LocalDate.now().minusYears(25 + i * 5))
                .gender(i % 2 == 0 ? Gender.FEMALE : Gender.MALE)
                .contactInfo("+880171234567" + i)
                .email(user.getEmail())
                .address(i + " Main Street, Dhaka")
                .emergencyContact("+880198765432" + i)
                .build();

            patients.add(patientRepository.save(patient));
            log.info(
                "   ✓ Created patient: {} (User ID: {})",
                patient.getFullName(),
                user.getId()
            );
        }

        log.info("✅ Patients seeded. Total: {}", patients.size());
        return patients;
    }

    private List<Doctor> seedDoctors(List<User> users) {
        log.info("👨‍⚕️ Seeding Doctors...");

        List<Doctor> doctors = new ArrayList<>();

        String[][] doctorData = {
            {
                "Dr. Sarah Smith",
                "Cardiology",
                "MD, MBBS, Cardiologist",
                "Cardiology",
                "MED12345",
            },
            {
                "Dr. Michael Jones",
                "Neurology",
                "MD, MBBS, Neurologist",
                "Neurology",
                "MED12346",
            },
            {
                "Dr. Emily Davis",
                "Pediatrics",
                "MD, MBBS, Pediatrician",
                "Pediatrics",
                "MED12347",
            },
            {
                "Dr. James Wilson",
                "Orthopedics",
                "MD, MBBS, Orthopedist",
                "Orthopedics",
                "MED12348",
            },
        };

        for (int i = 0; i < doctorData.length && (i + 6) < users.size(); i++) {
            User user = users.get(i + 6); // Doctor users start at index 6
            Doctor doctor = Doctor.builder()
                .fullName(doctorData[i][0])
                .specialization(doctorData[i][1])
                .qualifications(doctorData[i][2])
                .department(doctorData[i][3])
                .licenseNumber(doctorData[i][4])
                .contactInfo("+880181234567" + i)
                .email(user.getEmail())
                .yearsOfExperience(10 + i * 2)
                .consultationFee(new BigDecimal(1000 + i * 500))
                .build();

            doctors.add(doctorRepository.save(doctor));
            log.info(
                "   ✓ Created doctor: {} ({})",
                doctor.getFullName(),
                doctor.getSpecialization()
            );
        }

        log.info("✅ Doctors seeded. Total: {}", doctors.size());
        return doctors;
    }

    private List<Medicine> seedMedicines() {
        log.info("💊 Seeding Medicines...");

        List<Medicine> medicines = new ArrayList<>();

        String[][] medicineData = {
            {
                "Paracetamol 500mg",
                "Pain relief and fever reducer",
                "Generic Pharma",
                "50.00",
            },
            {
                "Amoxicillin 250mg",
                "Antibiotic for bacterial infections",
                "MedLife",
                "120.00",
            },
            {
                "Omeprazole 20mg",
                "Reduces stomach acid",
                "HealthCare Ltd",
                "80.00",
            },
            {
                "Metformin 500mg",
                "Diabetes medication",
                "Diabetic Care",
                "60.00",
            },
            {
                "Atorvastatin 10mg",
                "Cholesterol medication",
                "Heart Care",
                "150.00",
            },
            { "Aspirin 75mg", "Blood thinner", "CardioHealth", "40.00" },
            {
                "Vitamin D3 1000IU",
                "Vitamin supplement",
                "Wellness Co",
                "100.00",
            },
            { "Cetirizine 10mg", "Allergy relief", "AllergyFree", "45.00" },
        };

        for (String[] data : medicineData) {
            Medicine medicine = Medicine.builder()
                .name(data[0])
                .description(data[1])
                .price(new BigDecimal(data[3]))
                .quantityAvailable(100 + (int) (Math.random() * 200))
                .expiryDate(LocalDate.now().plusYears(2))
                .build();

            medicines.add(medicineRepository.save(medicine));
            log.info("   ✓ Created medicine: {}", medicine.getName());
        }

        log.info("✅ Medicines seeded. Total: {}", medicines.size());
        return medicines;
    }

    private List<LabTest> seedLabTests() {
        log.info("🔬 Seeding Lab Tests...");

        List<LabTest> labTests = new ArrayList<>();

        String[][] testData = {
            { "Complete Blood Count (CBC)", "Basic blood test", "500.00" },
            { "Lipid Profile", "Cholesterol and triglycerides test", "800.00" },
            { "Blood Sugar (Fasting)", "Diabetes screening", "300.00" },
            {
                "Liver Function Test (LFT)",
                "Liver health assessment",
                "1200.00",
            },
            {
                "Kidney Function Test (KFT)",
                "Kidney health assessment",
                "1000.00",
            },
            { "Thyroid Profile", "Thyroid function test", "1500.00" },
            { "Urine Analysis", "Basic urine test", "200.00" },
            { "X-Ray Chest", "Chest radiography", "600.00" },
        };

        for (String[] data : testData) {
            LabTest labTest = LabTest.builder()
                .testName(data[0])
                .description(data[1])
                .cost(new BigDecimal(data[2]))
                .build();

            labTests.add(labTestRepository.save(labTest));
            log.info("   ✓ Created lab test: {}", labTest.getTestName());
        }

        log.info("✅ Lab Tests seeded. Total: {}", labTests.size());
        return labTests;
    }

    private List<Appointment> seedAppointments(
        List<Patient> patients,
        List<Doctor> doctors
    ) {
        log.info("📅 Seeding Appointments...");

        List<Appointment> appointments = new ArrayList<>();

        if (patients.isEmpty() || doctors.isEmpty()) {
            log.warn(
                "⚠️  Cannot seed appointments: patients or doctors list is empty"
            );
            return appointments;
        }

        // Create appointments for each patient
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            Doctor doctor = doctors.get(i % doctors.size());

            // Past completed appointment
            Appointment pastAppt = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(LocalDateTime.now().minusDays(15 + i))
                .status(AppointmentStatus.COMPLETED)
                .reason("General checkup")
                .notes("Patient in good health")
                .appointmentType("IN_PERSON")
                .durationMinutes(30)
                .roomNumber("Room " + (100 + i))
                .consultationFee(doctor.getConsultationFee())
                .completedAt(LocalDateTime.now().minusDays(15 + i))
                .build();
            appointments.add(appointmentRepository.save(pastAppt));

            // Upcoming scheduled appointment
            Appointment upcomingAppt = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(LocalDateTime.now().plusDays(7 + i))
                .status(AppointmentStatus.SCHEDULED)
                .reason("Follow-up consultation")
                .notes("Regular checkup scheduled")
                .appointmentType(i % 3 == 0 ? "VIDEO" : "IN_PERSON")
                .durationMinutes(30)
                .roomNumber(i % 3 == 0 ? null : "Room " + (200 + i))
                .consultationFee(doctor.getConsultationFee())
                .build();
            appointments.add(appointmentRepository.save(upcomingAppt));

            log.info(
                "   ✓ Created appointments for patient: {}",
                patient.getFullName()
            );
        }

        log.info("✅ Appointments seeded. Total: {}", appointments.size());
        return appointments;
    }

    private void seedPrescriptions(
        List<Patient> patients,
        List<Doctor> doctors,
        List<Medicine> medicines
    ) {
        log.info("💉 Seeding Prescriptions...");

        if (patients.isEmpty() || doctors.isEmpty() || medicines.isEmpty()) {
            log.warn("⚠️  Cannot seed prescriptions: required data is missing");
            return;
        }

        int count = 0;
        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            Doctor doctor = doctors.get(i % doctors.size());

            // Create 2 prescriptions per patient
            for (int j = 0; j < 2 && (i * 2 + j) < medicines.size(); j++) {
                Medicine medicine = medicines.get(
                    (i * 2 + j) % medicines.size()
                );

                Prescription prescription = Prescription.builder()
                    .patient(patient)
                    .doctor(doctor)
                    .medicine(medicine)
                    .notes(
                        "Take " +
                            (j + 1) +
                            " tablet(s) " +
                            (j + 1) +
                            " times daily for " +
                            (7 + j * 7) +
                            " days. Take after meals. Complete the full course."
                    )
                    .prescribedAt(LocalDateTime.now().minusDays(10 - i))
                    .build();

                prescriptionRepository.save(prescription);
                count++;
            }

            log.info(
                "   ✓ Created prescriptions for patient: {}",
                patient.getFullName()
            );
        }

        log.info("✅ Prescriptions seeded. Total: {}", count);
    }

    private void seedLabOrders(List<Patient> patients, List<LabTest> labTests) {
        log.info("🧪 Seeding Lab Orders...");

        if (patients.isEmpty() || labTests.isEmpty()) {
            log.warn("⚠️  Cannot seed lab orders: required data is missing");
            return;
        }

        int count = 0;
        LabOrderStatus[] statuses = {
            LabOrderStatus.ORDERED,
            LabOrderStatus.IN_PROGRESS,
            LabOrderStatus.COMPLETED,
        };

        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            LabTest labTest = labTests.get(i % labTests.size());
            LabOrderStatus status = statuses[i % statuses.length];

            LabOrder labOrder = LabOrder.builder()
                .id(null) // Auto-generated
                .patient(patient)
                .labTest(labTest)
                .status(status)
                .notes("Routine test ordered")
                .orderedAt(LocalDateTime.now().minusDays(5 + i))
                .build();

            if (status == LabOrderStatus.COMPLETED) {
                labOrder.setReportPath("/reports/lab_report_" + i + ".pdf");
            }

            labOrderRepository.save(labOrder);
            count++;
            log.info(
                "   ✓ Created lab order for patient: {} ({})",
                patient.getFullName(),
                status
            );
        }

        log.info("✅ Lab Orders seeded. Total: {}", count);
    }

    private void seedBillings(
        List<Patient> patients,
        List<Appointment> appointments
    ) {
        log.info("💰 Seeding Billings...");

        if (patients.isEmpty() || appointments.isEmpty()) {
            log.warn("⚠️  Cannot seed billings: required data is missing");
            return;
        }

        int count = 0;
        BillingStatus[] statuses = {
            BillingStatus.PAID,
            BillingStatus.PENDING,
            BillingStatus.OVERDUE,
        };

        for (
            int i = 0;
            i < Math.min(patients.size(), appointments.size());
            i++
        ) {
            Appointment appointment = appointments.get(i * 2); // Use completed appointments
            if (
                appointment.getStatus() != AppointmentStatus.COMPLETED
            ) continue;

            Patient patient = appointment.getPatient();
            BillingStatus status = statuses[i % statuses.length];

            BigDecimal amount = appointment.getConsultationFee() != null
                ? appointment.getConsultationFee()
                : new BigDecimal("1000.00");

            Billing.BillingBuilder billingBuilder = Billing.builder()
                .patient(patient)
                .appointment(appointment)
                .amount(amount)
                .status(status)
                .description("Consultation fee for " + appointment.getReason())
                .billingDate(LocalDateTime.now().minusDays(15 + i))
                .patientPayable(amount);

            if (status == BillingStatus.PAID) {
                billingBuilder
                    .paidAt(LocalDateTime.now().minusDays(10 + i))
                    .paymentMethod("Credit Card");
            }

            Billing billing = billingBuilder.build();

            billingRepository.save(billing);
            count++;
            log.info(
                "   ✓ Created billing for patient: {} ({})",
                patient.getFullName(),
                status
            );
        }

        log.info("✅ Billings seeded. Total: {}", count);
    }

    private void seedMedicalRecords(List<Patient> patients) {
        log.info("📋 Seeding Medical Records...");

        if (patients.isEmpty()) {
            log.warn("⚠️  Cannot seed medical records: patients list is empty");
            return;
        }

        int count = 0;
        String[] recordTypes = {
            "DIAGNOSIS",
            "TREATMENT",
            "LAB_RESULT",
            "PRESCRIPTION",
            "NOTE",
        };
        String[] titles = {
            "Initial Consultation Report",
            "Blood Test Results",
            "Treatment Plan",
            "Follow-up Notes",
            "Prescription Record",
        };

        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);

            // Create 2 records per patient
            for (int j = 0; j < 2; j++) {
                int idx = (i + j) % recordTypes.length;

                MedicalRecord record = MedicalRecord.builder()
                    .patient(patient)
                    .recordType(recordTypes[idx])
                    .title(titles[idx])
                    .content(
                        "Medical record content for " +
                            patient.getFullName() +
                            ". This is a sample medical record entry containing patient health information."
                    )
                    .createdAt(LocalDateTime.now().minusDays(20 - i - j))
                    .build();

                medicalRecordRepository.save(record);
                count++;
            }

            log.info(
                "   ✓ Created medical records for patient: {}",
                patient.getFullName()
            );
        }

        log.info("✅ Medical Records seeded. Total: {}", count);
    }

    private void printSummary() {
        log.info("📊 Database Summary:");
        log.info("   - Roles: {}", roleRepository.count());
        log.info("   - Users: {}", userRepository.count());
        log.info("   - Patients: {}", patientRepository.count());
        log.info("   - Doctors: {}", doctorRepository.count());
        log.info("   - Appointments: {}", appointmentRepository.count());
        log.info("   - Medicines: {}", medicineRepository.count());
        log.info("   - Prescriptions: {}", prescriptionRepository.count());
        log.info("   - Lab Tests: {}", labTestRepository.count());
        log.info("   - Lab Orders: {}", labOrderRepository.count());
        log.info("   - Billings: {}", billingRepository.count());
        log.info("   - Medical Records: {}", medicalRecordRepository.count());
        log.info("");
        log.info("🔑 Test Credentials:");
        log.info("   Admin: admin / password123");
        log.info("   Patient: patient1 / password123");
        log.info("   Doctor: doctor1 / password123");
        log.info("");
        log.info("✨ Ready to test! Visit http://localhost:3000");
    }
}
