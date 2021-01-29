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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final DbExecutor<T> dbExecutor;
    private final SessionManagerJdbc sessionManager;
    private final EntityClassMetaDataImpl<T> entityCMD;
    private final EntitySQLMetaDataImpl entitySMD;


    public JdbcMapperImpl(Class<T> clazz, DbExecutor<T> dbExecutor, SessionManagerJdbc sessionManager) {
        this.dbExecutor = dbExecutor;
        this.sessionManager = sessionManager;
        entityCMD = new EntityClassMetaDataImpl<>(clazz);
        entitySMD = new EntitySQLMetaDataImpl(entityCMD);
    }

    // вставка объекта в БД
    @Override
    public void insert(T objectData) {

        List<Field> fields = entityCMD.getFieldsWithoutId();
        Object id = null;
        String sql = entitySMD.getInsertSql();
        List<Object> params = null;
        try {
            params = getFieldsValues(objectData, fields);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
        try {
            id = dbExecutor.executeInsert(getConnection(), sql, params);
        } catch (SQLException e) {
            logger.error(sql);
            logger.error(String.valueOf(params));
            logger.error(e.getMessage(), e);
        }
        Field idField =  entityCMD.getIdField();
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
    public void update(T objectData) {
        List<Field> fields = entityCMD.getFieldsWithoutId();
        try {
            String sql = entitySMD.getUpdateSql();
            List<Object> params;
            params = getFieldsValues(objectData, fields);

            Object id = getIdValue(objectData);
            params.add(id);
            dbExecutor.executeInsert(getConnection(), sql, params);
        } catch (SQLException | IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    // Сохранение или изменение информации об объекте в БД.
    // Наличе объекта в базе определяем только по его @Id полю.
    @Override
    public void insertOrUpdate(T objectData) {
        long id = getIdValue(objectData);
        if (id != 0) {
            try {
                String sql = entitySMD.getSelectByIdSql();
                Optional<T> optObject = dbExecutor.executeSelect(getConnection(), sql, id, rs -> {
                    try {
                        if (rs.next()) {
                            return entityCMD.createEmptyObject();
                        }
                    } catch (SQLException | InstantiationException | InvocationTargetException | IllegalAccessException ex) {
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
            String sql = entitySMD.getSelectByIdSql();
            optObject = dbExecutor.executeSelect(getConnection(), sql, id, this::apply);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return optObject.orElse(null);
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }

    // Возвращает значение поля, помеченного аннотацией @Id
    public long getIdValue(T objectData) {
        long id = 0;
        Field idField = entityCMD.getIdField();
        if (idField != null) {
            try {
                idField.setAccessible(true);
                id = idField.getLong(objectData);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return id;
    }

    // Метод возвращает список значений полей объекта object
    // в порядке, соответствующем списку полей fields
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
                T instance = entityCMD.createEmptyObject();
                restoreObjectFields(rs, instance);
                return instance;
            }
        } catch (SQLException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    private void restoreObjectFields(ResultSet rs, T instance) throws SQLException, IllegalAccessException {
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
