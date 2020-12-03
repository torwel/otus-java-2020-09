package ru.otus.torwel;

public class Launch {

    public static void main(String[] args) {
        // Количество  циклов тестирования
        int testCount = 10000;

        // Здесь начинаем тестирование оригинального объекта
        final long checkpoint1 = System.nanoTime();

        Car car = new Car();

        final long checkpoint2 = System.nanoTime();

        for (int i = 0; i < testCount; i++ ) {
            car.modifyValues(9000+i, 160+i, "Жига+ " + i);
            car.modifyValues(7000+i, 150+i);
            car.modifyValues(199+i);
            System.out.println(car.getModel());
        }

        final long checkpoint3 = System.nanoTime();

        // Здесь начинаем тестирование прокси-объекта
        // Оригинальный объект используем новый
        CarLogging proxyCar = Ioc.createCar();

        final long checkpoint4 = System.nanoTime();

        for (int i = 0; i < testCount; i++ ) {
            proxyCar.modifyValues(1200+i, 160+i, "Ока_" + i);
            proxyCar.modifyValues(1050+i, 140+i);
            proxyCar.modifyValues(850+i);
            System.out.println(proxyCar.getModel());
        }

        final long checkpoint5 = System.nanoTime();

        System.out.println("Preparing original object (microsec): " + ((checkpoint2 - checkpoint1) / 1000) );
        System.out.println("Execution original time (microsec): " + ((checkpoint3 - checkpoint2) / 1000) );

        System.out.println("Preparing proxy object (microsec): " + (checkpoint4 - checkpoint3) / 1000);
        System.out.println("Execution proxy time (microsec): " + ((checkpoint5 - checkpoint4) / 1000) );

    }
}
