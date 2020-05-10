package com.cinema.core.service;

import java.util.Arrays;

public enum InfoField {
    COUNTRY("страна"),
    DIRECTOR("режиссер"),
    WRITER("сценарий"),
    COMPOSER("композитор"),
    GENRE("жанр"),
    RELEASE("премьера (мир)"),
    DURATION("время"),
    ;

    private String value;

    InfoField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static InfoField valueFrom(String value) {
        return Arrays.stream(InfoField.values()).filter(infoField -> infoField.value.equals(value)).findFirst().orElse(null);
    }
}
