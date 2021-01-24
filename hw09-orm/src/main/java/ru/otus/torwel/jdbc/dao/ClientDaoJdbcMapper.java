package ru.otus.torwel.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.sessionmanager.SessionManager;
import ru.otus.torwel.jdbc.DbExecutorImpl;
import ru.otus.torwel.jdbc.mapper.*;
import ru.otus.torwel.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// todo: реализовать все методы класса (и может добавить другие) для выполнения ДЗ

public class ClientDaoJdbcMapper<T> implements ClientDao<T> {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoJdbcMapper.class);

    private final JdbcMapper<T> mapper;
    private final SessionManagerJdbc sessionManager;
    private final DbExecutorImpl<T> dbExecutor;

    public ClientDaoJdbcMapper(SessionManagerJdbc sessionManager,
                               DbExecutorImpl<T> dbExecutor,
                               JdbcMapper<T> jdbcMapper) {
        this.mapper = jdbcMapper;
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public Optional<T> findById(long id) {
//        mapper.findById(id, someClass);
        try {
            String sql = ((EntitySQLMetaData)mapper).getSelectByIdSql();
            return dbExecutor.executeSelect(getConnection(), sql,
                    id, this::apply);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insert(T client) {
//        mapper.insert(client);
        String sql = ((EntitySQLMetaData)mapper).getInsertSql();

        List<Field> fields = ((EntityClassMetaData<?>)mapper).getFieldsWithoutId();
        long id = 0;
        try {
            List<Object> params;
            params = getFieldsValues(client, fields);
            id = dbExecutor.executeInsert(getConnection(), sql, params);
        } catch (SQLException | IllegalAccessException ex) {
            ex.printStackTrace();
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



    private List<Object> getFieldsValues(T object, List<Field> fields) throws IllegalAccessException {
        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            values.add(field.get(object));
        }
        return values;
    }

    private T apply(ResultSet rs) {
        try {
            if (rs.next()) {
                EntityClassMetaData<T> entityCMD = (EntityClassMetaData<T>) mapper;

                Constructor<T> constructor = entityCMD.getConstructor();
                Class<?>[] paramTypes = constructor.getParameterTypes();
                Object[] params = new Object[paramTypes.length];
                for (int idx = 0; idx < params.length; idx++) {
                    params[idx] = paramTypes[idx].isPrimitive() ? 0 : null;
                }
                T instance = constructor.newInstance(params);
//                T instance = constructor.newInstance();
                restoreObjectFields(rs, entityCMD, instance);
                return instance;
            }
        } catch (SQLException | NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void restoreObjectFields(ResultSet rs, EntityClassMetaData<T> entityCMD, T instance) throws SQLException, IllegalAccessException {
        for (Field field : entityCMD.getAllFields()) {
            String fieldName = field.getName();
            Class<?> typeField = field.getType();
            field.setAccessible(true);

            if (!typeField.isPrimitive()) {
                Object value = rs.getObject(fieldName, typeField);
                field.set(instance, value);
            }
            else if (typeField == boolean.class) {
                field.setBoolean(instance, rs.getBoolean(fieldName));
            }
            else if (typeField == char.class) {
                String value = rs.getString(fieldName);
                if (value.length() <= 1) {
                    field.setChar(instance, value.charAt(0));
                }
            }
            else if (typeField == byte.class) {
                field.setByte(instance, rs.getByte(fieldName));
            }
            else if (typeField == short.class) {
                field.setShort(instance, rs.getShort(fieldName));
            }
            else if (typeField == int.class) {
                field.setInt(instance, rs.getInt(fieldName));
            }
            else if (typeField == long.class) {
                field.setLong(instance, rs.getLong(fieldName));
            }
            else if (typeField == float.class) {
                field.setFloat(instance, rs.getFloat(fieldName));
            }
            else if (typeField == double.class) {
                field.setDouble(instance, rs.getDouble(fieldName));
            }
        }
    }
}
