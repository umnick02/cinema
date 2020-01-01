package com.cinema.dao;

import com.cinema.config.HibernateUtil;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieEn;
import com.cinema.entity.MovieInternalize;
import com.cinema.entity.MovieRu;
import com.google.inject.Singleton;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Singleton
public class MovieDAO {

    private final EntityManager entityManager = HibernateUtil.entityManager();

    @Transactional(rollbackOn = Exception.class)
    public void create(Movie movie) {
        entityManager.persist(movie);
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(Movie movie) {
        entityManager.merge(movie);
    }

    @Transactional(rollbackOn = Exception.class)
    public void delete(Long id) {
        entityManager.remove(new Movie(id));
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
