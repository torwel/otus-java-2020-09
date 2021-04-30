package ru.otus.torwel.h16.handlers;

import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.torwel.h16.core.model.Client;
import ru.otus.torwel.h16.core.model.Clients;
import ru.otus.torwel.h16.core.service.DBServiceClient;

import java.util.Optional;

public class SaveClientRequestHandler implements RequestHandler<Client> {

    private final DBServiceClient dbServiceClient;

    public SaveClientRequestHandler(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        Client client = MessageHelper.getPayload(msg);
        dbServiceClient.saveClient(client);
        Clients clients = new Clients(dbServiceClient.findAll());

        Optional<Message> returnMessage = Optional.of(MessageBuilder.buildReplyMessage(msg, clients));
        return returnMessage;
    }
}
