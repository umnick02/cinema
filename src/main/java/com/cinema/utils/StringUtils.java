package com.cinema.utils;

import java.util.HashSet;
import java.util.Set;

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

    public static String buildCountries(String countries) {
        return countries.substring(1, countries.length() - 1).replaceAll("\"", "");
    }

    public static String buildGenres(String... genres) {
        Set<String> result = new HashSet<>();
        for (String genre : genres) {
            if (genre == null) break;
            result.add(genre);
        }
        return String.join(", ", result);
    }
}
