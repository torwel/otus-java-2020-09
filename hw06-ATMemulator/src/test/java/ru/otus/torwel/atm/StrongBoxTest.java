package ru.otus.torwel.atm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StrongBoxTest {

    StrongBox sb;

    @BeforeEach
    void setUp() {
        sb = new StrongBox(new ArrayList<>());

        for (CurrencyDenomination cd : CurrencyDenomination.values()) {
            sb.addCassette(new CassetteImpl(cd.name(), cd));
        }
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.TEN));
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.FIFTY));
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.ONE_HUNDRED));
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.TWO_HUNDREDS));
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.FIVE_HUNDREDS));
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.ONE_THOUSAND));
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.TWO_THOUSANDS));
//        sb.addCassette(new CassetteImpl("", CurrencyDenomination.FIVE_THOUSANDS));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDenominations() {
        System.out.println(sb.getDenominations());
        CurrencyDenomination[] expectedArr = {
                CurrencyDenomination.FIVE_THOUSANDS,
                CurrencyDenomination.TWO_THOUSANDS,
                CurrencyDenomination.ONE_THOUSAND,
                CurrencyDenomination.FIVE_HUNDREDS,
                CurrencyDenomination.TWO_HUNDREDS,
                CurrencyDenomination.ONE_HUNDRED,
                CurrencyDenomination.FIFTY,
                CurrencyDenomination.TEN
        };
        assertArrayEquals(expectedArr, sb.getDenominations().toArray(new CurrencyDenomination[0]));
    }

    @Test
    void takeBanknote() {
    }

    @Test
    void placeBanknote() {
    }

    @Test
    void addCassette() {
    }

    @Test
    void removeCassette() {
    }
}