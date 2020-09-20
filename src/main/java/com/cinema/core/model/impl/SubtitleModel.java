package com.cinema.core.model.impl;

import com.cinema.core.config.Lang;
import com.cinema.core.dto.SubtitleFileEntry;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import javafx.event.Event;

import java.time.LocalTime;
import java.util.Set;

public class SubtitleModel extends ObservableModel {

    public static SubtitleModel INSTANCE = new SubtitleModel();

    private Set<SubtitleFileEntry> subtitles;
    private SubtitleFileEntry subtitleFileEntry;
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

    public SubtitleFileEntry getSubtitleFileEntry() {
        return subtitleFileEntry;
    }

    public void actualSubtitleFileEntry(long time) {
        LocalTime localTime = LocalTime.ofNanoOfDay(time * 1_000_000);
        SubtitleFileEntry actualSubtitleFileEntry = subtitles.stream()
                .filter(subtitle -> subtitle.getFrom().isBefore(localTime) && subtitle.getTo().isAfter(localTime))
                .findFirst().orElse(null);
        if ((subtitleFileEntry == null && actualSubtitleFileEntry != null) ||
                (subtitleFileEntry != null && actualSubtitleFileEntry == null) ||
                (actualSubtitleFileEntry != null && !actualSubtitleFileEntry.equals(subtitleFileEntry)) ||
                (subtitleFileEntry != null && !subtitleFileEntry.equals(actualSubtitleFileEntry))) {
            subtitleFileEntry = actualSubtitleFileEntry;
            fireEvent(new Event(ModelEventType.SUBTITLE_UPDATE.getEventType()));
        }
    }
}
