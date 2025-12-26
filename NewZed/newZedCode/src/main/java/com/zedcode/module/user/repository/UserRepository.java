package com.zedcode.module.user.repository;

import com.zedcode.module.user.entity.User;
import com.zedcode.module.user.entity.User.UserRole;
import com.zedcode.module.user.entity.User.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entity
 * Provides CRUD operations and custom queries for user management
 *
 * @author ZedCode
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Find user by email
     *
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by username
     *
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Find user by email or username
     *
     * @param email    the email address
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> findByEmailOrUsername(String email, String username);

    /**
     * Find user by email and not deleted
     *
     * @param email the email address
     * @return Optional containing the user if found
     */
    Optional<User> findByEmailAndDeletedFalse(String email);

    /**
     * Find user by username and not deleted
     *
     * @param username the username
     * @return Optional containing the user if found
     */
    Optional<User> findByUsernameAndDeletedFalse(String username);

    /**
     * Find user by ID and not deleted
     *
     * @param id the user ID
     * @return Optional containing the user if found
     */
    Optional<User> findByIdAndDeletedFalse(Long id);

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
     * Check if email exists and not deleted
     *
     * @param email the email address
     * @return true if email exists and not deleted
     */
    boolean existsByEmailAndDeletedFalse(String email);

    /**
     * Check if username exists and not deleted
     *
     * @param username the username
     * @return true if username exists and not deleted
     */
    boolean existsByUsernameAndDeletedFalse(String username);

    /**
     * Find all users by status
     *
     * @param status   the user status
     * @param pageable pagination information
     * @return Page of users
     */
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    /**
     * Find all users by role
     *
     * @param role     the user role
     * @param pageable pagination information
     * @return Page of users
     */
    Page<User> findByRole(UserRole role, Pageable pageable);

    /**
     * Find all active users
     *
     * @param pageable pagination information
     * @return Page of active users
     */
    Page<User> findByStatusAndDeletedFalse(UserStatus status, Pageable pageable);

    /**
     * Find all users by role and status
     *
     * @param role     the user role
     * @param status   the user status
     * @param pageable pagination information
     * @return Page of users
     */
    Page<User> findByRoleAndStatus(UserRole role, UserStatus status, Pageable pageable);

    /**
     * Find all non-deleted users
     *
     * @param pageable pagination information
     * @return Page of non-deleted users
     */
    Page<User> findByDeletedFalse(Pageable pageable);

    /**
     * Find users by email verification status
     *
     * @param emailVerified the email verification status
     * @param pageable      pagination information
     * @return Page of users
     */
    Page<User> findByEmailVerified(Boolean emailVerified, Pageable pageable);

    /**
     * Search users by first name, last name, email, or username
     *
     * @param searchTerm the search term
     * @param pageable   pagination information
     * @return Page of matching users
     */
    @Query("SELECT u FROM User u WHERE " +
            "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
            "u.deleted = false")
    Page<User> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find users created between dates
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @param pageable  pagination information
     * @return Page of users
     */
    Page<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Find users by last login after date
     *
     * @param date     the date
     * @param pageable pagination information
     * @return Page of users
     */
    Page<User> findByLastLoginAtAfter(LocalDateTime date, Pageable pageable);

    /**
     * Count users by status
     *
     * @param status the user status
     * @return count of users
     */
    long countByStatus(UserStatus status);

    /**
     * Count users by role
     *
     * @param role the user role
     * @return count of users
     */
    long countByRole(UserRole role);

    /**
     * Count active users
     *
     * @return count of active users
     */
    long countByStatusAndDeletedFalse(UserStatus status);

    /**
     * Count non-deleted users
     *
     * @return count of non-deleted users
     */
    long countByDeletedFalse();

    /**
     * Update user status
     *
     * @param userId the user ID
     * @param status the new status
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.status = :status, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    int updateUserStatus(@Param("userId") Long userId, @Param("status") UserStatus status);

    /**
     * Update last login time
     *
     * @param userId the user ID
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = CURRENT_TIMESTAMP, u.failedLoginAttempts = 0 WHERE u.id = :userId")
    int updateLastLogin(@Param("userId") Long userId);

    /**
     * Increment failed login attempts
     *
     * @param userId the user ID
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = u.failedLoginAttempts + 1 WHERE u.id = :userId")
    int incrementFailedLoginAttempts(@Param("userId") Long userId);

    /**
     * Reset failed login attempts
     *
     * @param userId the user ID
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.failedLoginAttempts = 0 WHERE u.id = :userId")
    int resetFailedLoginAttempts(@Param("userId") Long userId);

    /**
     * Lock user account
     *
     * @param userId the user ID
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = false WHERE u.id = :userId")
    int lockUserAccount(@Param("userId") Long userId);

    /**
     * Unlock user account
     *
     * @param userId the user ID
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.accountNonLocked = true WHERE u.id = :userId")
    int unlockUserAccount(@Param("userId") Long userId);

    /**
     * Soft delete user
     *
     * @param userId the user ID
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.deleted = true, u.deletedAt = CURRENT_TIMESTAMP WHERE u.id = :userId")
    int softDeleteUser(@Param("userId") Long userId);

    /**
     * Verify user email
     *
     * @param userId the user ID
     * @return number of rows updated
     */
    @Modifying
    @Query("UPDATE User u SET u.emailVerified = true WHERE u.id = :userId")
    int verifyUserEmail(@Param("userId") Long userId);

    /**
     * Find all admins
     *
     * @return list of admin users
     */
    @Query("SELECT u FROM User u WHERE u.role IN ('ADMIN', 'SUPER_ADMIN') AND u.deleted = false")
    List<User> findAllAdmins();

    /**
     * Find users with failed login attempts exceeding threshold
     *
     * @param threshold the threshold
     * @return list of users
     */
    List<User> findByFailedLoginAttemptsGreaterThanEqual(Integer threshold);

    /**
     * Find inactive users (not logged in for specified days)
     *
     * @param date the cutoff date
     * @return list of inactive users
     */
    @Query("SELECT u FROM User u WHERE u.lastLoginAt < :date OR u.lastLoginAt IS NULL")
    List<User> findInactiveUsers(@Param("date") LocalDateTime date);
}
