package ru.otus.torwel.h16.core.service;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.torwel.h16.core.model.Client;
import ru.otus.torwel.h16.core.model.Clients;

public class FrontendServiceImpl implements FrontendService {
    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getAllClients(MessageCallback<Clients> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, null, MessageType.ALL_CLIENTS, dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void saveClient(Client client, MessageCallback<Clients> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, client, MessageType.SAVE_CLIENT, dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
