package ru.otus.torwel.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.core.dao.AccountDao;
import ru.otus.torwel.core.model.Account;

import java.util.Optional;

public class DbServiceAccountImpl implements DBServiceAccount {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceAccountImpl.class);

    private final AccountDao accountDao;

    public DbServiceAccountImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public String saveAccount(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var clientId = accountDao.insert(account);
                sessionManager.commitSession();

                logger.info("created account: {}", clientId);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<Account> getAccount(String id) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<Account> clientOptional = accountDao.findById(id);

                logger.info("account: {}", clientOptional.orElse(null));
                return clientOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    public void updateClient(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                accountDao.update(account);
                sessionManager.commitSession();

                logger.info("updated account: {}", account.getNo());
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    public String insertOrUpdate(Account account) {
        try (var sessionManager = accountDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var clientId = accountDao.insertOrUpdate(account);
                sessionManager.commitSession();

                logger.info("Inserted or updated account: {}", clientId);
                return clientId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }
}
