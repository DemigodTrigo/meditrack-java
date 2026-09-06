package com.airtribe.meditrack.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateUtil {

    private DateUtil() {

    }

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static LocalDateTime parse(String dateTime) {
        Validator.requireNonBlank(dateTime, "Date and time");

        try {
            return LocalDateTime.parse(dateTime.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Invalid date/time format. Use: dd-MM-yyyy HH:mm"
            );
        }
    }

    public static String format(LocalDateTime dateTime) {
        Validator.requireNotNull(dateTime, "Date and time");

        return dateTime.format(FORMATTER);
    }

    public static boolean isPast(LocalDateTime dateTime) {
        Validator.requireNotNull(dateTime, "Date and time");

        return dateTime.isBefore(LocalDateTime.now());
    }

    public static boolean isFuture(LocalDateTime dateTime) {
        Validator.requireNotNull(dateTime, "Date and time");

        return dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean isToday(LocalDateTime dateTime) {
        Validator.requireNotNull(dateTime, "Date and time");

        return dateTime.toLocalDate()
                .equals(LocalDateTime.now().toLocalDate());
    }
}