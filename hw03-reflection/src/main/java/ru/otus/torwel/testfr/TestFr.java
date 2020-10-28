package ru.otus.torwel.testfr;

import ru.otus.torwel.testfr.annotations.After;
import ru.otus.torwel.testfr.annotations.Before;
import ru.otus.torwel.testfr.annotations.Test;
import ru.otus.torwel.testfr.exceptions.FailedTestException;
import ru.otus.torwel.testfr.exceptions.PassedTestException;
import ru.otus.torwel.testfr.exceptions.TestFrResultException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFr {

    static private int countPassedTests = 0;
    static private int countFailedTests = 0;

    /**
     * Метод запускает тесты, описанные в методах класса TestThis.
     * Создаются наборы методов, каждый из которых затем выполняется
     * на своем экземпляре тестируемого класса. Каждый набор содержит
     * по порядку: сначала все методы, помеченные аннотацией @Before,
     * затем один метод @Test, в конце идут методы @After.
     * @param clazz - обект Class<TestThis>, из которого будут строиться
     *              наборы для тестирования
     */

    public static void launchTest(Class<?>  clazz) {

        Method[] methods = clazz.getDeclaredMethods();

        // Создаем список отдельных наборов методов для тестирования по количеству @Test методов
        List<List<Method>> execPlan = new ArrayList<>();
        for (Method method: methods) {
            if (method.isAnnotationPresent(Test.class)) {
                List<Method> testList = new ArrayList<>();
                testList.add(method);
                execPlan.add(testList);
            }
        }

        // Дополняем наборы тестирования @Before и @After методами
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                execPlan.forEach(testList -> testList.add(0, method));
            }
            if (method.isAnnotationPresent(After.class)) {
                execPlan.forEach(testList -> testList.add(method));
            }
        }

        // Для каждого набора тестирования создаем свой экземпляр класса
        // и на нем выполняем набор тестов
        for (List<Method> testList : execPlan) {
            try {
                final Object testObj = clazz.getConstructor().newInstance();
                for (Method method : testList) {
                    try {
                        method.invoke(testObj);
                    } catch (InvocationTargetException e) {
                        Throwable ext = e.getCause();
                        if (ext instanceof PassedTestException) {
                            System.out.println("Test '" + method.getName() + "': PASS");
                            countPassedTests++;
                        } else if (ext instanceof FailedTestException) {
                            System.out.println("Test '" + method.getName() + "': FAIL");
                            countFailedTests++;
                        } else {
                            System.out.println("Test '" + method.getName() + "': execution error - " + e.getCause());
                            countFailedTests++;
                        }
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
        printStat();
    }

    /**
     * Метод вызывается для сравнения полученного и ожидаемого результатов.
     * Порядок параметров не важен.
     * @param  a - значение для сравнения
     * @param  b - значение для сравнения
     * @throws PassedTestException если значения совпадают
     * @throws FailedTestException если значения не совпадают
     */
    public static void checkResult(int a, int b) throws TestFrResultException {
        if (a == b)
            throw new PassedTestException();
        else
            throw new FailedTestException();
    }

    /**
     * Метод вызывается для сравнения полученного и ожидаемого результатов.
     * Порядок параметров не важен.
     * @param  strA - строка для сравнения
     * @param  strB - строка для сравнения
     * @throws PassedTestException если строки совпадают
     * @throws FailedTestException если строки не совпадают
     */
    public static boolean checkResult(String strA, String strB) throws TestFrResultException {
        if (strA.equals(strB))
            throw new PassedTestException();
        else
            throw new FailedTestException();
    }

    /**
     * Метод выводит статистику выполнения тестов
     */
    private static void printStat() {
        System.out.println();
        System.out.println("Total performed tests: " + (countPassedTests + countFailedTests));
        System.out.println("Tests passed: " + countPassedTests);
        System.out.println("Tests failed: " + countFailedTests);
        countPassedTests = 0;
        countFailedTests = 0;
    }

}
