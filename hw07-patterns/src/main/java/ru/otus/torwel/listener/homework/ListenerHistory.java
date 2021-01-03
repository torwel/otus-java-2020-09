package ru.otus.torwel.listener.homework;

import ru.otus.torwel.listener.Listener;
import ru.otus.torwel.model.Message;
import ru.otus.torwel.model.ObjectForMessage;

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

    /**
     * Метод добавляет переданный объект Message в историю объектов.
     * Поля типа String у переданного объекта можно не менять.
     * Для поля типа ObjectForMessages должна быть создана полная копия,
     * чтобы избежать его неконтролируемых изменений.
     *
     * @param msg добавляется в историю изменений объекта
     */
    private void addMessage(Message msg) {
        ObjectForMessage newOFM = new ObjectForMessage();
        ArrayList<String> newData = new ArrayList<>(msg.getField13().getData());
        newOFM.setData(newData);
        var builder = msg.toBuilder();
        builder.field13(newOFM);
        history.add(builder.build());
    }

    public void printHistory() {
        history.forEach(System.out::println);
    }
}
