package ru.otus.torwel.h16.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.torwel.h16.core.model.Client;
import ru.otus.torwel.h16.core.service.DBServiceClient;

import java.util.List;

@Controller
public class ClientController {

    private final DBServiceClient dbServiceClient;

    public ClientController(DBServiceClient dbServiceClient) {
        this.dbServiceClient = dbServiceClient;
    }

    @GetMapping({"/client/list-old"})
    public String clientsListView(Model model) {
        List<Client> clients = dbServiceClient.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client());
        return "clientCreate";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client client) {
        dbServiceClient.saveClient(client);
        return new RedirectView("/client/list-old", true);
    }

}
