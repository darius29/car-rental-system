package sda.academy.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sda.academy.entities.Car;
import sda.academy.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class CarRepository extends BaseRepository<Car, Integer> {

    public CarRepository() {
        super(Car.class);
    }

    public List<Car> findAll(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Transaction transaction = session.getTransaction();

        String hql = "from Car";
        Query<Car> query = session.createQuery(hql, Car.class);
        List<Car> students = query.getResultList();


        transaction.commit();
        session.close();


        return students;
    }

    public Car findByLicensePlate(String licensePlate) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Transaction transaction = session.getTransaction();

        String hql = "from Car where licensePlate = :parametru";
        Query<Car> query = session.createQuery(hql, Car.class);
        query.setParameter("parametru", licensePlate);
        Car car = query.uniqueResult();

        transaction.commit();
        session.close();
        return car;
    }

    public List<Car> findAvailableCarsInPriceRange(int lowerBound, int upperBound, LocalDate startDate, LocalDate endDate, int limit) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        // Selectam masinile disponibile in intervalul de pret si care nu sunt rezervate sau in mentenanta
        String hql = "FROM Car c WHERE c.pricePerDay BETWEEN :lowerBound AND :upperBound " +
                "AND c.id NOT IN (SELECT r.car.id FROM Reservation r WHERE r.startDate <= :endDate AND r.endDate >= :startDate) " +
                "AND c.id NOT IN (SELECT m.car.id FROM MaintenanceRecord m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate)";

        Query<Car> query = session.createQuery(hql, Car.class);
        query.setParameter("lowerBound", lowerBound);
        query.setParameter("upperBound", upperBound);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setMaxResults(limit);

        List<Car> availableCars = query.getResultList();
        session.getTransaction().commit();
        session.close();

        return availableCars;
    }

    public List<Car> findAvailableCarsOrderedByPrice(LocalDate startDate, LocalDate endDate, int limit) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        // Selectam masinile disponibile, ordonate după preț
        String hql = "FROM Car c WHERE c.id NOT IN " +
                "(SELECT r.car.id FROM Reservation r WHERE r.startDate <= :endDate AND r.endDate >= :startDate) " +
                "AND c.id NOT IN (SELECT m.car.id FROM MaintenanceRecord m WHERE m.maintenanceDate BETWEEN :startDate AND :endDate) " +
                "ORDER BY c.pricePerDay ASC";

        Query<Car> query = session.createQuery(hql, Car.class);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setMaxResults(limit);

        List<Car> availableCars = query.getResultList();
        session.getTransaction().commit();
        session.close();

        return availableCars;
    }
}
