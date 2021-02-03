package ru.otus.torwel.core.dao;

public class ClientDaoException extends RuntimeException {
    public ClientDaoException(Exception ex) {
        super(ex);
    }
}
