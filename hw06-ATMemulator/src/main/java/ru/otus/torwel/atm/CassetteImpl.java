package ru.otus.torwel.atm;

import java.util.ArrayList;
import java.util.List;

/**
 * Кассета. Должна содержать банкноты одного номинала.
 * Помещается в сейф банкомата.
 */

public class CassetteImpl implements Cassette {

    /**
     * имя кассеты.
     */
    private String name;

    /**
     * Переменная обозначает номинал банкнот, которые можно хранить в данной
     * кассете. Если содержит {@code null}, то нельзя добавлять банкноты.
     * Нельзя изменить номинал, если кассета уже содержит банкноты.
     */
    private CurrencyDenomination currencyDenomination;

    /**
     * Содержит список банкнот, помещенных в кассету.
     */
    private List<Banknote> banknotes;

    /**
     * Создает объект кассеты.
     *
     * @param name имя кассеты
     */
    public CassetteImpl(String name) {
        this.name = name;
        this.banknotes = new ArrayList<>();
    }

    /**
     * Создает объект кассеты.
     *
     * @param name         имя кассеты
     * @param denomination номинал хранимых банкнот
     */
    public CassetteImpl(String name, CurrencyDenomination denomination) {
        this(name);
        this.currencyDenomination = denomination;
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
     * Возвращает ссылку на объект типа {@code CurrencyNominal}, указывающий
     * банкноты какого номинала можно хранить в данной кассете
     *
     * @return номинал, разрешенный для хранения в кассете.
     */
    @Override
    public CurrencyDenomination getCassetteDenomination() {
        return currencyDenomination;
    }

    /**
     * изменяет значение переменной, позволяющей хранить банкноты определенного
     * номинала в кассете. Если кассета уже содержит банкноты, изменять
     * переменную нельзя.
     *
     * @param denomination новый номинал.
     * @throws IllegalStateException, если кассета уже содержит банкноты.
     */
    @Override
    public void setCassetteDenomination(CurrencyDenomination denomination) {
        if (!isEmpty()) {
            throw new IllegalStateException("Unable to set cassette denomination. " +
                    " Cassette is not empty");
        }
        this.currencyDenomination = denomination;
    }

    /**
     * Метод добавляет банкноту в кассету. Перед этим происходит проверка.
     * Номинал банкноты должен соответствовать, номиналу кассеты. В ином случае
     * выбрасывается исключение.
     *
     * @param banknote добавляемая банкнота
     * @throws IllegalArgumentException если номинал кассеты не установлен,
     *         если он не совпадает с номиналом банкноты, если сама банкнота
     *         равна null.
     */
    @Override
    public boolean placeBanknote(Banknote banknote) {
        if (currencyDenomination == null) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    "Cassette denomination is not defined.");
        }
        if (banknote == null) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    "Received banknote is null.");
        }
        if (banknote.getDenomination() != currencyDenomination) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    " It's denomination does not match cassette denomination.");
        }
        return banknotes.add(banknote);
    }

    /**
     * Метод добавляет в кассету банкноты из полученного списка.
     *
     * @param banknotes список добавляемых банкнот
     * @throws IllegalArgumentException если параметр banknotes равен null
     */
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
        return banknotes.size() * currencyDenomination.getDenomination();
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
