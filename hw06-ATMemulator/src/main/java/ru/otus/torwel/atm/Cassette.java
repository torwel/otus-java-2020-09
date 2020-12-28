package ru.otus.torwel.atm;

import java.util.List;

public interface Cassette {

    /**
     * Возвращает ссылку на объект типа {@code CurrencyDignity}, указывающий
     * банкноты какого достоинства можно хранить в данной кассете
     *
     * @return достоинство банкнот, разрешенных для хранения в кассете.
     */
    CurrencyDignity getCassetteDignity();

    /**
     * Метод добавляет банкноту в кассету. Перед этим происходит проверка.
     * Достоинство банкноты должно соответствовать, достоинству кассеты. В ином случае
     * выбрасывается исключение.
     *
     * @param banknote добавляемая банкнота
     * @throws IllegalArgumentException если достоинство кассеты не установлено,
     *         если оно не совпадает с достоинством банкноты, если сама банкнота
     *         равна null.
     */
    void placeBanknote(Banknote banknote);

    /**
     * Метод добавляет в кассету банкноты из полученного списка.
     *
     * @param banknotes список добавляемых банкнот
     * @throws IllegalArgumentException если параметр banknotes равен null
     */
    void placeBanknotes(List<Banknote> banknotes);

    /**
     * Метод возвращает последнюю в кассете банкноту, при этом удаляя ее.
     *
     * @return последнюю в списке банкноту или {@code null}, если кассета пустая.
     * @throws IllegalStateException если кассета пуста
     */
    Banknote takeBanknote();

    /**
     * Возвращает количество банкнот, содержащихся в кассете.
     *
     * @return количество банкнот.
     */
    int getBanknotesCount();

    /**
     * Возвращает сумму остатка денежных средств, содержащихся в кассете.
     *
     * @return сумму остатка денежных средств.
     */
    int getBalance();

    /**
     * Возвращает {@code true}, если кассета не содержит банкнот.
     *
     * @return {@code true}, если кассета не содержит банкнот
     */
    boolean isEmpty();
}
