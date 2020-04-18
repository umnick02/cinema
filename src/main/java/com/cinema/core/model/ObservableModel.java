package com.cinema.core.model;

import javafx.event.Event;
import javafx.event.EventTarget;

import java.util.HashSet;
import java.util.Set;

public abstract class ObservableModel implements Observable {

    private final Set<EventTarget> eventTargets = new HashSet<>();

    @Override
    public void registerEventTarget(EventTarget eventTarget) {
        eventTargets.add(eventTarget);
    }

    @Override
    public void unRegisterEventTarget(EventTarget eventTarget) {
        eventTargets.remove(eventTarget);
    }

    @Override
    public void fireEvent(final Event event) {
        eventTargets.forEach(eventTarget -> Event.fireEvent(eventTarget, event));
    }
}
