package com.cinema.core.config;

public enum Lang {
    EN("eng", "English", "flag/united-kingdom.png"),
    RU("rus", "Russian", "flag/russia.png"),
    DE("ger", "German", "flag/german.png");

    private String value;
    private String fullName;
    private String icon;

    Lang(String value, String fullName, String icon) {
        this.value = value;
        this.fullName = fullName;
        this.icon = icon;
    }

    public String getFullName() {
        return fullName;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return value;
    }
}
