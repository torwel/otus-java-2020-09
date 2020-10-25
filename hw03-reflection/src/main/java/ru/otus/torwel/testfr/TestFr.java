package ru.otus.torwel.testfr;

import ru.otus.torwel.TestThis;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestFr {

    public static void launchTest(Class<TestThis>  clazz) {

        Method methods[] = clazz.getDeclaredMethods();

        try {
            final TestThis testObj1 = clazz.getConstructor().newInstance();

            testObj1.setTestObject();
            testObj1.setVariables();
            testObj1.testAddition();



            final TestThis testObj2 = clazz.getConstructor().newInstance();


/*
            Arrays.stream(methods).forEach(
                    method -> {
                        System.out.println(method);
                        Arrays.stream(method.getAnnotations()).forEach(System.out::println);
                        System.out.println("Before: " + method.isAnnotationPresent(Before.class));
                        System.out.println("Test: " + method.isAnnotationPresent(Test.class));
                        System.out.println("After: " + method.isAnnotationPresent(After.class));
                    }
            );
*/

            Arrays.stream(methods).forEach(method -> {
                try {
                    method.invoke(testObj2);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            });


        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

}
