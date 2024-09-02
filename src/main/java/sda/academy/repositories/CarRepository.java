package sda.academy.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sda.academy.entities.Car;
import sda.academy.util.HibernateUtil;

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
}
