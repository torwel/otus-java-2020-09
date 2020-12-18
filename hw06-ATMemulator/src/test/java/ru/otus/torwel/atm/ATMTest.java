package ru.otus.torwel.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    private ATM atm;

    @BeforeEach
    void setUp() {
    }

    @Test
    void common() {
        var strongBox = new StrongBox(new ArrayList<Cassette>());
        Cassette cas10 = new CassetteImpl("Десяточки");
        strongBox.addCassette(cas10);

        assertThrows(UnsupportedOperationException.class,
                () -> strongBox.placeBanknote(new Banknote(CurrencyDenomination.TEN, "")),
                "Unable to place banknote. Received denomination is null.");

        cas10.setCassetteDenomination(CurrencyDenomination.TEN);
        assertDoesNotThrow(() -> strongBox.placeBanknote(new Banknote(CurrencyDenomination.TEN, "")));
    }


    // Пытаемся забрать больше банкнот, чем есть. Тест пройден, если было выкинуто исключение.
    @Test
    void withdrawCash() {

        // Создаем сейф, создаем кассеты. Наполняем наличкой.
        var strongBox = new StrongBox(new ArrayList<Cassette>());
        strongBox.addCassette(createCassette(CurrencyDenomination.TEN,3));
        strongBox.addCassette(createCassette(CurrencyDenomination.FIFTY,3));
        strongBox.addCassette(createCassette(CurrencyDenomination.ONE_HUNDRED,3));
        strongBox.addCassette(createCassette(CurrencyDenomination.FIVE_HUNDREDS,3));
        strongBox.addCassette(createCassette(CurrencyDenomination.ONE_THOUSAND,3));

        atm = new ATM(strongBox);

        assertThrows(IllegalStateException.class,
                () -> {
                    for (int i = 0; i < 4; i++) {
                        System.out.printf("Снятие N: %d\n", +i + 1);
                        System.out.println(atm.withdrawCash(1660));
                    }
                },
                "Unable to dispense cash. Not enough banknotes.");
    }

    // Метод создает кассету указанного номинала, и заполняет
    // указанным количеством банкнот
    private Cassette createCassette(CurrencyDenomination cd, int count) {
        var cassette = new CassetteImpl(cd.name(), cd);
        cassette.placeBanknotes(createPack(cd,count));
        return cassette;
    }

    // метод создает список банкнот указанного номинала и количества
    private List<Banknote> createPack(CurrencyDenomination cd, int count) {
        List<Banknote> pack = new ArrayList<>();
        int numTemplate = cd.getDenomination() * 10000;
        for (int i = 1; i <= count; i++) {
            String number = String.format("SN %09d", numTemplate + i);
            pack.add(new Banknote(cd, number));
        }
        return pack;
    }
}