package ru.otus.torwel;

import ru.otus.torwel.handler.ComplexProcessor;
import ru.otus.torwel.listener.ListenerPrinter;
import ru.otus.torwel.model.Message;
import ru.otus.torwel.processor.LoggerProcessor;
import ru.otus.torwel.processor.ProcessorConcatFields;
import ru.otus.torwel.processor.ProcessorUpperField10;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        var processors = List.of(new ProcessorConcatFields(),
                new LoggerProcessor(new ProcessorUpperField10()));

        var complexProcessor = new ComplexProcessor(processors, (ex) -> {});
        var listenerPrinter = new ListenerPrinter();
        complexProcessor.addListener(listenerPrinter);

        var message = new Message.Builder(1L)
                .field1("field1")
                .field2("field2")
                .field3("field3")
                .field6("field6")
                .field10("field10")
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
    }
}
