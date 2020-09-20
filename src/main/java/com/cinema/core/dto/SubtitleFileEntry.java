package com.cinema.core.dto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubtitleFileEntry {

    private LocalTime from;
    private LocalTime to;
    private List<String> elements = new ArrayList<>();

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    public List<String> getElements() {
        return elements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubtitleFileEntry subtitle = (SubtitleFileEntry) o;
        return from.equals(subtitle.from) &&
                to.equals(subtitle.to) &&
                elements.equals(subtitle.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, elements);
    }
}
