package ru.otus.torwel.atm;

import java.util.Objects;

public class BanknoteImpl implements Banknote {

    /**
     * Достоинство банкноты.
     */
    CurrencyDignity dignity;

    /**
     * Номер банкноты.
     */
    private String number;

    /**
     * Создание банкноты
     * @param dignity достоинство банкноты
     * @param number серийный номер
     */
    public BanknoteImpl(CurrencyDignity dignity, String number) {
        this.dignity = dignity;
        this.number = number;
    }

    /**
     * Метод возвращает объект, указывающий на значение номинала банкноты.
     *
     * @return достоинство банкноты
     */
    @Override
    public CurrencyDignity getDignity() {
        return dignity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BanknoteImpl banknote = (BanknoteImpl) o;
        return dignity == banknote.dignity &&
                number.equals(banknote.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dignity, number);
    }

    @Override
    public String toString() {
        return "Банкнота. Номер: " + number + ". Достоинство: " + dignity.getDignity() + "\n";
    }
}
