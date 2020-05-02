package com.cinema.core.model;

import javafx.event.Event;
import javafx.event.EventType;

public enum ModelEventType {
    MOVIES_UPDATE(new EventType<>("MOVIES_UPDATE")),
    SEASON_CHANGE(new EventType<>("SEASON_CHANGE")),
    TRAILER_PLAY(new EventType<>("TRAILER_PLAY")),
    MOVIE_PLAY(new EventType<>("MOVIE_PLAY")),
    SEASONS_SHOW(new EventType<>("SEASONS_SHOW")),
    SHUTDOWN(new EventType<>("SHUTDOWN")),
    TORRENT_UPDATE(new EventType<>("TORRENT_UPDATE")),
//    PLAYER_START(new EventType<>("PLAYER_START")),
    TORRENT_START(new EventType<>("TORRENT_START"));

    private EventType<? extends Event> eventType;

    ModelEventType(EventType<? extends Event> eventType) {
        this.eventType = eventType;
    }

    public EventType<? extends Event> getEventType() {
        return eventType;
    }
}
