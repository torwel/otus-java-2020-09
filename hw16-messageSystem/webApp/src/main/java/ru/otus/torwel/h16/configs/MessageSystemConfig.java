package ru.otus.torwel.h16.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.torwel.h16.core.service.DBServiceClient;
import ru.otus.torwel.h16.core.service.FrontendService;
import ru.otus.torwel.h16.core.service.FrontendServiceImpl;
import ru.otus.torwel.h16.handlers.GetAllClientsRequestHandler;
import ru.otus.torwel.h16.handlers.GetAllClientsResponseHandler;
import ru.otus.torwel.h16.handlers.SaveClientRequestHandler;

@Configuration
public class MessageSystemConfig {

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";


    @Bean
    public MessageSystem messageSystem() {
        return new MessageSystemImpl();
    }

    @Bean
    public CallbackRegistry callbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean
    public MsClient databaseMsClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry, DBServiceClient dbServiceClient) {
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.ALL_CLIENTS, new GetAllClientsRequestHandler(dbServiceClient));
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_CLIENT, new SaveClientRequestHandler(dbServiceClient));
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean
    public MsClient frontendMsClient(MessageSystem messageSystem, CallbackRegistry callbackRegistry, DBServiceClient dbServiceClient) {
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        GetAllClientsResponseHandler handler = new GetAllClientsResponseHandler(callbackRegistry);
        requestHandlerFrontendStore.addHandler(MessageType.ALL_CLIENTS, handler);
        requestHandlerFrontendStore.addHandler(MessageType.SAVE_CLIENT, handler);
        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }

    @Bean
    public FrontendService frontendService(MsClient frontendMsClient) {
        return new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
    }
}
