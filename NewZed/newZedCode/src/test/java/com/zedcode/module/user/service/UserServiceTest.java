package com.zedcode.module.user.service;

import com.zedcode.common.exception.ConflictException;
import com.zedcode.common.exception.ResourceNotFoundException;
import com.zedcode.module.user.dto.CreateUserRequest;
import com.zedcode.module.user.dto.UpdateUserRequest;
import com.zedcode.module.user.dto.UserDTO;
import com.zedcode.module.user.entity.User;
import com.zedcode.module.user.entity.User.UserRole;
import com.zedcode.module.user.entity.User.UserStatus;
import com.zedcode.module.user.mapper.UserMapper;
import com.zedcode.module.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserServiceImpl
 * Tests the business logic of user management operations
 *
 * @author ZedCode
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;
    private CreateUserRequest createUserRequest;
    private UpdateUserRequest updateUserRequest;

    @BeforeEach
    void setUp() {
        // Setup test user entity
        testUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john.doe@example.com")
                .password("encodedPassword123")
                .phoneNumber("+1234567890")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .enabled(true)
                .accountNonLocked(true)
                .failedLoginAttempts(0)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Setup test user DTO
        testUserDTO = UserDTO.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .username("johndoe")
                .email("john.doe@example.com")
                .phoneNumber("+1234567890")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .enabled(true)
                .accountNonLocked(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Setup create user request
        createUserRequest = CreateUserRequest.builder()
                .firstName("Jane")
                .lastName("Smith")
                .username("janesmith")
                .email("jane.smith@example.com")
                .password("SecurePass123!")
                .phoneNumber("+1234567891")
                .role(UserRole.USER)
                .build();

        // Setup update user request
        updateUserRequest = UpdateUserRequest.builder()
                .firstName("John Updated")
                .lastName("Doe Updated")
                .phoneNumber("+9876543210")
                .bio("Updated bio")
                .build();
    }

    // ============================================================
    // CREATE USER TESTS
    // ============================================================

    @Test
    @DisplayName("Should create user successfully when valid request is provided")
    void createUser_WithValidRequest_ShouldReturnUserDTO() {
        // Arrange
        when(userRepository.existsByEmailAndDeletedFalse(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameAndDeletedFalse(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.createUser(createUserRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(result.getUsername()).isEqualTo("johndoe");

        verify(userRepository).existsByEmailAndDeletedFalse(anyString());
        verify(userRepository).existsByUsernameAndDeletedFalse(anyString());
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDTO(any(User.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when email already exists")
    void createUser_WithExistingEmail_ShouldThrowConflictException() {
        // Arrange
        when(userRepository.existsByEmailAndDeletedFalse(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(createUserRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Email already exists");

        verify(userRepository).existsByEmailAndDeletedFalse(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ConflictException when username already exists")
    void createUser_WithExistingUsername_ShouldThrowConflictException() {
        // Arrange
        when(userRepository.existsByEmailAndDeletedFalse(anyString())).thenReturn(false);
        when(userRepository.existsByUsernameAndDeletedFalse(anyString())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.createUser(createUserRequest))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Username already exists");

        verify(userRepository).existsByEmailAndDeletedFalse(anyString());
        verify(userRepository).existsByUsernameAndDeletedFalse(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    // ============================================================
    // GET USER TESTS
    // ============================================================

    @Test
    @DisplayName("Should get user by ID successfully when user exists")
    void getUserById_WithExistingId_ShouldReturnUserDTO() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.getUserById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(testUser.getEmail());

        verify(userRepository).findByIdAndDeletedFalse(1L);
        verify(userMapper).toDTO(testUser);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user ID does not exist")
    void getUserById_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getUserById(999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User")
                .hasMessageContaining("id")
                .hasMessageContaining("999");

        verify(userRepository).findByIdAndDeletedFalse(999L);
        verify(userMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Should get user by email successfully when user exists")
    void getUserByEmail_WithExistingEmail_ShouldReturnUserDTO() {
        // Arrange
        String email = "john.doe@example.com";
        when(userRepository.findByEmailAndDeletedFalse(email)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.getUserByEmail(email);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);

        verify(userRepository).findByEmailAndDeletedFalse(email);
        verify(userMapper).toDTO(testUser);
    }

    @Test
    @DisplayName("Should get user by username successfully when user exists")
    void getUserByUsername_WithExistingUsername_ShouldReturnUserDTO() {
        // Arrange
        String username = "johndoe";
        when(userRepository.findByUsernameAndDeletedFalse(username)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.getUserByUsername(username);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);

        verify(userRepository).findByUsernameAndDeletedFalse(username);
        verify(userMapper).toDTO(testUser);
    }

    // ============================================================
    // UPDATE USER TESTS
    // ============================================================

    @Test
    @DisplayName("Should update user successfully when valid request is provided")
    void updateUser_WithValidRequest_ShouldReturnUpdatedUserDTO() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.updateUser(1L, updateUserRequest);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).findByIdAndDeletedFalse(1L);
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDTO(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existing user")
    void updateUser_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.updateUser(999L, updateUserRequest))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository).findByIdAndDeletedFalse(999L);
        verify(userRepository, never()).save(any());
    }

    // ============================================================
    // DELETE USER TESTS
    // ============================================================

    @Test
    @DisplayName("Should soft delete user successfully when user exists")
    void deleteUser_WithExistingId_ShouldSoftDeleteUser() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).findByIdAndDeletedFalse(1L);
        verify(userRepository).save(argThat(user ->
            user.getDeleted() &&
            user.getStatus() == UserStatus.DELETED &&
            user.getDeletedAt() != null
        ));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existing user")
    void deleteUser_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.deleteUser(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(userRepository).findByIdAndDeletedFalse(999L);
        verify(userRepository, never()).save(any());
    }

    // ============================================================
    // ACTIVATE/DEACTIVATE USER TESTS
    // ============================================================

    @Test
    @DisplayName("Should activate user successfully")
    void activateUser_WithExistingId_ShouldActivateUser() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.activateUser(1L);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(argThat(user ->
            user.getStatus() == UserStatus.ACTIVE &&
            user.getEnabled()
        ));
    }

    @Test
    @DisplayName("Should deactivate user successfully")
    void deactivateUser_WithExistingId_ShouldDeactivateUser() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.deactivateUser(1L);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(argThat(user ->
            user.getStatus() == UserStatus.INACTIVE &&
            !user.getEnabled()
        ));
    }

    // ============================================================
    // BLOCK/UNBLOCK USER TESTS
    // ============================================================

    @Test
    @DisplayName("Should block user successfully")
    void blockUser_WithExistingId_ShouldBlockUser() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.blockUser(1L);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(argThat(user ->
            user.getStatus() == UserStatus.BLOCKED &&
            !user.getEnabled() &&
            !user.getAccountNonLocked()
        ));
    }

    @Test
    @DisplayName("Should unblock user successfully")
    void unblockUser_WithExistingId_ShouldUnblockUser() {
        // Arrange
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.unblockUser(1L);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(argThat(user ->
            user.getStatus() == UserStatus.ACTIVE &&
            user.getEnabled() &&
            user.getAccountNonLocked() &&
            user.getFailedLoginAttempts() == 0
        ));
    }

    // ============================================================
    // EMAIL VERIFICATION TESTS
    // ============================================================

    @Test
    @DisplayName("Should verify email successfully")
    void verifyEmail_WithExistingId_ShouldVerifyEmail() {
        // Arrange
        testUser.setEmailVerified(false);
        testUser.setStatus(UserStatus.PENDING);
        when(userRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(any(User.class))).thenReturn(testUserDTO);

        // Act
        UserDTO result = userService.verifyEmail(1L);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(argThat(user ->
            user.getEmailVerified() &&
            user.getStatus() == UserStatus.ACTIVE
        ));
    }

    // ============================================================
    // LOGIN TESTS
    // ============================================================

    @Test
    @DisplayName("Should update last login successfully")
    void updateLastLogin_WithExistingId_ShouldUpdateLastLogin() {
        // Arrange
        when(userRepository.updateLastLogin(1L)).thenReturn(1);

        // Act
        userService.updateLastLogin(1L);

        // Assert
        verify(userRepository).updateLastLogin(1L);
    }

    @Test
    @DisplayName("Should handle failed login and lock account after max attempts")
    void handleFailedLogin_WithMaxAttempts_ShouldLockAccount() {
        // Arrange
        testUser.setFailedLoginAttempts(4); // One before max
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.handleFailedLogin(1L);

        // Assert
        verify(userRepository).save(argThat(user ->
            user.getFailedLoginAttempts() == 5 &&
            !user.getAccountNonLocked() &&
            user.getStatus() == UserStatus.BLOCKED
        ));
    }

    // ============================================================
    // EXISTENCE CHECKS
    // ============================================================

    @Test
    @DisplayName("Should return true when email exists")
    void existsByEmail_WithExistingEmail_ShouldReturnTrue() {
        // Arrange
        when(userRepository.existsByEmailAndDeletedFalse(anyString())).thenReturn(true);

        // Act
        boolean result = userService.existsByEmail("test@example.com");

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).existsByEmailAndDeletedFalse(anyString());
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void existsByEmail_WithNonExistingEmail_ShouldReturnFalse() {
        // Arrange
        when(userRepository.existsByEmailAndDeletedFalse(anyString())).thenReturn(false);

        // Act
        boolean result = userService.existsByEmail("nonexisting@example.com");

        // Assert
        assertThat(result).isFalse();
        verify(userRepository).existsByEmailAndDeletedFalse(anyString());
    }

    @Test
    @DisplayName("Should return true when username exists")
    void existsByUsername_WithExistingUsername_ShouldReturnTrue() {
        // Arrange
        when(userRepository.existsByUsernameAndDeletedFalse(anyString())).thenReturn(true);

        // Act
        boolean result = userService.existsByUsername("johndoe");

        // Assert
        assertThat(result).isTrue();
        verify(userRepository).existsByUsernameAndDeletedFalse(anyString());
    }

    // ============================================================
    // COUNT TESTS
    // ============================================================

    @Test
    @DisplayName("Should return total user count")
    void countTotalUsers_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByDeletedFalse()).thenReturn(100L);

        // Act
        long result = userService.countTotalUsers();

        // Assert
        assertThat(result).isEqualTo(100L);
        verify(userRepository).countByDeletedFalse();
    }

    @Test
    @DisplayName("Should return user count by status")
    void countUsersByStatus_WithStatus_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByStatusAndDeletedFalse(UserStatus.ACTIVE)).thenReturn(75L);

        // Act
        long result = userService.countUsersByStatus(UserStatus.ACTIVE);

        // Assert
        assertThat(result).isEqualTo(75L);
        verify(userRepository).countByStatusAndDeletedFalse(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should return user count by role")
    void countUsersByRole_WithRole_ShouldReturnCount() {
        // Arrange
        when(userRepository.countByRole(UserRole.ADMIN)).thenReturn(5L);

        // Act
        long result = userService.countUsersByRole(UserRole.ADMIN);

        // Assert
        assertThat(result).isEqualTo(5L);
        verify(userRepository).countByRole(UserRole.ADMIN);
    }
}
