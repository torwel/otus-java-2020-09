package ru.otus.torwel.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ATMImplTest {

    ATMImpl atmImpl;

    @BeforeEach
    void setUp() {
        atmImpl = new ATMImpl(new ArrayList<>());

        // Заполняем банкомат кассетами и банкнотами
        // Всего по пять банкнот каждого номинала
        // Общее количество банкнот: 5 * 8 = 40
        // Общий баланс: 44300 денежных единиц

        for (CurrencyDignity cd : CurrencyDignity.values()) {
            atmImpl.addCassette(createCassette(cd, 5));
        }
    }

    @Test
    void getDignities() {
        CurrencyDignity[] expectedArr = {
                CurrencyDignity.FIVE_THOUSANDS,
                CurrencyDignity.TWO_THOUSANDS,
                CurrencyDignity.ONE_THOUSAND,
                CurrencyDignity.FIVE_HUNDREDS,
                CurrencyDignity.TWO_HUNDREDS,
                CurrencyDignity.ONE_HUNDRED,
                CurrencyDignity.FIFTY,
                CurrencyDignity.TEN
        };
        assertArrayEquals(expectedArr, atmImpl.getDignities().toArray(new CurrencyDignity[0]));
    }

    @Test
    void takeBanknote() {
        assertThrows(IllegalArgumentException.class, () -> atmImpl.takeBanknote(null));

        CurrencyDignity cd5000 = CurrencyDignity.FIVE_THOUSANDS;
        Banknote expected = new BanknoteImpl(cd5000, "SN 050000005");
        Banknote banknote = atmImpl.takeBanknote(cd5000);
        assertEquals(expected, banknote);

        int countBank = atmImpl.getCountBanknotes(cd5000);
        for (int i = 0; i < countBank; i++) {
            atmImpl.takeBanknote(cd5000);
        }
        banknote = atmImpl.takeBanknote(cd5000);
        assertNull(banknote);
    }

    @Test
    void placeBanknote() {
        // Пытаемся поместить банкноту в сейф.
        // Ожидаем исключения, т.к. банкнота не определена
        assertThrows(IllegalArgumentException.class,
                () -> atmImpl.placeBanknote(null));

        atmImpl.removeCassette(CurrencyDignity.FIFTY);

        // Пытаемся поместить банкноту неподходящего номинала в сейф.
        // Ожидаем исключения.
        assertThrows(IllegalStateException.class,
                () -> atmImpl.placeBanknote(new BanknoteImpl(CurrencyDignity.FIFTY, "")));
            }

    @Test
    void addCassette() {
        atmImpl = new ATMImpl(new ArrayList<>());

        assertThrows(IllegalArgumentException.class, () -> atmImpl.addCassette(null));

        Cassette cas100 = new CassetteImpl("", CurrencyDignity.ONE_HUNDRED);

        atmImpl.addCassette(cas100);
        CurrencyDignity[] expectedArr = {
                CurrencyDignity.ONE_HUNDRED
        };
        assertArrayEquals(expectedArr, atmImpl.getDignities().toArray(new CurrencyDignity[0]));

        assertThrows(IllegalArgumentException.class, () -> atmImpl.addCassette(cas100));
    }

    @Test
    void removeCassette() {
        Banknote banknote = new BanknoteImpl(CurrencyDignity.FIFTY, "");
        atmImpl.placeBanknote(banknote);
        atmImpl.removeCassette(CurrencyDignity.FIFTY);
        assertThrows(IllegalStateException.class, () -> atmImpl.placeBanknote(banknote));
    }

    @Test
    void getBalance() {
        assertEquals(44300, atmImpl.getBalance());
    }

    @Test
    void getCountBanknotes() {
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.TEN));
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.FIFTY));
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.ONE_HUNDRED));
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.TWO_HUNDREDS));
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.FIVE_HUNDREDS));
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.ONE_THOUSAND));
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.TWO_THOUSANDS));
        assertEquals(5, atmImpl.getCountBanknotes(CurrencyDignity.FIVE_THOUSANDS));
    }

    @Test
    void defineAllocationSet() {
        CurrencyDignity[] expectedArr = new CurrencyDignity[]{
                CurrencyDignity.FIVE_THOUSANDS,
                CurrencyDignity.FIVE_THOUSANDS,
                CurrencyDignity.FIVE_THOUSANDS,
                CurrencyDignity.TWO_THOUSANDS,
                CurrencyDignity.TWO_THOUSANDS
        };
        assertArrayEquals(expectedArr, atmImpl.defineAllocationSet(19_000).toArray(new CurrencyDignity[0]));

        expectedArr = new CurrencyDignity[]{
                CurrencyDignity.FIVE_THOUSANDS,
                CurrencyDignity.TWO_THOUSANDS,
                CurrencyDignity.ONE_THOUSAND,
                CurrencyDignity.FIVE_HUNDREDS,
                CurrencyDignity.TWO_HUNDREDS,
                CurrencyDignity.ONE_HUNDRED,
                CurrencyDignity.FIFTY,
                CurrencyDignity.TEN
        };
        assertArrayEquals(expectedArr, atmImpl.defineAllocationSet(8_860).toArray(new CurrencyDignity[0]));

        expectedArr = new CurrencyDignity[0];
        assertArrayEquals(expectedArr, atmImpl.defineAllocationSet(145).toArray(new CurrencyDignity[0]));
    }


    // Пытаемся забрать больше банкнот, чем есть. Тест пройден, если было выкинуто исключение.
    @Test
    void withdrawCashMoreThanAtmHave() {

        // Создаем сейф, создаем кассеты. Наполняем наличкой.
        atmImpl = new ATMImpl(new ArrayList<>());
        atmImpl.addCassette(createCassette(CurrencyDignity.TEN,4));
        atmImpl.addCassette(createCassette(CurrencyDignity.FIFTY,4));
        atmImpl.addCassette(createCassette(CurrencyDignity.ONE_HUNDRED,4));
        atmImpl.addCassette(createCassette(CurrencyDignity.FIVE_HUNDREDS,4));
        atmImpl.addCassette(createCassette(CurrencyDignity.ONE_THOUSAND,3));

        // Снимаем банкнот больше, чем есть в сейфе. Ожидаем исключение с определенным сообщением
        assertThrows(IllegalStateException.class,
                () -> {
                    for (int i = 0; i < 4; i++) {
                        System.out.printf("Снятие N: %d\n", +i + 1);
                        System.out.println(atmImpl.withdrawCash(1660));
                    }
                });
        System.out.println("Денежный баланс: " + atmImpl.getBalance());
    }

    // Метод создает кассету указанного номинала, и заполняет
    // указанным количеством банкнот
    private Cassette createCassette(CurrencyDignity cd, int count) {
        var cassette = new CassetteImpl(cd.name(), cd);
        cassette.placeBanknotes(createPack(cd,count));
        return cassette;
    }

    // метод создает список банкнот указанного номинала и количества
    private List<Banknote> createPack(CurrencyDignity cd, int count) {
        List<Banknote> pack = new ArrayList<>();
        int numTemplate = cd.getDignity() * 10000;
        for (int i = 1; i <= count; i++) {
            String number = String.format("SN %09d", numTemplate + i);
            pack.add(new BanknoteImpl(cd, number));
        }
        return pack;
    }

}