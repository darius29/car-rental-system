package sda.academy.util;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static SessionFactory sessionFactory = buildSessionfactory();

    public static SessionFactory getSessionFactory(){
        if(sessionFactory == null){
            sessionFactory = buildSessionfactory();
        }

        return sessionFactory;
    }

    private static SessionFactory buildSessionfactory(){
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        SessionFactory sessionFactoryLocal = configuration.buildSessionFactory();

        return sessionFactoryLocal;
    }


}
