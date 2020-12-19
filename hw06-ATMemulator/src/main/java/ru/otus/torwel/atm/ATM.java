package ru.otus.torwel.atm;

import java.util.*;

public class ATM {

    private StrongBox strongBox;


    public static void main(String[] args) {
    }

    public ATM(StrongBox strongBox) {
        if (strongBox == null) {
            throw new IllegalArgumentException("Can't create ATM. Argument not defined.");
        }
        this.strongBox = strongBox;
    }

    /**
     * Метод выдает определенную сумму денег из сейфа банкомата минимальным
     * количеством банкнот. Если сумму нельзя выдать, то выдается исключение
     * об ошибке.
     *
     * @param sumForPay запрошенная денежная сумма
     * @throws IllegalArgumentException если невозможно выдать запрошенную сумму
     */
    public List<Banknote> withdrawCash(int sumForPay) {
        int sum = sumForPay;

        // запросить доступные номиналы банкнот. Отсортированный по убыванию список.
        List<CurrencyDenomination> dens = strongBox.getDenominations();

        // сюда сохраняем промежуточный результат расчетов количества снимаемых банкнот
        Map<CurrencyDenomination, Integer> plan = new TreeMap<>();

        for (CurrencyDenomination den : dens) {

            // Определяем для текущего номинала количество необходимых банкнот (Сумма / номинал)
            int count = sum / den.getDenomination();

            if (count > 0) {
                // Проверяем, доступно ли необходимое количество
                int availableBanknotes = strongBox.getCountBanknotes(den);

                if (count > availableBanknotes) {
                    count = availableBanknotes;
                }
                // Запоминаем результат расчета
                plan.put(den, count);
                sum = sum - count * den.getDenomination();
            }
            // Если сумма равна 0, то решение найдено. Выходим из цикла.
            if (sum == 0) {
                break;
            }
        }
        // Если сумма не нуль, то деньги выдать невозможно
        if (sum != 0) {
            throw new IllegalStateException("Unable to dispense cash. Not enough banknotes.");
        }

        // выдаем результат в виде списка
        List<Banknote> withdrawal = new ArrayList<>();
        for (Map.Entry<CurrencyDenomination, Integer> pair : plan.entrySet()) {
            for (int i = 0; i < pair.getValue(); i++) {
                withdrawal.add(strongBox.takeBanknote(pair.getKey()));
            }
        }
        return withdrawal;
    }


    /**
     * TODO: описание метода сделай
     *
     * @param sumForDeposit
     */
    public void depositingCash(int sumForDeposit) {
        int sum = sumForDeposit;
        // Не любую сумму можно разместить. Необходдимо разбить на доступные номиналы. Или выкинуть ошибку.
        // 1. Запрашиваем номиналы сейфа
        List<CurrencyDenomination> dens = strongBox.getDenominations();

        // 2. Тем же самым жадным алгоритмом определяем на какие номиналы
        //    можно разбить сумму. Но без ограничений количества банкнот.

        // сюда сохраняем промежуточный результат расчетов количества снимаемых банкнот
        Map<CurrencyDenomination, Integer> plan = new TreeMap<>();

        for (CurrencyDenomination den : dens) {

            // Определяем для текущего номинала количество необходимых банкнот (Сумма / номинал)
            int count = sum / den.getDenomination();

            // Запоминаем результат расчета, если есть необходимость
            if (count > 0) {
                plan.put(den, count);
                sum = sum - count * den.getDenomination();
            }
            // Если сумма равна 0, то решение найдено. Выходим из цикла.
            if (sum == 0) {
                break;
            }
        }
        // Если сумма не нуль, то деньги раздать по кассетам невозможно
        if (sum != 0) {
            throw new IllegalArgumentException("Unable to deposit cash. Not supported banknote denominations.");
        }

        // выдаем результат в виде списка
        List<Banknote> depositing = new ArrayList<>();
        for (Map.Entry<CurrencyDenomination, Integer> pair : plan.entrySet()) {
            for (int i = 0; i < pair.getValue(); i++) {
                depositing.add(strongBox.takeBanknote(pair.getKey()));
            }
        }

        // 3. Раскладываем по кассетам сейфа
        depositingCash(depositing);

    }

    /**
     * Метод распределяет банкноты из переданного списка по кассетам сейфа
     * в зависимости от их номинала. Предварительно происходит проверка списка.
     * Если в списке есть банкнота неподдерживаемого номинала, то вызывается
     * исключение. Ни одна банкнота при этом не размещается в банкомате.
     *
     * @param pack список банкнот для размещения в банкомате.
     * @throws IllegalArgumentException если в спсике есть банкнота с номиналом,
                который не поддерживается.
     */
    public void depositingCash(List<Banknote> pack) {
        List<CurrencyDenomination> dens = strongBox.getDenominations();
        for (Banknote banknote : pack) {
            if (!dens.contains(banknote.getDenomination())) {
                throw new IllegalArgumentException("Unable to deposit cash. " +
                        "Banknote denomination not supported: " + banknote.getDenomination());
            }
        }
        pack.forEach(banknote -> strongBox.placeBanknote(banknote));
    }

    /**
     * Метод возвращает сумму банкнот, содержащихся в банкомате.
     *
     * @return сумму банкнот банкомата
     */
    public int cashBalance() {
        return strongBox.getBalance();
    }
}
