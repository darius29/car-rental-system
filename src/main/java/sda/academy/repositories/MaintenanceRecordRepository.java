package sda.academy.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sda.academy.entities.MaintenanceRecord;
import sda.academy.util.HibernateUtil;

import java.time.LocalDate;
import java.util.List;

public class MaintenanceRecordRepository  extends BaseRepository<MaintenanceRecord, Integer> {

    public MaintenanceRecordRepository() {
        super(MaintenanceRecord.class);
    }

    public List<MaintenanceRecord> findAll() {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Transaction transaction = session.getTransaction();

        String hql = "from MaintenanceRecord";
        Query<MaintenanceRecord> query = session.createQuery(hql, MaintenanceRecord.class);
        List<MaintenanceRecord> maintenanceRecords = query.getResultList();

        transaction.commit();
        session.close();

        return  maintenanceRecords;

    }

    public List<MaintenanceRecord> findMaintenanceRecordById(int id) {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Transaction transaction = session.getTransaction();

        String hql = "from MaintenanceRecord where id = :parametru";
        Query<MaintenanceRecord> query = session.createQuery(hql, MaintenanceRecord.class);
        query.setParameter("parametru", id);

        List<MaintenanceRecord> maintenanceRecords = query.getResultList();

        transaction.commit();
        session.close();

        return maintenanceRecords;

    }

    public List<MaintenanceRecord> findMaintenanceRecordsForCarInPeriod(int carId, LocalDate startDate, LocalDate endDate) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        String hql = "from MaintenanceRecord where car.id = :carId and maintenanceDate between :startDate and :endDate";
        Query<MaintenanceRecord> query = session.createQuery(hql, MaintenanceRecord.class);
        query.setParameter("carId", carId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        List<MaintenanceRecord> maintenanceRecords = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return maintenanceRecords;
    }



}
