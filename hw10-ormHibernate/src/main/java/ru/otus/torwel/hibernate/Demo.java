package ru.otus.torwel.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.model.AddressDataSet;
import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.model.PhoneDataSet;
import ru.otus.torwel.core.service.DBServiceClient;
import ru.otus.torwel.core.service.DbServiceClientImpl;
import ru.otus.torwel.flyway.MigrationsExecutorFlyway;
import ru.otus.torwel.hibernate.dao.ClientDaoHibernate;
import ru.otus.torwel.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.Optional;


public class Demo {
    private static final Logger logger = LoggerFactory.getLogger(Demo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        Configuration config = new Configuration().configure(HIBERNATE_CFG_FILE);
        flywayExecute(config);
        SessionFactory sessionFactory = HibernateUtils.createSessionFactory(config,
                Client.class, AddressDataSet.class, PhoneDataSet.class);

        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(clientDao);

        Client newClient = new Client(0, "Jack Black", new ArrayList<>());
        newClient.setAddressDataSet(new AddressDataSet(0, "Long street"));
        newClient.addPhone(new PhoneDataSet(0, "5550555", newClient));
        newClient.addPhone(new PhoneDataSet(0, "5550666", newClient));
        newClient.addPhone(new PhoneDataSet(0, "5550777", newClient));


        logger.info(" *** Object client, name: {}", newClient);

        long id = dbServiceClient.saveClient(newClient);
        logger.info(" *** Saved client, name: {}", newClient);

        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        clientOptional.ifPresentOrElse(
                client -> {
                    logger.info("created client, name: {}", client.getName());
                    logger.info("Clients equals: " + newClient.equals(client));
                },
                () -> logger.info("client was not created")
        );
    }

    private static void flywayExecute(Configuration config) {
        String dbUrl = config.getProperty("hibernate.connection.url");
        String dbUserName = config.getProperty("hibernate.connection.username");
        String dbPassword = config.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }
}
