package ru.otus.torwel;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.protobuf.generated.BoundsMessage;
import ru.otus.torwel.protobuf.generated.RemoteSequenceServiceGrpc;
import ru.otus.torwel.protobuf.generated.ValueMessage;

import java.util.concurrent.TimeUnit;

public class RemoteSequenceServiceImpl extends RemoteSequenceServiceGrpc.RemoteSequenceServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(RemoteSequenceServiceImpl.class);

    @Override
    public void getSequence(BoundsMessage request, StreamObserver<ValueMessage> responseObserver) {

        int firstIdx = request.getFirstNumber();
        int lastIdx = request.getLastNumber();
        int period = 2;

        logger.info("method getSequence() starts.");
        logger.info("Number range: [{}, {}]", firstIdx, lastIdx);
        logger.info("Sequence period: {} seconds", period);

        for (int idx = firstIdx + 1; idx <= lastIdx; idx++) {
            responseObserver.onNext(ValueMessage.newBuilder().setValue(idx).build());

            try {
                TimeUnit.SECONDS.sleep(period);
            } catch (InterruptedException e) {
                logger.info(e.getMessage(), e);
            }
        }
        responseObserver.onCompleted();
    }
}
