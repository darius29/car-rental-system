package sda.academy.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.Transaction;

import sda.academy.entities.Car;
import sda.academy.entities.Reservation;
import sda.academy.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;


public class ReservationRepository extends BaseRepository<Reservation, Integer>{
    public ReservationRepository() {
        super(Reservation.class);
    }

    public List<Reservation> findAllReservation(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query<Reservation> query = session.createQuery("from Reservation", Reservation.class);
        List<Reservation> reservations = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return reservations;
    }

    public Reservation findById(int id){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession(); // 1
        session.beginTransaction();

        Reservation reservation = session.get(Reservation.class, id);

        session.getTransaction().commit();
        session.close();

        return reservation;
    }

    public List<Reservation> findActiveReservationsByCustomer(int customerId) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query<Reservation> query = session.createQuery(
                "from Reservation where customer.id = :customerId and endDate >= :today", Reservation.class);
        query.setParameter("customerId", customerId);
        query.setParameter("today", LocalDate.now());

        List<Reservation> reservations = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return reservations;
    }

    public List<Reservation> findHistoricalReservationsByCustomer(int customerId) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query<Reservation> query = session.createQuery(
                "from Reservation where customer.id = :customerId and endDate < :today", Reservation.class);
        query.setParameter("customerId", customerId);
        query.setParameter("today", LocalDate.now());

        List<Reservation> reservations = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return reservations;
    }

    public List<Reservation> findOverlappingReservations(int carId, LocalDate startDate, LocalDate endDate) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query<Reservation> query = session.createQuery(
                "from Reservation where car.id = :carId and (startDate <= :endDate and endDate >= :startDate)",
                Reservation.class);
        query.setParameter("carId", carId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<Reservation> reservations = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return reservations;
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

            Reservation reservation = session.get(Reservation.class, reservationId);

            if (reservation != null) {
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
