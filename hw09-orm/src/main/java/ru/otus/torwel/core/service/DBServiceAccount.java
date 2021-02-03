package ru.otus.torwel.core.service;

import ru.otus.torwel.core.model.Account;

import java.util.Optional;

public interface DBServiceAccount {

    String saveAccount(Account account);

    Optional<Account> getAccount(String id);

    //List<Account> findAll();
}
