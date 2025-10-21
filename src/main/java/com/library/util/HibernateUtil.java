package com.library.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    // Private constructor to prevent instantiation (Singleton pattern)
    private HibernateUtil() {}

    // Get SessionFactory instance
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                // Create SessionFactory from hibernate.cfg.xml
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.cfg.xml");
                sessionFactory = configuration.buildSessionFactory();

                System.out.println("✅ Hibernate SessionFactory created successfully!");

            } catch (Exception e) {
                System.err.println("❌ Error creating SessionFactory: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    // Close SessionFactory
    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            System.out.println("✅ Hibernate SessionFactory closed.");
        }
    }
}