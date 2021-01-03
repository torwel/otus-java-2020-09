package ru.otus.torwel.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.torwel.processor.Processor;


import static org.junit.jupiter.api.Assertions.*;

class ProcessorEvenSecondsPhobiaTest {

    @Test
    @DisplayName("Тестируем процессор с исключениями в четную секунду")
    void process() throws InterruptedException {
        Processor processor = new ProcessorEvenSecondsPhobia();
        int step = 10;
        for (int time = 0; time < 2100; time += step) {
            try {
                assertNull(processor.process(null));
            } catch (IllegalStateException e) {
                assertTrue(e.getMessage().startsWith("Method run at even second: "));
            }
            Thread.sleep(step);
        }
    }
}