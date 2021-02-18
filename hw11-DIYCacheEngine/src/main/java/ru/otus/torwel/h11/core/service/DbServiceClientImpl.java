package ru.otus.torwel.h11.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.h11.cachehw.HwCache;
import ru.otus.torwel.h11.core.dao.ClientDao;
import ru.otus.torwel.h11.core.model.Client;
import ru.otus.torwel.h11.core.sessionmanager.SessionManager;

import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final ClientDao clientDao;
    private final HwCache<String, Client> cache;
    public DbServiceClientImpl(ClientDao clientDao, HwCache<String, Client> cache) {
        this.clientDao = clientDao;
        this.cache = cache;
    }

    @Override
    public long saveClient(Client client) {

        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                long clientId = clientDao.insertOrUpdate(client);
                sessionManager.commitSession();
                logger.info("created client: {}", clientId);
                if (cache != null) {
                    cache.put(buildKey(client.getId()), client);
                    logger.info("client cached: {}", cache.toString());
                }
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
        if (cache != null) {
            Client client = cache.get(buildKey(id));
            if (client != null) {
                return Optional.of(client);
            }
        }
        try (SessionManager sessionManager = clientDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Client> clientOptional = clientDao.findById(id);
                logger.info("client: {}", clientOptional.orElse(null));
                clientOptional.ifPresent(cl -> {
                    assert cache != null;
                    cache.put(buildKey(cl.getId()), cl);
                });
                return clientOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    private String buildKey(long id) {
        return String.valueOf(id);
    }
}
