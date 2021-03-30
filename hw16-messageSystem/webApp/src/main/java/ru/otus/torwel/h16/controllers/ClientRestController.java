package ru.otus.torwel.h16.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.torwel.h16.core.model.Client;
import ru.otus.torwel.h16.core.service.DBServiceClient;

@RestController
public class ClientRestController {

    private final DBServiceClient dbServiceClient;

    public ClientRestController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") long id) {
        return dbServiceClient.getClient(id).orElse(new Client());
    }

    @GetMapping("/api/client")
    public Client getClientByName(@RequestParam(name = "name") String name) {
        return dbServiceClient.getClient(name).orElse(new Client());
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        dbServiceClient.saveClient(client);
        return client;
    }
}
