package ru.otus.torwel.core.service;

import java.util.Optional;

public interface DBService<T> {
    long saveObject(T objectData);

    Optional<T> getObject(long id);

}
