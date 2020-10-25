package ru.otus.torwel;

import java.lang.reflect.Method;
import java.util.Arrays;
import ru.otus.torwel.annotations.*;

public class Launch {

    public static void main(String[] args) {
        launchTest(TestThis.class);
    }

    public static void launchTest(Class<?> clazz) {
        Method methods[] = clazz.getDeclaredMethods();
        Arrays.stream(methods).forEach(
            method -> {
            System.out.println(method);
            Arrays.stream(method.getAnnotations()).forEach(System.out::println);
            System.out.println("Before: " + method.isAnnotationPresent(Before.class));
            System.out.println("Test: " + method.isAnnotationPresent(Test.class));
            System.out.println("After: " + method.isAnnotationPresent(After.class));
        }
        );
    }
}
