package ru.otus.torwel.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.model.Account;
import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.sessionmanager.SessionManager;
import ru.otus.torwel.jdbc.DbExecutorImpl;
import ru.otus.torwel.jdbc.mapper.*;
import ru.otus.torwel.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// todo: реализовать все методы класса (и может добавить другие) для выполнения ДЗ

public class ClientDaoJdbcMapper implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoJdbc.class);

    private final JdbcMapper<Client> mapper;
    private final SessionManagerJdbc sessionManager;
    private final DbExecutorImpl<Client> dbExecutor;

    public ClientDaoJdbcMapper(SessionManagerJdbc sessionManager,
                               DbExecutorImpl<Client> dbExecutor,
                               JdbcMapper<Client> jdbcMapper) {
        this.mapper = jdbcMapper;
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<Client> findById(long id) {
        return Optional.empty();
    }

    @Override
    public long insert(Client client) {
        mapper.insert(client);
        String sql = ((EntitySQLMetaData)mapper).getInsertSql();

        // todo: сгенерить список значений полей объекта client, соблюсти порядок значений по fields
        List<Field> fields = ((EntityClassMetaData<?>)mapper).getFieldsWithoutId();
        List<Object> params = null;
        try {
            params = getFieldsValues(client, fields);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        System.out.println(sql);
        System.out.println(params);

        long id = 0;
        try {
            id = dbExecutor.executeInsert(getConnection(), sql, params);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    private List<Object> getFieldsValues(Client object, List<Field> fields) throws IllegalAccessException {
        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            values.add(field.get(object));
        }
        return values;
    }
}
