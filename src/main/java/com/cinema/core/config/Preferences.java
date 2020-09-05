package com.cinema.core.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.cinema.core.config.Preferences.PrefKey.*;

public class Preferences {
    public enum PrefKey {
        SCREEN_WIDTH, CARD_WIDTH, STORAGE;
    }
    public static final double PRELOAD_MIN = 0.5;

    public static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(8);
    private static final java.util.prefs.Preferences preferences = java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
    private static final Map<PrefKey, String> defaultPreferences = new HashMap<>();
    static {
        defaultPreferences.put(SCREEN_WIDTH, "1200");
        defaultPreferences.put(STORAGE, "C:\\Users\\umnick\\Downloads\\Cinema\\");
        defaultPreferences.put(CARD_WIDTH, "150");
    }

    public static String getPreference(PrefKey key) {
        return preferences.get(key.name(), defaultPreferences.get(key));
    }

    public static void setPreference(PrefKey key, String value) {
        preferences.put(key.name(), value);
    }
}
