package com.cinema.dao;

import com.cinema.entity.Movie;
import com.cinema.entity.MovieEn;
import com.cinema.entity.MovieInternalize;
import com.cinema.entity.MovieRu;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.EntityManager;

@Singleton
public class MovieDAO {

    private static final Logger logger = LogManager.getLogger(MovieDAO.class);

    private final EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Inject
    public MovieDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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

    public Movie getMovie(String title, Class<? extends MovieInternalize> cls) {
        MovieInternalize movieInternalize = null;
        if (MovieEn.class.isAssignableFrom(cls)) {
            movieInternalize = entityManager.unwrap(Session.class).bySimpleNaturalId(MovieEn.class).load(title);
        } else if (MovieRu.class.isAssignableFrom(cls)) {
            movieInternalize = entityManager.unwrap(Session.class).bySimpleNaturalId(MovieRu.class).load(title);
        }
        return movieInternalize != null ? movieInternalize.getMovie() : null;
    }
}
