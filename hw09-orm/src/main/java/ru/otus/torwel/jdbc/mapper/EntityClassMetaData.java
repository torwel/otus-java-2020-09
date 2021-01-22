package ru.otus.torwel.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

/**
 * "Разбирает" объект на составные части
 * @param <T>
 */
public interface EntityClassMetaData<T> {
    String getName();

    Constructor<T> getConstructor() throws NoSuchMethodException;

    Field getIdField();

    List<Field> getAllFields();

    List<Field> getFieldsWithoutId();
}
