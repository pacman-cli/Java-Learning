package com.zedcode.module.user.entity;

/**
 * Role Enum
 * Defines the different roles that users can have in the system
 */
public enum Role {

    /**
     * Regular user with basic permissions
     */
    ROLE_USER("User", "Regular user with basic access"),

    /**
     * Administrator with full system access
     */
    ROLE_ADMIN("Admin", "Administrator with full system access"),

    /**
     * Moderator with elevated permissions
     */
    ROLE_MODERATOR("Moderator", "Moderator with content management permissions"),

    /**
     * Super admin with unrestricted access
     */
    ROLE_SUPER_ADMIN("Super Admin", "Super administrator with unrestricted access");

    private final String displayName;
    private final String description;

    /**
     * Constructor
     *
     * @param displayName the display name of the role
     * @param description the description of the role
     */
    Role(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Get the display name of the role
     *
     * @return display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get the description of the role
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get role by name (case-insensitive)
     *
     * @param name the role name
     * @return the Role enum
     */
    public static Role fromString(String name) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No role found with name: " + name);
    }
}
