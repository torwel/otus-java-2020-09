package ru.otus.torwel.h11;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.h11.cachehw.MyCache;
import ru.otus.torwel.h11.core.dao.ClientDao;
import ru.otus.torwel.h11.core.model.AddressDataSet;
import ru.otus.torwel.h11.core.model.Client;
import ru.otus.torwel.h11.core.model.PhoneDataSet;
import ru.otus.torwel.h11.core.service.DbServiceClientImpl;
import ru.otus.torwel.h11.flyway.MigrationsExecutorFlyway;
import ru.otus.torwel.h11.hibernate.HibernateUtils;
import ru.otus.torwel.h11.hibernate.dao.ClientDaoHibernate;
import ru.otus.torwel.h11.hibernate.sessionmanager.SessionManagerHibernate;

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
        MyCache<String, Client> cache = new MyCache<>();
        DbServiceClientImpl dbServiceClient = new DbServiceClientImpl(clientDao, cache);

        testCacheClearingByInserting(dbServiceClient, cache);
        testCacheClearingBySelecting(dbServiceClient, cache);

//        Для сравнения с работой без кэша раскоментировать
//        dbServiceClient.setCache(null);
        testClientLoadingWithCache(dbServiceClient);
   }

    private static void testCacheClearingByInserting(DbServiceClientImpl dbServiceClient,
                                                     MyCache<String, Client> cache) {
        long firstCachedClientId = -1;
        String firstCachedClientIdStr = "";
        for (int i = 1; i <= 150; i++) {
            Client newClient = createDefaultClient("Jack Black " + i);
            dbServiceClient.saveClient(newClient);
            logger.info(" *** Saved client, name: {}", newClient.getName());

            // Если кэш будет сброшен, то цикл остановится раньше
            // VM Options: -Xmx15m
            if (firstCachedClientId < 0) {
                firstCachedClientId = newClient.getId();
                firstCachedClientIdStr = String.valueOf(firstCachedClientId);
            }
            else {
                if (cache.get(firstCachedClientIdStr) == null) {
                    logger.info("cache cleared");
                    break;
                }
            }
        }
    }

    // Необходимо достаточное количество записей в БД. Ориентировочно 80.
    private static void testCacheClearingBySelecting(DbServiceClientImpl dbServiceClient,
                                                     MyCache<String, Client> cache) {
        final long[] firstCachedClientId = {-1};
        final String[] firstCachedClientIdStr = {""};
        for (int i = 1; i <= 150; i++) {
            Optional<Client> clientOptional = dbServiceClient.getClient(i);
            clientOptional.ifPresentOrElse(
                    client -> logger.info(" *** Loaded client, id: {}, name: {}", client.getId(), client.getName()),
                    () -> logger.info("client was not loaded")
            );

            // Если кэш будет сброшен, то цикл остановится раньше
            // VM Options: -Xmx15m
            if (firstCachedClientId[0] < 0) {
                clientOptional.ifPresent(
                        (client) -> {
                            firstCachedClientId[0] = client.getId();
                            firstCachedClientIdStr[0] = String.valueOf(firstCachedClientId[0]);
                        }
                );
            }
            else {
                if (cache.get(firstCachedClientIdStr[0]) == null) {
                    logger.info("cache cleared");
                    break;
                }
            }
        }
    }

        private static void testClientLoadingWithCache(DbServiceClientImpl dbServiceClient) {
        Client newClient = createDefaultClient("Billy Jones");
        logger.info(" *** object client, name: {}", newClient);

        long id = dbServiceClient.saveClient(newClient);
        logger.info(" *** saved client, name: {}", newClient);

        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        clientOptional.ifPresentOrElse(
                client -> {
                    logger.info(" *** loaded client, name: {}", client.getName());
                    logger.info("Clients equals: " + newClient.equals(client));
                },
                () -> logger.info("client was not created")
        );
    }

    private static Client createDefaultClient(String name) {
        Client newClient = new Client(0, name, new ArrayList<>());
        newClient.setAddressDataSet(new AddressDataSet(0, "Long street" ));
        newClient.addPhone(new PhoneDataSet(0, "55501", newClient));
        newClient.addPhone(new PhoneDataSet(0, "55502", newClient));
        newClient.addPhone(new PhoneDataSet(0, "55503", newClient));
        return newClient;
    }


    private static void flywayExecute(Configuration config) {
        String dbUrl = config.getProperty("hibernate.connection.url");
        String dbUserName = config.getProperty("hibernate.connection.username");
        String dbPassword = config.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }
}
