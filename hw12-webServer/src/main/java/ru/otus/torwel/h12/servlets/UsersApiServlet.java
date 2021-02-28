package ru.otus.torwel.h12.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
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
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        if (id > 0 ) {
            User user = dbServiceUser.getUser(id).orElse(null);
            out.print(gson.toJson(user));
        }
        else if (id == -1 ) {
            List<User> users = dbServiceUser.findAll();
            out.print(gson.toJson(users));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        JsonMessage msg;
        try{
            User user = gson.fromJson(request.getReader(), User.class);
            dbServiceUser.saveUser(user);
            msg = new JsonMessage("success", "id: " + user.getId());
        }
        catch (JsonParseException e){
            response.setStatus(200);
            msg = new JsonMessage("Error", "Ошибка парсинга данных пользователя.");
        } catch (DbServiceException e) {
            response.setStatus(200);
            msg = new JsonMessage("Error", "Пользователь не создан - " + e.getMessage());
        }
        out.print(gson.toJson(msg));
    }

    private long extractIdFromRequest(HttpServletRequest request) {
        String[] path = request.getPathInfo().split("/");
        String id = (path.length > 1)? path[ID_PATH_PARAM_POSITION]: String.valueOf(- 1);
        return Long.parseLong(id);
    }

    private class JsonMessage {
        private final String status;
        private final String message;

        public JsonMessage(String status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
