package com.zedcode.common.exception;

/**
 * Exception thrown when a user attempts to access a resource without proper authorization
 */
public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException() {
        super("Unauthorized access");
    }
}
