package sda.academy.repositories;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sda.academy.entities.Car;
import sda.academy.entities.Customer;
import sda.academy.util.HibernateUtil;

import java.util.List;

public class CustomerRepository extends BaseRepository<Customer, Integer>{
    public CustomerRepository() {
        super(Customer.class);
    }

    public List<Customer> findAll(){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Transaction transaction = session.getTransaction();

        String hql = "from Customer";
        Query<Customer> query = session.createQuery(hql, Customer.class);
        List<Customer> customers = query.getResultList();


        transaction.commit();
        session.close();


        return customers;
    }

    public List<Customer> findListByLastName(String lastName){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Transaction transaction = session.getTransaction();

        String hql = "from Customer where lastName = :parametru";
        Query<Customer> query = session.createQuery(hql, Customer.class);
        query.setParameter("parametru", lastName);

        List<Customer> customers = query.getResultList();

        transaction.commit();
        session.close();
        return customers;
    }

    public Customer findSingleByLastName(String lastName){
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Transaction transaction = session.getTransaction();

        String hql = "from Customer where id = :parametru";
        Query<Customer> query = session.createQuery(hql, Customer.class);
        query.setParameter("parametru", id);

        Customer customer = query.getSingleResult();

        transaction.commit();
        session.close();
        return customer;
    }
}
