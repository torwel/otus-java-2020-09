package ru.otus.torwel.h14;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.torwel.h14.core.dao.ClientDao;
import ru.otus.torwel.h14.core.model.AddressDataSet;
import ru.otus.torwel.h14.core.model.Client;
import ru.otus.torwel.h14.core.model.PhoneDataSet;
import ru.otus.torwel.h14.core.service.DBServiceClient;
import ru.otus.torwel.h14.core.service.DbServiceClientImpl;
import ru.otus.torwel.h14.flyway.MigrationsExecutorFlyway;
import ru.otus.torwel.h14.hibernate.HibernateUtils;
import ru.otus.torwel.h14.hibernate.dao.ClientDaoHibernate;
import ru.otus.torwel.h14.hibernate.sessionmanager.SessionManagerHibernate;

@Configuration
public class AppConfig {

    @Bean
    public MigrationsExecutorFlyway migrationsExecutorFlyway() {
        MigrationsExecutorFlyway migrationsExecutorFlyway = MigrationsExecutorFlyway.buildDefaultMigrationsExecutorFlyway();
        migrationsExecutorFlyway.cleanDb();
        migrationsExecutorFlyway.executeMigrations();
        return migrationsExecutorFlyway;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return HibernateUtils.createSessionFactory(Client.class, AddressDataSet.class, PhoneDataSet.class);
    }

    @Bean
    public SessionManagerHibernate sessionManagerHibernate(SessionFactory sessionFactory) {
        return new SessionManagerHibernate(sessionFactory);
    }

    @Bean
    public ClientDao clientDao(SessionManagerHibernate sessionManagerHibernate) {
        return new ClientDaoHibernate(sessionManagerHibernate);
    }

    @Bean
    public DBServiceClient dbServiceClient(ClientDao clientDao) {
        return new DbServiceClientImpl(clientDao);
    }


}
