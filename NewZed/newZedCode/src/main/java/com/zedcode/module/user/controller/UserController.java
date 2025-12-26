package com.zedcode.module.user.controller;

import com.zedcode.common.dto.ApiResponse;
import com.zedcode.common.dto.PageResponse;
import com.zedcode.module.user.dto.CreateUserRequest;
import com.zedcode.module.user.dto.UpdateUserRequest;
import com.zedcode.module.user.dto.UserDTO;
import com.zedcode.module.user.entity.User.UserRole;
import com.zedcode.module.user.entity.User.UserStatus;
import com.zedcode.module.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for User Management
 * Provides endpoints for CRUD operations and user management
 *
 * @author ZedCode
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    /**
     * Create a new user
     *
     * @param request the user creation request
     * @return created user
     */
    @PostMapping
    @Operation(
        summary = "Create new user",
        description = "Creates a new user account. Admin role required."
    )
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "201",
                description = "User created successfully",
                content = @Content(
                    schema = @Schema(implementation = UserDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "400",
                description = "Invalid request data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "409",
                description = "User already exists"
            ),
        }
    )
    @PreAuthorize("hasRole('ADMIN')")
    // UNDERSTANDING THE RETURN TYPE:
    // ResponseEntity<ApiResponse<UserDTO>> means:
    // - ResponseEntity = HTTP response wrapper (contains status code, headers, body)
    // - ApiResponse<UserDTO> = Our standard response format
    // - UserDTO = The actual data type being returned
    // So this returns an HTTP response containing our ApiResponse which contains a UserDTO
    public ResponseEntity<ApiResponse<UserDTO>> createUser(
        @Valid @RequestBody CreateUserRequest request
    ) {
        log.info("REST request to create user: {}", request.getEmail());

        // Call service to create user - returns UserDTO
        UserDTO createdUser = userService.createUser(request);

        // Build HTTP response:
        // 1. ResponseEntity.status(201) = HTTP 201 Created
        // 2. ApiResponse.success(...) = Wraps UserDTO in our standard response format
        // 3. Result: { "success": true, "message": "...", "data": { user object } }
        return ResponseEntity.status(HttpStatus.CREATED).body(
            ApiResponse.success(createdUser, "User created successfully")
        );
    }

    /**
     * Get user by ID
     *
     * @param id the user ID
     * @return user details
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves user details by their ID"
    )
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "User found",
                content = @Content(
                    schema = @Schema(implementation = UserDTO.class)
                )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "User not found"
            ),
        }
    )
    // UNDERSTANDING THE RETURN TYPE:
    // ResponseEntity<ApiResponse<UserDTO>>
    // Same as createUser - returns HTTP response with ApiResponse containing UserDTO
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.info("REST request to get user by ID: {}", id);

        // Get user from service
        UserDTO user = userService.getUserById(id);

        // ResponseEntity.ok() = HTTP 200 OK
        // Returns: { "success": true, "message": "...", "data": { user object } }
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Get user by email
     *
     * @param email the user email
     * @return user details
     */
    @GetMapping("/email/{email}")
    @Operation(
        summary = "Get user by email",
        description = "Retrieves user details by their email address"
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUserByEmail(
        @Parameter(
            description = "User email",
            required = true
        ) @PathVariable String email
    ) {
        log.info("REST request to get user by email: {}", email);

        UserDTO user = userService.getUserByEmail(email);

        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Get user by username
     *
     * @param username the username
     * @return user details
     */
    @GetMapping("/username/{username}")
    @Operation(
        summary = "Get user by username",
        description = "Retrieves user details by their username"
    )
    public ResponseEntity<ApiResponse<UserDTO>> getUserByUsername(
        @Parameter(
            description = "Username",
            required = true
        ) @PathVariable String username
    ) {
        log.info("REST request to get user by username: {}", username);

        UserDTO user = userService.getUserByUsername(username);

        return ResponseEntity.ok(ApiResponse.success(user));
    }

    /**
     * Get all users with pagination
     *
     * @param page page number (0-indexed)
     * @param size page size
     * @param sort sort field and direction (e.g., "id,desc")
     * @return paginated list of users
     */
    @GetMapping
    @Operation(
        summary = "Get all users",
        description = "Retrieves all users with pagination and sorting"
    )
    @PreAuthorize("hasRole('ADMIN')")
    // UNDERSTANDING THE RETURN TYPE (Nested Generics!):
    // ResponseEntity<ApiResponse<PageResponse<UserDTO>>> - Let's break it down:
    //
    // 1. ResponseEntity = HTTP response wrapper
    // 2. ApiResponse<...> = Our standard response format
    // 3. PageResponse<UserDTO> = Paginated list of UserDTO objects
    // 4. UserDTO = Individual user data
    //
    // So this returns: HTTP response → ApiResponse → PageResponse → List of UserDTOs
    //
    // JSON structure:
    // {
    //   "success": true,
    //   "message": "...",
    //   "data": {
    //     "content": [ { user1 }, { user2 }, { user3 } ],
    //     "pageNumber": 0,
    //     "pageSize": 10,
    //     "totalElements": 100
    //   }
    // }
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getAllUsers(
        @Parameter(description = "Page number (0-indexed)") @RequestParam(
            defaultValue = "0"
        ) int page,
        @Parameter(description = "Page size") @RequestParam(
            defaultValue = "10"
        ) int size,
        @Parameter(
            description = "Sort criteria (e.g., 'id,desc' or 'email,asc')"
        ) @RequestParam(defaultValue = "id,desc") String sort
    ) {
        log.info(
            "REST request to get all users - page: {}, size: {}, sort: {}",
            page,
            size,
            sort
        );

        // Create pagination parameters
        Pageable pageable = createPageable(page, size, sort);

        // Get paginated users from service
        // PageResponse<UserDTO> contains List<UserDTO> + pagination info
        PageResponse<UserDTO> users = userService.getAllUsers(pageable);

        // Wrap PageResponse in ApiResponse, then in ResponseEntity
        // This gives us consistent response format with pagination
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Search users
     *
     * @param searchTerm search term
     * @param page       page number
     * @param size       page size
     * @param sort       sort criteria
     * @return paginated search results
     */
    @GetMapping("/search")
    @Operation(
        summary = "Search users",
        description = "Search users by name, email, or username"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> searchUsers(
        @Parameter(description = "Search term") @RequestParam String searchTerm,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String sort
    ) {
        log.info("REST request to search users with term: {}", searchTerm);

        Pageable pageable = createPageable(page, size, sort);
        PageResponse<UserDTO> users = userService.searchUsers(
            searchTerm,
            pageable
        );

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Get users by status
     *
     * @param status user status
     * @param page   page number
     * @param size   page size
     * @param sort   sort criteria
     * @return paginated list of users with specified status
     */
    @GetMapping("/status/{status}")
    @Operation(
        summary = "Get users by status",
        description = "Retrieves users filtered by status"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsersByStatus(
        @Parameter(
            description = "User status (ACTIVE, INACTIVE, PENDING, BLOCKED, DELETED)"
        ) @PathVariable UserStatus status,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String sort
    ) {
        log.info("REST request to get users by status: {}", status);

        Pageable pageable = createPageable(page, size, sort);
        PageResponse<UserDTO> users = userService.getUsersByStatus(
            status,
            pageable
        );

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Get users by role
     *
     * @param role user role
     * @param page page number
     * @param size page size
     * @param sort sort criteria
     * @return paginated list of users with specified role
     */
    @GetMapping("/role/{role}")
    @Operation(
        summary = "Get users by role",
        description = "Retrieves users filtered by role"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getUsersByRole(
        @Parameter(
            description = "User role (USER, ADMIN, MODERATOR, SUPER_ADMIN)"
        ) @PathVariable UserRole role,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id,desc") String sort
    ) {
        log.info("REST request to get users by role: {}", role);

        Pageable pageable = createPageable(page, size, sort);
        PageResponse<UserDTO> users = userService.getUsersByRole(
            role,
            pageable
        );

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    /**
     * Update user
     *
     * @param id      user ID
     * @param request update request
     * @return updated user
     */
    @PutMapping("/{id}")
    @Operation(
        summary = "Update user",
        description = "Updates user information"
    )
    @ApiResponses(
        value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "200",
                description = "User updated successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                responseCode = "404",
                description = "User not found"
            ),
        }
    )
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        log.info("REST request to update user with ID: {}", id);

        UserDTO updatedUser = userService.updateUser(id, request);

        return ResponseEntity.ok(
            ApiResponse.success(updatedUser, "User updated successfully")
        );
    }

    /**
     * Delete user (soft delete)
     *
     * @param id user ID
     * @return success message
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete user",
        description = "Soft delete a user account"
    )
    @PreAuthorize("hasRole('ADMIN')")
    // UNDERSTANDING THE RETURN TYPE:
    // ResponseEntity<ApiResponse<Void>>
    // - Void means "no data" (only success message, no data field)
    // - Used for operations that don't need to return data (delete, update status, etc.)
    public ResponseEntity<ApiResponse<Void>> deleteUser(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.info("REST request to delete user: {}", id);

        // Delete the user (soft delete - just marks as deleted)
        userService.deleteUser(id);

        // Return success message without data
        // JSON: { "success": true, "message": "User deleted successfully" }
        return ResponseEntity.ok(
            ApiResponse.success("User deleted successfully")
        );
    }

    /**
     * Permanently delete user
     *
     * @param id user ID
     * @return success message
     */
    @DeleteMapping("/{id}/permanent")
    @Operation(
        summary = "Permanently delete user",
        description = "Permanently deletes a user (cannot be restored)"
    )
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> permanentlyDeleteUser(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.warn("REST request to permanently delete user with ID: {}", id);

        userService.permanentlyDeleteUser(id);

        return ResponseEntity.ok(
            ApiResponse.success("User permanently deleted")
        );
    }

    /**
     * Activate user
     *
     * @param id user ID
     * @return activated user
     */
    @PatchMapping("/{id}/activate")
    @Operation(
        summary = "Activate user",
        description = "Activates a user account"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> activateUser(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.info("REST request to activate user with ID: {}", id);

        UserDTO user = userService.activateUser(id);

        return ResponseEntity.ok(
            ApiResponse.success(user, "User activated successfully")
        );
    }

    /**
     * Deactivate user
     *
     * @param id user ID
     * @return deactivated user
     */
    @PatchMapping("/{id}/deactivate")
    @Operation(
        summary = "Deactivate user",
        description = "Deactivates a user account"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> deactivateUser(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.info("REST request to deactivate user with ID: {}", id);

        UserDTO user = userService.deactivateUser(id);

        return ResponseEntity.ok(
            ApiResponse.success(user, "User deactivated successfully")
        );
    }

    /**
     * Block user
     *
     * @param id user ID
     * @return blocked user
     */
    @PatchMapping("/{id}/block")
    @Operation(summary = "Block user", description = "Blocks a user account")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> blockUser(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.info("REST request to block user with ID: {}", id);

        UserDTO user = userService.blockUser(id);

        return ResponseEntity.ok(
            ApiResponse.success(user, "User blocked successfully")
        );
    }

    /**
     * Unblock user
     *
     * @param id user ID
     * @return unblocked user
     */
    @PatchMapping("/{id}/unblock")
    @Operation(
        summary = "Unblock user",
        description = "Unblocks a user account"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> unblockUser(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.info("REST request to unblock user with ID: {}", id);

        UserDTO user = userService.unblockUser(id);

        return ResponseEntity.ok(
            ApiResponse.success(user, "User unblocked successfully")
        );
    }

    /**
     * Verify user email
     *
     * @param id user ID
     * @return user with verified email
     */
    @PatchMapping("/{id}/verify-email")
    @Operation(
        summary = "Verify user email",
        description = "Marks user's email as verified"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserDTO>> verifyEmail(
        @Parameter(
            description = "User ID",
            required = true
        ) @PathVariable Long id
    ) {
        log.info("REST request to verify email for user with ID: {}", id);

        UserDTO user = userService.verifyEmail(id);

        return ResponseEntity.ok(
            ApiResponse.success(user, "Email verified successfully")
        );
    }

    /**
     * Check if email exists
     *
     * @param email email address
     * @return true if exists
     */
    @GetMapping("/exists/email")
    @Operation(
        summary = "Check if email exists",
        description = "Checks if an email address is already registered"
    )
    public ResponseEntity<ApiResponse<Boolean>> checkEmailExists(
        @Parameter(description = "Email address") @RequestParam String email
    ) {
        log.debug("REST request to check if email exists: {}", email);

        boolean exists = userService.existsByEmail(email);

        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    /**
     * Check if username exists
     *
     * @param username username
     * @return true if exists
     */
    @GetMapping("/exists/username")
    @Operation(
        summary = "Check if username exists",
        description = "Checks if a username is already taken"
    )
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameExists(
        @Parameter(description = "Username") @RequestParam String username
    ) {
        log.debug("REST request to check if username exists: {}", username);

        boolean exists = userService.existsByUsername(username);

        return ResponseEntity.ok(ApiResponse.success(exists));
    }

    /**
     * Get user statistics
     *
     * @return user statistics
     */
    @GetMapping("/stats")
    @Operation(
        summary = "Get user statistics",
        description = "Retrieves statistics about users"
    )
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<UserStats>> getUserStats() {
        log.info("REST request to get user statistics");

        long totalUsers = userService.countTotalUsers();
        long activeUsers = userService.countUsersByStatus(UserStatus.ACTIVE);
        long inactiveUsers = userService.countUsersByStatus(
            UserStatus.INACTIVE
        );
        long blockedUsers = userService.countUsersByStatus(UserStatus.BLOCKED);
        long pendingUsers = userService.countUsersByStatus(UserStatus.PENDING);

        long adminCount = userService.countUsersByRole(UserRole.ADMIN);
        long userCount = userService.countUsersByRole(UserRole.USER);
        long moderatorCount = userService.countUsersByRole(UserRole.MODERATOR);

        UserStats stats = UserStats.builder()
            .totalUsers(totalUsers)
            .activeUsers(activeUsers)
            .inactiveUsers(inactiveUsers)
            .blockedUsers(blockedUsers)
            .pendingUsers(pendingUsers)
            .adminCount(adminCount)
            .userCount(userCount)
            .moderatorCount(moderatorCount)
            .build();

        return ResponseEntity.ok(ApiResponse.success(stats));
    }

    /**
     * Helper method to create Pageable object
     */
    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 &&
            sortParams[1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC
            : Sort.Direction.ASC;

        return PageRequest.of(page, size, Sort.by(direction, sortField));
    }

    /**
     * Inner class for user statistics
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class UserStats {

        private long totalUsers;
        private long activeUsers;
        private long inactiveUsers;
        private long blockedUsers;
        private long pendingUsers;
        private long adminCount;
        private long userCount;
        private long moderatorCount;
    }
}
