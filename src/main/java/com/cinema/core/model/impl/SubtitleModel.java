package com.cinema.core.model.impl;

import com.cinema.core.config.Lang;
import com.cinema.core.dto.SubtitleFileEntry;
import com.cinema.core.model.ObservableModel;

import java.time.LocalTime;
import java.util.Set;

public class SubtitleModel extends ObservableModel {

    public static SubtitleModel INSTANCE = new SubtitleModel();

    private Set<SubtitleFileEntry> subtitles;
    private SubtitleFileEntry activeSubtitle;
    private Lang lang;
    private boolean showSubtitles = false;

    public void setLang(Lang lang) {
        this.lang = lang;
    }

    public Lang getLang() {
        return lang;
    }

    public boolean isShowSubtitles() {
        return showSubtitles;
    }

    public void setShowSubtitles(boolean showSubtitles) {
        this.showSubtitles = showSubtitles;
    }

    public void setSubtitles(Set<SubtitleFileEntry> subtitles) {
        this.subtitles = subtitles;
        showSubtitles = true;
    }

    public SubtitleFileEntry getActiveSubtitle() {
        return activeSubtitle;
    }

    public SubtitleFileEntry actualSubtitle(long time) {
        LocalTime localTime = LocalTime.ofNanoOfDay(time * 1_000_000);
        return subtitles.stream()
                .filter(subtitle -> subtitle.getFrom().isBefore(localTime) && subtitle.getTo().isAfter(localTime))
                .findFirst().orElse(null);
    }
}
