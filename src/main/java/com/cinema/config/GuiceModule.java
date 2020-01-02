package com.cinema.config;

import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.entity.*;
import com.cinema.service.bt.selectors.DraftFilesSelector;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Properties;

public class GuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TorrentFileSelector.class).to(DraftFilesSelector.class);
    }
    private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE = new ThreadLocal<>();

    @Provides
    @Singleton
    public EntityManagerFactory provideEntityManagerFactory() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.JPA_PERSISTENCE_PROVIDER, "org.hibernate.jpa.HibernatePersistenceProvider");
        settings.put(Environment.JPA_JTA_DATASOURCE, "org.hibernate.jpa.HibernateTransactionManager");
        settings.put(Environment.DRIVER, "org.h2.Driver");
        settings.put(Environment.URL, "jdbc:h2:file:~/IdeaProjects/cinema/h2_db");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
        settings.put(Environment.SHOW_SQL, "true");
        settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//        settings.put(Environment.HBM2DDL_AUTO, "update");
        configuration.setProperties(settings);
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(MovieEn.class);
        configuration.addAnnotatedClass(MovieRu.class);
        configuration.addAnnotatedClass(CastEn.class);
        configuration.addAnnotatedClass(CastRu.class);
        configuration.addAnnotatedClass(Episode.class);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
        return configuration.buildSessionFactory(serviceRegistry);
    }

    @Provides
    public EntityManager provideEntityManager(EntityManagerFactory entityManagerFactory) {
        EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
        if (entityManager == null) {
            ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory.createEntityManager());
        }
        return entityManager;
    }
}
