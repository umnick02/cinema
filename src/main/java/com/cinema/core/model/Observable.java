package com.cinema.core.model;

import javafx.event.Event;
import javafx.event.EventTarget;

public interface Observable {
    void registerEventTarget(EventTarget eventTarget);
    void unRegisterEventTarget(EventTarget eventTarget);
    void fireEvent(Event event);
}
