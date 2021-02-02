package ru.otus.torwel.jdbc.mapper;

import java.sql.SQLException;

public class JdbcMapperSQLException extends SQLException {
    public JdbcMapperSQLException(Throwable cause) {
        super(cause);
    }
}
