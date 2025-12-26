package com.zedcode.module.user.service;

import com.zedcode.common.dto.PageResponse;
import com.zedcode.module.user.dto.CreateUserRequest;
import com.zedcode.module.user.dto.UpdateUserRequest;
import com.zedcode.module.user.dto.UserDTO;
import com.zedcode.module.user.entity.User.UserRole;
import com.zedcode.module.user.entity.User.UserStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 * User Service Interface
 * Defines business logic operations for user management
 *
 * @author ZedCode
 * @version 1.0
 */
public interface UserService {
    /**
     * Create a new user
     *
     * @param request the create user request
     * @return created user DTO
     */
    UserDTO createUser(CreateUserRequest request);

    /**
     * Update an existing user
     *
     * @param id      the user ID
     * @param request the update user request
     * @return updated user DTO
     */
    UserDTO updateUser(Long id, UpdateUserRequest request);

    /**
     * Get user by ID
     *
     * @param id the user ID
     * @return user DTO
     */
    UserDTO getUserById(Long id);

    /**
     * Get user by email
     *
     * @param email the email address
     * @return user DTO
     */
    UserDTO getUserByEmail(String email);

    /**
     * Get user by username
     *
     * @param username the username
     * @return user DTO
     */
    UserDTO getUserByUsername(String username);

    /**
     * Get all users with pagination
     *
     * @param pageable pagination information
     * @return page of users
     */
    PageResponse<UserDTO> getAllUsers(Pageable pageable);

    /**
     * Search users by search term
     *
     * @param searchTerm the search term
     * @param pageable   pagination information
     * @return page of users matching the search
     */
    PageResponse<UserDTO> searchUsers(String searchTerm, Pageable pageable);

    /**
     * Get users by status
     *
     * @param status   the user status
     * @param pageable pagination information
     * @return page of users with the specified status
     */
    PageResponse<UserDTO> getUsersByStatus(
        UserStatus status,
        Pageable pageable
    );

    /**
     * Get users by role
     *
     * @param role     the user role
     * @param pageable pagination information
     * @return page of users with the specified role
     */
    PageResponse<UserDTO> getUsersByRole(UserRole role, Pageable pageable);

    /**
     * Delete a user (soft delete)
     *
     * @param id the user ID
     */
    void deleteUser(Long id);

    /**
     * Permanently delete a user
     *
     * @param id the user ID
     */
    void permanentlyDeleteUser(Long id);

    /**
     * Check if email exists
     *
     * @param email the email address
     * @return true if email exists
     */
    boolean existsByEmail(String email);

    /**
     * Check if username exists
     *
     * @param username the username
     * @return true if username exists
     */
    boolean existsByUsername(String username);

    /**
     * Activate a user account
     *
     * @param id the user ID
     * @return activated user DTO
     */
    UserDTO activateUser(Long id);

    /**
     * Deactivate a user account
     *
     * @param id the user ID
     * @return deactivated user DTO
     */
    UserDTO deactivateUser(Long id);

    /**
     * Block a user account
     *
     * @param id the user ID
     * @return blocked user DTO
     */
    UserDTO blockUser(Long id);

    /**
     * Unblock a user account
     *
     * @param id the user ID
     * @return unblocked user DTO
     */
    UserDTO unblockUser(Long id);

    /**
     * Verify user email
     *
     * @param id the user ID
     * @return verified user DTO
     */
    UserDTO verifyEmail(Long id);

    /**
     * Update last login time
     *
     * @param userId the user ID
     */
    void updateLastLogin(Long userId);

    /**
     * Handle failed login attempt
     *
     * @param userId the user ID
     */
    void handleFailedLogin(Long userId);

    /**
     * Count total users
     *
     * @return total number of users
     */
    long countTotalUsers();

    /**
     * Count users by status
     *
     * @param status the user status
     * @return count of users with the specified status
     */
    long countUsersByStatus(UserStatus status);

    /**
     * Count users by role
     *
     * @param role the user role
     * @return count of users with the specified role
     */
    long countUsersByRole(UserRole role);
}
