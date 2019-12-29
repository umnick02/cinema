package com.cinema.entity;

public enum Currency {
    USD, EUR, RUB;

    public static Currency getCurrencyFromChar(char symbol) {
        if (symbol == '$') {
            return USD;
        }
        return null;
    }
}
