package ru.otus.torwel.testfr;

import ru.otus.torwel.TestThis;
import ru.otus.torwel.testfr.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestFr {

    public static void launchTest(Class<TestThis>  clazz) {

        Method methods[] = clazz.getDeclaredMethods();

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
                execPlan.stream().forEach(testList -> testList.add(0, method));
            }
            if (method.isAnnotationPresent(After.class)) {
                execPlan.stream().forEach(testList -> testList.add(method));
            }
        }

        // Для каждого набора тестирования создаем свой экземпляр класса
        // и на нем выполняем набор тестов
        for (List<Method> testList : execPlan) {
            try {
                final TestThis testObj = clazz.getConstructor().newInstance();
                for (Method method : testList) {
                    try {
                        method.invoke(testObj);
//                        TODO: Здесь нужно ловить все возможные exceptions
//                         и учитывать это как-то в статистике выполнения
                    } catch (ReflectiveOperationException e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }
    }

    void checkResult() {
//      TODO: Нужна методика проверки полученного резльтата теста
    }

    void collectStatistic() {
//        TODO: Нужна методика сбора статистики пройденных тестов. А также ее вывод.
    }
}
