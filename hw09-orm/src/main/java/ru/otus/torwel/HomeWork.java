package ru.otus.torwel;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.ClientDao;
import ru.otus.torwel.core.model.Account;
import ru.otus.torwel.core.model.Client;
import ru.otus.torwel.core.service.DbServiceClientImpl;
import ru.otus.torwel.jdbc.DbExecutorImpl;
import ru.otus.torwel.jdbc.dao.ClientDaoJdbcMapper;
import ru.otus.torwel.jdbc.mapper.JdbcMapper;
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
        JdbcMapper<Client> jdbcMapperClient = new JdbcMapperImpl<>(Client.class); //
        ClientDao<Client> clientDao = new ClientDaoJdbcMapper<>(sessionManager, dbExecutor, jdbcMapperClient); // = new UserDaoJdbcMapper(sessionManager, dbExecutor);


// Код дальше должен остаться, т.е. clientDao должен использоваться
        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var id = dbServiceClient.saveClient(new Client(0, "dbServiceClient", 35));
        Optional<Client> clientOptional = dbServiceClient.getClient(id);

        clientOptional.ifPresentOrElse(
                client -> logger.info("created client, name:{}", client.getName()),
                () -> logger.info("client was not created")
        );
// Работа со счетом


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
