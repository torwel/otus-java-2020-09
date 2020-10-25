package ru.otus.torwel;

import ru.otus.torwel.testfr.annotations.After;
import ru.otus.torwel.testfr.annotations.Before;
import ru.otus.torwel.testfr.annotations.Test;

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
        System.out.println("Run setSomething. Object hash: " + this.hashCode());
    }

    @Before
    public void setVariables() {
        System.out.println("Run setTestObject. Object hash: " + this.hashCode());
        DC.setArgA(10);
        DC.setArgB(5);
    }

    @Test
    public void testAddition() {
        System.out.println("Run testAddition. Object hash: " + this.hashCode());
        DC.setOperation('+');
        DC.calculate();
        //TODO: Как проверять результат?
    }

    @Test
    public void testSubtraction() {
        System.out.println("Run testSubtraction. Object hash: " + this.hashCode());
        DC.setOperation('-');
        DC.calculate();
        //TODO: Как проверять результат?
    }

    @Test
    public void testMultiplication() {
        System.out.println("Run testMultiplication. Object hash: " + this.hashCode());
        DC.setOperation('*');
        DC.calculate();
        //TODO: Как проверять результат?
    }

/*
    @Test
    public void testDivision() {
        System.out.println("Run testDivision. Object hash: " + this.hashCode());
        DC.setOperation('/');
        DC.calculate();
        //TODO: Как проверять результат?
    }
*/

    @After
    public void tearDown() {
        System.out.println("Run tearDown. Object hash: " + this.hashCode());

    }
}
