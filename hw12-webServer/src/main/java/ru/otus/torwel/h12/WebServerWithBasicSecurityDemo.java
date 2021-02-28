package ru.otus.torwel.h12;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.LoginService;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.otus.torwel.h12.core.dao.UserDao;
import ru.otus.torwel.h12.core.model.User;
import ru.otus.torwel.h12.core.service.DBServiceUser;
import ru.otus.torwel.h12.core.service.DbServiceUserImpl;
import ru.otus.torwel.h12.hibernate.dao.UserDaoHibernate;
import ru.otus.torwel.h12.server.UsersWebServer;
import ru.otus.torwel.h12.server.UsersWebServerWithBasicSecurity;
import ru.otus.torwel.h12.flyway.MigrationsExecutorFlyway;
import ru.otus.torwel.h12.services.HibernateLoginServiceImpl;
import ru.otus.torwel.h12.services.TemplateProcessor;
import ru.otus.torwel.h12.services.TemplateProcessorImpl;
import ru.otus.torwel.h12.hibernate.HibernateUtils;
import ru.otus.torwel.h12.hibernate.sessionmanager.SessionManagerHibernate;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithBasicSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String REALM_NAME = "AnyRealm";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        Configuration config = new Configuration().configure(HIBERNATE_CFG_FILE);
        flywayExecute(config);

        SessionFactory sessionFactory = HibernateUtils.createSessionFactory(config, User.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        LoginService loginService = new HibernateLoginServiceImpl(REALM_NAME, dbServiceUser);

        UsersWebServer usersWebServer = new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT,
                loginService, dbServiceUser, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void flywayExecute(Configuration config) {
        String dbUrl = config.getProperty("hibernate.connection.url");
        String dbUserName = config.getProperty("hibernate.connection.username");
        String dbPassword = config.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }

}
