package com.zedcode.module.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for updating user information
 * Contains fields that can be modified after user creation
 *
 * @author ZedCode
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateUserRequest {

    /**
     * User's first name
     */
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    /**
     * User's last name
     */
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    /**
     * User's email address
     */
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * User's phone number
     */
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phoneNumber;

    /**
     * User's bio or description
     */
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;

    /**
     * URL to user's profile image
     */
    @Size(max = 500, message = "Profile image URL must not exceed 500 characters")
    private String profileImageUrl;

    /**
     * User's status (admin only)
     * Possible values: ACTIVE, INACTIVE, PENDING, BLOCKED, DELETED
     */
    private String status;

    /**
     * User's role (admin only)
     * Possible values: USER, ADMIN, MODERATOR, SUPER_ADMIN
     */
    private String role;

    /**
     * Whether the user's account is enabled (admin only)
     */
    private Boolean enabled;

    /**
     * Whether the user's account is locked (admin only)
     */
    private Boolean accountNonLocked;
}
