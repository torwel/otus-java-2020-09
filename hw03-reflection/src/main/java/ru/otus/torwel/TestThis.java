package ru.otus.torwel;

import ru.otus.torwel.testfr.annotations.After;
import ru.otus.torwel.testfr.annotations.Before;
import ru.otus.torwel.testfr.annotations.Test;
import ru.otus.torwel.testfr.exceptions.TestFrResultException;

import static ru.otus.torwel.testfr.TestFr.checkResult;

public class TestThis {

    private DemoClass DC;

    /**
     * Don't set private DC variable in @Before annotated methods.
     * Their invoke order are not guaranteed.
     */
    public TestThis() {
        DC = new DemoClass("Test case");
    }

    @Before
    public void setSomething() {
        System.out.println("Тест обекта: " + DC.hashCode());
    }

    @Before
    public void setVariables() {
        DC.setArgA(10);
        DC.setArgB(5);
    }

    @Test
    public void testAddition() throws TestFrResultException {
        DC.setOperation('+');
        DC.setArgA(10);
        DC.setArgB(10);
        DC.calculate();
        checkResult(DC.getResult(), 19);
    }

    @Test
    public void testSubtraction() throws TestFrResultException {
        DC.setOperation('-');
        DC.setArgA(50);
        DC.setArgB(10);
        DC.calculate();
        checkResult(DC.getResult(), 40);
    }

    @Test
    public void testMultiplication() throws TestFrResultException {
        DC.setOperation('*');
        DC.setArgA(50);
        DC.setArgB(10);
        DC.calculate();
        checkResult(DC.getResult(), 500);
    }

    @Test
    public void testDivision1() throws TestFrResultException {
        DC.setOperation('/');
        DC.setArgA(5);
        DC.setArgB(1);
        DC.calculate();
        checkResult(DC.getResult(), 5);
    }

    @Test
    public void testDivision2() throws TestFrResultException {
        DC.setOperation('/');
        DC.setArgA(5);
        DC.setArgB(2);
        DC.calculate();
        checkResult(DC.getResult(), 3);
    }

    @Test
    public void testDivision3() throws TestFrResultException {
        DC.setOperation('/');
        DC.setArgA(5);
        DC.setArgB(0);
        DC.calculate();
        checkResult(DC.getResult(), 5);
    }

    @Test
    public void testUnsupportedOp() throws TestFrResultException {
        DC.setOperation('%');
        DC.calculate();
        checkResult(DC.getResult(), 5);
    }

    @Test
    public void testName() throws TestFrResultException {
        checkResult(DC.getOpName(), "Test case");
    }

    @After
    public void tearDown() {
        System.out.println("Тест выполнен: " + DC.hashCode());
    }
}
