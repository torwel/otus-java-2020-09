package ru.otus.torwel.core.dao;

import ru.otus.torwel.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface Dao<T> {
    Optional<T> findById(long id);

    long insert(T objectData);

    void update(T objectData);
    long insertOrUpdate(T objectData);

    SessionManager getSessionManager();
}
