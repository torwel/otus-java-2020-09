package ru.otus.torwel.atm;

import java.util.Objects;

public class Banknote {

    /**
     * Номинал банкноты.
     */
    CurrencyDenomination denomination;

    /**
     * Номер банкноты.
     */
    private String number;

    /**
     * Создание банкноты
     * @param denomination номинал банкноты
     * @param number серийный номер
     */
    public Banknote(CurrencyDenomination denomination, String number) {
        this.denomination = denomination;
        this.number = number;
    }

    public CurrencyDenomination getDenomination() {
        return denomination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banknote banknote = (Banknote) o;
        return denomination == banknote.denomination &&
                number.equals(banknote.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(denomination, number);
    }

    @Override
    public String toString() {
        return "Банкнота. Номер: " + number + ". Номинал: " + denomination.getDenomination() + "\n";
    }
}
