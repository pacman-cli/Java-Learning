package com.zedcode.common.exception;

/**
 * Exception thrown when there is a conflict with the current state of the resource.
 * This exception maps to HTTP 409 Conflict status.
 *
 * Typically used when:
 * - Attempting to create a resource that already exists
 * - Updating a resource that has been modified by another user
 * - Deleting a resource that is still referenced by other entities
 */
public class ConflictException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new ConflictException with the specified detail message.
     *
     * @param message the detail message
     */
    public ConflictException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConflictException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new ConflictException with the specified cause.
     *
     * @param cause the cause of the exception
     */
    public ConflictException(Throwable cause) {
        super(cause);
    }
}
