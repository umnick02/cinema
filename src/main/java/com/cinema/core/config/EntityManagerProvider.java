package com.cinema.core.config;

import com.cinema.core.entity.Cast;
import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Movie;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

public class EntityManagerProvider {

    private static EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

    private static EntityManagerFactory createEntityManagerFactory() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.JPA_PERSISTENCE_PROVIDER, "org.hibernate.jpa.HibernatePersistenceProvider");
        settings.put(Environment.JPA_JTA_DATASOURCE, "org.hibernate.jpa.HibernateTransactionManager");
        settings.put(Environment.DRIVER, "org.h2.Driver");
        settings.put(Environment.URL, "jdbc:h2:file:~/IdeaProjects/cinema/h2_db");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        settings.put(Environment.SHOW_SQL, "false");
        settings.put(Environment.FORMAT_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//        settings.put(Environment.HBM2DDL_AUTO, "update");
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Cast.class);
        configuration.addAnnotatedClass(Episode.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    public static EntityManager provideEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
}
