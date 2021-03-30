package ru.otus.torwel.h16.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.torwel.h16.helpers.HibernateConfigurationHelper;

import java.util.Arrays;

public class HibernateUtils {

    private HibernateUtils() {}

    public static SessionFactory createSessionFactory(Configuration config, Class<?>... annotatedClasses) {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(config.getProperties()).build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        Arrays.stream(annotatedClasses).forEach(metadataSources::addAnnotatedClass);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    public static SessionFactory createSessionFactory(Class<?>... annotatedClasses) {
        return createSessionFactory(getDefaultConfiguration(), annotatedClasses);
    }

    private static Configuration getDefaultConfiguration() {
        return new HibernateConfigurationHelper().getConfiguration();
    }

}
