package ru.otus.torwel.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectForMessage {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    protected Object clone() {
        ObjectForMessage newOFM = new ObjectForMessage();
        newOFM.setData(new ArrayList<>(data));
        return newOFM;
    }
}
