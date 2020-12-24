package ru.otus.torwel.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CassetteTest {

    Cassette cassette;

    @BeforeEach
    void setUp() {
        cassette = new CassetteImpl("Test Cassette", CurrencyDignity.ONE_THOUSAND);
        cassette.placeBanknote(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND,"sn 001"));
        cassette.placeBanknote(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND,"sn 002"));
        cassette.placeBanknote(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND,"sn 003"));
        cassette.placeBanknote(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND,"sn 004"));
        cassette.placeBanknote(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND,"sn 005"));
    }

    @Test
    void getCassetteDignity() {
        CurrencyDignity expected = CurrencyDignity.ONE_THOUSAND;
        assertEquals(expected, cassette.getCassetteDignity());
    }

    @Test
    void placeBanknote() {
        cassette.placeBanknote(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND, ""));
        assertEquals(6, cassette.getBanknotesCount());
        assertEquals(6000, cassette.getBalance());

        Banknote notSupported = new BanknoteImpl(CurrencyDignity.TEN, "");
        assertThrows(IllegalArgumentException.class, () -> cassette.placeBanknote(notSupported));
    }

    @Test
    void placeBanknotes() {
        ArrayList<Banknote> list = new ArrayList<>();
        list.add(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND, "sn 006"));
        list.add(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND, "sn 007"));
        list.add(new BanknoteImpl(CurrencyDignity.ONE_THOUSAND, "sn 008"));

        cassette.placeBanknotes(list);
        assertEquals(8, cassette.getBanknotesCount());
        assertEquals(8000, cassette.getBalance());

        list.add(new BanknoteImpl(CurrencyDignity.FIFTY, "sn 008"));
        assertThrows(IllegalArgumentException.class, () -> cassette.placeBanknotes(list));
    }

    @Test
    void takeBanknote() {
        Banknote expected = new BanknoteImpl(CurrencyDignity.ONE_THOUSAND,"sn 005");
        assertEquals(expected, cassette.takeBanknote());
        assertEquals(4, cassette.getBanknotesCount());
        assertEquals(4000, cassette.getBalance());

        expected = new BanknoteImpl(CurrencyDignity.ONE_THOUSAND,"sn 001");
        assertNotEquals(expected, cassette.takeBanknote());
        assertEquals(3, cassette.getBanknotesCount());
        assertEquals(3000, cassette.getBalance());

    }

    @Test
    void isEmpty() {
        assertFalse(cassette.isEmpty());
        while (!cassette.isEmpty()) {
            cassette.takeBanknote();
        }
        assertTrue(cassette.isEmpty());
    }
}