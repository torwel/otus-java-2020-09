package ru.otus.torwel.processor.homework;

import java.time.LocalDateTime;

public interface TimeProvider {
    LocalDateTime getTime();
}
