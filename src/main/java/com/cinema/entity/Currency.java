package com.cinema.entity;

public enum Currency {
    USD, EUR, RUB;

    public static Currency getCurrencyFromChar(char symbol) {
        switch (symbol) {
            case '$':
                return USD;
            default:
                return null;
        }
    }
}
