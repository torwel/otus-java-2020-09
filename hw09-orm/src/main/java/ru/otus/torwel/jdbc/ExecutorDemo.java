package ru.otus.torwel.jdbc;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.DataSourceImpl;
import ru.otus.torwel.core.model.Client;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author sergey
 * created on 03.02.19.
 */
// этот класс не должен быть в домашней работе
public class ExecutorDemo {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorDemo.class);

    public static void main(String[] args) throws SQLException {
        var dataSource = new DataSourceImpl();
        flywayMigrations(dataSource);

        try (Connection connection = getConnection(dataSource)) {
            DbExecutorImpl<Client> executor = new DbExecutorImpl<>();
            long clientId = executor.executeInsert(connection, "insert into client(name, age) values (?, ?)",
                    Arrays.asList("testUserName", 30));
            logger.info("created client:{}", clientId);
            connection.commit();

            Optional<Client> client = executor.executeSelect(connection, "select id, name, age from client where id  = ?",
                    clientId, rs -> {
                        try {
                            if (rs.next()) {
                                return new Client(rs.getLong("id"), rs.getString("name"), rs.getInt("age"));
                            }
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                        }
                        return null;
                    });
            logger.info("client:{}", client);
        }
    }

    private static Connection getConnection(DataSource dataSource) throws SQLException {
        var connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
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
