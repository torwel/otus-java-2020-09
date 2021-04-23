package ru.otus.torwel.h16.core.service;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.torwel.h16.core.model.Client;
import ru.otus.torwel.h16.core.model.Clients;

public interface FrontendService {
    void getAllClients(MessageCallback<Clients> dataConsumer);
    void saveClient(Client client, MessageCallback<Clients> dataConsumer);

}
