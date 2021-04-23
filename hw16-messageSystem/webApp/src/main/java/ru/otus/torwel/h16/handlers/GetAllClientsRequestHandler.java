package ru.otus.torwel.h16.handlers;

import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.torwel.h16.core.model.Clients;
import ru.otus.torwel.h16.core.service.DBServiceClient;

import java.util.Optional;

public class GetAllClientsRequestHandler implements RequestHandler<Clients> {

    private final DBServiceClient dbServiceClient;

    public GetAllClientsRequestHandler(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        Clients data = new Clients(dbServiceClient.findAll());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
    }
}
