package com.zedcode.common.constants;

/**
 * Application-wide constants
 * Contains all constant values used across the application
 */
public final class AppConstants {

    private AppConstants() {
        throw new IllegalStateException("Utility class - cannot instantiate");
    }

    // ===========================
    // API VERSION AND BASE PATHS
    // ===========================
    public static final String API_VERSION = "v1";
    public static final String API_BASE_PATH = "/api/" + API_VERSION;

    // ===========================
    // PAGINATION DEFAULTS
    // ===========================
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final int MAX_PAGE_SIZE = 100;
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "ASC";

    // ===========================
    // DATE AND TIME FORMATS
    // ===========================
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT = "HH:mm:ss";
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DEFAULT_TIMEZONE = "UTC";

    // ===========================
    // SECURITY CONSTANTS
    // ===========================
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";
    public static final long TOKEN_VALIDITY = 24 * 60 * 60 * 1000; // 24 hours
    public static final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 days

    // ===========================
    // USER ROLES
    // ===========================
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
    public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";

    // ===========================
    // USER STATUS
    // ===========================
    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_INACTIVE = "INACTIVE";
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_BLOCKED = "BLOCKED";
    public static final String STATUS_DELETED = "DELETED";

    // ===========================
    // HTTP STATUS MESSAGES
    // ===========================
    public static final String SUCCESS_MESSAGE = "Operation completed successfully";
    public static final String CREATED_MESSAGE = "Resource created successfully";
    public static final String UPDATED_MESSAGE = "Resource updated successfully";
    public static final String DELETED_MESSAGE = "Resource deleted successfully";
    public static final String NOT_FOUND_MESSAGE = "Resource not found";
    public static final String BAD_REQUEST_MESSAGE = "Invalid request data";
    public static final String UNAUTHORIZED_MESSAGE = "Unauthorized access";
    public static final String FORBIDDEN_MESSAGE = "Access forbidden";
    public static final String INTERNAL_ERROR_MESSAGE = "Internal server error occurred";

    // ===========================
    // VALIDATION MESSAGES
    // ===========================
    public static final String VALIDATION_ERROR = "Validation failed";
    public static final String REQUIRED_FIELD = "This field is required";
    public static final String INVALID_EMAIL = "Invalid email format";
    public static final String INVALID_PHONE = "Invalid phone number format";
    public static final String PASSWORD_TOO_SHORT = "Password must be at least 8 characters";
    public static final String PASSWORD_TOO_WEAK = "Password must contain uppercase, lowercase, number and special character";

    // ===========================
    // REGEX PATTERNS
    // ===========================
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String PHONE_REGEX = "^[+]?[0-9]{10,15}$";
    public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9._-]{3,20}$";
    public static final String ALPHANUMERIC_REGEX = "^[a-zA-Z0-9]+$";

    // ===========================
    // FILE UPLOAD CONSTANTS
    // ===========================
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
    public static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif", "image/webp"};
    public static final String[] ALLOWED_DOCUMENT_TYPES = {"application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

    // ===========================
    // CACHE NAMES
    // ===========================
    public static final String USERS_CACHE = "users";
    public static final String USER_CACHE = "user";
    public static final String ROLES_CACHE = "roles";
    public static final String PERMISSIONS_CACHE = "permissions";

    // ===========================
    // CACHE TTL (Time To Live)
    // ===========================
    public static final long CACHE_TTL_SHORT = 5 * 60; // 5 minutes
    public static final long CACHE_TTL_MEDIUM = 30 * 60; // 30 minutes
    public static final long CACHE_TTL_LONG = 60 * 60; // 1 hour
    public static final long CACHE_TTL_VERY_LONG = 24 * 60 * 60; // 24 hours

    // ===========================
    // API ENDPOINTS
    // ===========================
    public static final String AUTH_ENDPOINT = "/auth";
    public static final String USER_ENDPOINT = "/users";
    public static final String LOGIN_ENDPOINT = "/login";
    public static final String REGISTER_ENDPOINT = "/register";
    public static final String LOGOUT_ENDPOINT = "/logout";
    public static final String REFRESH_TOKEN_ENDPOINT = "/refresh-token";

    // ===========================
    // REQUEST PARAMETERS
    // ===========================
    public static final String PAGE_PARAM = "page";
    public static final String SIZE_PARAM = "size";
    public static final String SORT_PARAM = "sort";
    public static final String SEARCH_PARAM = "search";
    public static final String FILTER_PARAM = "filter";

    // ===========================
    // CORS CONFIGURATION
    // ===========================
    public static final String[] ALLOWED_ORIGINS = {"http://localhost:3000", "http://localhost:4200"};
    public static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"};
    public static final String[] ALLOWED_HEADERS = {"*"};
    public static final long MAX_AGE = 3600L;

    // ===========================
    // DATABASE CONSTANTS
    // ===========================
    public static final String SOFT_DELETE_COLUMN = "deleted";
    public static final String CREATED_AT_COLUMN = "created_at";
    public static final String UPDATED_AT_COLUMN = "updated_at";
    public static final String CREATED_BY_COLUMN = "created_by";
    public static final String UPDATED_BY_COLUMN = "updated_by";

    // ===========================
    // ERROR CODES
    // ===========================
    public static final String ERR_VALIDATION = "ERR_VALIDATION";
    public static final String ERR_NOT_FOUND = "ERR_NOT_FOUND";
    public static final String ERR_UNAUTHORIZED = "ERR_UNAUTHORIZED";
    public static final String ERR_FORBIDDEN = "ERR_FORBIDDEN";
    public static final String ERR_CONFLICT = "ERR_CONFLICT";
    public static final String ERR_INTERNAL = "ERR_INTERNAL";
    public static final String ERR_BAD_REQUEST = "ERR_BAD_REQUEST";

    // ===========================
    // MISCELLANEOUS
    // ===========================
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String COMMA_SEPARATOR = ",";
    public static final String SEMICOLON_SEPARATOR = ";";
    public static final String PIPE_SEPARATOR = "|";
    public static final String EMPTY_STRING = "";
    public static final String SPACE = " ";
}
