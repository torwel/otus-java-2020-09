package ru.otus.torwel.listener;

import ru.otus.torwel.model.Message;

public interface Listener {

    void onUpdated(Message oldMsg, Message newMsg);

    //todo: 4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
}
