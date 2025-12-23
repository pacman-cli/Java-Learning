/**
 * API Client for User Management
 * Provides functions to interact with the Spring Boot backend API
 *
 * @author ZedCode Frontend
 * @version 1.0
 */

import axios, { AxiosError, AxiosInstance } from "axios";
import type {
    User,
    CreateUserRequest,
    UpdateUserRequest,
    ApiResponse,
    PageResponse,
    UserStats,
    ErrorResponse,
    PaginationParams,
    UserStatus,
    UserRole,
} from "./types";

// ============================================================================
// AXIOS INSTANCE CONFIGURATION
// ============================================================================

/**
 * Base API URL from environment variable
 * Defaults to localhost:8080 if not set
 */
const BASE_URL =
    process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api/v1";

/**
 * Configured axios instance with default settings
 */
const api: AxiosInstance = axios.create({
    baseURL: BASE_URL,
    headers: {
        "Content-Type": "application/json",
    },
    timeout: 30000, // 30 seconds
});

// ============================================================================
// REQUEST INTERCEPTOR
// ============================================================================

/**
 * Add authentication token to requests if available
 */
api.interceptors.request.use(
    (config) => {
        // Get token from localStorage (if you implement authentication)
        const token =
            typeof window !== "undefined"
                ? localStorage.getItem("authToken")
                : null;

        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    },
    (error) => {
        return Promise.reject(error);
    },
);

// ============================================================================
// RESPONSE INTERCEPTOR
// ============================================================================

/**
 * Handle API responses and errors globally
 */
api.interceptors.response.use(
    (response) => {
        return response;
    },
    (error: AxiosError<ErrorResponse>) => {
        // Handle specific error cases
        if (error.response) {
            // Server responded with error status
            const errorData = error.response.data;

            // You can add custom error handling here
            console.error("API Error:", errorData);

            // For 401 Unauthorized, redirect to login (if you have auth)
            if (error.response.status === 401) {
                if (typeof window !== "undefined") {
                    // localStorage.removeItem('authToken');
                    // window.location.href = '/login';
                }
            }
        } else if (error.request) {
            // Request made but no response received
            console.error("Network Error:", error.message);
        } else {
            // Something else happened
            console.error("Error:", error.message);
        }

        return Promise.reject(error);
    },
);

// ============================================================================
// USER CRUD OPERATIONS
// ============================================================================

/**
 * Create a new user
 *
 * @param userData User creation data
 * @returns Promise with created user
 * @throws AxiosError if request fails
 *
 * @example
 * ```typescript
 * const newUser = await createUser({
 *   firstName: 'John',
 *   lastName: 'Doe',
 *   username: 'johndoe',
 *   email: 'john@example.com',
 *   password: 'SecurePass123!',
 * });
 * ```
 */
export const createUser = async (
    userData: CreateUserRequest,
): Promise<ApiResponse<User>> => {
    const response = await api.post<ApiResponse<User>>("/users", userData);
    return response.data;
};

/**
 * Get all users with pagination and sorting
 *
 * @param params Pagination and sorting parameters
 * @returns Promise with paginated user list
 *
 * @example
 * ```typescript
 * const users = await getAllUsers({ page: 0, size: 10, sort: 'id,desc' });
 * console.log(users.data.content); // Array of users
 * console.log(users.data.totalElements); // Total count
 * ```
 */
export const getAllUsers = async (
    params: PaginationParams = {},
): Promise<ApiResponse<PageResponse<User>>> => {
    const { page = 0, size = 10, sort = "id,desc" } = params;

    const response = await api.get<ApiResponse<PageResponse<User>>>("/users", {
        params: { page, size, sort },
    });

    return response.data;
};

/**
 * Get a single user by ID
 *
 * @param id User ID
 * @returns Promise with user details
 * @throws AxiosError with 404 if user not found
 *
 * @example
 * ```typescript
 * const user = await getUserById(1);
 * console.log(user.data.email);
 * ```
 */
export const getUserById = async (id: number): Promise<ApiResponse<User>> => {
    const response = await api.get<ApiResponse<User>>(`/users/${id}`);
    return response.data;
};

/**
 * Get a user by email address
 *
 * @param email User's email address
 * @returns Promise with user details
 * @throws AxiosError with 404 if user not found
 */
export const getUserByEmail = async (
    email: string,
): Promise<ApiResponse<User>> => {
    const response = await api.get<ApiResponse<User>>(`/users/email/${email}`);
    return response.data;
};

/**
 * Get a user by username
 *
 * @param username User's username
 * @returns Promise with user details
 * @throws AxiosError with 404 if user not found
 */
export const getUserByUsername = async (
    username: string,
): Promise<ApiResponse<User>> => {
    const response = await api.get<ApiResponse<User>>(
        `/users/username/${username}`,
    );
    return response.data;
};

/**
 * Update an existing user
 *
 * @param id User ID to update
 * @param userData Partial user data to update
 * @returns Promise with updated user
 * @throws AxiosError with 404 if user not found
 *
 * @example
 * ```typescript
 * const updated = await updateUser(1, {
 *   firstName: 'Jane',
 *   bio: 'Updated bio',
 * });
 * ```
 */
export const updateUser = async (
    id: number,
    userData: UpdateUserRequest,
): Promise<ApiResponse<User>> => {
    const response = await api.put<ApiResponse<User>>(`/users/${id}`, userData);
    return response.data;
};

/**
 * Soft delete a user (marks as deleted but keeps in database)
 *
 * @param id User ID to delete
 * @returns Promise with success message
 *
 * @example
 * ```typescript
 * await deleteUser(1);
 * // User is marked as deleted but data is preserved
 * ```
 */
export const deleteUser = async (id: number): Promise<ApiResponse<null>> => {
    const response = await api.delete<ApiResponse<null>>(`/users/${id}`);
    return response.data;
};

/**
 * Permanently delete a user (removes from database)
 *
 * @param id User ID to permanently delete
 * @returns Promise with success message
 *
 * @example
 * ```typescript
 * await permanentlyDeleteUser(1);
 * // User is completely removed from database
 * ```
 */
export const permanentlyDeleteUser = async (
    id: number,
): Promise<ApiResponse<null>> => {
    const response = await api.delete<ApiResponse<null>>(
        `/users/${id}/permanent`,
    );
    return response.data;
};

// ============================================================================
// SEARCH AND FILTER OPERATIONS
// ============================================================================

/**
 * Search users by keyword
 * Searches in firstName, lastName, username, and email
 *
 * @param keyword Search term
 * @param params Pagination and sorting parameters
 * @returns Promise with paginated search results
 *
 * @example
 * ```typescript
 * const results = await searchUsers('john', { page: 0, size: 20 });
 * console.log(results.data.content); // Users matching 'john'
 * ```
 */
export const searchUsers = async (
    keyword: string,
    params: PaginationParams = {},
): Promise<ApiResponse<PageResponse<User>>> => {
    const { page = 0, size = 10, sort = "id,desc" } = params;

    const response = await api.get<ApiResponse<PageResponse<User>>>(
        "/users/search",
        {
            params: { keyword, page, size, sort },
        },
    );

    return response.data;
};

/**
 * Get users filtered by status
 *
 * @param status User status to filter by
 * @param params Pagination and sorting parameters
 * @returns Promise with paginated filtered users
 *
 * @example
 * ```typescript
 * const activeUsers = await getUsersByStatus('ACTIVE', { page: 0, size: 10 });
 * ```
 */
export const getUsersByStatus = async (
    status: UserStatus,
    params: PaginationParams = {},
): Promise<ApiResponse<PageResponse<User>>> => {
    const { page = 0, size = 10, sort = "id,desc" } = params;

    const response = await api.get<ApiResponse<PageResponse<User>>>(
        `/users/status/${status}`,
        {
            params: { page, size, sort },
        },
    );

    return response.data;
};

/**
 * Get users filtered by role
 *
 * @param role User role to filter by
 * @param params Pagination and sorting parameters
 * @returns Promise with paginated filtered users
 *
 * @example
 * ```typescript
 * const admins = await getUsersByRole('ADMIN', { page: 0, size: 10 });
 * ```
 */
export const getUsersByRole = async (
    role: UserRole,
    params: PaginationParams = {},
): Promise<ApiResponse<PageResponse<User>>> => {
    const { page = 0, size = 10, sort = "id,desc" } = params;

    const response = await api.get<ApiResponse<PageResponse<User>>>(
        `/users/role/${role}`,
        {
            params: { page, size, sort },
        },
    );

    return response.data;
};

// ============================================================================
// USER STATUS MANAGEMENT
// ============================================================================

/**
 * Activate a user account
 * Sets status to ACTIVE and enabled to true
 *
 * @param id User ID to activate
 * @returns Promise with updated user
 *
 * @example
 * ```typescript
 * await activateUser(1);
 * // User can now log in and use the system
 * ```
 */
export const activateUser = async (id: number): Promise<ApiResponse<User>> => {
    const response = await api.patch<ApiResponse<User>>(
        `/users/${id}/activate`,
    );
    return response.data;
};

/**
 * Deactivate a user account
 * Sets status to INACTIVE and enabled to false
 *
 * @param id User ID to deactivate
 * @returns Promise with updated user
 *
 * @example
 * ```typescript
 * await deactivateUser(1);
 * // User cannot log in until reactivated
 * ```
 */
export const deactivateUser = async (
    id: number,
): Promise<ApiResponse<User>> => {
    const response = await api.patch<ApiResponse<User>>(
        `/users/${id}/deactivate`,
    );
    return response.data;
};

/**
 * Block a user account
 * Sets status to BLOCKED and accountNonLocked to false
 *
 * @param id User ID to block
 * @returns Promise with updated user
 *
 * @example
 * ```typescript
 * await blockUser(1);
 * // User is blocked from accessing the system
 * ```
 */
export const blockUser = async (id: number): Promise<ApiResponse<User>> => {
    const response = await api.patch<ApiResponse<User>>(`/users/${id}/block`);
    return response.data;
};

/**
 * Unblock a user account
 * Sets status to ACTIVE and accountNonLocked to true
 *
 * @param id User ID to unblock
 * @returns Promise with updated user
 *
 * @example
 * ```typescript
 * await unblockUser(1);
 * // User can access the system again
 * ```
 */
export const unblockUser = async (id: number): Promise<ApiResponse<User>> => {
    const response = await api.patch<ApiResponse<User>>(`/users/${id}/unblock`);
    return response.data;
};

/**
 * Verify a user's email address
 * Sets emailVerified to true and status to ACTIVE
 *
 * @param id User ID to verify
 * @returns Promise with updated user
 *
 * @example
 * ```typescript
 * await verifyUserEmail(1);
 * // User's email is now verified
 * ```
 */
export const verifyUserEmail = async (
    id: number,
): Promise<ApiResponse<User>> => {
    const response = await api.patch<ApiResponse<User>>(
        `/users/${id}/verify-email`,
    );
    return response.data;
};

// ============================================================================
// VALIDATION AND CHECKS
// ============================================================================

/**
 * Check if an email address is already registered
 *
 * @param email Email address to check
 * @returns Promise with boolean result (true if exists)
 *
 * @example
 * ```typescript
 * const exists = await checkEmailExists('john@example.com');
 * if (exists.data) {
 *   console.log('Email already registered');
 * }
 * ```
 */
export const checkEmailExists = async (
    email: string,
): Promise<ApiResponse<boolean>> => {
    const response = await api.get<ApiResponse<boolean>>(
        `/users/exists/email/${email}`,
    );
    return response.data;
};

/**
 * Check if a username is already taken
 *
 * @param username Username to check
 * @returns Promise with boolean result (true if exists)
 *
 * @example
 * ```typescript
 * const exists = await checkUsernameExists('johndoe');
 * if (exists.data) {
 *   console.log('Username already taken');
 * }
 * ```
 */
export const checkUsernameExists = async (
    username: string,
): Promise<ApiResponse<boolean>> => {
    const response = await api.get<ApiResponse<boolean>>(
        `/users/exists/username/${username}`,
    );
    return response.data;
};

// ============================================================================
// STATISTICS AND ANALYTICS
// ============================================================================

/**
 * Get user statistics
 * Returns counts of users by status and role
 *
 * @returns Promise with user statistics
 *
 * @example
 * ```typescript
 * const stats = await getUserStats();
 * console.log(`Total users: ${stats.data.totalUsers}`);
 * console.log(`Active users: ${stats.data.activeUsers}`);
 * console.log(`Admins: ${stats.data.adminCount}`);
 * ```
 */
export const getUserStats = async (): Promise<ApiResponse<UserStats>> => {
    const response = await api.get<ApiResponse<UserStats>>("/users/stats");
    return response.data;
};

// ============================================================================
// UTILITY FUNCTIONS
// ============================================================================

/**
 * Handle API errors and return user-friendly messages
 *
 * @param error Axios error object
 * @returns User-friendly error message
 */
export const handleApiError = (error: unknown): string => {
    if (axios.isAxiosError(error)) {
        const axiosError = error as AxiosError<ErrorResponse>;

        if (axiosError.response?.data) {
            const errorData = axiosError.response.data;

            // If there are validation errors, format them
            if (errorData.validationErrors) {
                const errors = Object.entries(errorData.validationErrors)
                    .map(([field, message]) => `${field}: ${message}`)
                    .join(", ");
                return errors;
            }

            // Return the error message from backend
            return errorData.message || "An error occurred";
        }

        // Network error
        if (axiosError.request && !axiosError.response) {
            return "Network error. Please check your connection.";
        }
    }

    // Unknown error
    return "An unexpected error occurred";
};

/**
 * Export the configured axios instance for custom requests
 */
export default api;
