package ru.otus.torwel.h16.flyway;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.torwel.h16.helpers.HibernateConfigurationHelper;

@Component
public class MigrationsExecutorFlyway {
    private static final Logger logger = LoggerFactory.getLogger(MigrationsExecutorFlyway.class);

    private final Flyway flyway;

    public MigrationsExecutorFlyway(String dbUrl, String dbUserName, String dbPassword) {
        flyway = Flyway.configure()
                .dataSource(dbUrl, dbUserName, dbPassword)
                .locations("classpath:/db/migration")
                .load();
    }

    public static MigrationsExecutorFlyway buildDefaultMigrationsExecutorFlyway() {
        HibernateConfigurationHelper conf = new HibernateConfigurationHelper();
        String url = conf.getUrl();
        return new MigrationsExecutorFlyway(url,
                conf.getUsername(),
                conf.getPassword());
    }



    public void cleanDb() {
        flyway.clean();
    }

    public void executeMigrations() {
        logger.info("db migration started...");
        flyway.migrate();
        logger.info("db migration finished.");
    }
}
