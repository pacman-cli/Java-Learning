package com.zedcode.common.util;

import java.util.Collection;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Utility class for String operations
 * Provides common string manipulation and validation methods
 */
public class StringUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[+]?[(]?[0-9]{1,4}[)]?[-\\s\\.]?[(]?[0-9]{1,4}[)]?[-\\s\\.]?[0-9]{1,9}$"
    );

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Checks if a string is null or empty
     *
     * @param str the string to check
     * @return true if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Checks if a string is null, empty, or contains only whitespace
     *
     * @param str the string to check
     * @return true if string is blank
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a string is not null and not empty
     *
     * @param str the string to check
     * @return true if string is not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Checks if a string is not blank
     *
     * @param str the string to check
     * @return true if string is not blank
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Returns the string or default value if null or empty
     *
     * @param str          the string to check
     * @param defaultValue the default value
     * @return the string or default value
     */
    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    /**
     * Returns the string or default value if null or blank
     *
     * @param str          the string to check
     * @param defaultValue the default value
     * @return the string or default value
     */
    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    /**
     * Capitalizes the first letter of a string
     *
     * @param str the string to capitalize
     * @return capitalized string
     */
    public static String capitalize(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Converts a string to camelCase
     *
     * @param str the string to convert
     * @return camelCase string
     */
    public static String toCamelCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        String[] parts = str.split("[\\s_-]+");
        StringBuilder result = new StringBuilder(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            result.append(capitalize(parts[i].toLowerCase()));
        }
        return result.toString();
    }

    /**
     * Converts a string to snake_case
     *
     * @param str the string to convert
     * @return snake_case string
     */
    public static String toSnakeCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }

    /**
     * Validates if a string is a valid email address
     *
     * @param email the email to validate
     * @return true if valid email
     */
    public static boolean isValidEmail(String email) {
        if (isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates if a string is a valid phone number
     *
     * @param phone the phone number to validate
     * @return true if valid phone number
     */
    public static boolean isValidPhone(String phone) {
        if (isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Truncates a string to a specified length
     *
     * @param str    the string to truncate
     * @param length the maximum length
     * @return truncated string
     */
    public static String truncate(String str, int length) {
        if (isEmpty(str) || str.length() <= length) {
            return str;
        }
        return str.substring(0, length);
    }

    /**
     * Truncates a string and adds ellipsis
     *
     * @param str    the string to truncate
     * @param length the maximum length (including ellipsis)
     * @return truncated string with ellipsis
     */
    public static String truncateWithEllipsis(String str, int length) {
        if (isEmpty(str) || str.length() <= length) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }

    /**
     * Joins a collection of strings with a delimiter
     *
     * @param collection the collection to join
     * @param delimiter  the delimiter
     * @return joined string
     */
    public static String join(Collection<?> collection, String delimiter) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Object item : collection) {
            if (!first) {
                result.append(delimiter);
            }
            result.append(item);
            first = false;
        }
        return result.toString();
    }

    /**
     * Removes all whitespace from a string
     *
     * @param str the string to process
     * @return string without whitespace
     */
    public static String removeWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("\\s+", "");
    }

    /**
     * Generates a random UUID string
     *
     * @return UUID string
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Masks a string except for the last n characters
     *
     * @param str           the string to mask
     * @param visibleChars  number of characters to leave visible
     * @param maskCharacter the character to use for masking
     * @return masked string
     */
    public static String maskString(String str, int visibleChars, char maskCharacter) {
        if (isEmpty(str) || str.length() <= visibleChars) {
            return str;
        }
        int maskLength = str.length() - visibleChars;
        return String.valueOf(maskCharacter).repeat(maskLength) + str.substring(maskLength);
    }

    /**
     * Checks if a string contains only digits
     *
     * @param str the string to check
     * @return true if string contains only digits
     */
    public static boolean isNumeric(String str) {
        if (isBlank(str)) {
            return false;
        }
        return str.matches("\\d+");
    }

    /**
     * Checks if a string contains only letters
     *
     * @param str the string to check
     * @return true if string contains only letters
     */
    public static boolean isAlpha(String str) {
        if (isBlank(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    /**
     * Checks if a string contains only letters and digits
     *
     * @param str the string to check
     * @return true if string contains only letters and digits
     */
    public static boolean isAlphanumeric(String str) {
        if (isBlank(str)) {
            return false;
        }
        return str.matches("[a-zA-Z0-9]+");
    }

    /**
     * Reverses a string
     *
     * @param str the string to reverse
     * @return reversed string
     */
    public static String reverse(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Checks if two strings are equal ignoring case
     *
     * @param str1 first string
     * @param str2 second string
     * @return true if strings are equal ignoring case
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.equalsIgnoreCase(str2);
    }
}
