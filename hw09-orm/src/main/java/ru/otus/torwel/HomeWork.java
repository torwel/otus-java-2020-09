package ru.otus.torwel;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.AccountDao;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.model.Account;
import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.service.DbServiceAccountImpl;
import ru.otus.torwel.core.service.DbServiceClientImpl;
import ru.otus.torwel.jdbc.DbExecutor;
import ru.otus.torwel.jdbc.DbExecutorImpl;
import ru.otus.torwel.jdbc.dao.ClientDaoJdbc;
import ru.otus.torwel.jdbc.dao.AccountDaoJdbc;
import ru.otus.torwel.jdbc.mapper.*;
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
        DbExecutor<Client> dbExecutor = new DbExecutorImpl<>();
        var entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        JdbcMapper<Client> jdbcMapperClient = new JdbcMapperImpl<>(
                dbExecutor,
                sessionManager,
                entityClassMetaDataClient,
                new EntitySQLMetaDataImpl(entityClassMetaDataClient),
                new JdbcMapperReflectionHelper<>(entityClassMetaDataClient)); //
        ClientDao clientDao = new ClientDaoJdbc(sessionManager, jdbcMapperClient); // = new UserDaoJdbcMapper(sessionManager, dbExecutor);


// Код дальше должен остаться, т.е. clientDao должен использоваться
        logger.info("*** Working with Client ***");
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var id = dbServiceClient.saveClient(new Client(0, "dbServiceClient", 35));
        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        clientOptional.ifPresentOrElse(
                client -> logger.info("created client, name: {}", client.getName()),
                () -> logger.info("client was not created")
        );

        Client clientForUpdate = new Client(id, "newManClient", 20);
        dbServiceClient.insertOrUpdate(clientForUpdate);

// Работа со счетом
        logger.info("*** Working with Account ***");
        DbExecutor<Account> dbExecutorAccount = new DbExecutorImpl<>();
        var entityClassMetaDataAccount = new EntityClassMetaDataImpl<>(Account.class);
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(
                dbExecutorAccount,
                sessionManager,
                entityClassMetaDataAccount,
                new EntitySQLMetaDataImpl(entityClassMetaDataAccount),
                new JdbcMapperReflectionHelper<>(entityClassMetaDataAccount));
        AccountDao accountDao = new AccountDaoJdbc(sessionManager, jdbcMapperAccount);
        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        String noAccount = dbServiceAccount.saveAccount(new Account("","VISA", 10.8));
        Optional<Account> accountOptional = dbServiceAccount.getAccount(noAccount);

        accountOptional.ifPresentOrElse(
                account -> logger.info("created account, number: {}", account.getNo()),
                () -> logger.info("account was not created")
        );
        Account accountForUpdate = new Account(noAccount, "MASTER CARD", 200.9);
        dbServiceAccount.insertOrUpdate(accountForUpdate);
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
