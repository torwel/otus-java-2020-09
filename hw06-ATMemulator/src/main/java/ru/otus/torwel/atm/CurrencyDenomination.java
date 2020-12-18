package ru.otus.torwel.atm;

public enum CurrencyDenomination {
    TEN(10),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDREDS(200),
    FIVE_HUNDREDS(500),
    ONE_THOUSAND(1000),
    TWO_THOUSANDS(2000),
    FIVE_THOUSANDS(5000);

    private final int denomination;


    CurrencyDenomination(int denomination) {
        this.denomination = denomination;
    }

    public int getDenomination() {
        return denomination;
    }
}
