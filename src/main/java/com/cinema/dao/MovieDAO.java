package com.cinema.dao;

import com.cinema.config.EntityManagerProvider;
import com.cinema.entity.Movie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.EntityManager;

public enum MovieDAO {
    INSTANCE;

    private static final Logger logger = LogManager.getLogger(MovieDAO.class);

    private final EntityManager entityManager = EntityManagerProvider.provideEntityManager();

    public void create(Movie movie) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(movie);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            logger.error(e.getMessage(), e);
        }
    }

    public void update(Movie movie) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(movie);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            logger.error(e.getMessage(), e);
        }
    }

    public void delete(Movie id) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            logger.error(e.getMessage(), e);
        }
    }

    public Movie getMovie(Long id) {
        return entityManager.find(Movie.class, id);
    }

    public Movie getMovie(String title) {
        return entityManager.unwrap(Session.class).bySimpleNaturalId(Movie.class).load(title);
    }
}
