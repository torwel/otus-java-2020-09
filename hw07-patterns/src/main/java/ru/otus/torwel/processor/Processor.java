package ru.otus.torwel.processor;

import ru.otus.torwel.model.Message;

public interface Processor {

    Message process(Message message);

    //todo: 2. Сделать процессор, который поменяет местами значения field11 и field12
}
