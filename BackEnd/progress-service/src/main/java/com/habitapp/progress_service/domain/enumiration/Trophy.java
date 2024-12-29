package com.habitapp.progress_service.domain.enumiration;

public enum Trophy {
    BRONZE,
    SILVER,
    GOLD,
    PLATINUM,
    DIAMOND;

    public static Trophy fromString(String value) {
        try {
            return value != null ? Trophy.valueOf(value.toUpperCase()) : BRONZE;
        } catch (IllegalArgumentException e) {
            return BRONZE;
        }
    }
}
