package com.cinema.core.model.impl;

import com.cinema.core.dto.Subtitle;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import javafx.event.Event;

import java.util.Set;

public class SubtitleModel extends ObservableModel {

    public static SubtitleModel INSTANCE = new SubtitleModel();

    private Set<Subtitle> subtitles;


    public void setSubtitles(Set<Subtitle> subtitles) {
        this.subtitles = subtitles;
        fireEvent(new Event(ModelEventType.SUBTITLE_SHOW.getEventType()));
    }
}
