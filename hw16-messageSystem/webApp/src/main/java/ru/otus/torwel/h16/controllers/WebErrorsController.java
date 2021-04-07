package ru.otus.torwel.h16.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebErrorsController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "errorView";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
