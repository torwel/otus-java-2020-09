package ru.otus.torwel;

import ru.otus.torwel.testfr.annotations.After;
import ru.otus.torwel.testfr.annotations.Before;
import ru.otus.torwel.testfr.annotations.Test;
import ru.otus.torwel.testfr.exceptions.TestFrResultException;

import static ru.otus.torwel.testfr.TestFr.checkResult;

public class DemoClassTest {

    private DemoClass DC;

    /**
     * Don't set private DC variable in @Before annotated methods.
     * Their invoke order are not guaranteed.
     */
    public DemoClassTest() {
        DC = new DemoClass("Test case");
    }

    @Before
    public void setVariables() {
        DC.setArgA(2);
        DC.setArgB(2);
    }

    @Test
    public void testAddition() throws TestFrResultException {
        DC.setOperation('+');
        DC.calculate();
        checkResult(DC.getResult(), 4);
    }

    @After
    public void tearDown() {
        System.out.println("Тест выполнен: " + DC.hashCode());
    }
}
