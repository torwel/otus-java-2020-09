package ru.otus.torwel.jdbc.dao;

import ru.otus.torwel.core.dao.Dao;
import ru.otus.torwel.core.sessionmanager.SessionManager;
import ru.otus.torwel.jdbc.mapper.JdbcMapperImpl;
import ru.otus.torwel.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class JdbcDaoImpl<T> implements Dao<T> {
    private final JdbcMapperImpl<T> mapper;
    private final SessionManagerJdbc sessionManager;
    private final Class<T> clazz;

    public JdbcDaoImpl(SessionManagerJdbc sessionManager, JdbcMapperImpl<T> mapper, Class<T> clazz) {
        this.mapper = mapper;
        this.sessionManager = sessionManager;
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findById(long id) {
        return Optional.ofNullable(mapper.findById(id, clazz));
    }

    @Override
    public long insert(T objectData) {
        mapper.insert(objectData);
        return mapper.getIdValue(objectData);
    }

    @Override
    public void update(T objectData) {
        mapper.update(objectData);
    }

    @Override
    public long insertOrUpdate(T objectData) {
        mapper.insertOrUpdate(objectData);
        return mapper.getIdValue(objectData);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
