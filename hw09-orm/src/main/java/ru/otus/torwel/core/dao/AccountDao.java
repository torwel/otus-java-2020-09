package ru.otus.torwel.core.dao;

import ru.otus.torwel.core.model.Account;
import ru.otus.torwel.core.sessionmanager.SessionManager;

import java.util.Optional;

public interface AccountDao {
    Optional<Account> findById(String id);
    //List<Account> findAll();

    String insert(Account account);

    void update(Account account);
    String insertOrUpdate(Account account);

    SessionManager getSessionManager();
}
