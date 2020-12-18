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

            // Проверяем, доступно ли необходимое количество
            int availableBanknotes = strongBox.getCountBanknotes(den);
            if (count > availableBanknotes) {
                count = availableBanknotes;
            }
            // Запоминаем результат расчета
            plan.put(den, count);
            sum = sum - count * den.getDenomination();

            // Если сумма равна 0, то решение найдено. Выходим из цикла.
            if (sum == 0) {
                break;
            }
        }
        // Если сумма положительна, то деньги выдать невозможно
        if (sum > 0) {
            throw new IllegalStateException("Unable to dispense cash. Not enough banknotes.");
        }

        // готовим выдаем результат
        List<Banknote> withdrawal = new ArrayList<>();
        for (Map.Entry<CurrencyDenomination, Integer> pair : plan.entrySet()) {
            for (int i = 0; i < pair.getValue(); i++) {
                withdrawal.add(strongBox.takeBanknote(pair.getKey()));
            }
        }
        return withdrawal;
    }

    public void depositingCash(int sum) {

    }

    public int cashBalance() {

        return 0;
    }
}
