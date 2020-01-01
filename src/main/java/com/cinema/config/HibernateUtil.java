package com.cinema.config;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
    private static final EntityManagerFactory entityManagerFactory =
            Persistence.createEntityManagerFactory("cinema");

    public static EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
