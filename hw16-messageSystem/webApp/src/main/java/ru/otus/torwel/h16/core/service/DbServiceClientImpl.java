package ru.otus.torwel.h16.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.torwel.h16.core.dao.ClientDao;
import ru.otus.torwel.h16.core.model.Client;
import ru.otus.torwel.h16.core.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientDao clientDao;

    public DbServiceClientImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public long saveClient(Client client) {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long clientId = clientDao.insertOrUpdate(client);
                sessionManager.commitSession();

                logger.info("created client: {}", clientId);
                return clientId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Client> getClient(long id) {
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
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

    @Override
    public Optional<Client> getClient(String name){
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Client> clientOptional = clientDao.findByName(name);

                logger.info("client get: {}", clientOptional.orElse(null));
                return clientOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                clients = clientDao.findAll();
                logger.info("clients found: {}", clients.size());
                return clients;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return clients;
        }
    }
}
