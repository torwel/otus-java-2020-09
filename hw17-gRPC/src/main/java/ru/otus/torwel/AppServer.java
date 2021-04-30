package ru.otus.torwel;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AppServer {

    private static final Logger logger = LoggerFactory.getLogger(AppServer.class);

    public static final int SERVER_PORT = 8090;

    public static void main(String[] args) throws IOException, InterruptedException {
        RemoteSequenceServiceImpl remoteSequenceService = new RemoteSequenceServiceImpl();
        Server server = ServerBuilder.forPort(SERVER_PORT).addService(remoteSequenceService).build();
        server.start();
        logger.info("Waiting for connections...");
        server.awaitTermination();
    }
}
