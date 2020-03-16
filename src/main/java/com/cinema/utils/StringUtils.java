package com.cinema.utils;

public final class StringUtils {

    public static String longToString(long number) {
        if (number > 1_000_000) {
            return String.format("%.1fM", (double) number / 1_000_000);
        } else if (number > 1_000) {
            return String.format("%dk", number / 1_000);
        } else {
            return Long.toString(number);
        }
    }
}
