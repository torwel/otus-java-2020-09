package ru.otus.torwel;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
     * Класс-обработчик, наоснове которого создается прокси-объект.
     * Содержит оригинальный объект `Car`.
     */
    private static class CarInvocationHandler implements InvocationHandler {
        private final CarLogging car;

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

            // Реализация логирования методов, отмеченных аннотацией @Log
            // Формат вывода:
            // "executed method: methodName, param1: 1, param2: 2"
            // "executed method: methodName, param: 1"
            // "executed method: methodName"

            if (method.isAnnotationPresent(Log.class)) {
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
                System.out.println(sb.toString());
            }

            // Выполнение метода оригинального объекта
            return method.invoke(car, args);
        }
    }
}
