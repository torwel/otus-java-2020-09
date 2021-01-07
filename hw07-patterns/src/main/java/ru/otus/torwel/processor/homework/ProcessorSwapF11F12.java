package ru.otus.torwel.processor.homework;

import ru.otus.torwel.model.Message;
import ru.otus.torwel.processor.Processor;

public class ProcessorSwapF11F12 implements Processor {
    @Override
    public Message process(Message message) {
        return message.toBuilder().field11(message.getField12()).field12(message.getField11()).build();
    }
}
