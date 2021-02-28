package ru.otus.torwel.h12.services;

import java.io.IOException;
import java.util.Map;

public interface TemplateProcessor {
    String getPage(String filename, Map<String, Object> data) throws IOException;
}
