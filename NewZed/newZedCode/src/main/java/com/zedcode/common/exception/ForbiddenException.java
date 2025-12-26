package com.zedcode.common.exception;

/**
 * Exception thrown when a user attempts to access a resource they don't have permission to access.
 * This exception maps to HTTP 403 Forbidden status.
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
