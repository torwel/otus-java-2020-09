package ru.otus.torwel.atm;

import java.util.*;

/**
 * Класс описывает сейф для кассет с банкнотами.
 * Что должен делать:
 * + хранить кассеты
 * + знать, с каким достоинством есть банкноты
 * + добавлять кассеты
 * + удалять (извлекать) кассеты
 * + выдавать указанные банкноты
 * - принимать банкноты, пускай они сразу кладутся в кассету и доступны для выдачи
 */

public class ATMImpl implements ATM {
    private List<Cassette> strongBox;

    /**
     * Конструктор банкомата.
     *
     * @param strongBox указатель на список кассет для банкнот. Не может быть {@code null}
     * @throws NullPointerException если переданный параметр {@code null}
     */
    public ATMImpl(List<Cassette> strongBox) {
        if (strongBox == null) {
            throw new NullPointerException("Can't create StrongBox. Argument cannot be null.");
        }
        this.strongBox = strongBox;
    }


    /**
     * Метод возвращает банкноту указанного достоинства, извлекая ее из подходящей
     * кассеты. Количество банкнот в соответствующей кассете уменьшается на 1.
     *
     * @param dignity запрашиваемой банкноты.
     * @return банкноту или {@code null}, если кассета данного достоинства пуста.
     * @throws IllegalArgumentException, если параметр {@code dignity = null}.
     */
    public Banknote takeBanknote(CurrencyDignity dignity) {
        if (dignity == null) {
            throw new IllegalArgumentException("Unable to take banknote. " +
                    "Received banknote dignity is null.");
        }
        Banknote banknote = null;
        for (Cassette cas : strongBox) {
            if (cas.getCassetteDignity() == dignity) {
                if (!cas.isEmpty()) {
                    banknote = cas.takeBanknote();
                    break;
                }
            }
        }
        return banknote;
    }


    /**
     * Метод находит подходящую по достоинству кассету в сейфе и кладет в нее
     * банкноту, передаваемую аргументом.
     *
     * @param banknote банкнота для размещения в сейфе.
     * @throws IllegalArgumentException, если параметр banknote = null
     * @throws IllegalStateException если не найдена кассета с достоинством,
     *         соответствующим достоинству переданной банкноты.
     */
    @Override
    public void placeBanknote(Banknote banknote) {
        if (banknote == null) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    "Received banknote dignity is null.");
        }
        boolean placed = false;
        for (Cassette cas : strongBox) {
            if (cas.getCassetteDignity() == banknote.getDignity()) {
                cas.placeBanknote(banknote);
                placed = true;
                break;
            }
        }
        if (!placed) {
            throw new IllegalStateException("Unable to place banknote. " +
                    "Banknotes of this dignity are not accepted.");
        }
    }


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
    @Override
    public void addCassette(Cassette cassette) {
        if (cassette == null) {
            throw new IllegalArgumentException("Unable to add cassette." +
                    "Argument must be defined.");
        }
        if (containsDignityCassette(cassette)) {
            throw new IllegalArgumentException("Unable to add cassette." +
                    "A cassette of this dignity is already in the ATM.");
        }
        strongBox.add(cassette);
    }


    /**
     * Метод удаляет из сейфа кассету для банкнот.
     *
     * @param currencyDignity кассета для банкнот.
     * @throws IllegalArgumentException, если параметр cassette = null.
     */
    public Cassette removeCassette(CurrencyDignity currencyDignity) {
        if (currencyDignity == null) {
            throw  new IllegalArgumentException("Unable to remove cassette. " +
                    "Argument must be defined.");
        }
        Cassette result = null;
        Iterator<Cassette> it = strongBox.iterator();
        while(it.hasNext()) {
            result = it.next();
            if (result.getCassetteDignity() == currencyDignity) {
                it.remove();
                break;
            }
        }
        return result;
    }


    /**
     * Метод возвращает список номиналов кассет, помещенных в сейф.
     * Список отсортирован по убыванию значений достоинства.
     *
     * @return список номиналов размещенных кассет.
     */
    public List<CurrencyDignity> getDignities() {
        List<CurrencyDignity> digs = new ArrayList<>();
        for (Cassette cas : strongBox) {
            digs.add(cas.getCassetteDignity());
        }
        digs.sort((o1, o2) -> o2.getDignity() - o1.getDignity());
        return digs;
    }


    /**
     * Метод возвращает количество банкнот для запрошенного достоинства
     *
     * @param cd достоинство, для которого запрошено количество банкнот
     * @return количество банкнот указанного достоинства
     */
    public int getCountBanknotes(CurrencyDignity cd) {
        if (cd == null) {
            throw new IllegalArgumentException("Can't return banknotes count. " +
                    "CurrencyDignity must be defined.");
        }
        int count = 0;
        for (Cassette cas : strongBox) {
          if (cas.getCassetteDignity() == cd) {
              count = cas.getBanknotesCount();
              break;
          }
        }
        return count;
    }


    /**
     * Метод возвращает сумму банкнот, содержащихся в кассетах сейфа.
     *
     * @return сумму банкнот сейфа
     */
    @Override
    public int getBalance() {
        int balance = 0;
        for (Cassette cas : strongBox) {
            balance = balance + cas.getBalance();
        }
        return balance;
    }


    /**
     * Метод выдает определенную сумму денег из сейфа банкомата минимальным
     * количеством банкнот. Если сумму нельзя выдать, то выдается исключение
     * об ошибке.
     *
     * @param sumForPay запрошенная денежная сумма
     * @throws IllegalStateException если невозможно выдать запрошенную сумму
     */
    @Override
    public List<Banknote> withdrawCash(int sumForPay) {
        int sum = sumForPay;

        // запросить доступные номиналы банкнот. Отсортирован по убыванию.
        List<CurrencyDignity> dens = getDignities();

        // для сохранения промежуточных результатов расчета количества банкнот
        // подлежащих снятию
        Map<CurrencyDignity, Integer> plan = new TreeMap<>();

        for (CurrencyDignity den : dens) {

            // Определяем для текущего номинала количество необходимых банкнот (Сумма / номинал)
            int count = sum / den.getDignity();

            if (count > 0) {
                // Проверяем, доступно ли необходимое количество
                int availableBanknotes = getCountBanknotes(den);

                if (count > availableBanknotes) {
                    count = availableBanknotes;
                }
                // Запоминаем результат расчета
                plan.put(den, count);
                sum = sum - count * den.getDignity();
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
        for (Map.Entry<CurrencyDignity, Integer> pair : plan.entrySet()) {
            for (int i = 0; i < pair.getValue(); i++) {
                withdrawal.add(takeBanknote(pair.getKey()));
            }
        }
        return withdrawal;
    }


    /**
     * Метод можно использовать для определения возможно ли принять
     * указанную сумму и разложить по имеющимся кассетам с банкнотами.
     * Если возможно, то возвращается список необходимых номиналов банкнот.
     * Если не возможно, то возвращаемый список пуст.
     *
     * @return список номиналов банкнот для размещения суммы
     * @param sumForDeposit сумма для размещения
     */
    public List<CurrencyDignity> defineAllocationSet(int sumForDeposit) {
        int sum = sumForDeposit;
        // Не любую сумму можно разместить. Необходимо разбить на доступные номиналы.
        // 1. Запрашиваем номиналы сейфа
        List<CurrencyDignity> digs = getDignities();

        // 2. Жадным алгоритмом определяем на какие номиналы
        //    можно разбить сумму. Но без ограничений количества банкнот.

        // сюда сохраняем промежуточный результат состава банкнот
        List<CurrencyDignity> requiredDignities = new ArrayList<>();

        for (CurrencyDignity dignity : digs) {

            // Определяем для текущего номинала количество необходимых банкнот (Сумма / номинал)
            int count = sum / dignity.getDignity();

            // Запоминаем результат расчета, если есть необходимость
            if (count > 0) {
                for (int i = 0; i < count; i++) {
                    requiredDignities.add(dignity);
                }
                sum = sum - count * dignity.getDignity();
            }
            // Если сумма равна 0, то решение найдено. Выходим из цикла.
            if (sum == 0) {
                break;
            }
        }
        // Если сумма не нуль, то деньги распределить по кассетам невозможно
        if (sum != 0) {
            requiredDignities = new ArrayList<>();
        }
        return requiredDignities;
    }


    /**
     * Метод распределяет банкноты из переданного списка по кассетам сейфа
     * в зависимости от их номинала. Предварительно происходит проверка списка.
     * Если в списке есть банкнота неподдерживаемого номинала, то вызывается
     * исключение. Ни одна банкнота при этом не размещается в банкомате.
     *
     * @param pack список банкнот для размещения в банкомате.
     * @throws IllegalArgumentException если в списке есть банкнота с номиналом,
     *         который не поддерживается.
     */
    public void depositingCash(List<Banknote> pack) {
        List<CurrencyDignity> dens = getDignities();
        for (Banknote banknote : pack) {
            if (!dens.contains(banknote.getDignity())) {
                throw new IllegalArgumentException("Unable to deposit cash. " +
                        "Banknote denomination not supported: " + banknote.getDignity());
            }
        }
        pack.forEach(this::placeBanknote);
    }


    private boolean containsDignityCassette(Cassette cassette) {
        boolean contains = false;
        for (Cassette internalCas : strongBox) {
            if (internalCas.getCassetteDignity() == cassette.getCassetteDignity()) {
                contains = true;
                break;
            }
        }
        return contains;
    }
}
