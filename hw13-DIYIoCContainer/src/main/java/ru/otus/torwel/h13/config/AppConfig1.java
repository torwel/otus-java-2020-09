package ru.otus.torwel.h13.config;

import ru.otus.torwel.h13.appcontainer.api.AppComponent;
import ru.otus.torwel.h13.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.torwel.h13.services.*;

@AppComponentsContainerConfig(order = 0)
public class AppConfig1 {
    @AppComponent(order = 0, name = "equationPreparer")
    public EquationPreparer equationPreparer(){
        return new EquationPreparerImpl();
    }

    @AppComponent(order = 1, name = "playerService")
    public PlayerService playerService(IOService ioService) {
        return new PlayerServiceImpl(ioService);
    }

    @AppComponent(order = 0, name = "ioService")
    public IOService ioService() {
        return new IOServiceConsole(System.out, System.in);
    }
}
