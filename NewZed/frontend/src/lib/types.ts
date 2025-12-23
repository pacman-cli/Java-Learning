/**
 * TypeScript type definitions matching the backend API
 * These types ensure type safety when communicating with the Spring Boot backend
 *
 * @author ZedCode Frontend
 * @version 1.0
 */

// ============================================================================
// ENUMS
// ============================================================================

/**
 * User role enumeration
 * Matches backend: com.zedcode.module.user.entity.User.UserRole
 */
export enum UserRole {
    USER = "USER",
    ADMIN = "ADMIN",
    MODERATOR = "MODERATOR",
    SUPER_ADMIN = "SUPER_ADMIN",
}

/**
 * User status enumeration
 * Matches backend: com.zedcode.module.user.entity.User.UserStatus
 */
export enum UserStatus {
    ACTIVE = "ACTIVE",
    INACTIVE = "INACTIVE",
    PENDING = "PENDING",
    BLOCKED = "BLOCKED",
    DELETED = "DELETED",
}

// ============================================================================
// USER INTERFACES
// ============================================================================

/**
 * User Data Transfer Object
 * Matches backend: com.zedcode.module.user.dto.UserDTO
 *
 * Represents a user without sensitive information (no password)
 */
export interface User {
    /** User's unique identifier */
    id: number;

    /** User's first name */
    firstName: string;

    /** User's last name */
    lastName: string;

    /** User's full name (computed from first and last name) */
    fullName: string;

    /** User's unique username */
    username: string;

    /** User's email address */
    email: string;

    /** User's phone number (optional) */
    phoneNumber?: string;

    /** User's role in the system */
    role: UserRole;

    /** User's account status */
    status: UserStatus;

    /** Whether the user's email has been verified */
    emailVerified: boolean;

    /** Whether the user's account is enabled */
    enabled: boolean;

    /** Whether the user's account is not locked */
    accountNonLocked: boolean;

    /** Last login timestamp (ISO 8601 format) */
    lastLoginAt?: string;

    /** URL to user's profile image */
    profileImageUrl?: string;

    /** User's bio or description */
    bio?: string;

    /** Timestamp when user was created (ISO 8601 format) */
    createdAt: string;

    /** Timestamp when user was last updated (ISO 8601 format) */
    updatedAt?: string;
}

/**
 * Request payload for creating a new user
 * Matches backend: com.zedcode.module.user.dto.CreateUserRequest
 */
export interface CreateUserRequest {
    /** User's first name (required, 2-50 characters) */
    firstName: string;

    /** User's last name (required, 2-50 characters) */
    lastName: string;

    /** User's unique username (required, 3-20 characters, alphanumeric with ._-) */
    username: string;

    /** User's email address (required, valid email format) */
    email: string;

    /** User's password (required, min 8 chars, must contain uppercase, lowercase, number, special char) */
    password: string;

    /** User's phone number (optional, 10-15 digits) */
    phoneNumber?: string;

    /** User's role (optional, defaults to USER) */
    role?: UserRole;

    /** User's bio or description (optional, max 500 characters) */
    bio?: string;
}

/**
 * Request payload for updating an existing user
 * Matches backend: com.zedcode.module.user.dto.UpdateUserRequest
 * All fields are optional - only send the fields you want to update
 */
export interface UpdateUserRequest {
    /** User's first name (2-50 characters) */
    firstName?: string;

    /** User's last name (2-50 characters) */
    lastName?: string;

    /** User's username (3-20 characters, alphanumeric with ._-) */
    username?: string;

    /** User's email address (valid email format) */
    email?: string;

    /** User's phone number (10-15 digits) */
    phoneNumber?: string;

    /** User's bio or description (max 500 characters) */
    bio?: string;

    /** URL to user's profile image */
    profileImageUrl?: string;
}

// ============================================================================
// API RESPONSE INTERFACES
// ============================================================================

/**
 * Standard API response wrapper
 * Matches backend: com.zedcode.common.dto.ApiResponse<T>
 *
 * All successful API responses are wrapped in this format
 *
 * @template T The type of data being returned
 */
export interface ApiResponse<T> {
    /** Whether the request was successful */
    success: boolean;

    /** Human-readable message about the operation */
    message: string;

    /** The actual data payload */
    data: T;

    /** ISO 8601 timestamp of when the response was generated */
    timestamp: string;
}

/**
 * Paginated response wrapper
 * Matches backend: com.zedcode.common.dto.PageResponse<T>
 *
 * Used for endpoints that return paginated data
 *
 * @template T The type of items in the page
 */
export interface PageResponse<T> {
    /** Array of items for the current page */
    content: T[];

    /** Current page number (0-indexed) */
    pageNumber: number;

    /** Number of items per page */
    pageSize: number;

    /** Total number of items across all pages */
    totalElements: number;

    /** Total number of pages */
    totalPages: number;

    /** Whether this is the last page */
    last: boolean;

    /** Whether this is the first page */
    first: boolean;

    /** Whether there is a next page */
    hasNext: boolean;

    /** Whether there is a previous page */
    hasPrevious: boolean;

    /** Number of items in the current page */
    numberOfElements: number;

    /** Whether the page is empty */
    empty: boolean;
}

/**
 * Error response format
 * Matches backend: com.zedcode.common.dto.ErrorResponse
 *
 * Returned when an API request fails
 */
export interface ErrorResponse {
    /** ISO 8601 timestamp of when the error occurred */
    timestamp: string;

    /** HTTP status code */
    status: number;

    /** HTTP status text (e.g., "Bad Request", "Not Found") */
    error: string;

    /** Detailed error message */
    message: string;

    /** API path that caused the error */
    path: string;

    /** Validation errors (if applicable) */
    validationErrors?: Record<string, string>;
}

// ============================================================================
// USER STATISTICS INTERFACE
// ============================================================================

/**
 * User statistics summary
 * Matches backend: UserController.UserStats
 */
export interface UserStats {
    /** Total number of users in the system */
    totalUsers: number;

    /** Number of active users */
    activeUsers: number;

    /** Number of inactive users */
    inactiveUsers: number;

    /** Number of blocked users */
    blockedUsers: number;

    /** Number of pending users */
    pendingUsers: number;

    /** Number of admin users */
    adminCount: number;

    /** Number of regular users */
    userCount: number;

    /** Number of moderator users */
    moderatorCount: number;
}

// ============================================================================
// QUERY PARAMETERS INTERFACES
// ============================================================================

/**
 * Parameters for paginated requests
 */
export interface PaginationParams {
    /** Page number (0-indexed) */
    page?: number;

    /** Number of items per page */
    size?: number;

    /** Sort criteria (e.g., "id,desc" or "email,asc") */
    sort?: string;
}

/**
 * Parameters for searching users
 */
export interface SearchParams extends PaginationParams {
    /** Search keyword */
    keyword: string;
}

/**
 * Parameters for filtering users by status
 */
export interface StatusFilterParams extends PaginationParams {
    /** Filter by status */
    status: UserStatus;
}

/**
 * Parameters for filtering users by role
 */
export interface RoleFilterParams extends PaginationParams {
    /** Filter by role */
    role: UserRole;
}

// ============================================================================
// UTILITY TYPES
// ============================================================================

/**
 * Generic API success response
 */
export type SuccessResponse<T> = ApiResponse<T>;

/**
 * Generic API error
 */
export type ApiError = ErrorResponse;

/**
 * User list response (paginated)
 */
export type UserListResponse = ApiResponse<PageResponse<User>>;

/**
 * Single user response
 */
export type UserResponse = ApiResponse<User>;

/**
 * User statistics response
 */
export type UserStatsResponse = ApiResponse<UserStats>;

/**
 * Delete operation response (no data)
 */
export type DeleteResponse = ApiResponse<null>;

/**
 * Boolean check response (e.g., email exists, username exists)
 */
export type BooleanResponse = ApiResponse<boolean>;
