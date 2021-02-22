package ru.otus.torwel.h12.servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.torwel.h12.core.model.User;
import ru.otus.torwel.h12.core.service.DBServiceUser;
import ru.otus.torwel.h12.core.service.DbServiceException;

import java.io.IOException;
import java.util.List;


public class UsersApiServlet extends HttpServlet {

    public static final String PARAM_NAME = "name";
    public static final String PARAM_LOGIN = "login";
    public static final String PARAM_PASSWORD = "password";
    public static final String PARAM_IS_ADMIN = "isAdmin";
    private static final int ID_PATH_PARAM_POSITION = 1;

    private final DBServiceUser dbServiceUser;
    private final Gson gson;

    public UsersApiServlet(DBServiceUser dbServiceUser, Gson gson) {
        this.gson = gson;
        this.dbServiceUser = dbServiceUser;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long id = extractIdFromRequest(request);
        if (id > 0 ) {
            User user = dbServiceUser.getUser(id).orElse(null);
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(user));
        }
        else if (id == -1 ) {
            List<User> users = dbServiceUser.findAll();
            response.setContentType("application/json;charset=UTF-8");
            ServletOutputStream out = response.getOutputStream();
            out.print(gson.toJson(users));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter(PARAM_NAME);
        String login = request.getParameter(PARAM_LOGIN);
        String password = request.getParameter(PARAM_PASSWORD);
        boolean isAdmin = request.getParameter(PARAM_IS_ADMIN).equals("true");
        User user = new User(0L, name, login, password, isAdmin);

        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        String msg = null;
        try {
            dbServiceUser.saveUser(user);
            msg = "{\"id\": " + user.getId() + "}";
        } catch (DbServiceException e) {
            msg = "{\"Error\": \"Пользователь не создан\", "
                    + "\"Message\": \"" + e.getMessage() + "\"}";
        }
        out.print(msg);
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

}
