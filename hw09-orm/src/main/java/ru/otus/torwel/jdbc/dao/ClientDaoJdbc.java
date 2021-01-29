package ru.otus.torwel.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.sessionmanager.SessionManager;
import ru.otus.torwel.jdbc.mapper.*;
import ru.otus.torwel.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class ClientDaoJdbc implements ClientDao {
    private final JdbcMapper<Client> mapper;
    private final SessionManagerJdbc sessionManager;

    public ClientDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<Client> jdbcMapper) {
        this.mapper = jdbcMapper;
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Client> findById(long id) {
        return Optional.ofNullable(mapper.findById(id, Client.class));
    }

    @Override
    public long insert(Client client) {
        mapper.insert(client);
        return client.getId();
    }

    @Override
    public void update(Client client) {
        mapper.update(client);
    }

    @Override
    public long insertOrUpdate(Client client) {
        mapper.insertOrUpdate(client);
        return client.getId();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
