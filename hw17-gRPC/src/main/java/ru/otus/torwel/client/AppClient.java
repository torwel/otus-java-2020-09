package ru.otus.torwel.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.protobuf.generated.BoundsMessage;
import ru.otus.torwel.protobuf.generated.RemoteSequenceServiceGrpc;

import java.util.concurrent.TimeUnit;

public class AppClient {

    private static final Logger logger = LoggerFactory.getLogger(AppClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8090;


    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        logger.info("Channel created...");

        RemoteSequenceServiceGrpc.RemoteSequenceServiceStub stub = RemoteSequenceServiceGrpc.newStub(channel);

        int firstV = 0;
        int lastV = 30;
        BoundsMessage bm = BoundsMessage.newBuilder().setFirstNumber(firstV).setLastNumber(lastV).build();
        logger.info("Bounds created...");

        int currentValue = 0;

        ClientStreamObserver responseObserver = new ClientStreamObserver();
        stub.getSequence(bm, responseObserver);

        for (int i = 0; i < 50; i++) {
            currentValue = currentValue + responseObserver.getLastValueAndReset() + 1;
            logger.info("currentValue: {}", currentValue);
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        channel.shutdown();
        logger.info("Client completed work");
    }
}
