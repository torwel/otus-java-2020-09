package ru.otus.torwel.h14.core.dao;

import ru.otus.torwel.h14.core.model.User;
import ru.otus.torwel.h14.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);

    List<User> findAll();

    long insert(User user);

    void update(User user);

    long insertOrUpdate(User user);

    SessionManager getSessionManager();
}
