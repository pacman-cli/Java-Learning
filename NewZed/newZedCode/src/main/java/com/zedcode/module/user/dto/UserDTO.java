package com.zedcode.module.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zedcode.module.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * User Data Transfer Object
 * Used for transferring user data between layers
 * Excludes sensitive information like passwords
 *
 * @author ZedCode
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    /**
     * User's unique identifier
     */
    private Long id;

    /**
     * User's first name
     */
    private String firstName;

    /**
     * User's last name
     */
    private String lastName;

    /**
     * User's full name (computed)
     */
    private String fullName;

    /**
     * User's unique username
     */
    private String username;

    /**
     * User's email address
     */
    private String email;

    /**
     * User's phone number
     */
    private String phoneNumber;

    /**
     * User's role in the system
     */
    private User.UserRole role;

    /**
     * User's account status
     */
    private User.UserStatus status;

    /**
     * Whether the user's email is verified
     */
    private Boolean emailVerified;

    /**
     * Whether the user's account is enabled
     */
    private Boolean enabled;

    /**
     * Whether the user's account is locked
     */
    private Boolean accountNonLocked;

    /**
     * Last login timestamp
     */
    private LocalDateTime lastLoginAt;

    /**
     * User's profile image URL
     */
    private String profileImageUrl;

    /**
     * User's bio or description
     */
    private String bio;

    /**
     * Timestamp when the user was created
     */
    private LocalDateTime createdAt;

    /**
     * Timestamp when the user was last updated
     */
    private LocalDateTime updatedAt;
}
