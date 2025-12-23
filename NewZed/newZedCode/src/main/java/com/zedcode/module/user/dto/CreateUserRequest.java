package com.zedcode.module.user.dto;

import com.zedcode.module.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new user
 * Contains all required fields for user registration
 *
 * @author ZedCode
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

    /**
     * User's first name
     * Required, must be between 2 and 50 characters
     */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    /**
     * User's last name
     * Required, must be between 2 and 50 characters
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    /**
     * User's unique username
     * Required, must be between 3 and 20 characters
     * Can only contain letters, numbers, dots, underscores, and hyphens
     */
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]+$",
            message = "Username can only contain letters, numbers, dots, underscores, and hyphens"
    )
    private String username;

    /**
     * User's email address
     * Required, must be a valid email format
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    /**
     * User's password
     * Required, must be at least 8 characters
     * Should contain uppercase, lowercase, number, and special character
     */
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;

    /**
     * User's phone number (optional)
     * If provided, must match valid phone number format
     */
    @Pattern(
            regexp = "^[+]?[0-9]{10,15}$",
            message = "Phone number must be between 10 and 15 digits"
    )
    private String phoneNumber;

    /**
     * User's role (optional)
     * If not provided, defaults to USER
     * Allowed values: USER, ADMIN, MODERATOR, SUPER_ADMIN
     */
    private User.UserRole role;

    /**
     * User's bio or description (optional)
     */
    @Size(max = 500, message = "Bio must not exceed 500 characters")
    private String bio;
}
