package ru.otus.torwel.h16.helpers;

import org.hibernate.cfg.Configuration;

public class HibernateConfigurationHelper {

    private final static String DEFAULT_HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private final Configuration configuration;

    public HibernateConfigurationHelper() {
        this(DEFAULT_HIBERNATE_CFG_FILE);
    }

    public HibernateConfigurationHelper(String configFileName) {
        configuration = new Configuration().configure(configFileName);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public String getUrl() {
        return configuration.getProperty("hibernate.connection.url");
    }

    public String getUsername() {
        return configuration.getProperty("hibernate.connection.username");
    }

    public String getPassword() {
        return configuration.getProperty("hibernate.connection.password");
    }


}
