package com.zedcode.common.exception;

/**
 * Exception thrown when a bad request is made (HTTP 400)
 * This exception is typically used when client sends invalid data
 */
public class BadRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
