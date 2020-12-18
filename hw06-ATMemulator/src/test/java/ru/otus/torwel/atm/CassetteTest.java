package ru.otus.torwel.atm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CassetteTest {

    @BeforeEach
    void setUp() {
        
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getBalance() {
        Cassette cassette = new CassetteImpl("Десяточки");
        System.out.println("Банкнота принята. Да!");

    }

    @Test
    void isEmpty() {
    }
}