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

        // генерация SQL-запроса
        // требуется
        // - имя таблицы
        // - количество полей


        // генерация списка имен полей БД

//        this.objectData = objectData;


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

        // SELECT * FROM table WHERE idn = ?
        //  table - clazz.getSimpleName()
        //  idn - EntityClassMetaData.getIdField.getName()
        //  ? - param id

        try {
            Constructor<?> c = getConstructor();

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


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
        return clazz.getConstructor(paramTypes);

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

    @Override
    public String getSelectAllSql() {
        return null;
    }

    @Override
    public String getSelectByIdSql() {
        return null;
    }

    @Override
    public String getInsertSql() {
        List<Field> fields = getFieldsWithoutId();

        StringBuilder request = new StringBuilder();
        request.append("INSERT INTO ");
        request.append(getName().toLowerCase());
        request.append(" (");

        StringBuilder values = new StringBuilder();
        for (Field field : fields) {
            request.append(field.getName()).append(",");
            values.append("?,");
        }
        values.deleteCharAt(values.length() - 1);
        values.append(')');
        request.deleteCharAt(request.length() - 1);
        request.append(") VALUES (");
        request.append(values);
        return request.toString();
    }

    @Override
    public String getUpdateSql() {
        return null;
    }

}
