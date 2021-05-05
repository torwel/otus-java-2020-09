package ru.otus.torwel.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.protobuf.generated.ValueMessage;

public class ClientStreamObserver implements StreamObserver<ValueMessage> {

    private static final Logger logger = LoggerFactory.getLogger(ClientStreamObserver.class);

    private int lastValue = 0;

    @Override
    public void onNext(ValueMessage value) {
        setLastValue(value.getValue());
        logger.info("new value: {}", lastValue);
    }

    @Override
    public void onError(Throwable t) {
        logger.error(t.getMessage(), t);
    }

    @Override
    public void onCompleted() {
        logger.info("Server completed work");
    }

    public synchronized void setLastValue(int value) {
        lastValue = value;
    }

    public synchronized int getLastValueAndReset() {
        int result = lastValue;
        lastValue = 0;
        return result;
    }
}
