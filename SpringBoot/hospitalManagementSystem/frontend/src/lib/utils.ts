import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";

/**
 * Utility function to merge Tailwind CSS classes
 */
export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs));
}

/**
 * Format currency values
 */
export function formatCurrency(
    amount: number,
    currency: string = "USD",
    locale: string = "en-US"
): string {
    return new Intl.NumberFormat(locale, {
        style: "currency",
        currency,
    }).format(amount);
}

/**
 * Format date to readable string
 */
export function formatDate(
    date: Date | string,
    options: Intl.DateTimeFormatOptions = {
        year: "numeric",
        month: "long",
        day: "numeric",
    }
): string {
    const dateObj = typeof date === "string" ? new Date(date) : date;
    return dateObj.toLocaleDateString("en-US", options);
}

/**
 * Format date and time to readable string
 */
export function formatDateTime(
    date: Date | string,
    options: Intl.DateTimeFormatOptions = {
        year: "numeric",
        month: "short",
        day: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    }
): string {
    const dateObj = typeof date === "string" ? new Date(date) : date;
    return dateObj.toLocaleString("en-US", options);
}

/**
 * Format time to readable string
 */
export function formatTime(
    date: Date | string,
    options: Intl.DateTimeFormatOptions = {
        hour: "2-digit",
        minute: "2-digit",
    }
): string {
    const dateObj = typeof date === "string" ? new Date(date) : date;
    return dateObj.toLocaleTimeString("en-US", options);
}

/**
 * Format phone number
 */
export function formatPhoneNumber(phoneNumber: string): string {
    // Remove all non-numeric characters
    const cleaned = phoneNumber.replace(/\D/g, "");

    // Format as (XXX) XXX-XXXX for US numbers
    if (cleaned.length === 10) {
        return cleaned.replace(/(\d{3})(\d{3})(\d{4})/, "($1) $2-$3");
    }

    // Format as +X (XXX) XXX-XXXX for international numbers
    if (cleaned.length === 11 && cleaned.startsWith("1")) {
        return cleaned.replace(/(\d{1})(\d{3})(\d{3})(\d{4})/, "+$1 ($2) $3-$4");
    }

    // Return original if doesn't match expected patterns
    return phoneNumber;
}

/**
 * Validate email address
 */
export function isValidEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate phone number
 */
export function isValidPhoneNumber(phoneNumber: string): boolean {
    const phoneRegex = /^[+]?[\d\s\-\(\)]{10,15}$/;
    return phoneRegex.test(phoneNumber);
}

/**
 * Generate random ID
 */
export function generateId(): string {
    return Math.random().toString(36).substring(2) + Date.now().toString(36);
}

/**
 * Debounce function
 */
export function debounce<T extends (...args: any[]) => any>(
    func: T,
    wait: number
): (...args: Parameters<T>) => void {
    let timeout: NodeJS.Timeout;
    return (...args: Parameters<T>) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => func(...args), wait);
    };
}

/**
 * Throttle function
 */
export function throttle<T extends (...args: any[]) => any>(
    func: T,
    limit: number
): (...args: Parameters<T>) => void {
    let inThrottle: boolean;
    return (...args: Parameters<T>) => {
        if (!inThrottle) {
            func(...args);
            inThrottle = true;
            setTimeout(() => (inThrottle = false), limit);
        }
    };
}

/**
 * Deep clone object
 */
export function deepClone<T>(obj: T): T {
    if (obj === null || typeof obj !== "object") {
        return obj;
    }

    if (obj instanceof Date) {
        return new Date(obj.getTime()) as unknown as T;
    }

    if (obj instanceof Array) {
        return obj.map((item) => deepClone(item)) as unknown as T;
    }

    if (typeof obj === "object") {
        const clonedObj = {} as T;
        for (const key in obj) {
            if (obj.hasOwnProperty(key)) {
                clonedObj[key] = deepClone(obj[key]);
            }
        }
        return clonedObj;
    }

    return obj;
}

/**
 * Convert string to slug
 */
export function slugify(text: string): string {
    return text
        .toLowerCase()
        .trim()
        .replace(/[^\w\s-]/g, "")
        .replace(/[\s_-]+/g, "-")
        .replace(/^-+|-+$/g, "");
}

/**
 * Capitalize first letter of each word
 */
export function titleCase(text: string): string {
    return text.replace(/\w\S*/g, (txt) =>
        txt.charAt(0).toUpperCase() + txt.substring(1).toLowerCase()
    );
}

/**
 * Truncate text to specified length
 */
export function truncate(text: string, length: number): string {
    if (text.length <= length) return text;
    return text.substring(0, length).trim() + "...";
}

/**
 * Get file extension from filename
 */
export function getFileExtension(filename: string): string {
    return filename.slice(((filename.lastIndexOf(".") - 1) >>> 0) + 2);
}

/**
 * Format file size
 */
export function formatFileSize(bytes: number): string {
    if (bytes === 0) return "0 Bytes";

    const k = 1024;
    const sizes = ["Bytes", "KB", "MB", "GB", "TB"];
    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + " " + sizes[i];
}

/**
 * Check if file is image
 */
export function isImageFile(filename: string): boolean {
    const imageExtensions = ["jpg", "jpeg", "png", "gif", "bmp", "webp", "svg"];
    const extension = getFileExtension(filename).toLowerCase();
    return imageExtensions.includes(extension);
}

/**
 * Generate color from string (for avatars, etc.)
 */
export function generateColorFromString(str: string): string {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
        hash = str.charCodeAt(i) + ((hash << 5) - hash);
    }

    const hue = hash % 360;
    return `hsl(${hue}, 70%, 50%)`;
}

/**
 * Get initials from full name
 */
export function getInitials(name: string): string {
    return name
        .split(" ")
        .map((word) => word.charAt(0))
        .join("")
        .toUpperCase()
        .substring(0, 2);
}

/**
 * Calculate age from date of birth
 */
export function calculateAge(dateOfBirth: Date | string): number {
    const today = new Date();
    const birthDate = new Date(dateOfBirth);
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();

    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
        age--;
    }

    return age;
}

/**
 * Get relative time (e.g., "2 hours ago")
 */
export function getRelativeTime(date: Date | string): string {
    const now = new Date();
    const past = new Date(date);
    const diffInSeconds = Math.floor((now.getTime() - past.getTime()) / 1000);

    const intervals = [
        { label: "year", seconds: 31536000 },
        { label: "month", seconds: 2592000 },
        { label: "week", seconds: 604800 },
        { label: "day", seconds: 86400 },
        { label: "hour", seconds: 3600 },
        { label: "minute", seconds: 60 },
    ];

    for (const interval of intervals) {
        const count = Math.floor(diffInSeconds / interval.seconds);
        if (count >= 1) {
            return `${count} ${interval.label}${count > 1 ? "s" : ""} ago`;
        }
    }

    return "just now";
}

/**
 * Check if date is today
 */
export function isToday(date: Date | string): boolean {
    const today = new Date();
    const checkDate = new Date(date);

    return (
        checkDate.getDate() === today.getDate() &&
        checkDate.getMonth() === today.getMonth() &&
        checkDate.getFullYear() === today.getFullYear()
    );
}

/**
 * Check if date is within the next week
 */
export function isWithinWeek(date: Date | string): boolean {
    const now = new Date();
    const checkDate = new Date(date);
    const weekFromNow = new Date(now.getTime() + 7 * 24 * 60 * 60 * 1000);

    return checkDate >= now && checkDate <= weekFromNow;
}

/**
 * Format business hours
 */
export function formatBusinessHours(startTime: string, endTime: string): string {
    try {
        const start = new Date(`1970-01-01T${startTime}`);
        const end = new Date(`1970-01-01T${endTime}`);

        return `${formatTime(start)} - ${formatTime(end)}`;
    } catch (error) {
        return "Invalid time range";
    }
}

/**
 * Get appointment status color
 */
export function getAppointmentStatusColor(status: string): string {
    const statusColors: Record<string, string> = {
        SCHEDULED: "bg-info-100 text-info-800 dark:bg-info-900 dark:text-info-300",
        CONFIRMED: "bg-primary-100 text-primary-800 dark:bg-primary-900 dark:text-primary-300",
        IN_PROGRESS: "bg-warning-100 text-warning-800 dark:bg-warning-900 dark:text-warning-300",
        COMPLETED: "bg-success-100 text-success-800 dark:bg-success-900 dark:text-success-300",
        CANCELLED: "bg-error-100 text-error-800 dark:bg-error-900 dark:text-error-300",
        NO_SHOW: "bg-neutral-100 text-neutral-800 dark:bg-neutral-800 dark:text-neutral-300",
        RESCHEDULED: "bg-accent-100 text-accent-800 dark:bg-accent-900 dark:text-accent-300",
    };

    return statusColors[status] || statusColors.SCHEDULED;
}

/**
 * Get priority color
 */
export function getPriorityColor(priority: string): string {
    const priorityColors: Record<string, string> = {
        LOW: "bg-success-100 text-success-800 dark:bg-success-900 dark:text-success-300",
        MEDIUM: "bg-warning-100 text-warning-800 dark:bg-warning-900 dark:text-warning-300",
        HIGH: "bg-error-100 text-error-800 dark:bg-error-900 dark:text-error-300",
        URGENT: "bg-error-200 text-error-900 dark:bg-error-800 dark:text-error-100",
    };

    return priorityColors[priority] || priorityColors.MEDIUM;
}

/**
 * Convert enum value to display text
 */
export function enumToDisplayText(enumValue: string): string {
    return enumValue
        .split("_")
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
        .join(" ");
}

/**
 * Sort array of objects by property
 */
export function sortBy<T>(
    array: T[],
    property: keyof T,
    direction: "asc" | "desc" = "asc"
): T[] {
    return [...array].sort((a, b) => {
        const aVal = a[property];
        const bVal = b[property];

        if (aVal < bVal) return direction === "asc" ? -1 : 1;
        if (aVal > bVal) return direction === "asc" ? 1 : -1;
        return 0;
    });
}

/**
 * Group array of objects by property
 */
export function groupBy<T>(array: T[], property: keyof T): Record<string, T[]> {
    return array.reduce((groups, item) => {
        const key = String(item[property]);
        if (!groups[key]) {
            groups[key] = [];
        }
        groups[key].push(item);
        return groups;
    }, {} as Record<string, T[]>);
}

/**
 * Check if object is empty
 */
export function isEmpty(obj: any): boolean {
    if (obj == null) return true;
    if (Array.isArray(obj) || typeof obj === "string") return obj.length === 0;
    if (typeof obj === "object") return Object.keys(obj).length === 0;
    return false;
}

/**
 * Safe JSON parse
 */
export function safeJsonParse<T>(json: string, fallback: T): T {
    try {
        return JSON.parse(json);
    } catch {
        return fallback;
    }
}

/**
 * Create query string from object
 */
export function createQueryString(params: Record<string, any>): string {
    const searchParams = new URLSearchParams();

    Object.entries(params).forEach(([key, value]) => {
        if (value !== undefined && value !== null && value !== "") {
            searchParams.set(key, String(value));
        }
    });

    return searchParams.toString();
}

/**
 * Parse query string to object
 */
export function parseQueryString(queryString: string): Record<string, string> {
    const params = new URLSearchParams(queryString);
    const result: Record<string, string> = {};

    for (const [key, value] of params.entries()) {
        result[key] = value;
    }

    return result;
}

/**
 * Download file from blob
 */
export function downloadFile(blob: Blob, filename: string): void {
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
}

/**
 * Copy text to clipboard
 */
export async function copyToClipboard(text: string): Promise<boolean> {
    try {
        await navigator.clipboard.writeText(text);
        return true;
    } catch (error) {
        // Fallback for older browsers
        const textArea = document.createElement("textarea");
        textArea.value = text;
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();

        try {
            document.execCommand("copy");
            document.body.removeChild(textArea);
            return true;
        } catch (fallbackError) {
            document.body.removeChild(textArea);
            return false;
        }
    }
}
