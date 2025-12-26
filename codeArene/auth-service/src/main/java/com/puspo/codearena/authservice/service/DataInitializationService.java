package com.puspo.codearena.authservice.service;

import com.puspo.codearena.authservice.entity.User;
import com.puspo.codearena.authservice.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class DataInitializationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting data initialization...");

        // Check if data already exists (this will run after Flyway migrations)
        long userCount = userRepository.count();
        log.info("Current user count: {}", userCount);

        if (userCount > 0) {
            log.info("Data already exists, skipping initialization");
            return;
        }

        // This should not run if Flyway migrations have already populated data
        // But keeping as backup initialization
        log.info("No users found, initializing with seed data...");
        initializeUsers();

        log.info("Data initialization completed successfully");
    }

    private void initializeUsers() {
        // Create admin user
        createUserIfNotExists(
            "admin",
            "admin@codearena.com",
            "password123",
            "System",
            "Administrator",
            User.Role.ADMIN
        );

        // Create moderator user
        createUserIfNotExists(
            "moderator",
            "moderator@codearena.com",
            "password123",
            "John",
            "Moderator",
            User.Role.MODERATOR
        );

        // Create test users
        createUserIfNotExists(
            "testuser",
            "testuser@codearena.com",
            "password123",
            "Test",
            "User",
            User.Role.USER
        );

        createUserIfNotExists(
            "alice.smith",
            "alice.smith@example.com",
            "password123",
            "Alice",
            "Smith",
            User.Role.USER
        );

        createUserIfNotExists(
            "bob.johnson",
            "bob.johnson@example.com",
            "password123",
            "Bob",
            "Johnson",
            User.Role.USER
        );

        createUserIfNotExists(
            "charlie.brown",
            "charlie.brown@example.com",
            "password123",
            "Charlie",
            "Brown",
            User.Role.USER
        );

        createUserIfNotExists(
            "diana.prince",
            "diana.prince@example.com",
            "password123",
            "Diana",
            "Prince",
            User.Role.MODERATOR
        );

        createUserIfNotExists(
            "eve.adams",
            "eve.adams@example.com",
            "password123",
            "Eve",
            "Adams",
            User.Role.USER
        );

        log.info("Created {} seed users", userRepository.count());
    }

    private void createUserIfNotExists(
        String username,
        String email,
        String plainPassword,
        String firstName,
        String lastName,
        User.Role role
    ) {
        if (userRepository.existsByUsername(username)) {
            log.debug("User {} already exists, skipping creation", username);
            return;
        }

        User user = User.builder()
            .username(username)
            .email(email)
            .password(passwordEncoder.encode(plainPassword))
            .firstName(firstName)
            .lastName(lastName)
            .role(role)
            .isEnabled(true)
            .isAccountNonExpired(true)
            .isAccountNonLocked(true)
            .isCredentialsNonExpired(true)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        userRepository.save(user);
        log.debug("Created user: {}", username);
    }
}
