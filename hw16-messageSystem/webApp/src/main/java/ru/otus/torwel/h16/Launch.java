package ru.otus.torwel.h16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class, ThymeleafAutoConfiguration.class})
public class Launch {

    public static void main(String[] args) {
        try {
            SpringApplication.run(Launch.class, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
