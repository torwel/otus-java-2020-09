package ru.otus.torwel.h12.servlets;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.torwel.h12.services.TemplateProcessor;

import java.io.IOException;

public class UsersServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "users.html";

    private final TemplateProcessor templateProcessor;

    public UsersServlet(TemplateProcessor templateProcessor) {
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, null));
    }

}
