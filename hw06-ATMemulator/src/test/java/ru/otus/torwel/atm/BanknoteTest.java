package ru.otus.torwel.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BanknoteTest {
    Banknote banknote;

    @BeforeEach
    void setUp() {
        banknote = new BanknoteImpl(CurrencyDignity.FIVE_THOUSANDS, "SN1550");
    }

    @Test
    void objectsEquals() {
        var expectedB = new BanknoteImpl(CurrencyDignity.FIVE_THOUSANDS, "SN1550");
        assertNotSame(expectedB, banknote);
        assertEquals(expectedB, banknote);
    }

    @Test
    void getterWorks() {
        assertEquals(CurrencyDignity.FIVE_THOUSANDS, banknote.getDignity());
    }
}