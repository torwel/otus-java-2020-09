package ru.otus.torwel.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.model.Client;

import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientDao clientDao;

    public DbServiceClientImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public long saveClient(Client client) {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var clientId = clientDao.insert(client);
                sessionManager.commitSession();

                logger.info("created client: {}", clientId);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Client> getClient(long id) {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Client> clientOptional = clientDao.findById(id);

                logger.info("client: {}", clientOptional.orElse(null));
                return clientOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    public void updateClient(Client client) {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                clientDao.update(client);
                sessionManager.commitSession();

                logger.info("updated client: {}", client.getId());
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    public long insertOrUpdate(Client client) {
        try (var sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var clientId = clientDao.insertOrUpdate(client);
                sessionManager.commitSession();

                logger.info("Inserted or updated client: {}", clientId);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }
}
