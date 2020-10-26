package ru.otus.torwel;

public class DemoClass {

    private String opName = "initValue";

    private char operation;

    private int argA;
    private int argB;
    private int result;

    public DemoClass(String opName) {
        this.opName = opName;
    }

    public String getOpName() {
        return opName;
    }

    public void setArgA(int argA) {
        this.argA = argA;
    }

    public int getArgA() {
        return argA;
    }

    public void setArgB(int argB) {
        this.argB = argB;
    }

    public int getArgB() {
        return argB;
    }

    public void setOperation(char operation) {
        this.operation = operation;
    }

    public int getResult() {
        return result;
    }

    public void calculate() {
        switch (operation) {
            case '+':
                result = argA + argB;
                break;
            case '-':
                result = argA - argB;
                break;
            case '*':
                result = argA * argB;
                break;
            case '/':
                result = argA / argB;
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public String toString() {
        return String.format("%s \n[ %d + %d = %d ]", opName, argA, argB, result);
    }

}
