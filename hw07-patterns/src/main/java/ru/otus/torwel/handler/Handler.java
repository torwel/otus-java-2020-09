package ru.otus.torwel.handler;

import ru.otus.torwel.model.Message;
import ru.otus.torwel.listener.Listener;

public interface Handler {
    Message handle(Message msg);

    void addListener(Listener listener);
    void removeListener(Listener listener);
}
