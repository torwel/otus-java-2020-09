package ru.otus.torwel.processor.homework;

import ru.otus.torwel.model.Message;
import ru.otus.torwel.processor.Processor;

import java.time.LocalDateTime;

public class ProcessorEvenSecondsPhobia implements Processor {

    @Override
    public Message process(Message message) {

        var dateTime = LocalDateTime.now();

        if (dateTime.getSecond() % 2 == 0) {
            throw new IllegalStateException("Method run at even second: " + dateTime.toString());
        }

        return message;
    }
}
