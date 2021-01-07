package ru.otus.torwel.listener.homework;

import ru.otus.torwel.listener.Listener;
import ru.otus.torwel.model.Message;

import java.util.ArrayList;
import java.util.List;

public class ListenerHistory implements Listener {
    
    private final List<Message> history;

    public ListenerHistory() {
        history = new ArrayList<>();
    }

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        if (history.size() == 0) {
            addMessage(oldMsg);
        }
        addMessage(newMsg);
    }

    private void addMessage(Message msg) {
        history.add( (Message) msg.clone() );
    }

    public void printHistory() {
        history.forEach(System.out::println);
    }
}
