package ru.otus.torwel;

public interface CarLogging extends Vehicle {

    @Log
    void modifyValues(int weight);

    @Log
    void modifyValues(int weight, int height);

    @Log
    void modifyValues(int weight, int height, String model);

    @Log
    String getModel();
}
