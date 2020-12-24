package ru.otus.torwel.atm;

import java.util.List;

public interface ATM {

    /**
     * Метод находит подходящую по достоинству кассету в сейфе и кладет в нее
     * банкноту, передаваемую аргументом.
     *
     * @param banknote банкнота для размещения в сейфе.
     * @throws IllegalArgumentException, если параметр banknote = null
     * @throws IllegalStateException если не найдена кассета с достоинством,
     *         соответствующим достоинству переданной банкноты.
     */
    void placeBanknote(Banknote banknote);


    /**
     * Метод возвращает сумму банкнот, содержащихся в кассетах сейфа.
     *
     * @return сумму банкнот сейфа
     */
    int getBalance();


    /**
     * Метод выдает определенную сумму денег из сейфа банкомата минимальным
     * количеством банкнот. Если сумму нельзя выдать, то выдается исключение
     * об ошибке.
     *
     * @param sumForPay запрошенная денежная сумма
     * @throws IllegalStateException если невозможно выдать запрошенную сумму
     */
    List<Banknote> withdrawCash(int sumForPay);


    /**
     * Метод добавляет в сейф кассету для банкнот. Если кассета для банкнот
     * определенного достоинства уже содержится в банкомате, то она не будет
     * добавлена.
     *
     * @param cassette кассета для банкнот.
     * @throws IllegalArgumentException, если параметр cassette = null или
     *                          если кассета для банкнот такого достоинства уже
     *                          есть в банкомате.
     */
    void addCassette(Cassette cassette);
}
