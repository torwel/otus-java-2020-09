package ru.otus.torwel.core.dao;

import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.sessionmanager.SessionManager;

import java.util.Optional;

// torwel сделал интерфейс параметризованным

public interface ClientDao<T> {
    Optional<T> findById(long id);
    //List<Client> findAll();

    long insert(T client);

    //void update(Client client);
    //long insertOrUpdate(Client client);

    SessionManager getSessionManager();
}
