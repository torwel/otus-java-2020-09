package ru.otus.torwel.h12.core.service;

import ru.otus.torwel.h12.core.model.Client;
import ru.otus.torwel.h12.core.model.User;

import java.util.List;
import java.util.Optional;

public interface DBServiceUser {
    long saveUser(User user);

    Optional<User> getUser(long id);

    Optional<User> getUser(String login);

    List<User> findAll();
}
