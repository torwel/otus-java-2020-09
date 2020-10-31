package ru.otus.torwel;

import static ru.otus.torwel.testfr.TestFr.*;


public class Launch {

    public static void main(String[] args) {
        System.out.println("\nRun tests for class TestThis...\n");
        launchTest(TestThis.class);

        System.out.println("\nRun tests for class DemoClassTest...\n");
        launchTest(DemoClassTest.class);

        System.out.println("\nRun tests for class IntegerTest...\n");
        launchTest(IntegerTest.class);


//        debugDC();
    }

    public static void debugDC() {
        DemoClass dc = new DemoClass("Calc expression");
        try {
            System.out.println(dc);
            dc.setArgA(2);
            dc.setArgB(5);
            dc.setOperation('+');
            dc.calculate();
            System.out.println(dc);

            dc.setOperation('-');
            dc.calculate();
            System.out.println(dc);

            dc.setOperation('*');
            dc.calculate();
            System.out.println(dc);

//            dc.setOperation('/');
//            dc.calculate();
//            System.out.println(dc);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
}
