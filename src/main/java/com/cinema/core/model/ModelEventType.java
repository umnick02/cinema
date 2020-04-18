package com.cinema.core.model;

import javafx.event.Event;
import javafx.event.EventType;

public enum ModelEventType {
    MOVIES_UPDATE(new EventType<>("MOVIES_UPDATE")),
    SEASON_CHANGE(new EventType<>("SEASON_CHANGE")),
    SHUTDOWN(new EventType<>("SHUTDOWN"));

    private EventType<? extends Event> eventType;

    ModelEventType(EventType<? extends Event> eventType) {
        this.eventType = eventType;
    }

    public EventType<? extends Event> getEventType() {
        return eventType;
    }
}
