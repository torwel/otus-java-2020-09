package ru.otus.torwel.atm;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ATMTest {

    ATM atm;

    @Test
    void commonTest() {
        atm = new ATMImpl(new ArrayList<>());

        // Пустой банкомат. Баланс 0
        int balance = atm.getBalance();
        assertEquals(0, balance);

        // Добавляем в банкомат кассеты для хранения банкнот
        // Используем все доступные номиналы
        for (CurrencyDignity dig : CurrencyDignity.values()) {
            atm.addCassette(new CassetteImpl("", dig));
        }

        // Добавляем банкноту. Баланс 1000
        atm.placeBanknote(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND, ""));
        balance = atm.getBalance();
        assertEquals(1000, balance);

        // Добавляем все до
        int count = 2;
        for (CurrencyDignity dig : CurrencyDignity.values()) {
            for (int i = 0; i < count; i++ ) {
                atm.placeBanknote(new BanknoteImpl(dig, ""));
            }
        }
        balance = atm.getBalance();
        assertEquals(18720, balance);


        // Далее снимаем суммы денег, в консоль выводим список банкнот

        System.out.println(atm.withdrawCash(12550));
        balance = atm.getBalance();
        System.out.println("Баланс: " + balance);

        System.out.println(atm.withdrawCash(6160));
        balance = atm.getBalance();
        System.out.println("Баланс: " + balance);


        // Пытаемся превысить денежный лимит банкомата
        // Должны получить исключение
        assertThrows(IllegalStateException.class, () -> atm.withdrawCash(20));
    }

}