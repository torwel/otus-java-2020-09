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
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoJdbc.class);

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
        try {
            mapper.insert(client);
        } catch (JdbcMapperSQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
        return client.getId();
    }

    @Override
    public void update(Client client) {
        try {
            mapper.update(client);
        } catch (JdbcMapperSQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    @Override
    public long insertOrUpdate(Client client) {
        try {
            mapper.insertOrUpdate(client);
        } catch (JdbcMapperSQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
        return client.getId();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

}
