package ru.otus.torwel.core.model;

import ru.otus.torwel.jdbc.mapper.Id;

public class Account {
    private String no;
    private String type;
    private double rest;

    public Account() {}

    public Account(String no, String type, double rest) {
        this.no = no;
        this.type = type;
        this.rest = rest;
    }

    public double getRest() {
        return rest;
    }

    public void setRest(double rest) {
        this.rest = rest;
    }

    @Override
    public String toString() {
        return "Account{" +
                ", no='" + no + '\'' +
                ", type='" + type + '\'' +
                ", rest=" + rest +
                '}';
    }
}
