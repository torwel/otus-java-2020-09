package ru.otus.torwel.atm;

public enum CurrencyDignity {
    TEN(10),
    FIFTY(50),
    ONE_HUNDRED(100),
    TWO_HUNDREDS(200),
    FIVE_HUNDREDS(500),
    ONE_THOUSAND(1000),
    TWO_THOUSANDS(2000),
    FIVE_THOUSANDS(5000);

    private final int dignity;


    CurrencyDignity(int dignity) {
        this.dignity = dignity;
    }

    public int getDignity() {
        return dignity;
    }
}
