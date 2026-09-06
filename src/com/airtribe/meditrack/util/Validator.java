package com.airtribe.meditrack.util;

public final class Validator {

    private Validator() {
    }

    public static void requireNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be null"
            );
        }
    }

    public static void requirePositive(long value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be positive"
            );
        }
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new IllegalArgumentException(
                    fieldName + " must be positive"
            );
        }
    }

    public static void requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be blank"
            );
        }
    }

    public static void requireValidAge(int age) {
        if (age <= 0 || age > 120) {
            throw new IllegalArgumentException(
                    "age must be between 1 and 120"
            );
        }
    }
}