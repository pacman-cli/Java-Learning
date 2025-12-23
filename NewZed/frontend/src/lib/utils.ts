/**
 * Utility functions for the User Management Frontend
 * Provides formatting, validation, and helper functions
 *
 * @author ZedCode Frontend
 * @version 1.0
 */

import {
    format,
    formatDistance,
    formatDistanceToNow,
    parseISO,
} from "date-fns";
import { UserRole, UserStatus } from "./types";

// ============================================================================
// DATE FORMATTING UTILITIES
// ============================================================================

/**
 * Format ISO date string to readable date
 *
 * @param dateString ISO 8601 date string
 * @param formatStr Date format pattern (default: 'MMM dd, yyyy')
 * @returns Formatted date string
 *
 * @example
 * ```typescript
 * formatDate('2024-01-15T10:30:00Z') // 'Jan 15, 2024'
 * formatDate('2024-01-15T10:30:00Z', 'yyyy-MM-dd') // '2024-01-15'
 * ```
 */
export const formatDate = (
    dateString: string,
    formatStr: string = "MMM dd, yyyy",
): string => {
    try {
        const date = parseISO(dateString);
        return format(date, formatStr);
    } catch (error) {
        console.error("Error formatting date:", error);
        return dateString;
    }
};

/**
 * Format ISO date string to readable date and time
 *
 * @param dateString ISO 8601 date string
 * @returns Formatted date and time string
 *
 * @example
 * ```typescript
 * formatDateTime('2024-01-15T10:30:00Z') // 'Jan 15, 2024 at 10:30 AM'
 * ```
 */
export const formatDateTime = (dateString: string): string => {
    try {
        const date = parseISO(dateString);
        return format(date, "MMM dd, yyyy 'at' h:mm a");
    } catch (error) {
        console.error("Error formatting datetime:", error);
        return dateString;
    }
};

/**
 * Get relative time from now (e.g., "2 hours ago")
 *
 * @param dateString ISO 8601 date string
 * @returns Relative time string
 *
 * @example
 * ```typescript
 * formatRelativeTime('2024-01-15T10:30:00Z') // '2 hours ago'
 * ```
 */
export const formatRelativeTime = (dateString: string): string => {
    try {
        const date = parseISO(dateString);
        return formatDistanceToNow(date, { addSuffix: true });
    } catch (error) {
        console.error("Error formatting relative time:", error);
        return dateString;
    }
};

/**
 * Format time duration between two dates
 *
 * @param startDate Start date string
 * @param endDate End date string
 * @returns Duration string
 *
 * @example
 * ```typescript
 * formatDuration('2024-01-15T10:00:00Z', '2024-01-15T12:00:00Z') // '2 hours'
 * ```
 */
export const formatDuration = (startDate: string, endDate: string): string => {
    try {
        const start = parseISO(startDate);
        const end = parseISO(endDate);
        return formatDistance(start, end);
    } catch (error) {
        console.error("Error formatting duration:", error);
        return "";
    }
};

// ============================================================================
// COLOR UTILITIES FOR BADGES
// ============================================================================

/**
 * Get Tailwind CSS classes for user status badges
 *
 * @param status User status
 * @returns Object with background and text color classes
 *
 * @example
 * ```typescript
 * const colors = getStatusColor('ACTIVE');
 * // { bg: 'bg-green-100', text: 'text-green-800' }
 * ```
 */
export const getStatusColor = (
    status: UserStatus,
): { bg: string; text: string; border: string } => {
    const colorMap: Record<
        UserStatus,
        { bg: string; text: string; border: string }
    > = {
        [UserStatus.ACTIVE]: {
            bg: "bg-green-100",
            text: "text-green-800",
            border: "border-green-200",
        },
        [UserStatus.INACTIVE]: {
            bg: "bg-gray-100",
            text: "text-gray-800",
            border: "border-gray-200",
        },
        [UserStatus.PENDING]: {
            bg: "bg-yellow-100",
            text: "text-yellow-800",
            border: "border-yellow-200",
        },
        [UserStatus.BLOCKED]: {
            bg: "bg-red-100",
            text: "text-red-800",
            border: "border-red-200",
        },
        [UserStatus.DELETED]: {
            bg: "bg-gray-100",
            text: "text-gray-600",
            border: "border-gray-200",
        },
    };

    return (
        colorMap[status] || {
            bg: "bg-gray-100",
            text: "text-gray-800",
            border: "border-gray-200",
        }
    );
};

/**
 * Get Tailwind CSS classes for user role badges
 *
 * @param role User role
 * @returns Object with background and text color classes
 *
 * @example
 * ```typescript
 * const colors = getRoleColor('ADMIN');
 * // { bg: 'bg-blue-100', text: 'text-blue-800' }
 * ```
 */
export const getRoleColor = (
    role: UserRole,
): { bg: string; text: string; border: string } => {
    const colorMap: Record<
        UserRole,
        { bg: string; text: string; border: string }
    > = {
        [UserRole.USER]: {
            bg: "bg-blue-100",
            text: "text-blue-800",
            border: "border-blue-200",
        },
        [UserRole.ADMIN]: {
            bg: "bg-purple-100",
            text: "text-purple-800",
            border: "border-purple-200",
        },
        [UserRole.MODERATOR]: {
            bg: "bg-indigo-100",
            text: "text-indigo-800",
            border: "border-indigo-200",
        },
        [UserRole.SUPER_ADMIN]: {
            bg: "bg-pink-100",
            text: "text-pink-800",
            border: "border-pink-200",
        },
    };

    return (
        colorMap[role] || {
            bg: "bg-gray-100",
            text: "text-gray-800",
            border: "border-gray-200",
        }
    );
};

/**
 * Get color for boolean values (e.g., emailVerified, enabled)
 *
 * @param value Boolean value
 * @returns Object with background and text color classes
 */
export const getBooleanColor = (
    value: boolean,
): { bg: string; text: string; border: string } => {
    return value
        ? {
              bg: "bg-green-100",
              text: "text-green-800",
              border: "border-green-200",
          }
        : { bg: "bg-red-100", text: "text-red-800", border: "border-red-200" };
};

// ============================================================================
// STRING FORMATTING UTILITIES
// ============================================================================

/**
 * Truncate long text with ellipsis
 *
 * @param text Text to truncate
 * @param maxLength Maximum length before truncation
 * @returns Truncated text
 *
 * @example
 * ```typescript
 * truncate('This is a very long text', 10) // 'This is a...'
 * ```
 */
export const truncate = (text: string, maxLength: number = 50): string => {
    if (!text) return "";
    if (text.length <= maxLength) return text;
    return text.slice(0, maxLength) + "...";
};

/**
 * Capitalize first letter of a string
 *
 * @param text Text to capitalize
 * @returns Capitalized text
 *
 * @example
 * ```typescript
 * capitalize('hello world') // 'Hello world'
 * ```
 */
export const capitalize = (text: string): string => {
    if (!text) return "";
    return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
};

/**
 * Convert enum value to readable text
 * Converts SNAKE_CASE to Title Case
 *
 * @param enumValue Enum value (e.g., 'SUPER_ADMIN')
 * @returns Readable text (e.g., 'Super Admin')
 *
 * @example
 * ```typescript
 * formatEnumValue('SUPER_ADMIN') // 'Super Admin'
 * formatEnumValue('ACTIVE') // 'Active'
 * ```
 */
export const formatEnumValue = (enumValue: string): string => {
    if (!enumValue) return "";
    return enumValue
        .split("_")
        .map((word) => capitalize(word))
        .join(" ");
};

/**
 * Format phone number for display
 *
 * @param phoneNumber Raw phone number
 * @returns Formatted phone number
 *
 * @example
 * ```typescript
 * formatPhoneNumber('1234567890') // '(123) 456-7890'
 * formatPhoneNumber('+11234567890') // '+1 (123) 456-7890'
 * ```
 */
export const formatPhoneNumber = (phoneNumber: string): string => {
    if (!phoneNumber) return "";

    // Remove all non-digit characters except +
    const cleaned = phoneNumber.replace(/[^\d+]/g, "");

    // Check if it starts with country code
    if (cleaned.startsWith("+")) {
        const countryCode = cleaned.slice(0, cleaned.length - 10);
        const number = cleaned.slice(-10);
        const areaCode = number.slice(0, 3);
        const firstPart = number.slice(3, 6);
        const secondPart = number.slice(6);
        return `${countryCode} (${areaCode}) ${firstPart}-${secondPart}`;
    }

    // Format as US number
    if (cleaned.length === 10) {
        const areaCode = cleaned.slice(0, 3);
        const firstPart = cleaned.slice(3, 6);
        const secondPart = cleaned.slice(6);
        return `(${areaCode}) ${firstPart}-${secondPart}`;
    }

    return phoneNumber;
};

// ============================================================================
// VALIDATION UTILITIES
// ============================================================================

/**
 * Validate email format
 *
 * @param email Email address to validate
 * @returns True if valid email format
 */
export const isValidEmail = (email: string): boolean => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
};

/**
 * Validate username format
 * Must be 3-20 characters, alphanumeric with dots, underscores, and hyphens
 *
 * @param username Username to validate
 * @returns True if valid username format
 */
export const isValidUsername = (username: string): boolean => {
    const usernameRegex = /^[a-zA-Z0-9._-]{3,20}$/;
    return usernameRegex.test(username);
};

/**
 * Validate password strength
 * Must be at least 8 characters with uppercase, lowercase, number, and special character
 *
 * @param password Password to validate
 * @returns Object with validation result and error message
 */
export const validatePassword = (
    password: string,
): { valid: boolean; message: string } => {
    if (password.length < 8) {
        return {
            valid: false,
            message: "Password must be at least 8 characters long",
        };
    }

    if (!/[a-z]/.test(password)) {
        return {
            valid: false,
            message: "Password must contain at least one lowercase letter",
        };
    }

    if (!/[A-Z]/.test(password)) {
        return {
            valid: false,
            message: "Password must contain at least one uppercase letter",
        };
    }

    if (!/\d/.test(password)) {
        return {
            valid: false,
            message: "Password must contain at least one number",
        };
    }

    if (!/[@$!%*?&]/.test(password)) {
        return {
            valid: false,
            message:
                "Password must contain at least one special character (@$!%*?&)",
        };
    }

    return { valid: true, message: "Password is strong" };
};

/**
 * Validate phone number format
 *
 * @param phoneNumber Phone number to validate
 * @returns True if valid phone number format (10-15 digits)
 */
export const isValidPhoneNumber = (phoneNumber: string): boolean => {
    const phoneRegex = /^[+]?[0-9]{10,15}$/;
    const cleaned = phoneNumber.replace(/[^\d+]/g, "");
    return phoneRegex.test(cleaned);
};

// ============================================================================
// ARRAY AND OBJECT UTILITIES
// ============================================================================

/**
 * Group array items by a key
 *
 * @param array Array to group
 * @param key Key to group by
 * @returns Grouped object
 */
export const groupBy = <T extends Record<string, any>>(
    array: T[],
    key: keyof T,
): Record<string, T[]> => {
    return array.reduce(
        (result, item) => {
            const groupKey = String(item[key]);
            if (!result[groupKey]) {
                result[groupKey] = [];
            }
            result[groupKey].push(item);
            return result;
        },
        {} as Record<string, T[]>,
    );
};

/**
 * Sort array by a key
 *
 * @param array Array to sort
 * @param key Key to sort by
 * @param direction Sort direction ('asc' or 'desc')
 * @returns Sorted array
 */
export const sortBy = <T extends Record<string, any>>(
    array: T[],
    key: keyof T,
    direction: "asc" | "desc" = "asc",
): T[] => {
    return [...array].sort((a, b) => {
        const aVal = a[key];
        const bVal = b[key];

        if (aVal < bVal) return direction === "asc" ? -1 : 1;
        if (aVal > bVal) return direction === "asc" ? 1 : -1;
        return 0;
    });
};

/**
 * Remove duplicates from array
 *
 * @param array Array with potential duplicates
 * @param key Optional key to check for uniqueness in objects
 * @returns Array without duplicates
 */
export const unique = <T>(array: T[], key?: keyof T): T[] => {
    if (!key) {
        return Array.from(new Set(array));
    }

    const seen = new Set();
    return array.filter((item) => {
        const value = (item as any)[key];
        if (seen.has(value)) {
            return false;
        }
        seen.add(value);
        return true;
    });
};

// ============================================================================
// URL AND QUERY UTILITIES
// ============================================================================

/**
 * Build query string from object
 *
 * @param params Object with query parameters
 * @returns Query string (without leading ?)
 *
 * @example
 * ```typescript
 * buildQueryString({ page: 0, size: 10, sort: 'id,desc' })
 * // 'page=0&size=10&sort=id,desc'
 * ```
 */
export const buildQueryString = (params: Record<string, any>): string => {
    const queryParts: string[] = [];

    Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== "") {
            queryParts.push(
                `${encodeURIComponent(key)}=${encodeURIComponent(String(value))}`,
            );
        }
    });

    return queryParts.join("&");
};

/**
 * Parse query string to object
 *
 * @param queryString Query string (with or without leading ?)
 * @returns Object with parsed parameters
 */
export const parseQueryString = (
    queryString: string,
): Record<string, string> => {
    const params: Record<string, string> = {};
    const cleaned = queryString.startsWith("?")
        ? queryString.slice(1)
        : queryString;

    cleaned.split("&").forEach((part) => {
        const [key, value] = part.split("=");
        if (key && value) {
            params[decodeURIComponent(key)] = decodeURIComponent(value);
        }
    });

    return params;
};

// ============================================================================
// NUMBER FORMATTING UTILITIES
// ============================================================================

/**
 * Format number with thousand separators
 *
 * @param num Number to format
 * @returns Formatted number string
 *
 * @example
 * ```typescript
 * formatNumber(1234567) // '1,234,567'
 * ```
 */
export const formatNumber = (num: number): string => {
    return num.toLocaleString("en-US");
};

/**
 * Format number as percentage
 *
 * @param num Number to format (0-1 or 0-100)
 * @param decimals Number of decimal places
 * @returns Formatted percentage string
 */
export const formatPercentage = (num: number, decimals: number = 1): string => {
    const value = num > 1 ? num : num * 100;
    return `${value.toFixed(decimals)}%`;
};

// ============================================================================
// CLASS NAME UTILITIES
// ============================================================================

/**
 * Conditionally join class names
 * Similar to clsx/classnames but simpler
 *
 * @param classes Class names or conditional objects
 * @returns Joined class string
 *
 * @example
 * ```typescript
 * cn('btn', 'btn-primary', { 'disabled': isDisabled })
 * // 'btn btn-primary disabled' (if isDisabled is true)
 * ```
 */
export const cn = (
    ...classes: (string | Record<string, boolean> | undefined | null | false)[]
): string => {
    return classes
        .filter(Boolean)
        .map((cls) => {
            if (typeof cls === "string") return cls;
            if (typeof cls === "object" && cls !== null) {
                return Object.entries(cls)
                    .filter(([, value]) => value)
                    .map(([key]) => key)
                    .join(" ");
            }
            return "";
        })
        .join(" ")
        .trim();
};

// ============================================================================
// DEBOUNCE AND THROTTLE
// ============================================================================

/**
 * Debounce function - delays execution until after specified time has passed
 *
 * @param func Function to debounce
 * @param delay Delay in milliseconds
 * @returns Debounced function
 */
export const debounce = <T extends (...args: any[]) => any>(
    func: T,
    delay: number,
): ((...args: Parameters<T>) => void) => {
    let timeoutId: NodeJS.Timeout;

    return (...args: Parameters<T>) => {
        clearTimeout(timeoutId);
        timeoutId = setTimeout(() => func(...args), delay);
    };
};

/**
 * Throttle function - limits execution to once per specified time period
 *
 * @param func Function to throttle
 * @param limit Time limit in milliseconds
 * @returns Throttled function
 */
export const throttle = <T extends (...args: any[]) => any>(
    func: T,
    limit: number,
): ((...args: Parameters<T>) => void) => {
    let inThrottle: boolean;

    return (...args: Parameters<T>) => {
        if (!inThrottle) {
            func(...args);
            inThrottle = true;
            setTimeout(() => (inThrottle = false), limit);
        }
    };
};
