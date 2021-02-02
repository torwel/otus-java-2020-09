package ru.otus.torwel.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMapperReflectionHelper<T> {
    private final static Logger logger = LoggerFactory.getLogger(JdbcMapperReflectionHelper.class);

    private final EntityClassMetaData<T> entityClassMetaData;

    public JdbcMapperReflectionHelper(EntityClassMetaData<T> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    // Возвращает значение поля, помеченного аннотацией @Id
    public Object getIdValue(T objectData) {
        Object id = null;
        Field idField = entityClassMetaData.getIdField();
        if (idField != null) {
            try {
                idField.setAccessible(true);
                id = idField.get(objectData);
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return id;
    }

    // Метод возвращает список значений полей объекта objectData
    // в порядке, соответствующем списку полей fields
    public List<Object> getFieldsValues(T objectData, List<Field> fields) {
        List<Object> values = new ArrayList<>();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                values.add(field.get(objectData));
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return values;
    }

    // Создает экземпляр класса T. Все поля примитивного типа заполнены 0,
    // все наследники Object - null.
    public T createEmptyObject() throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int idx = 0; idx < params.length; idx++) {
            params[idx] = paramTypes[idx].isPrimitive() ? 0 : null;
        }
        return constructor.newInstance(params);
    }

}
