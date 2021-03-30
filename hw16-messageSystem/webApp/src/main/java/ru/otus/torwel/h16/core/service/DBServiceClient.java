package ru.otus.torwel.h16.core.service;

import ru.otus.torwel.h16.core.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceClient {

    long saveClient(Client client);

    Optional<Client> getClient(long id);

    Optional<Client> getClient(String name);

    List<Client> findAll();
}
