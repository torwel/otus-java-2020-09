package ru.otus.torwel.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> clazz;

    private String cacheName;
    private Constructor<T> cacheConstructor;
    private boolean idFieldIsPresented;
    private Field cacheIdField;
    private List<Field> cacheAllFields;
    private List<Field> cacheFieldsWithoutId;
    private T emptyObject;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
        if (cacheName == null) {
            cacheName = clazz.getSimpleName();
        }
        return cacheName;
    }

    @Override
    public Constructor<T> getConstructor() {
        if (cacheConstructor == null) {
            cacheConstructor = (Constructor<T>) clazz.getConstructors()[0];
        }
        return cacheConstructor;
    }

    // Возвращает ссылку на поле класса помеченную аннотацией @Id.
    // Если такого поля нет в классе, вернется null.
    @Override
    public Field getIdField() {
        if (idFieldIsPresented) {
            return cacheIdField;
        }
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldIsPresented = true;
                return cacheIdField  = field;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        if (cacheAllFields == null) {
            cacheAllFields = List.of(clazz.getDeclaredFields());
        }
        return cacheAllFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        if (cacheFieldsWithoutId != null) {
            return cacheFieldsWithoutId;
        }
        cacheFieldsWithoutId = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                cacheFieldsWithoutId.add(field);
            }
        }
        return cacheFieldsWithoutId;
    }

    public T createEmptyObject() throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (emptyObject != null) {
            return emptyObject;
        }
        Constructor<T> constructor = getConstructor();
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int idx = 0; idx < params.length; idx++) {
            params[idx] = paramTypes[idx].isPrimitive() ? 0 : null;
        }
        return emptyObject = constructor.newInstance(params);
    }
}
