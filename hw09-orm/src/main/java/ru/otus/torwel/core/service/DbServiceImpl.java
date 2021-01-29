package ru.otus.torwel.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.Dao;

import java.util.Optional;

public class DbServiceImpl<T> implements DBService<T> {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceImpl.class);

    private final Dao<T> objectDao;

    public DbServiceImpl(Dao<T> objectDao) {
        this.objectDao = objectDao;
    }

    @Override
    public long saveObject(T objectData) {
        try (var sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var objectId = objectDao.insert(objectData);
                sessionManager.commitSession();

                logger.info("object created");
                return objectId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<T> getObject(long id) {
        try (var sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<T> objectOptional = objectDao.findById(id);

                logger.info("client: {}", objectOptional.orElse(null));
                return objectOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    public void updateClient(T objectData) {
        try (var sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                objectDao.update(objectData);
                sessionManager.commitSession();

                logger.info("object updated");
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    public long insertOrUpdate(T objectData) {
        try (var sessionManager = objectDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var objectId = objectDao.insertOrUpdate(objectData);
                sessionManager.commitSession();

                logger.info("object inserted or updated");
                return objectId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }
}
