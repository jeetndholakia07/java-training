package org.example.exercise1.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBConnection {
    private static SessionFactory sessionFactory;
    static {
        try{
            Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
            configuration.setProperty("hibernate.connection.url", System.getenv("DB_URL"));
            configuration.setProperty("hibernate.connection.username", System.getenv("DB_USERNAME"));
            configuration.setProperty("hibernate.connection.password", System.getenv("DB_PASSWORD"));
            sessionFactory = configuration.buildSessionFactory();
        }
        catch (Exception e){
            throw new ExceptionInInitializerError(e);
        }
    }
    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
