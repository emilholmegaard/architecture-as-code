package com.webshop.shared.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class for date operations.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public final class DateUtil {
    
    private static final DateTimeFormatter DEFAULT_FORMATTER = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private DateUtil() {
        // Prevent instantiation
    }
    
    /**
     * Formats a date to string using default format
     * @param dateTime the date to format
     * @return formatted string
     */
    public static String format(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DEFAULT_FORMATTER) : "";
    }
    
    /**
     * Calculates days between two dates
     * @param start start date
     * @param end end date
     * @return number of days
     */
    public static long daysBetween(LocalDateTime start, LocalDateTime end) {
        return ChronoUnit.DAYS.between(start, end);
    }
    
    /**
     * Checks if a date is within business hours (9 AM - 6 PM)
     * @param dateTime the date to check
     * @return true if within business hours
     */
    public static boolean isBusinessHours(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return hour >= 9 && hour < 18;
    }
    
    /**
     * Gets the start of the day for a given date
     * @param dateTime the date
     * @return start of day
     */
    public static LocalDateTime startOfDay(LocalDateTime dateTime) {
        return dateTime.truncatedTo(ChronoUnit.DAYS);
    }
    
    /**
     * Gets the end of the day for a given date
     * @param dateTime the date
     * @return end of day
     */
    public static LocalDateTime endOfDay(LocalDateTime dateTime) {
        return startOfDay(dateTime).plusDays(1).minusNanos(1);
    }
}
