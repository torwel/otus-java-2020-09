package ru.otus.torwel.h11.core.service;

import ru.otus.torwel.h11.core.model.Client;

import java.util.Optional;

public interface DBServiceClient {

    long saveClient(Client client);

    Optional<Client> getClient(long id);

    //List<Client> findAll();
}
