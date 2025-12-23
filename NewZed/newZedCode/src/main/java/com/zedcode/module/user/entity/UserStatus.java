package com.zedcode.module.user.entity;

/**
 * Enum representing the status of a user in the system
 *
 * @author ZedCode
 * @version 1.0
 */
public enum UserStatus {

    /**
     * User account is active and can access the system
     */
    ACTIVE("Active", "User account is active and operational"),

    /**
     * User account is inactive (deactivated by user or admin)
     */
    INACTIVE("Inactive", "User account has been deactivated"),

    /**
     * User account is pending activation (e.g., awaiting email verification)
     */
    PENDING("Pending", "User account is pending activation"),

    /**
     * User account has been blocked/banned
     */
    BLOCKED("Blocked", "User account has been blocked"),

    /**
     * User account has been suspended temporarily
     */
    SUSPENDED("Suspended", "User account has been temporarily suspended"),

    /**
     * User account is marked for deletion (soft delete)
     */
    DELETED("Deleted", "User account has been deleted");

    private final String displayName;
    private final String description;

    /**
     * Constructor
     *
     * @param displayName the display name of the status
     * @param description the description of the status
     */
    UserStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the display name of the status
     *
     * @return the display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description of the status
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Check if the status allows login
     *
     * @return true if user can login with this status
     */
    public boolean canLogin() {
        return this == ACTIVE;
    }

    /**
     * Check if the status is active
     *
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return this == ACTIVE;
    }

    /**
     * Get UserStatus from string value (case-insensitive)
     *
     * @param value the string value
     * @return the UserStatus enum or null if not found
     */
    public static UserStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        for (UserStatus status : UserStatus.values()) {
            if (status.name().equalsIgnoreCase(value.trim())) {
                return status;
            }
        }

        return null;
    }
}
