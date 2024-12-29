package com.habitapp.progress_service.domain.enumiration;

public enum WeekDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static WeekDay fromString(String value) {
        try {
            return value != null ? WeekDay.valueOf(value.toUpperCase()) : MONDAY;
        } catch (IllegalArgumentException e) {
            return MONDAY;
        }
    }
}
