package com.pacman.hospital.core.security.config;

import com.pacman.hospital.core.security.model.Role;
import com.pacman.hospital.core.security.model.User;
import com.pacman.hospital.core.security.model.UserRole;
import com.pacman.hospital.core.security.repository.RoleRepository;
import com.pacman.hospital.core.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeDefaultUsers();
    }

    private void initializeRoles() {
        for (UserRole userRole : UserRole.values()) {
            if (!roleRepository.existsByName(userRole.getAuthority())) {
                Role role = Role.builder()
                        .name(userRole.getAuthority())
                        .build();
                roleRepository.save(role);
                log.info("Created role: {}", userRole.getAuthority());
            }
        }
    }

    private void initializeDefaultUsers() {
        // Create default admin user if not exists
        if (!userRepository.existsByUsername("admin")) {
            Role adminRole = roleRepository.findByName(UserRole.ADMIN.getAuthority())
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Administrator")
                    .email("admin@hospital.com")
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .roles(Set.of(adminRole))
                    .build();

            userRepository.save(admin);
            log.info("Created default admin user: admin/admin123");
        }

        // Create default doctor user if not exists
        if (!userRepository.existsByUsername("doctor")) {
            Role doctorRole = roleRepository.findByName(UserRole.DOCTOR.getAuthority())
                    .orElseThrow(() -> new RuntimeException("Doctor role not found"));

            User doctor = User.builder()
                    .username("doctor")
                    .password(passwordEncoder.encode("doctor123"))
                    .fullName("Dr. John Smith")
                    .email("doctor@hospital.com")
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .roles(Set.of(doctorRole))
                    .build();

            userRepository.save(doctor);
            log.info("Created default doctor user: doctor/doctor123");
        }

        // Create default patient user if not exists
        if (!userRepository.existsByUsername("patient")) {
            Role patientRole = roleRepository.findByName(UserRole.PATIENT.getAuthority())
                    .orElseThrow(() -> new RuntimeException("Patient role not found"));

            User patient = User.builder()
                    .username("patient")
                    .password(passwordEncoder.encode("patient123"))
                    .fullName("Jane Doe")
                    .email("patient@hospital.com")
                    .enabled(true)
                    .createdAt(LocalDateTime.now())
                    .roles(Set.of(patientRole))
                    .build();

            userRepository.save(patient);
            log.info("Created default patient user: patient/patient123");
        }
    }
}
