package com.library;

import com.library.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class TestConnection {

    public static void main(String[] args) {
        System.out.println("🔄 Testing Hibernate connection to Oracle Database...");

        try {
            // Get SessionFactory
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

            if (sessionFactory != null) {
                // Open a session
                Session session = sessionFactory.openSession();

                System.out.println("✅ Database connection successful!");
                System.out.println("✅ Session opened: " + session.isOpen());

                // Close session
                session.close();
                System.out.println("✅ Session closed.");

            } else {
                System.err.println("❌ SessionFactory is null!");
            }

        } catch (Exception e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }

        System.out.println("✅ Test completed!");
    }
}
