package ru.otus.torwel.jdbc.mapper;

/**
 * Сохраняет объект в базу, читает объект из базы
 * @param <T>
 */
public interface JdbcMapper<T> {
    void insert(T objectData) throws JdbcMapperSQLException;

    void update(T objectData) throws JdbcMapperSQLException;

    void insertOrUpdate(T objectData) throws JdbcMapperSQLException;

    T findById(Object id, Class<T> clazz);
//    List<T> findAll(Class<T> clazz);
}
