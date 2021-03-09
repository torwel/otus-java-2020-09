package ru.otus.torwel.h13.config;

import ru.otus.torwel.h13.appcontainer.api.AppComponent;
import ru.otus.torwel.h13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.torwel.h13.services.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig2 {

    @AppComponent(order = 0, name = "gameProcessor")
    public GameProcessor gameProcessor(IOService ioService,
                                       PlayerService playerService,
                                       EquationPreparer equationPreparer) {
        return new GameProcessorImpl(ioService, equationPreparer, playerService);
    }
}
