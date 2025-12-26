package com.zedcode.common.exception;

/**
 * Exception thrown when a requested resource is not found in the system.
 * This exception is typically used in service layer methods when attempting
 * to retrieve an entity by ID or other unique identifier.
 *
 * <p>Example usage:
 * <pre>
 * User user = userRepository.findById(id)
 *     .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
 * </pre>
 *
 * @author ZedCode
 * @version 1.0
 */
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    /**
     * Constructs a new ResourceNotFoundException with a custom message.
     *
     * @param message the detail message
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new ResourceNotFoundException with resource details.
     *
     * @param resourceName the name of the resource (e.g., "User", "Product")
     * @param fieldName    the name of the field used for lookup (e.g., "id", "email")
     * @param fieldValue   the value of the field that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Constructs a new ResourceNotFoundException with a message and cause.
     *
     * @param message the detail message
     * @param cause   the cause of the exception
     */
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Gets the name of the resource that was not found.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Gets the name of the field used for lookup.
     *
     * @return the field name
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Gets the value of the field that was not found.
     *
     * @return the field value
     */
    public Object getFieldValue() {
        return fieldValue;
    }
}
