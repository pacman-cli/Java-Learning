package com.zedcode.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API Response Wrapper
 *
 * This class wraps all API responses in a consistent format.
 *
 * WHAT IS <T>?
 * ------------
 * <T> is a "Generic Type" - think of it as a placeholder for ANY type of data.
 *
 * Examples:
 * - ApiResponse<UserDTO> means the data will be a UserDTO object
 * - ApiResponse<String> means the data will be a String
 * - ApiResponse<List<UserDTO>> means the data will be a List of UserDTOs
 *
 * WHY USE GENERICS?
 * -----------------
 * Instead of creating separate classes like:
 * - UserApiResponse
 * - ProductApiResponse
 * - OrderApiResponse
 *
 * We create ONE class (ApiResponse<T>) that works with ANY type!
 *
 * JSON Response Example:
 * {
 *   "success": true,
 *   "message": "User found successfully",
 *   "data": { "id": 1, "name": "John" },
 *   "timestamp": "2024-01-15T10:30:00"
 * }
 *
 * @param <T> The type of data this response contains (e.g., UserDTO, String, List, etc.)
 * @author ZedCode
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Don't include null fields in JSON
public class ApiResponse<T> {

    /**
     * Indicates whether the request was successful
     * - true = Operation succeeded
     * - false = Operation failed (error occurred)
     */
    private boolean success;

    /**
     * Human-readable message about the result
     * Examples: "User created successfully", "Error: Email already exists"
     */
    private String message;

    /**
     * The actual data payload - this is where <T> is used!
     *
     * This field can hold ANY type of data:
     * - A single object (UserDTO)
     * - A list of objects (List<UserDTO>)
     * - A page of objects (PageResponse<UserDTO>)
     * - Or even just a simple String or number
     */
    private T data;

    /**
     * Timestamp when the response was created
     * Automatically set to current time
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Optional metadata (extra information)
     * Example: pagination info, counts, etc.
     */
    private Object metadata;

    // ==================== SUCCESS RESPONSE METHODS ====================

    /**
     * Create a SUCCESS response with data
     *
     * Usage Example:
     * UserDTO user = new UserDTO();
     * return ApiResponse.success(user);
     *
     * Result JSON:
     * {
     *   "success": true,
     *   "message": "Operation successful",
     *   "data": { user object here },
     *   "timestamp": "2024-01-15T10:30:00"
     * }
     *
     * @param <T> The type of data being returned
     * @param data The actual data to return
     * @return ApiResponse with success=true and the data
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .message("Operation successful")
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Create a SUCCESS response with data and a custom message
     *
     * Usage Example:
     * return ApiResponse.success(user, "User created successfully");
     *
     * Result JSON:
     * {
     *   "success": true,
     *   "message": "User created successfully",
     *   "data": { user object here },
     *   "timestamp": "2024-01-15T10:30:00"
     * }
     *
     * @param <T> The type of data being returned
     * @param data The actual data to return
     * @param message Custom success message
     * @return ApiResponse with success=true, custom message, and data
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Create a SUCCESS response with just a message (no data)
     *
     * Usage Example:
     * return ApiResponse.success("User deleted successfully");
     *
     * Result JSON:
     * {
     *   "success": true,
     *   "message": "User deleted successfully",
     *   "timestamp": "2024-01-15T10:30:00"
     * }
     *
     * Note: data field will be null and won't appear in JSON (because of @JsonInclude)
     *
     * @param <T> The type of data (not used here, but needed for type safety)
     * @param message Success message
     * @return ApiResponse with success=true and message only
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Create a SUCCESS response with data, custom message, and metadata
     *
     * Usage Example:
     * Map<String, Object> meta = new HashMap<>();
     * meta.put("totalCount", 100);
     * return ApiResponse.success(users, "Users retrieved", meta);
     *
     * Result JSON:
     * {
     *   "success": true,
     *   "message": "Users retrieved",
     *   "data": [ list of users ],
     *   "metadata": { "totalCount": 100 },
     *   "timestamp": "2024-01-15T10:30:00"
     * }
     *
     * @param <T> The type of data being returned
     * @param data The actual data to return
     * @param message Custom success message
     * @param metadata Additional information (pagination, counts, etc.)
     * @return ApiResponse with all fields populated
     */
    public static <T> ApiResponse<T> success(
        T data,
        String message,
        Object metadata
    ) {
        return ApiResponse.<T>builder()
            .success(true)
            .message(message)
            .data(data)
            .metadata(metadata)
            .timestamp(LocalDateTime.now())
            .build();
    }

    // ==================== ERROR RESPONSE METHODS ====================

    /**
     * Create an ERROR response with just a message
     *
     * Usage Example:
     * return ApiResponse.error("User not found");
     *
     * Result JSON:
     * {
     *   "success": false,
     *   "message": "User not found",
     *   "timestamp": "2024-01-15T10:30:00"
     * }
     *
     * @param <T> The type of data (not used here, but needed for type safety)
     * @param message Error message explaining what went wrong
     * @return ApiResponse with success=false and error message
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .timestamp(LocalDateTime.now())
            .build();
    }

    /**
     * Create an ERROR response with message and error details
     *
     * Usage Example:
     * Map<String, String> errors = new HashMap<>();
     * errors.put("email", "Email is invalid");
     * errors.put("password", "Password too short");
     * return ApiResponse.error("Validation failed", errors);
     *
     * Result JSON:
     * {
     *   "success": false,
     *   "message": "Validation failed",
     *   "data": {
     *     "email": "Email is invalid",
     *     "password": "Password too short"
     *   },
     *   "timestamp": "2024-01-15T10:30:00"
     * }
     *
     * @param <T> The type of error data (usually Map<String, String> for validation errors)
     * @param message Error message
     * @param data Error details or validation errors
     * @return ApiResponse with success=false, error message, and error details
     */
    public static <T> ApiResponse<T> error(String message, T data) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
