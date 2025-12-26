package com.zedcode.module.user.service;

import com.zedcode.common.dto.PageResponse;
import com.zedcode.common.exception.BadRequestException;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for User operations
 * Handles business logic for user management
 *
 * @author ZedCode
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    private static final int MAX_FAILED_LOGIN_ATTEMPTS = 5;

    /**
     * Create a new user
     *
     * @param request the user creation request
     * @return created user DTO
     */
    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        log.info("Creating new user with email: {}", request.getEmail());

        // Validate email uniqueness
        if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
            log.warn("User creation failed: Email already exists - {}", request.getEmail());
            throw new ConflictException("Email already exists: " + request.getEmail());
        }

        // Validate username uniqueness
        if (userRepository.existsByUsernameAndDeletedFalse(request.getUsername())) {
            log.warn("User creation failed: Username already exists - {}", request.getUsername());
            throw new ConflictException("Username already exists: " + request.getUsername());
        }

        // Build user entity
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole() != null ? request.getRole() : UserRole.USER)
                .status(UserStatus.PENDING)
                .bio(request.getBio())
                .emailVerified(false)
                .enabled(true)
                .accountNonLocked(true)
                .failedLoginAttempts(0)
                .deleted(false)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User created successfully with ID: {}", savedUser.getId());

        return userMapper.toDTO(savedUser);
    }

    /**
     * Update user information
     *
     * @param userId  the user ID
     * @param request the update request
     * @return updated user DTO
     */
    @Override
    @Transactional
    public UserDTO updateUser(Long userId, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update email if changed and validate uniqueness
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmailAndDeletedFalse(request.getEmail())) {
                throw new ConflictException("Email already exists: " + request.getEmail());
            }
            user.setEmail(request.getEmail().toLowerCase());
            user.setEmailVerified(false); // Reset email verification
        }

        // Update basic information
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getBio() != null) {
            user.setBio(request.getBio());
        }
        if (request.getProfileImageUrl() != null) {
            user.setProfileImageUrl(request.getProfileImageUrl());
        }

        // Update status (admin only - should be validated in controller)
        if (request.getStatus() != null) {
            try {
                user.setStatus(UserStatus.valueOf(request.getStatus()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid status: " + request.getStatus());
            }
        }

        // Update role (admin only - should be validated in controller)
        if (request.getRole() != null) {
            try {
                user.setRole(UserRole.valueOf(request.getRole()));
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role: " + request.getRole());
            }
        }

        // Update account flags (admin only)
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }
        if (request.getAccountNonLocked() != null) {
            user.setAccountNonLocked(request.getAccountNonLocked());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", updatedUser.getId());

        return userMapper.toDTO(updatedUser);
    }

    /**
     * Get user by ID
     *
     * @param userId the user ID
     * @return user DTO
     */
    @Override
    public UserDTO getUserById(Long userId) {
        log.debug("Fetching user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return userMapper.toDTO(user);
    }

    /**
     * Get user by email
     *
     * @param email the email address
     * @return user DTO
     */
    @Override
    public UserDTO getUserByEmail(String email) {
        log.debug("Fetching user with email: {}", email);

        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        return userMapper.toDTO(user);
    }

    /**
     * Get user by username
     *
     * @param username the username
     * @return user DTO
     */
    @Override
    public UserDTO getUserByUsername(String username) {
        log.debug("Fetching user with username: {}", username);

        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        return userMapper.toDTO(user);
    }

    /**
     * Get all users with pagination
     *
     * @param pageable pagination information
     * @return page response with users
     */
    @Override
    public PageResponse<UserDTO> getAllUsers(Pageable pageable) {
        log.debug("Fetching all users with pagination: {}", pageable);

        Page<User> userPage = userRepository.findByDeletedFalse(pageable);
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return buildPageResponse(userPage, userDTOs);
    }

    /**
     * Search users by term
     *
     * @param searchTerm the search term
     * @param pageable   pagination information
     * @return page response with matching users
     */
    @Override
    public PageResponse<UserDTO> searchUsers(String searchTerm, Pageable pageable) {
        log.debug("Searching users with term: {}", searchTerm);

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllUsers(pageable);
        }

        Page<User> userPage = userRepository.searchUsers(searchTerm, pageable);
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return buildPageResponse(userPage, userDTOs);
    }

    /**
     * Get users by role
     *
     * @param role     the user role
     * @param pageable pagination information
     * @return page response with users
     */
    @Override
    public PageResponse<UserDTO> getUsersByRole(UserRole role, Pageable pageable) {
        log.debug("Fetching users with role: {}", role);

        Page<User> userPage = userRepository.findByRole(role, pageable);
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return buildPageResponse(userPage, userDTOs);
    }

    /**
     * Get users by status
     *
     * @param status   the user status
     * @param pageable pagination information
     * @return page response with users
     */
    @Override
    public PageResponse<UserDTO> getUsersByStatus(UserStatus status, Pageable pageable) {
        log.debug("Fetching users with status: {}", status);

        Page<User> userPage = userRepository.findByStatusAndDeletedFalse(status, pageable);
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return buildPageResponse(userPage, userDTOs);
    }

    /**
     * Delete user (soft delete)
     *
     * @param userId the user ID
     */
    @Override
    @Transactional
    public void deleteUser(Long userId) {
        log.info("Soft deleting user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setDeleted(true);
        user.setDeletedAt(LocalDateTime.now());
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);

        log.info("User soft deleted successfully with ID: {}", userId);
    }

    /**
     * Permanently delete user
     *
     * @param userId the user ID
     */
    @Override
    @Transactional
    public void permanentlyDeleteUser(Long userId) {
        log.warn("Permanently deleting user with ID: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        userRepository.deleteById(userId);
        log.warn("User permanently deleted with ID: {}", userId);
    }

    /**
     * Activate user account
     *
     * @param userId the user ID
     * @return updated user DTO
     */
    @Override
    @Transactional
    public UserDTO activateUser(Long userId) {
        log.info("Activating user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(UserStatus.ACTIVE);
        user.setEnabled(true);
        User updatedUser = userRepository.save(user);

        log.info("User activated successfully with ID: {}", userId);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Deactivate user account
     *
     * @param userId the user ID
     * @return updated user DTO
     */
    @Override
    @Transactional
    public UserDTO deactivateUser(Long userId) {
        log.info("Deactivating user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(UserStatus.INACTIVE);
        user.setEnabled(false);
        User updatedUser = userRepository.save(user);

        log.info("User deactivated successfully with ID: {}", userId);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Block user account
     *
     * @param userId the user ID
     * @return updated user DTO
     */
    @Override
    @Transactional
    public UserDTO blockUser(Long userId) {
        log.info("Blocking user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(UserStatus.BLOCKED);
        user.setEnabled(false);
        user.setAccountNonLocked(false);
        User updatedUser = userRepository.save(user);

        log.info("User blocked successfully with ID: {}", userId);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Unblock user account
     *
     * @param userId the user ID
     * @return updated user DTO
     */
    @Override
    @Transactional
    public UserDTO unblockUser(Long userId) {
        log.info("Unblocking user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setStatus(UserStatus.ACTIVE);
        user.setEnabled(true);
        user.setAccountNonLocked(true);
        user.setFailedLoginAttempts(0);
        User updatedUser = userRepository.save(user);

        log.info("User unblocked successfully with ID: {}", userId);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Verify user email
     *
     * @param userId the user ID
     * @return updated user DTO
     */
    @Override
    @Transactional
    public UserDTO verifyEmail(Long userId) {
        log.info("Verifying email for user with ID: {}", userId);

        User user = userRepository.findByIdAndDeletedFalse(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setEmailVerified(true);
        if (user.getStatus() == UserStatus.PENDING) {
            user.setStatus(UserStatus.ACTIVE);
        }
        User updatedUser = userRepository.save(user);

        log.info("Email verified successfully for user with ID: {}", userId);
        return userMapper.toDTO(updatedUser);
    }

    /**
     * Update last login time
     *
     * @param userId the user ID
     */
    @Override
    @Transactional
    public void updateLastLogin(Long userId) {
        log.debug("Updating last login for user with ID: {}", userId);
        userRepository.updateLastLogin(userId);
    }

    /**
     * Handle failed login attempt
     *
     * @param userId the user ID
     */
    @Override
    @Transactional
    public void handleFailedLogin(Long userId) {
        log.warn("Failed login attempt for user with ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        int failedAttempts = user.getFailedLoginAttempts() + 1;
        user.setFailedLoginAttempts(failedAttempts);

        if (failedAttempts >= MAX_FAILED_LOGIN_ATTEMPTS) {
            log.warn("Max failed login attempts reached. Locking account for user ID: {}", userId);
            user.setAccountNonLocked(false);
            user.setStatus(UserStatus.BLOCKED);
        }

        userRepository.save(user);
    }

    /**
     * Check if email exists
     *
     * @param email the email address
     * @return true if email exists
     */
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailAndDeletedFalse(email);
    }

    /**
     * Check if username exists
     *
     * @param username the username
     * @return true if username exists
     */
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameAndDeletedFalse(username);
    }

    /**
     * Count total users
     *
     * @return total user count
     */
    @Override
    public long countTotalUsers() {
        return userRepository.countByDeletedFalse();
    }

    /**
     * Count users by status
     *
     * @param status the user status
     * @return user count
     */
    @Override
    public long countUsersByStatus(UserStatus status) {
        return userRepository.countByStatusAndDeletedFalse(status);
    }

    /**
     * Count users by role
     *
     * @param role the user role
     * @return user count
     */
    @Override
    public long countUsersByRole(UserRole role) {
        return userRepository.countByRole(role);
    }

    /**
     * Build page response from Page object
     */
    /**
     * Build a PageResponse from Spring's Page object
     *
     * UNDERSTANDING THE GENERIC TYPES HERE:
     * ======================================
     *
     * 1. <T> before the method name means this is a GENERIC METHOD
     *    - T can be ANY type (UserDTO, ProductDTO, etc.)
     *    - The actual type is determined when the method is called
     *
     * 2. PageResponse<T> is the return type
     *    - If T is UserDTO, this returns PageResponse<UserDTO>
     *    - If T is ProductDTO, this returns PageResponse<ProductDTO>
     *
     * 3. Page<?> means "a Page containing UNKNOWN type"
     *    - The ? is called a "wildcard"
     *    - We don't care what type is in the Spring Page object
     *    - We only need the pagination metadata (page number, size, etc.)
     *
     * 4. List<T> is our actual data
     *    - This is the list we've already converted (e.g., from User to UserDTO)
     *    - This must match the <T> in PageResponse<T>
     *
     * EXAMPLE USAGE:
     * ==============
     * Page<User> userPage = userRepository.findAll(pageable);        // Spring Page of User entities
     * List<UserDTO> userDTOs = userMapper.toDTOList(userPage.getContent());  // Convert to DTOs
     * PageResponse<UserDTO> response = buildPageResponse(userPage, userDTOs); // Build response
     *
     * In this example:
     * - Page<?> matches Page<User> (we ignore the User part, just need metadata)
     * - List<T> is List<UserDTO>
     * - <T> becomes UserDTO
     * - Return type becomes PageResponse<UserDTO>
     *
     * WHY NOT USE Page<T> INSTEAD OF Page<?>?
     * ========================================
     * Because the Page object contains User entities, but our List contains UserDTOs!
     * They're different types, so we use Page<?> to say "we don't care about the entity type,
     * just give us the pagination metadata"
     *
     * @param <T> The type of items in the content list (e.g., UserDTO)
     * @param page Spring's Page object containing pagination metadata
     * @param content Our converted list of DTOs
     * @return PageResponse with our DTOs and pagination info from Spring's Page
     */
    private <T> PageResponse<T> buildPageResponse(Page<?> page, List<T> content) {
        // Build PageResponse using the Builder pattern
        // PageResponse.<T>builder() explicitly tells Java what type we're building
        return PageResponse.<T>builder()
                .content(content)                              // Our converted DTOs
                .pageNumber(page.getNumber())                  // Current page number (0-based)
                .pageSize(page.getSize())                      // Items per page
                .totalElements(page.getTotalElements())        // Total items in database
                .totalPages(page.getTotalPages())              // Total number of pages
                .last(page.isLast())                          // Is this the last page?
                .first(page.isFirst())                        // Is this the first page?
                .hasNext(page.hasNext())                      // Is there a next page?
                .hasPrevious(page.hasPrevious())              // Is there a previous page?
                .numberOfElements(page.getNumberOfElements()) // Items in current page
                .empty(page.isEmpty())                        // Is page empty?
                .build();
    }
</text>

}
