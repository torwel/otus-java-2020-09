package ru.otus.torwel;

import ru.otus.torwel.testfr.annotations.After;
import ru.otus.torwel.testfr.annotations.Before;
import ru.otus.torwel.testfr.annotations.Test;
import ru.otus.torwel.testfr.exceptions.TestFrResultException;

import static ru.otus.torwel.testfr.TestFr.checkResult;

public class IntegerTest {

    private Integer x;

    @Before
    public void initVariable() {
        x = 1000;
    }

    @Test
    public void integerEquals1000() throws TestFrResultException {
        checkResult(x, 1000);
    }

    @Test
    public void integerEquals155551() throws TestFrResultException {
        x = 155_551;
        checkResult(x, 155_551);
    }

    @After
    public void printObjectHash() {
        System.out.println("Хэш-кодтестируемого класса: " + x.hashCode());
    }
}
