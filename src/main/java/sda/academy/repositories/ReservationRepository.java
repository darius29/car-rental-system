package sda.academy.repositories;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sda.academy.entities.Reservation;
import sda.academy.util.HibernateUtil;

import java.util.List;

public class ReservationRepository extends BaseRepository<Reservation, Integer>{
    public ReservationRepository() {
        super(Reservation.class);
    }

    public static void saveReservation(Reservation reservation) {
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

    public static void updateReservation(Reservation reservation) {
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

    public static Reservation getReservation(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Reservation.class, id);
        }
    }

    public static List<Reservation> getReservationsByCustomer(int customerId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Reservation> reservations = null;

        try {
            Query<Reservation> query = session.createQuery("FROM Reservation WHERE customer.id = :customerId", Reservation.class);
            query.setParameter("customerId", customerId);
            reservations = query.list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return reservations;
    }

    public static void deleteReservation(int reservationId) {
        Transaction transaction = null;
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            transaction = session.beginTransaction();

            // Căutarea rezervării după ID
            Reservation reservation = session.get(Reservation.class, reservationId);

            if (reservation != null) {
                // Ștergerea rezervării dacă aceasta există
                session.delete(reservation);
                System.out.println("Reservation deleted successfully.");
            } else {
                System.out.println("Reservation not found.");
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
