package com.zedcode.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Utility class for date and time operations
 * Provides helper methods for date conversions, formatting, and calculations
 *
 * @author ZedCode
 * @version 1.0
 */
public final class DateUtils {

    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    // Common Date Formatters
    public static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DISPLAY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public static final DateTimeFormatter DISPLAY_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Get current date and time in UTC
     *
     * @return current LocalDateTime in UTC
     */
    public static LocalDateTime nowUtc() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    /**
     * Get current date in UTC
     *
     * @return current LocalDate in UTC
     */
    public static LocalDate todayUtc() {
        return LocalDate.now(ZoneOffset.UTC);
    }

    /**
     * Convert LocalDateTime to Date
     *
     * @param localDateTime the LocalDateTime to convert
     * @return Date object
     */
    public static Date toDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert Date to LocalDateTime
     *
     * @param date the Date to convert
     * @return LocalDateTime object
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Convert LocalDate to Date
     *
     * @param localDate the LocalDate to convert
     * @return Date object
     */
    public static Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Convert Date to LocalDate
     *
     * @param date the Date to convert
     * @return LocalDate object
     */
    public static LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Format LocalDateTime to string
     *
     * @param dateTime  the LocalDateTime to format
     * @param formatter the DateTimeFormatter to use
     * @return formatted date string
     */
    public static String format(LocalDateTime dateTime, DateTimeFormatter formatter) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(formatter);
    }

    /**
     * Format LocalDate to string
     *
     * @param date      the LocalDate to format
     * @param formatter the DateTimeFormatter to use
     * @return formatted date string
     */
    public static String format(LocalDate date, DateTimeFormatter formatter) {
        if (date == null) {
            return null;
        }
        return date.format(formatter);
    }

    /**
     * Parse string to LocalDateTime
     *
     * @param dateTimeString the date time string to parse
     * @param formatter      the DateTimeFormatter to use
     * @return LocalDateTime object
     */
    public static LocalDateTime parseToDateTime(String dateTimeString, DateTimeFormatter formatter) {
        if (dateTimeString == null || dateTimeString.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    /**
     * Parse string to LocalDate
     *
     * @param dateString the date string to parse
     * @param formatter  the DateTimeFormatter to use
     * @return LocalDate object
     */
    public static LocalDate parseToDate(String dateString, DateTimeFormatter formatter) {
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Calculate days between two dates
     *
     * @param startDate the start date
     * @param endDate   the end date
     * @return number of days between the dates
     */
    public static long daysBetween(LocalDate startDate, LocalDate endDate) {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    /**
     * Calculate hours between two date times
     *
     * @param startDateTime the start date time
     * @param endDateTime   the end date time
     * @return number of hours between the date times
     */
    public static long hoursBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.HOURS.between(startDateTime, endDateTime);
    }

    /**
     * Calculate minutes between two date times
     *
     * @param startDateTime the start date time
     * @param endDateTime   the end date time
     * @return number of minutes between the date times
     */
    public static long minutesBetween(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return ChronoUnit.MINUTES.between(startDateTime, endDateTime);
    }

    /**
     * Add days to a date
     *
     * @param date the base date
     * @param days number of days to add
     * @return new LocalDate with added days
     */
    public static LocalDate addDays(LocalDate date, long days) {
        if (date == null) {
            return null;
        }
        return date.plusDays(days);
    }

    /**
     * Add hours to a date time
     *
     * @param dateTime the base date time
     * @param hours    number of hours to add
     * @return new LocalDateTime with added hours
     */
    public static LocalDateTime addHours(LocalDateTime dateTime, long hours) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.plusHours(hours);
    }

    /**
     * Check if a date is in the past
     *
     * @param date the date to check
     * @return true if the date is in the past
     */
    public static boolean isPast(LocalDate date) {
        return date != null && date.isBefore(LocalDate.now());
    }

    /**
     * Check if a date time is in the past
     *
     * @param dateTime the date time to check
     * @return true if the date time is in the past
     */
    public static boolean isPast(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }

    /**
     * Check if a date is in the future
     *
     * @param date the date to check
     * @return true if the date is in the future
     */
    public static boolean isFuture(LocalDate date) {
        return date != null && date.isAfter(LocalDate.now());
    }

    /**
     * Check if a date time is in the future
     *
     * @param dateTime the date time to check
     * @return true if the date time is in the future
     */
    public static boolean isFuture(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    /**
     * Get the start of the day for a given date
     *
     * @param date the date
     * @return LocalDateTime at the start of the day (00:00:00)
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    /**
     * Get the end of the day for a given date
     *
     * @param date the date
     * @return LocalDateTime at the end of the day (23:59:59)
     */
    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atTime(23, 59, 59, 999999999);
    }

    /**
     * Convert LocalDateTime to epoch milliseconds
     *
     * @param dateTime the LocalDateTime to convert
     * @return epoch milliseconds
     */
    public static Long toEpochMilli(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Convert epoch milliseconds to LocalDateTime
     *
     * @param epochMilli the epoch milliseconds
     * @return LocalDateTime object
     */
    public static LocalDateTime fromEpochMilli(Long epochMilli) {
        if (epochMilli == null) {
            return null;
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneId.systemDefault());
    }

    /**
     * Check if two date ranges overlap
     *
     * @param start1 start of first range
     * @param end1   end of first range
     * @param start2 start of second range
     * @param end2   end of second range
     * @return true if the ranges overlap
     */
    public static boolean isOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return !start1.isAfter(end2) && !end1.isBefore(start2);
    }

    /**
     * Get the current timestamp as ISO string
     *
     * @return current timestamp in ISO format
     */
    public static String getCurrentTimestampIso() {
        return LocalDateTime.now(ZoneOffset.UTC).format(ISO_DATE_TIME_FORMATTER);
    }
}
