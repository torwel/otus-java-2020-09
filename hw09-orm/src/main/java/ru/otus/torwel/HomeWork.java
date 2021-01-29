package ru.otus.torwel;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.dao.Dao;
import ru.otus.torwel.core.model.Account;
import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.service.DbServiceClientImpl;
import ru.otus.torwel.core.service.DbServiceImpl;
import ru.otus.torwel.jdbc.DbExecutorImpl;
import ru.otus.torwel.jdbc.dao.ClientDaoJdbc;
import ru.otus.torwel.jdbc.dao.JdbcDaoImpl;
import ru.otus.torwel.jdbc.mapper.JdbcMapperImpl;
import ru.otus.torwel.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceImpl();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
        JdbcMapperImpl<Client> jdbcMapperClient = new JdbcMapperImpl<>(Client.class, dbExecutor, sessionManager); //
        ClientDao clientDao = new ClientDaoJdbc(sessionManager, jdbcMapperClient); // = new UserDaoJdbcMapper(sessionManager, dbExecutor);


// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var id = dbServiceClient.saveClient(new Client(0, "dbServiceClient", 35));
        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        clientOptional.ifPresentOrElse(
                client -> logger.info("created client, name:{}", client.getName()),
                () -> logger.info("client was not created")
        );

        Client clientForUpdate = new Client(id, "newManClient", 20);
        dbServiceClient.insertOrUpdate(clientForUpdate);

// Работа со счетом

        DbExecutorImpl<Account> dbExecutorAccount = new DbExecutorImpl<>();
        JdbcMapperImpl<Account> jdbcMapperAccount = new JdbcMapperImpl<>(Account.class, dbExecutorAccount, sessionManager);
        Dao<Account> accountDao = new JdbcDaoImpl<>(sessionManager, jdbcMapperAccount, Account.class);
        var dbServiceAccount = new DbServiceImpl<>(accountDao);
        Account account = new Account("1111222233334444", "VISA", 10.8);
        long idAccount = dbServiceAccount.saveObject(account);
        logger.info("account created, id: {}", idAccount);
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
