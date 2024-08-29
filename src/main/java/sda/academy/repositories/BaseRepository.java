package sda.academy.repositories;

import sda.academy.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.Serializable;

public class BaseRepository<T, ID extends Serializable> {

    private final Class<T> entityType;

    public BaseRepository(Class<T> entityType) {
        this.entityType = entityType;
    }

    public void save(T entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.save(entity);

            transaction.commit();
        }
    }

    public T findById(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(entityType, id);
        }
    }

    public void update(T entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.update(entity);

            transaction.commit();
        }
    }

    public void delete(ID id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            T entity = findById(id);
            if (entity != null) {
                session.delete(entity);
            }

            transaction.commit();
        }
    }

    public void delete(T entity) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            session.delete(entity);

            transaction.commit();
        }
    }

}