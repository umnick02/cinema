package com.cinema.model;

import com.cinema.config.EntityManagerProvider;
import com.cinema.dao.MovieDAO;
import com.cinema.entity.Movie;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.*;

public enum MovieModel {
    INSTANCE;

    private EntityManager entityManager = EntityManagerProvider.provideEntityManager();
    private MovieDAO movieDAO = MovieDAO.INSTANCE;

    public void updateMovie(Movie movie) {
        movieDAO.update(movie);
    }

    public Set<Movie> getMovies(Filter filter) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);
        query.select(root).orderBy(builder.asc(root.get("ratingImdb")));
        if (filter.getGenresIncl() != null) {
            query.where(addGenresPredicate(root, builder, filter));
        }
        List<Movie> movies = entityManager.createQuery(query).getResultList();
        return new HashSet<>(movies);
    }

    private Predicate addGenresPredicate(Root<Movie> root, CriteriaBuilder builder, Filter filter) {
        Predicate predicate1 = root.get("genre1").in((Object[]) filter.getGenresIncl());
        Predicate predicate2 = root.get("genre2").in((Object[]) filter.getGenresIncl());
        Predicate predicate3 = root.get("genre3").in((Object[]) filter.getGenresIncl());
        return builder.or(predicate1, predicate2, predicate3);
    }

    private List<Object[]> findAllGenres() {
        String query = "select 'Any' as genre, cast(count(id) as int) as cnt from movie" +
                " union (select genres.genre as genre, cast(sum(genres.cnt) as int) as cnt from" +
                " (select GENRE_1 as genre, count(GENRE_1) as cnt from movie where GENRE_1 is not null group by GENRE_1" +
                " union select GENRE_2 as genre, count(GENRE_2) as cnt from movie where GENRE_2 is not null group by GENRE_2" +
                " union select GENRE_3 as genre, count(GENRE_3) as cnt from movie where GENRE_3 is not null group by GENRE_3) as genres" +
                " group by genres.genre order by genre desc)";
        @SuppressWarnings("unchecked")
        List<Object[]> genres = entityManager.createNativeQuery(query).getResultList();
        return genres;
    }

    public Map<String, Integer> getGenres() {
        List<Object[]> genreList = findAllGenres();
        Map<String, Integer> genres = new HashMap<>();
        for (Object[] genre : genreList) {
            genres.put(genre[0].toString(), (int) genre[1]);
        }
        return genres;
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

    public Set<Movie> findByTitleLike(String pattern) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Movie> query = builder.createQuery(Movie.class);
        Root<Movie> root = query.from(Movie.class);
        query.where(builder.like(builder.lower(root.get("title")),
                "%" + pattern.toLowerCase() + "%"));
        List<Movie> movies = entityManager.createQuery(query).getResultList();
        return new HashSet<>(movies);
    }
}
