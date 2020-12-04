package ru.otus.torwel;

public class Car implements Vehicle, CarLogging {
    private String model;
    private int weight;
    private int height;

    public Car() {
    }

    public String getModel() {
        return model;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void modifyValues(int weight) {
        setWeight(weight);
    }

    @Override
    public void modifyValues(int weight, int height) {
        setWeight(weight);
        setHeight(height);
    }

//    @Override
    @Log
    public void modifyValues(int weight, int height, String model) {
        setWeight(weight);
        setHeight(height);
        setModel(model);
    }

    @Override
    public String toString() {
        return "Model: " + model;
    }
}
