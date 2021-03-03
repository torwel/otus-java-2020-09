package ru.otus.torwel.h14.hibernate.sessionmanager;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.otus.torwel.h14.core.sessionmanager.DatabaseSession;

public class DatabaseSessionHibernate implements DatabaseSession {
    private final Session session;
    private final Transaction transaction;

    public DatabaseSessionHibernate(Session session) {
        this.session = session;
        this.transaction = session.beginTransaction();
    }

    public Session getHibernateSession() {
        return session;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void close() {
        if (transaction.isActive()) {
            transaction.commit();
        }
        session.close();
    }
}
