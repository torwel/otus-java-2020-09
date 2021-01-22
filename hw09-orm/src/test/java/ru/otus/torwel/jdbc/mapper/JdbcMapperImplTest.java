package ru.otus.torwel.jdbc.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.torwel.core.model.Client;

import static org.junit.jupiter.api.Assertions.*;

class JdbcMapperImplTest {

    JdbcMapperImpl<Client> mapper;

    @BeforeEach
    void setUp() {
        mapper = new JdbcMapperImpl<>(Client.class);
    }

    @Test
    void insert() {
        Client client = new Client(10, "Jakie Chan", 55);
        mapper.insert(client);
        String expected = "INSERT INTO client (name,age) VALUES (?,?)";
        String actual = ((EntitySQLMetaData) mapper).getInsertSql();
        assertEquals(expected, actual);
    }
    @Test
    void getName() {

        System.out.println("Class name: " + ((EntityClassMetaData<?>) mapper).getName());
    }

    @Test
    void getConstructor() throws NoSuchMethodException {
        System.out.println("Constructor: " + ((EntityClassMetaData<?>) mapper).getConstructor());
    }

    @Test
    void getIdField() {
        System.out.println("Id field: " + ((EntityClassMetaData<?>) mapper).getIdField());
    }

    @Test
    void getAllFields() {
        System.out.println(((EntityClassMetaData<?>) mapper).getAllFields());
    }

    @Test
    void getFieldsWithoutId() {
        System.out.println(((EntityClassMetaData<?>) mapper).getFieldsWithoutId());
    }

}