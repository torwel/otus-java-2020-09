package ru.otus.torwel.core.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import ru.otus.torwel.core.base.TestContainersConfig;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.model.AddressDataSet;
import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.model.PhoneDataSet;
import ru.otus.torwel.flyway.MigrationsExecutorFlyway;
import ru.otus.torwel.hibernate.HibernateUtils;
import ru.otus.torwel.hibernate.dao.ClientDaoHibernate;
import ru.otus.torwel.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.torwel.hibernate.Demo.HIBERNATE_CFG_FILE;

@DisplayName("Демо работы с hibernate:")
class DbServiceClientImplTest {

    DBServiceClient dbServiceClient;
    protected SessionFactory sessionFactory;
    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;


    @BeforeAll
    static void beforeAll() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    static void afterAll() {
        CONTAINER.stop();
    }

    @BeforeEach
    void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.cleanDb();
        migrationsExecutor.executeMigrations();

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = HibernateUtils.createSessionFactory(configuration,
                Client.class, AddressDataSet.class, PhoneDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao clientDao = new ClientDaoHibernate(sessionManager);
        dbServiceClient = new DbServiceClientImpl(clientDao);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    @Test
    @DisplayName("сохрание клиента")
    void shouldCorrectSaveClient() {
        Client savedClient = buildDefaultClient();
        dbServiceClient.saveClient(savedClient);
        Optional<Client> optClient = dbServiceClient.getClient(savedClient.getId());

        Client loadClient = optClient.orElse(null);
        assertThat(loadClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient);

        System.out.println(savedClient);
        System.out.println(loadClient);
    }

    @Test
    @DisplayName("загрузка клиента")
    void shouldLoadCorrectClient() {
        Client savedClient = buildDefaultClient();
        dbServiceClient.saveClient(savedClient);


        Optional<Client> mayBeClient = dbServiceClient.getClient(savedClient.getId());

        assertThat(mayBeClient).isPresent().get().usingRecursiveComparison().isEqualTo(savedClient);

        System.out.println(savedClient);
        mayBeClient.ifPresent(System.out::println);
    }

    @Test
    @DisplayName("изменение ранее сохраненного клиента")
    void shouldCorrectUpdateSavedClient() {
        Client savedClient = buildDefaultClient();
        dbServiceClient.saveClient(savedClient);

        Client savedClient2 = new Client(savedClient.getId(), "Joe", new ArrayList<>());
        savedClient.getPhones().forEach(savedClient2::addPhone);
        long id = dbServiceClient.saveClient(savedClient2);
        Optional<Client> optClient = dbServiceClient.getClient(id);

        Client loadClient = optClient.orElse(null);
        assertThat(loadClient).isNotNull().usingRecursiveComparison().isEqualTo(savedClient2);

        System.out.println(savedClient);
        System.out.println(savedClient2);
        System.out.println(loadClient);
    }

    private Client buildDefaultClient() {
        Client client = new Client(0,"Elusive Joe", new ArrayList<>());
        client.setAddressDataSet(new AddressDataSet(0, "Main street"));
        client.addPhone(new PhoneDataSet(0, "88005553535"));
        client.addPhone(new PhoneDataSet(0, "222-33-44"));
        return client;
    }

}