package com.cinema.core.config;

public enum Lang {
    EN("eng"), RU("rus"), DE("ger");

    private String value;

    Lang(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
