package com.puspo.codearena.authservice.integration;

import com.puspo.codearena.authservice.entity.User;
import com.puspo.codearena.authservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class DatabaseMigrationIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserTableStructure() {
        // Test that we can create and persist a User entity with all fields
        User user = User.builder()
                .username("integrationtest")
                .email("integration@test.com")
                .password("encodedPassword")
                .firstName("Integration")
                .lastName("Test")
                .role(User.Role.USER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Persist the user
        User savedUser = entityManager.persistAndFlush(user);

        // Verify all fields are correctly saved
        assertNotNull(savedUser.getId());
        assertEquals("integrationtest", savedUser.getUsername());
        assertEquals("integration@test.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("Integration", savedUser.getFirstName());
        assertEquals("Test", savedUser.getLastName());
        assertEquals(User.Role.USER, savedUser.getRole());
        assertTrue(savedUser.getIsEnabled());
        assertTrue(savedUser.getIsAccountNonExpired());
        assertTrue(savedUser.getIsAccountNonLocked());
        assertTrue(savedUser.getIsCredentialsNonExpired());
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());
    }

    @Test
    public void testUserRoleEnum() {
        // Test all role types can be persisted
        User userRole = createUserWithRole("user1", User.Role.USER);
        User adminRole = createUserWithRole("admin1", User.Role.ADMIN);
        User moderatorRole = createUserWithRole("moderator1", User.Role.MODERATOR);

        entityManager.persistAndFlush(userRole);
        entityManager.persistAndFlush(adminRole);
        entityManager.persistAndFlush(moderatorRole);

        // Verify roles are correctly saved
        assertEquals(User.Role.USER, userRole.getRole());
        assertEquals(User.Role.ADMIN, adminRole.getRole());
        assertEquals(User.Role.MODERATOR, moderatorRole.getRole());
    }

    @Test
    public void testUniqueConstraints() {
        // Test username unique constraint
        User user1 = createUserWithRole("uniquetest", User.Role.USER);
        user1.setEmail("unique1@test.com");
        entityManager.persistAndFlush(user1);

        User user2 = createUserWithRole("uniquetest", User.Role.USER);
        user2.setEmail("unique2@test.com");

        // This should fail due to username constraint
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(user2);
        });
    }

    @Test
    public void testEmailUniqueConstraints() {
        // Test email unique constraint
        User user1 = createUserWithRole("unique1", User.Role.USER);
        user1.setEmail("same@test.com");
        entityManager.persistAndFlush(user1);

        User user2 = createUserWithRole("unique2", User.Role.USER);
        user2.setEmail("same@test.com");

        // This should fail due to email constraint
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(user2);
        });
    }

    @Test
    public void testRepositoryOperations() {
        // Create and save users
        User user1 = createUserWithRole("repo1", User.Role.USER);
        user1.setEmail("repo1@test.com");
        User user2 = createUserWithRole("repo2", User.Role.ADMIN);
        user2.setEmail("repo2@test.com");

        userRepository.save(user1);
        userRepository.save(user2);

        // Test findByUsername
        Optional<User> foundByUsername = userRepository.findByUsername("repo1");
        assertTrue(foundByUsername.isPresent());
        assertEquals("repo1", foundByUsername.get().getUsername());

        // Test findByEmail
        Optional<User> foundByEmail = userRepository.findByEmail("repo2@test.com");
        assertTrue(foundByEmail.isPresent());
        assertEquals("repo2", foundByEmail.get().getUsername());

        // Test findByUsernameOrEmail
        Optional<User> foundByUsernameOrEmail1 = userRepository.findByUsernameOrEmail("repo1", "wrongemail@test.com");
        assertTrue(foundByUsernameOrEmail1.isPresent());

        Optional<User> foundByUsernameOrEmail2 = userRepository.findByUsernameOrEmail("wrongusername", "repo2@test.com");
        assertTrue(foundByUsernameOrEmail2.isPresent());

        // Test existsByUsername
        assertTrue(userRepository.existsByUsername("repo1"));
        assertFalse(userRepository.existsByUsername("nonexistent"));

        // Test existsByEmail
        assertTrue(userRepository.existsByEmail("repo1@test.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@test.com"));

        // Test findAll
        List<User> allUsers = userRepository.findAll();
        assertTrue(allUsers.size() >= 2);
    }

    @Test
    public void testBooleanDefaults() {
        // Test that boolean fields have correct defaults
        User user = User.builder()
                .username("defaulttest")
                .email("default@test.com")
                .password("password")
                .firstName("Default")
                .lastName("Test")
                .role(User.Role.USER)
                .build();

        User savedUser = entityManager.persistAndFlush(user);

        // Verify defaults are applied
        assertTrue(savedUser.getIsEnabled());
        assertTrue(savedUser.getIsAccountNonExpired());
        assertTrue(savedUser.getIsAccountNonLocked());
        assertTrue(savedUser.getIsCredentialsNonExpired());
    }

    @Test
    public void testTimestampFields() {
        // Test created_at and updated_at functionality
        User user = createUserWithRole("timestamptest", User.Role.USER);
        user.setEmail("timestamp@test.com");

        LocalDateTime beforeSave = LocalDateTime.now();
        User savedUser = entityManager.persistAndFlush(user);
        LocalDateTime afterSave = LocalDateTime.now();

        // Verify timestamps are set
        assertNotNull(savedUser.getCreatedAt());
        assertNotNull(savedUser.getUpdatedAt());
        assertTrue(savedUser.getCreatedAt().isAfter(beforeSave.minusSeconds(1)));
        assertTrue(savedUser.getCreatedAt().isBefore(afterSave.plusSeconds(1)));

        // Test update timestamp
        LocalDateTime originalUpdatedAt = savedUser.getUpdatedAt();
        try {
            Thread.sleep(10); // Small delay to ensure timestamp difference
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        savedUser.setFirstName("Updated");
        savedUser.setUpdatedAt(LocalDateTime.now());
        User updatedUser = entityManager.persistAndFlush(savedUser);

        assertTrue(updatedUser.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    public void testLastLogin() {
        // Test last_login field
        User user = createUserWithRole("logintest", User.Role.USER);
        user.setEmail("login@test.com");

        // Initially last_login should be null
        User savedUser = entityManager.persistAndFlush(user);
        assertNull(savedUser.getLastLogin());

        // Set last login
        LocalDateTime loginTime = LocalDateTime.now();
        savedUser.setLastLogin(loginTime);
        User updatedUser = entityManager.persistAndFlush(savedUser);

        assertNotNull(updatedUser.getLastLogin());
        assertEquals(loginTime.withNano(0), updatedUser.getLastLogin().withNano(0));
    }

    private User createUserWithRole(String username, User.Role role) {
        return User.builder()
                .username(username)
                .email(username + "@test.com")
                .password("password123")
                .firstName("Test")
                .lastName("User")
                .role(role)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
