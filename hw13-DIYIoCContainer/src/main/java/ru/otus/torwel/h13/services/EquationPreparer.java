package ru.otus.torwel.h13.services;

import ru.otus.torwel.h13.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
