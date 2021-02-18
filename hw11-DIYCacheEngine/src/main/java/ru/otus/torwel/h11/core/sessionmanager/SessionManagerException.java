package ru.otus.torwel.h11.core.sessionmanager;


public class SessionManagerException extends RuntimeException {
    public SessionManagerException(String msg) {
        super(msg);
    }

    public SessionManagerException(Exception ex) {
        super(ex);
    }
}
