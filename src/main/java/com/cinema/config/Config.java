package com.cinema.config;

import com.cinema.CinemaApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import static com.cinema.config.Config.PrefKey.*;

public class Config {
    public enum PrefKey {
        SCREEN_WIDTH, CARD_WIDTH, LANGUAGE; public enum Language {EN, RU}
    }

    private static Language lang = Language.EN;
    public static final String TITLE = "Cinema";

    public static Language getLang() {
        return lang;
    }

    public static void setLang(Language lang) {
        Config.lang = lang;
    }

    public static String getGenre() {
        return getLang() == Language.RU ? "Жанры" : "Genres";
    }

    private static final Preferences preferences = Preferences.userNodeForPackage(CinemaApplication.class);
    private static final Map<PrefKey, String> defaultPreferences = new HashMap<>();
    static {
        defaultPreferences.put(SCREEN_WIDTH, "1024");
        defaultPreferences.put(CARD_WIDTH, "150");
        defaultPreferences.put(LANGUAGE, Language.EN.name());
    }

    public static String getPreference(PrefKey key) {
        return preferences.get(key.name(), defaultPreferences.get(key));
    }

    public static void setPreference(PrefKey key, String value) {
        preferences.put(key.name(), value);
    }
}
