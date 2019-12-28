package com.cinema.config;

import com.cinema.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import static com.cinema.config.Config.PrefKey.CARD_WIDTH;

public class Config {
    public enum PrefKey {
        CARD_WIDTH
    }

    private static final Preferences preferences = Preferences.userNodeForPackage(Main.class);
    private static final Map<PrefKey, String> defaultPreferences = new HashMap<>();
    static {
        defaultPreferences.put(CARD_WIDTH, "150");
    }

    public static String getPreference(PrefKey key) {
        return preferences.get(key.name(), defaultPreferences.get(key));
    }

    public static void setPreference(PrefKey key, String value) {
        preferences.put(key.name(), value);
    }
}
