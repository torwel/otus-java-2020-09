package ru.otus.torwel.h11.core.dao;

import ru.otus.torwel.h11.core.model.Client;
import ru.otus.torwel.h11.core.sessionmanager.SessionManager;

import java.util.Optional;


public interface ClientDao {
    Optional<Client> findById(long id);
    //List<Client> findAll();

    long insert(Client client);

    void update(Client client);

    long insertOrUpdate(Client client);

    SessionManager getSessionManager();
}
