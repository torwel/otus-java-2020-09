package ru.otus.torwel.atm;

public interface Banknote {

    /**
     * Метод возвращает объект, указывающий на значение номинала банкноты.
     *
     * @return достоинство банкноты
     */
    CurrencyDignity getDignity();
}
