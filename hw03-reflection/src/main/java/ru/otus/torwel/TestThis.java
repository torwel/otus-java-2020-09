package ru.otus.torwel;

import ru.otus.torwel.annotations.*;

public class TestThis {

    @Before
    public void setUp() {
        System.out.println("Run setUp()");
    }

    @Test
    public void testMethod1() {
        System.out.println("Run testMethod1");
    }

    @Test
    public void testMethod2() {
        System.out.println("Run testMethod2");
    }

    @After
    public void tearDown() {
        System.out.println("Run tearDown");
    }
}
