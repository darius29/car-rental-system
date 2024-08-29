package sda.academy.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import sda.academy.entities.Reservation;
import sda.academy.util.HibernateUtil;
import java.util.List;

public class ReservationDAO {
    public void saveReservation(Reservation reservation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(reservation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void updateReservation(Reservation reservation) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(reservation);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public Reservation getReservation(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Reservation.class, id);
        }
    }

    public List<Reservation> getAllReservations() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Reservation", Reservation.class).list();
        }
    }
}
