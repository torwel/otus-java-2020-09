package ru.otus.torwel.processor.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.torwel.processor.Processor;

import static org.junit.jupiter.api.Assertions.*;

class ProcessorEvenSecondsPhobiaTest {

    @Test
    @DisplayName("Тестируем выполнение процессора в четную секунду")
    void processAtEvenSecond() {
        Processor processor = new ProcessorEvenSecondsPhobia(() -> 30);
        assertThrows(IllegalStateException.class, () -> processor.process(null));
    }

    @Test
    @DisplayName("Тестируем выполнение процессора в нечетную секунду")
    void processAtOddSecond() {
        Processor processor = new ProcessorEvenSecondsPhobia(() -> 29);
        assertDoesNotThrow(() -> processor.process(null));
    }

}