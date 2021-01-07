package ru.otus.torwel.processor.homework;

import ru.otus.torwel.model.Message;
import ru.otus.torwel.processor.Processor;

public class ProcessorEvenSecondsPhobia implements Processor {

    private final TimeProvider timeProvider;

    public ProcessorEvenSecondsPhobia(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {

        var second = timeProvider.getTime().getSecond();

        if (second % 2 == 0) {
            throw new IllegalStateException("Method run at even second: " + second);
        }

        return message;
    }
}
