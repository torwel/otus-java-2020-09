package ru.otus.torwel.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JdbcMapperImpl<T> implements JdbcMapper<T>, EntityClassMetaData<T>, EntitySQLMetaData {

    private final Class<T> clazz;

    public JdbcMapperImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void insert(T objectData) {
        // todo: подготовка вставки объекта в БД:

    }

    @Override
    public void update(T objectData) {
        // todo: подготовка изменения информации об объекте в БД:
        //  генерация SQL-запроса, списка имен полей БД

    }

    @Override
    public void insertOrUpdate(T objectData) {
        // todo: сохранение или изменение информации об объекте в БД

    }

    @Override
    public T findById(Object id, Class<T> clazz) {
        // todo: подготовка чтения определенного объекта из БД
        //  генерация SQL-запроса, списка имен полей БД
        return null;
    }

    // -----  реализация EntityClassMetaData<T> ----------------------------------

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() throws NoSuchMethodException {
        Class<?>[] paramTypes = new Class[]{long.class, String.class, int.class};
        return (Constructor<T>) clazz.getConstructors()[0];

    }

    @Override
    public Field getIdField() {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                return field;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> list = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                list.add(field);
            }
        }
        return list;
    }

// -----  реализация EntitySQLMetaData ----------------------------------

    // SELECT * FROM tableName
    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + getName().toLowerCase();
    }

    // SELECT * FROM tableName WHERE idName = ?
    @Override
    public String getSelectByIdSql() {

        StringBuilder request = new StringBuilder();
        request.append("SELECT * FROM ")
                .append(getName().toLowerCase())
                .append(" WHERE ")
                .append(getIdField().getName().toLowerCase())
                .append(" = ?");
        return request.toString();
    }

    // INSERT INTO tableName (field1,field2) VALUES (?,?)
    @Override
    public String getInsertSql() {
        List<Field> fields = getFieldsWithoutId();
        StringBuilder request = new StringBuilder();
        StringBuilder values = new StringBuilder();

        request.append("INSERT INTO ").append(getName().toLowerCase()).append(" (");
        for (Field field : fields) {
            request.append(field.getName()).append(",");
            values.append("?,");
        }
        values.deleteCharAt(values.length() - 1);
        values.append(')');
        request.deleteCharAt(request.length() - 1);
        request.append(") VALUES (").append(values);
        return request.toString();
    }

    // UPDATE tableName SET field1 = ?, field2 = ?, ... fieldN = ?
    @Override
    public String getUpdateSql() {
        List<Field> fields = getFieldsWithoutId();
        StringBuilder request = new StringBuilder();

        request.append("UPDATE ").append(getName().toLowerCase()).append(" SET ");
        for (Field field : fields) {
            request.append(field.getName()).append(" = ?,");
        }
        request.deleteCharAt(request.length() - 1);
        return request.toString();
    }

}
