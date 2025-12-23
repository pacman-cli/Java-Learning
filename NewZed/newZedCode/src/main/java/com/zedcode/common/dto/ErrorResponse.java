package com.zedcode.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard Error Response DTO
 * Used to return consistent error responses across the application
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    /**
     * Timestamp when the error occurred
     */
    private LocalDateTime timestamp;

    /**
     * HTTP status code
     */
    private Integer status;

    /**
     * HTTP status reason phrase (e.g., "Bad Request", "Not Found")
     */
    private String error;

    /**
     * Detailed error message
     */
    private String message;

    /**
     * Request path that caused the error
     */
    private String path;

    /**
     * Validation errors (field-level errors)
     * Key: field name, Value: error message
     */
    private Map<String, String> validationErrors;

    /**
     * Additional debug information (only in development)
     */
    private String debugMessage;
}
