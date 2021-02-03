package ru.otus.torwel.jdbc.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.AccountDao;
import ru.otus.torwel.core.model.Account;
import ru.otus.torwel.core.sessionmanager.SessionManager;
import ru.otus.torwel.jdbc.mapper.JdbcMapper;
import ru.otus.torwel.jdbc.mapper.JdbcMapperSQLException;
import ru.otus.torwel.jdbc.sessionmanager.SessionManagerJdbc;

import java.util.Optional;

public class AccountDaoJdbc implements AccountDao {
    private static final Logger logger = LoggerFactory.getLogger(AccountDaoJdbc.class);

    private final JdbcMapper<Account> mapper;
    private final SessionManagerJdbc sessionManager;


    public AccountDaoJdbc(SessionManagerJdbc sessionManager, JdbcMapper<Account> mapper) {
        this.mapper = mapper;
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Account> findById(String id) {
        return Optional.ofNullable(mapper.findById(id, Account.class));
    }

    @Override
    public String insert(Account account) {
        try {
            mapper.insert(account);
        } catch (JdbcMapperSQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
        return account.getNo();
    }

    @Override
    public void update(Account account) {
        try {
            mapper.update(account);
        } catch (JdbcMapperSQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
    }

    @Override
    public String insertOrUpdate(Account account) {
        try {
            mapper.insertOrUpdate(account);
        } catch (JdbcMapperSQLException throwables) {
            logger.error(throwables.getMessage(), throwables);
        }
        return account.getNo();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
