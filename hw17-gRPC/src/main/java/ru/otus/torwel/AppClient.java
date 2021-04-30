package ru.otus.torwel;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.protobuf.generated.BoundsMessage;
import ru.otus.torwel.protobuf.generated.RemoteSequenceServiceGrpc;
import ru.otus.torwel.protobuf.generated.ValueMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AppClient {

    private static final Logger logger = LoggerFactory.getLogger(AppClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8090;


    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        logger.info("Channel created...");

        CountDownLatch latch = new CountDownLatch(1);
        RemoteSequenceServiceGrpc.RemoteSequenceServiceStub stub = RemoteSequenceServiceGrpc.newStub(channel);

        int firstV = 0;
        int lastV = 30;
        BoundsMessage bm = BoundsMessage.newBuilder().setFirstNumber(firstV).setLastNumber(lastV).build();
        logger.info("Bounds created...");

        AtomicInteger newValue = new AtomicInteger();
        int currentValue = 0;

        stub.getSequence(bm, new StreamObserver<ValueMessage>() {
            @Override
            public void onNext(ValueMessage value) {
                newValue.set(value.getValue());
                logger.info("new value: {}", newValue.get());
            }

            @Override
            public void onError(Throwable t) {
                logger.error(t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                logger.info("Server completed work");
                latch.countDown();
            }
        });

        for (int i = 0; i < 50; i++) {
            currentValue = currentValue + newValue.get() + 1;
            newValue.set(0);
            logger.info("currentValue: {}", currentValue);
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        latch.await();
        channel.shutdown();
        logger.info("Client completed work");
    }
}
