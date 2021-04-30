package ru.otus.torwel.h16.core.model;

import ru.otus.messagesystem.client.ResultDataType;

import java.util.List;

public class Clients extends ResultDataType {

    private List<Client> clientList;

    public Clients(List<Client> clientList) {
        this.clientList = clientList;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }
}
