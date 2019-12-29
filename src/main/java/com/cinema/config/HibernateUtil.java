package com.cinema.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
    private static EntityManagerFactory entityManagerFactory;

    private static void setupEntityManagerFactory() {
        entityManagerFactory = Persistence.createEntityManagerFactory("com.cinema");
    }

    public static EntityManager entityManager() {
        if (entityManagerFactory == null) {
            setupEntityManagerFactory();
        }
        return entityManagerFactory.createEntityManager();
    }
}
