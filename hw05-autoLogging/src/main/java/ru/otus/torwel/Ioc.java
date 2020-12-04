package ru.otus.torwel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Ioc {

    private Ioc() {
    }

    /**
     * Возвращает прокси-объект на основе создаваемого в методе
     * оригинального объекта, реализующего интерфейс CarLogging.
     * @return proxy-объект
     */
    public static CarLogging createCar() {
        Car car = new Car();
        return createProxyCarLogging(car);
    }

    /**
     * Возвращает прокси-объект на основе переданного оригинального объекта,
     * реализующего интерфейс CarLogging.
     * @param car оригинальный объект
     * @return proxy-объект
     */
    public static CarLogging createCar(Car car) {
        return createProxyCarLogging(car);
    }

    /**
     * Создает прокси-объект на основе переданного оригинального объекта,
     * реализующего интерфейс CarLogging.
     * @param car оригинальный объект
     * @return proxy-объект
     */
    private static CarLogging createProxyCarLogging(Car car) {
        InvocationHandler handler = new CarInvocationHandler(car);
        Class<?>[] interfaces = car.getClass().getInterfaces();
        ClassLoader carClassLoader = car.getClass().getClassLoader();
        return (CarLogging) Proxy.newProxyInstance(carClassLoader, interfaces, handler);
    }

    /**
     * Класс-обработчик, на основе которого создается прокси-объект.
     * Содержит оригинальный объект `Car`.
     */
    private static class CarInvocationHandler implements InvocationHandler {
        private final CarLogging car;
        private List<Method> methodsForLogging = new ArrayList<>();
        private List<Method> methodsExcludeLogging = new ArrayList<>();

        CarInvocationHandler(CarLogging car) {
            this.car = car;
        }

        /**
         * В методе реализовано логирование вызова методов, отмеченных
         * аннотацией `@Log`. Затем вызывается соответствующий метод
         * оригинального объекта.
         * @param proxy прокси-объект, на котором вызван метод.
         * @param method метод интерфейса, который был вызван на прокси-объекте.
         * @param args массив параметров вызванного метода.
         * @return
         * @throws Throwable
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

/*
            // Логирование методов, отмеченных аннотацией @Log без использования
            // кэширования вызванных ранее методов
            if (method.isAnnotationPresent(Log.class)) {
                System.out.println(buildLogMessage(method, args));
            }
*/

            // Логирование методов, отмеченных аннотацией @Log с использованием
            // кэширования вызванных ранее методов
            if (methodsForLogging.contains(method)) {
                System.out.println(buildLogMessage(method, args));
            }
            else {
                if (!methodsExcludeLogging.contains(method)) {
                    if (method.isAnnotationPresent(Log.class)) {
                        methodsForLogging.add(method);
                    }
                    else {
                        methodsExcludeLogging.add(method);
                    }
                }
            }

            // Выполнение метода оригинального объекта
            return method.invoke(car, args);
        }

        /**
         * Метод создает строку для вывода в лог.
         * Формат вывода:
         * "executed method: methodName, param1: 1, param2: 2"
         * "executed method: methodName, param: 1"
         * "executed method: methodName"
         * @param method метод интерфейса, который был вызван на прокси-объекте.
         * @param args массив параметров вызванного метода.
         * @return строка для логирования
         */
        private String buildLogMessage(Method method, Object[] args) {
            StringBuilder sb = new StringBuilder(0);
            sb.append("executed method: ").append(method.getName());
            if (args != null) {
                if (args.length == 1) {
                    sb.append(", param: ").append(args[0]);
                } else if (args.length > 1) {
                    for (int i = 0; i < args.length; i++) {
                        sb.append(", param").append(i + 1).append(": ").append(args[i]);
                    }
                }
            }
            return sb.toString();
        }
    }
}
