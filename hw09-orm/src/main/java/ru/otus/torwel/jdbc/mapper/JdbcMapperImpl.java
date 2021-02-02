package ru.otus.torwel.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.jdbc.DbExecutor;
import ru.otus.torwel.jdbc.sessionmanager.SessionManagerJdbc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final DbExecutor<T> dbExecutor;
    private final SessionManagerJdbc sessionManager;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;
    private final JdbcMapperReflectionHelper<T> jdbcMapperReflectionHelper;

    public JdbcMapperImpl(DbExecutor<T> dbExecutor,
                          SessionManagerJdbc sessionManager,
                          EntityClassMetaData<T> entityClassMetaData,
                          EntitySQLMetaData entitySQLMetaData,
                          JdbcMapperReflectionHelper<T> jdbcMapperReflectionHelper) {
        this.dbExecutor = dbExecutor;
        this.sessionManager = sessionManager;
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
        this.jdbcMapperReflectionHelper = jdbcMapperReflectionHelper;
    }

    // вставка объекта в БД
    @Override
    public void insert(T objectData) throws JdbcMapperSQLException {

        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        Object id;
        String sql = entitySQLMetaData.getInsertSql();
        List<Object> params = jdbcMapperReflectionHelper.getFieldsValues(objectData, fields);
        try {
            id = dbExecutor.executeInsert(getConnection(), sql, params);
        } catch (SQLException e) {
            throw new JdbcMapperSQLException(e);
        }
        Field idField =  entityClassMetaData.getIdField();
        if (idField != null) {
            try {
                idField.setAccessible(true);
                idField.set(objectData, id);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    // Изменения информации об объекте в БД
    @Override
    public void update(T objectData) throws JdbcMapperSQLException {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        try {
            String sql = entitySQLMetaData.getUpdateSql();
            List<Object> params;
            params = jdbcMapperReflectionHelper.getFieldsValues(objectData, fields);

            Object id = jdbcMapperReflectionHelper.getIdValue(objectData);
            params.add(id);
            dbExecutor.executeInsert(getConnection(), sql, params);
        } catch (SQLException e) {
            throw new JdbcMapperSQLException(e);
        }
    }

    // Сохранение или изменение информации об объекте в БД.
    // Наличе объекта в базе определяем только по его @Id полю.
    @Override
    public void insertOrUpdate(T objectData) throws JdbcMapperSQLException {
        Object id = jdbcMapperReflectionHelper.getIdValue(objectData);
        if (id != null) {
            try {
                String sql = String.format("SELECT 1 FROM %s WHERE %s=? limit 1",
                        entityClassMetaData.getName().toLowerCase(),
                        entityClassMetaData.getIdField().getName().toLowerCase());
                Optional<T> optObject = dbExecutor.executeSelect(getConnection(), sql, id, rs -> {
                    try {
                        if (rs.next()) {
                            return jdbcMapperReflectionHelper.createEmptyObject();
                        }
                    } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException ex) {
                        logger.error(ex.getMessage(), ex);
                    }
                    return null;
                });
                if (optObject.isPresent()) {
                    update(objectData);
                }
                else {
                    // объекта с таким id нет в БД, вставляем объект в БД
                    insert(objectData);
                }
            } catch (SQLException throwables) {
                logger.error(throwables.getMessage(), throwables);
            }
        }
        else {
            // у класса объекта нет помеченного @Id поля или
            // у объекта пустой id, вставляем объект в БД
            insert(objectData);
        }
    }

    // Чтение объекта с определенным Id из БД
    @Override
    public T findById(Object id, Class<T> clazz) {
        Optional<T> optObject = Optional.empty();
        try {
            String sql = entitySQLMetaData.getSelectByIdSql();
            optObject = dbExecutor.executeSelect(getConnection(), sql, id, this::apply);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return optObject.orElse(null);
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    private T apply(ResultSet rs) {
        try {
            if (rs.next()) {
                T instance = jdbcMapperReflectionHelper.createEmptyObject();
                restoreObjectFields(rs, instance);
                return instance;
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private void restoreObjectFields(ResultSet rs, T instance) throws SQLException, IllegalAccessException {
        for (Field field : entityClassMetaData.getAllFields()) {
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
