package ru.otus.torwel.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BanknoteTest {
    Banknote banknote;

    @BeforeEach
    void setUp() {
        banknote = new Banknote(CurrencyDenomination.FIVE_THOUSANDS, "SN1550");
    }

    @Test
    void objectsEquals() {
        var expectedB = new Banknote(CurrencyDenomination.FIVE_THOUSANDS, "SN1550");
        assertNotSame(expectedB, banknote);
        assertEquals(expectedB, banknote);
    }

    @Test
    void getterWorks() {
        assertEquals(CurrencyDenomination.FIVE_THOUSANDS, banknote.getDenomination());
    }
}