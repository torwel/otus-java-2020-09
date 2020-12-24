package ru.otus.torwel.atm;

import java.util.ArrayList;
import java.util.List;

/**
 * Кассета. Должна содержать банкноты одного достоинства.
 */

public class CassetteImpl implements Cassette {

    /**
     * имя кассеты.
     */
    private String name;


    /**
     * Переменная обозначает достоинство банкнот, которые можно хранить в данной
     * кассете. Если содержит {@code null}, то нельзя добавлять банкноты.
     * Нельзя изменить достоинство, если кассета уже содержит банкноты.
     */
    private CurrencyDignity currencyDignity;


    /**
     * Содержит список банкнот, помещенных в кассету.
     */
    private List<Banknote> banknotes;


    /**
     * Создает объект кассеты.
     *
     * @param name         имя кассеты
     * @param dignity достоинство хранимых банкнот
     */
    public CassetteImpl(String name, CurrencyDignity dignity) {
        this.name = name;
        this.banknotes = new ArrayList<>();
        this.currencyDignity = dignity;
    }


    /**
     * Возвращает имя кассеты.
     *
     * @return имя кассеты
     */
    public String getName() {
        return name;
    }


    /**
     * Устанавливает имя кассеты
     *
     * @param name имя кассеты
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Возвращает ссылку на объект типа {@code CurrencyDignity}, указывающий
     * банкноты какого достоинства можно хранить в данной кассете
     *
     * @return достоинство банкнот, разрешенных для хранения в кассете.
     */
    @Override
    public CurrencyDignity getCassetteDignity() {
        return currencyDignity;
    }


//    /**
//     * изменяет значение переменной, позволяющей хранить банкноты определенного
//     * достоинства в кассете. Если кассета уже содержит банкноты, изменять
//     * переменную нельзя.
//     *
//     * @param dignity новое значение достоинства.
//     * @throws IllegalStateException, если кассета уже содержит банкноты.
//     */
//    public void setCassetteDignity(CurrencyDignity dignity) {
//        if (!isEmpty()) {
//            throw new IllegalStateException("Unable to set cassette dignity. " +
//                    " Cassette is not empty");
//        }
//        this.currencyDignity = dignity;
//    }


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
    @Override
    public void placeBanknote(Banknote banknote) {
        if (currencyDignity == null) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    "Cassette dignity is not defined.");
        }
        if (banknote == null) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    "Received banknote is null.");
        }
        if (banknote.getDignity() != currencyDignity) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    "It's dignity does not match cassette dignity.");
        }
        banknotes.add(banknote);
    }


    /**
     * Метод добавляет в кассету банкноты из полученного списка.
     *
     * @param banknotes список добавляемых банкнот
     * @throws IllegalArgumentException если параметр banknotes равен null
     */
    @Override
    public void placeBanknotes(List<Banknote> banknotes) {
        if (banknotes == null) {
            throw new IllegalArgumentException("Unable to place banknotes. " +
                    "Received list of banknotes is null.");
        }
        banknotes.forEach(this::placeBanknote);
    }


    /**
     * Метод возвращает последнюю в кассете банкноту, при этом удаляя ее.
     *
     * @return последнюю в списке банкноту или {@code null}, если кассета пустая.
     * @throws IllegalStateException если кассета пуста
     */
    @Override
    public Banknote takeBanknote() {
        if (banknotes.isEmpty()) {
            throw new IllegalStateException("Unable to give banknote. Cassette is empty.");
        }
        return banknotes.remove(banknotes.size() - 1);
    }


    /**
     * Возвращает количество банкнот, содержащихся в кассете.
     *
     * @return количество банкнот.
     */
    @Override
    public int getBanknotesCount() {
        return banknotes.size();
    }


    /**
     * Возвращает сумму остатка денежных средств, содержащихся в кассете.
     *
     * @return сумму остатка денежных средств.
     */
    @Override
    public int getBalance() {
        return banknotes.size() * currencyDignity.getDignity();
    }


    /**
     * Возвращает {@code true}, если кассета не содержит банкнот.
     *
     * @return {@code true}, если кассета не содержит банкнот
     */
    @Override
    public boolean isEmpty() {
        return banknotes.isEmpty();
    }
}
