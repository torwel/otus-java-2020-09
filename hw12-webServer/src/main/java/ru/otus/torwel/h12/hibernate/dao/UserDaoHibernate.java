package ru.otus.torwel.h12.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.h12.core.dao.UserDao;
import ru.otus.torwel.h12.core.dao.UserDaoException;
import ru.otus.torwel.h12.core.model.User;
import ru.otus.torwel.h12.core.sessionmanager.SessionManager;
import ru.otus.torwel.h12.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.torwel.h12.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoHibernate implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session session = currentDbSession.getHibernateSession();
            return Optional.ofNullable(session.find(User.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session session = currentDbSession.getHibernateSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            Root<User> from = criteria.from(User.class);
            criteria.select(from);
            criteria.where(builder.equal(from.get("login"), login));
            Query<User> query = session.createQuery(criteria);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session session = currentDbSession.getHibernateSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            users = session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return users;
    }

    @Override
    public long insert(User user) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session session = currentDbSession.getHibernateSession();
            session.persist(user);
            session.flush();
            return user.getId();
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public void update(User user) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentDbSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(User user) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentDbSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
            return user.getId();
        } catch (Exception e) {
            throw new UserDaoException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
