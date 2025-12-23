package com.zedcode.module.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User Entity
 * Represents a user in the system
 *
 * @author ZedCode
 * @version 1.0
 */
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /**
     * Unique identifier for the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User's first name
     */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    /**
     * User's last name
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    /**
     * User's unique username
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    /**
     * User's email address
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * User's encrypted password
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * User's phone number
     */
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    /**
     * User's role in the system
     * Possible values: USER, ADMIN, MODERATOR, SUPER_ADMIN
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    @Builder.Default
    private UserRole role = UserRole.USER;

    /**
     * User's account status
     * Possible values: ACTIVE, INACTIVE, PENDING, BLOCKED, DELETED
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @Builder.Default
    private UserStatus status = UserStatus.PENDING;

    /**
     * Whether the user's email is verified
     */
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = false;

    /**
     * Whether the user's account is enabled
     */
    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    /**
     * Whether the user's account is locked
     */
    @Column(name = "account_non_locked", nullable = false)
    @Builder.Default
    private Boolean accountNonLocked = true;

    /**
     * Number of failed login attempts
     */
    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    /**
     * Last login date and time
     */
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    /**
     * User's profile image URL
     */
    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    /**
     * User's bio or description
     */
    @Column(name = "bio", length = 500)
    private String bio;

    /**
     * Timestamp when the user was created
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user was last updated
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Soft delete flag
     */
    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    /**
     * Timestamp when the user was deleted (soft delete)
     */
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * User Role Enum
     */
    public enum UserRole {
        USER,
        ADMIN,
        MODERATOR,
        SUPER_ADMIN
    }

    /**
     * User Status Enum
     */
    public enum UserStatus {
        ACTIVE,
        INACTIVE,
        PENDING,
        BLOCKED,
        DELETED
    }

    /**
     * Get full name of the user
     *
     * @return full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Check if user is active
     *
     * @return true if user status is ACTIVE
     */
    public boolean isActive() {
        return UserStatus.ACTIVE.equals(this.status);
    }

    /**
     * Check if user is admin
     *
     * @return true if user role is ADMIN or SUPER_ADMIN
     */
    public boolean isAdmin() {
        return UserRole.ADMIN.equals(this.role) || UserRole.SUPER_ADMIN.equals(this.role);
    }
}
