package ru.otus.torwel.h12.hibernate.dao;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.torwel.h12.core.dao.ClientDao;
import ru.otus.torwel.h12.core.dao.ClientDaoException;
import ru.otus.torwel.h12.core.model.Client;
import ru.otus.torwel.h12.core.sessionmanager.SessionManager;
import ru.otus.torwel.h12.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.torwel.h12.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientDaoHibernate implements ClientDao {
    private static final Logger logger = LoggerFactory.getLogger(ClientDaoHibernate.class);

    private final SessionManagerHibernate sessionManager;

    public ClientDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<Client> findById(long id) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session session = currentDbSession.getHibernateSession();
            return Optional.ofNullable(session.find(Client.class, id));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public long insert(Client client) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session session = currentDbSession.getHibernateSession();
            session.persist(client);
            session.flush();
            return client.getId();
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public void update(Client client) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentDbSession.getHibernateSession();
            hibernateSession.merge(client);
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public long insertOrUpdate(Client client) {
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentDbSession.getHibernateSession();
            if (client.getId() > 0) {
                hibernateSession.merge(client);
            } else {
                hibernateSession.persist(client);
                hibernateSession.flush();
            }
            return client.getId();
        } catch (Exception e) {
            throw new ClientDaoException(e);
        }
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        DatabaseSessionHibernate currentDbSession = sessionManager.getCurrentSession();
        try {
            Session session = currentDbSession.getHibernateSession();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Client> criteria = builder.createQuery(Client.class);
            criteria.from(Client.class);
            clients = session.createQuery(criteria).getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return clients;
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
