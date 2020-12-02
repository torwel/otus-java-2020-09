package ru.otus.torwel;

public class Launch {

    public static void main(String[] args) {

        Car car = new Car();
        car.setModel("Жига");
        car.setWeight(1200);
        car.setHeight(199);

        CarLogging proxyCar = Ioc.createCar(car);

        proxyCar.modifyValues(1200, 160, "Ока");
        proxyCar.modifyValues(1050, 140);
        proxyCar.modifyValues(850);

        System.out.println(proxyCar.getModel());

    }
}
