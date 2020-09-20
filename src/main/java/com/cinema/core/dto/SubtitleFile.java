package com.cinema.core.dto;

import com.cinema.core.config.Lang;
import com.cinema.core.config.Preferences;

import java.util.Objects;

public class SubtitleFile {

    private Lang lang;
    private String file;

    public Lang getLang() {
        return lang;
    }

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public String getFile() {
        return Preferences.getPreference(Preferences.PrefKey.STORAGE) + file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubtitleFile subtitle = (SubtitleFile) o;
        return lang == subtitle.lang &&
                file.equals(subtitle.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lang, file);
    }
}
