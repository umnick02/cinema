package com.cinema.config;

import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.entity.*;
import com.cinema.presenter.PlayerPresentable;
import com.cinema.presenter.PlayerPresenter;
import com.cinema.presenter.SearchPresentable;
import com.cinema.presenter.SearchPresenter;
import com.cinema.service.bt.selectors.DraftFilesSelector;
import com.cinema.view.Playable;
import com.cinema.view.Searchable;
import com.cinema.view.components.ListContentContainer;
import com.cinema.view.components.PlayerContainer;
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
        bind(SearchPresentable.class).to(SearchPresenter.class);
        bind(Searchable.class).to(ListContentContainer.class);
        bind(Playable.class).to(PlayerContainer.class);
        bind(PlayerPresentable.class).to(PlayerPresenter.class);
    }

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

    @Provides
    public EntityManager provideEntityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }
}
