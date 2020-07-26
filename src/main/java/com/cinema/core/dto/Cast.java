package com.cinema.core.dto;

import java.util.Arrays;

public class Cast {

    private String name;
    private Short priority;
    private Role role;

    public enum Role {
        COMPOSER("Композитор"), DIRECTOR("Режиссер"), ACTOR("Актёр"), WRITER("Сценарий");

        private String value;

        Role(String value) {
            this.value = value;
        }

        public static Role roleOf(String value) {
            return Arrays.stream(Role.values()).filter(role -> value.contains(role.value)).findFirst().orElse(null);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public static Cast buildCast(String name, Cast.Role role, short priority) {
        Cast cast = new Cast();
        cast.setPriority(priority);
        cast.setName(name);
        cast.setRole(role);
        return cast;
    }
}
