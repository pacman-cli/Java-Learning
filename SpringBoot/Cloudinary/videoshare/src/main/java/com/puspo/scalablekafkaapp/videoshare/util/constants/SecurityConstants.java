package com.puspo.scalablekafkaapp.videoshare.util.constants;

public class SecurityConstants {
    // JWT Configuration - Use environment variables in production
    public static final String JWT_SECRET = System.getenv("JWT_SECRET") != null ? System.getenv("JWT_SECRET")
            : "7f1a8d9e1c3b5a7c9d2e4f6a8b0c2d4e7f1a8d9e1c3b5a7c9d2e4f6a8b0c2d4e";
    public static final long JWT_EXPIRATION_MS = 86400000; // 24 hours
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    // Role Constants
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

    // Security Headers
    public static final String[] SECURITY_HEADERS = {
            "Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin"
    };
}
