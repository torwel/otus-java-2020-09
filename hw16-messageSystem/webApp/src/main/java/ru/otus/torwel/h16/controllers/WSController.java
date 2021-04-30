package ru.otus.torwel.h16.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.torwel.h16.core.model.Client;
import ru.otus.torwel.h16.core.service.FrontendService;

@Controller
public class WSController {
    private static final Logger logger = LoggerFactory.getLogger(WSController.class);

    private final FrontendService frontendService;
    private final SimpMessagingTemplate template;

    public WSController(FrontendService frontendService, SimpMessagingTemplate template) {
        this.frontendService = frontendService;
        this.template = template;
    }

    @MessageMapping("/client/list")
    public void getClients() {
        frontendService.getAllClients(clients -> template.convertAndSend("/topic/client/list", clients));
    }

    @MessageMapping("/client/new")
    public void addClient(Client client) {
        frontendService.saveClient(client, clients -> template.convertAndSend("/topic/client/list", clients));
    }
}
