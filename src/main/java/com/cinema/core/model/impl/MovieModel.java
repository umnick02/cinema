package com.cinema.core.model.impl;

import com.cinema.core.config.EntityManagerProvider;
import com.cinema.core.dao.MovieDAO;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.Filter;
import com.cinema.core.model.MovieService;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;

import static com.cinema.core.model.PredicateUtils.*;

public enum MovieModel implements MovieService {
    INSTANCE;

    private EntityManager entityManager = EntityManagerProvider.provideEntityManager();
    private MovieDAO movieDAO = MovieDAO.INSTANCE;

    public void updateMovie(Movie movie) {
        movieDAO.update(movie);
    }

    @Override
    public Set<Movie> getMovies(Filter filter) {
        return getMovies(filter, 12, 0);
    }

    @Override
    public Set<Movie> getMovies(Filter filter, int limit, int offset) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);
        query.select(root).orderBy(builder.asc(root.get("ratingImdb")));
        query.where(buildPredicates(builder, root, filter));
        List<Movie> movies = entityManager.createQuery(query).setFirstResult(offset).setMaxResults(limit).getResultList();
        return new HashSet<>(movies);
    }

    public Movie processMovieFromMagnet(Movie movie) {
        if (movie.getOriginalTitle() != null) {
            Movie dbMovie = movieDAO.getMovie(movie.getOriginalTitle());
            if (dbMovie != null) return dbMovie;
            movieDAO.create(movie);
            return movie;
        }
        return null;
    }
}
