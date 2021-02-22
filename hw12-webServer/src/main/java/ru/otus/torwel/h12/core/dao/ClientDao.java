package ru.otus.torwel.h12.core.dao;

import ru.otus.torwel.h12.core.model.Client;
import ru.otus.torwel.h12.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;


public interface ClientDao {
    Optional<Client> findById(long id);
    List<Client> findAll();

    long insert(Client client);

    void update(Client client);

    long insertOrUpdate(Client client);

    SessionManager getSessionManager();
}
