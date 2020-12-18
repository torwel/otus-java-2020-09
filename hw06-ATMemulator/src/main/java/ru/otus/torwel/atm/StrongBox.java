package ru.otus.torwel.atm;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс описывает сейф для кассет с банкнотами.
 * Что должен делать:
 * + хранить кассеты
 * + знать, с каким номиналом есть банкноты
 * + добавлять кассеты
 * + удалять (извлекать) кассеты
 * + выдавать указанные банкноты
 * - принимать банкноты, пускай они сразу кладутся в кассету и доступны для выдачи
 */

// TODO: Сделать интерфейс (а может абстрактный класс?).
//       Данный класс, допустим, "резиновая" реализация интерфейса. Т.е. не имеет
//       ограничений на количество кассет

// TODO: Сделать так, чтобы нельзя было добавлять кассеты с одинаковым номиналом.

public class StrongBox {
    private List<Cassette> cassettes;

    /**
     * Конструктор сейфа.
     *
     * @param cassettes указатель на список кассет для банкнот. Не может быть {@code null}
     * @throws NullPointerException если переданный параметр {@code null}
     */
    public StrongBox(List<Cassette> cassettes) {
        if (cassettes == null) {
            throw new NullPointerException("Can't create StrongBox. Argument cannot be null.");
        }
        this.cassettes = cassettes;
    }

    /**
     * Метод возвращает банкноту указанного номинала, извлекая ее из подходящей
     * кассеты. Количество банкнот в соответствующей кассете уменьшается на 1.
     *
     * @param denomination запрашиваемой банкноты.
     * @return банкноту или {@code null}, если кассета данного номинала пуста.
     * @throws IllegalArgumentException, если параметр {@code denomination = null}.
     */
    public Banknote takeBanknote(CurrencyDenomination denomination) {
        if (denomination == null) {
            throw new IllegalArgumentException("Unable to take banknote. " +
                    "Received denomination is null.");
        }
        Banknote banknote = null;
        for (Cassette cas : cassettes) {
            if (cas.getCassetteDenomination() == denomination) {
                if (!cas.isEmpty()) {
                    banknote = cas.takeBanknote();
                    break;
                }
            }
        }
        return banknote;
    }

    /**
     * Метод находит подходящую по номиналу кассету в сейфе и кладет в нее
     * банкноту, передаваемую аргументом.
     *
     * @param banknote банкнота для размещения в сейфе.
     * @throws IllegalArgumentException, если параметр banknote = null.
     * @throws UnsupportedOperationException, если не найдена кассета
     *         с номиналом, соответствующим номиналу переданной банкноты.
     */
    public void placeBanknote(Banknote banknote) {
        if (banknote == null) {
            throw new IllegalArgumentException("Unable to place banknote. " +
                    "Received denomination is null.");
        }
        boolean placed = false;
        for (Cassette cas : cassettes) {
            if (cas.getCassetteDenomination() == banknote.getDenomination()) {
                placed = cas.placeBanknote(banknote);
                break;
            }
        }
        if (!placed) {
            throw new UnsupportedOperationException("Unable to place banknote. " +
                    "Banknotes of this denomination are not accepted.");
        }
    }

    /**
     * Метод добавляет в сейф кассету для банкнот.
     *
     * @param cassette кассета для банкнот.
     * @throws IllegalArgumentException, если параметр cassette = null.
     */
    public void addCassette(Cassette cassette) {
        if (cassette == null) {
            throw new IllegalArgumentException("Unable to add cassette." +
                    "Cassette must not be null.");
        }
        cassettes.add(cassette);
    }

    /**
     * Метод удаляет из сейфа кассету для банкнот.
     *
     * @param cassette кассета для банкнот.
     * @throws IllegalArgumentException, если параметр cassette = null.
     */
    public void removeCassette(Cassette cassette) {
        if (cassette == null) {
            throw new IllegalArgumentException("Unable to add cassette." +
                    "Cassette must not be null.");
        }
        cassettes.remove(cassette);
    }

    /**
     * Метод возвращает список номиналов кассет, помещенных в сейф.
     * Список отсортирован по убыванию значений номиналов . Если в
     * сейфе нет кассет, возвращает null.
     *
     * @return список номиналов размещенных кассет или
     *         null, если их нет.
     */
    public List<CurrencyDenomination> getDenominations() {
        List<CurrencyDenomination> dens = null;
        if (cassettes != null) {
            dens = new ArrayList<>();
            for (Cassette cas : cassettes) {
                dens.add(cas.getCassetteDenomination());
            }
            dens.sort((o1, o2) -> o2.getDenomination() - o1.getDenomination());
        }
        return dens;
    }

    /**
     * Метод возвращает количество банкнот для запрошенного номинала
     *
     * @param cd номинал, для которого запрошено количество банкнот
     * @return количество банкнот указанного номинала
     */
    public int getCountBanknotes(CurrencyDenomination cd) {
        if (cd == null) {
            throw new IllegalArgumentException("Can't return banknotes count. " +
                    "CurrencyDenomination must be defined.");
        }
        int count = 0;
        for (Cassette cas : cassettes) {
          if (cas.getCassetteDenomination() == cd) {
              count = cas.getBanknotesCount();
              break;
          }
        }
        return count;
    }
}
