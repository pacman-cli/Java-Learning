package com.puspo.codearena.authservice.service;

import com.puspo.codearena.authservice.dto.UserListResponseDto;
import com.puspo.codearena.authservice.dto.UserResponseDto;
import com.puspo.codearena.authservice.entity.User;
import com.puspo.codearena.authservice.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private User testAdmin;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .role(User.Role.USER)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testAdmin = User.builder()
                .id(2L)
                .username("admin")
                .email("admin@example.com")
                .password("encodedPassword")
                .firstName("Admin")
                .lastName("User")
                .role(User.Role.ADMIN)
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void findUserById_ExistingUser_ReturnsUserResponseDto() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        UserResponseDto result = userService.findUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(testUser.getEmail(), result.getEmail());
        verify(userRepository).findById(1L);
    }

    @Test
    void findUserById_NonExistingUser_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.findUserById(999L));
        verify(userRepository).findById(999L);
    }

    @Test
    void findUserByUsername_ExistingUser_ReturnsUserResponseDto() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        UserResponseDto result = userService.findUserByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        verify(userRepository).findByUsername("testuser");
    }

    @Test
    void findUserByUsername_NonExistingUser_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.findUserByUsername("nonexistent"));
        verify(userRepository).findByUsername("nonexistent");
    }

    @Test
    void findAllUsers_ReturnsPagedResponse() {
        // Arrange
        List<User> users = Arrays.asList(testUser, testAdmin);
        Page<User> userPage = new PageImpl<>(users, PageRequest.of(0, 10), 2);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Act
        UserListResponseDto result = userService.findAllUsers(0, 10, "createdAt", "desc");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getUsers().size());
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());
        verify(userRepository).findAll(any(Pageable.class));
    }

    @Test
    void findUsersByRole_ReturnsUsersWithSpecificRole() {
        // Arrange
        List<User> allUsers = Arrays.asList(testUser, testAdmin);
        when(userRepository.findAll()).thenReturn(allUsers);

        // Act
        List<UserResponseDto> result = userService.findUsersByRole(User.Role.USER);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(User.Role.USER, result.get(0).getRole());
        verify(userRepository).findAll();
    }

    @Test
    void updateUserRole_ExistingUser_UpdatesRoleSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDto result = userService.updateUserRole(1L, User.Role.ADMIN);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUserRole_NonExistingUser_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> userService.updateUserRole(999L, User.Role.ADMIN));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void enableUser_ExistingUser_EnablesUserSuccessfully() {
        // Arrange
        testUser.setIsEnabled(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDto result = userService.enableUser(1L);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void disableUser_ExistingUser_DisablesUserSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDto result = userService.disableUser(1L);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void lockUser_ExistingUser_LocksUserSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDto result = userService.lockUser(1L);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void unlockUser_ExistingUser_UnlocksUserSuccessfully() {
        // Arrange
        testUser.setIsAccountNonLocked(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDto result = userService.unlockUser(1L);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void changePassword_ExistingUser_ChangesPasswordSuccessfully() {
        // Arrange
        String newPassword = "newPassword123";
        String encodedPassword = "encodedNewPassword";

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        UserResponseDto result = userService.changePassword(1L, newPassword);

        // Assert
        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateLastLogin_ExistingUser_UpdatesLastLoginSuccessfully() {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        // Act
        userService.updateLastLogin("testuser");

        // Assert
        verify(userRepository).findByUsername("testuser");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateLastLogin_NonExistingUser_DoesNothing() {
        // Arrange
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // Act
        userService.updateLastLogin("nonexistent");

        // Assert
        verify(userRepository).findByUsername("nonexistent");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ExistingUser_DeletesUserSuccessfully() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        userService.deleteUser(1L);

        // Assert
        verify(userRepository).findById(1L);
        verify(userRepository).delete(testUser);
    }

    @Test
    void deleteUser_NonExistingUser_ThrowsException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.deleteUser(999L));
        verify(userRepository).findById(999L);
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    void existsByUsername_ExistingUser_ReturnsTrue() {
        // Arrange
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        // Act
        boolean result = userService.existsByUsername("testuser");

        // Assert
        assertTrue(result);
        verify(userRepository).existsByUsername("testuser");
    }

    @Test
    void existsByUsername_NonExistingUser_ReturnsFalse() {
        // Arrange
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        // Act
        boolean result = userService.existsByUsername("nonexistent");

        // Assert
        assertFalse(result);
        verify(userRepository).existsByUsername("nonexistent");
    }

    @Test
    void countUsers_ReturnsCorrectCount() {
        // Arrange
        when(userRepository.count()).thenReturn(5L);

        // Act
        long result = userService.countUsers();

        // Assert
        assertEquals(5L, result);
        verify(userRepository).count();
    }

    @Test
    void findActiveUsers_ReturnsOnlyEnabledUsers() {
        // Arrange
        User disabledUser = User.builder()
                .id(3L)
                .username("disabled")
                .email("disabled@example.com")
                .isEnabled(false)
                .role(User.Role.USER)
                .build();

        List<User> allUsers = Arrays.asList(testUser, testAdmin, disabledUser);
        when(userRepository.findAll()).thenReturn(allUsers);

        // Act
        List<UserResponseDto> result = userService.findActiveUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(user -> user.getIsEnabled()));
        verify(userRepository).findAll();
    }

    @Test
    void findRecentUsers_ReturnsLimitedResults() {
        // Arrange
        List<User> users = Arrays.asList(testUser, testAdmin);
        Page<User> userPage = new PageImpl<>(users);

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        // Act
        List<UserResponseDto> result = userService.findRecentUsers(5);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll(any(Pageable.class));
    }
}
