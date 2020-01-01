package com.cinema.model;

import com.cinema.config.Config;
import com.cinema.config.HibernateUtil;
import com.cinema.dao.MovieDAO;
import com.cinema.entity.Movie;
import com.cinema.view.components.SideMenuContainer;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import java.util.*;

import static com.cinema.config.Config.getLang;

@Singleton
public class MovieModel {

    private EntityManager entityManager = HibernateUtil.entityManager();
    private MovieDAO movieDAO;

    @Inject
    public MovieModel(MovieDAO movieDAO) {
        this.movieDAO = movieDAO;
    }

    public void saveMovie(Movie movie) {
        movieDAO.create(movie);
    }

    public Movie updateFilePath(Movie movie) {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("update Movie set file=:file where id=:id");
        query.setParameter("file", movie.getFile());
        query.setParameter("id", movie.getId());
        entityManager.getTransaction().commit();
        return movie;
    }

    public Movie getMovie(Long id) {
        return entityManager.find(Movie.class, id);
    }

    public boolean isValid(Movie movie) {
        return (movie.getMovieRu() != null && movie.getMovieRu().getTitle() != null) ||
                (movie.getMovieEn() != null && movie.getMovieEn().getTitle() != null);
    }

    public Movie getMovieByTitle(Movie movie) {
        Query query;
        if (movie.getMovieRu() != null) {
            query = entityManager.createQuery("from Movie where movieRu.title=:title");
            query.setParameter("title", movie.getMovieRu().getTitle());
        } else {
            query = entityManager.createQuery("from Movie where movieEn.title=:title");
            query.setParameter("title", movie.getMovieEn().getTitle());
        }
        @SuppressWarnings("unchecked")
        List<Movie> movies = query.getResultList();
        return movies.size() > 0 ? movies.get(0) : null;
    }

    public Set<Movie> getMovies(int page, int cardsPerPage) {
        Config.PrefKey.Language language = getLang();
        EntityGraph<Movie> entityGraph = entityManager.createEntityGraph(Movie.class);
        entityGraph.addAttributeNodes(language == Config.PrefKey.Language.RU ? "movieRu" : "movieEn");
        CriteriaQuery<Movie> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(Movie.class);
        criteriaQuery.from(Movie.class);
        TypedQuery<Movie> typedQuery = entityManager.createQuery(criteriaQuery).setFirstResult(page * cardsPerPage).setMaxResults(cardsPerPage);
        typedQuery.setHint("javax.persistence.loadgraph", entityGraph);
        List<Movie> movies = typedQuery.getResultList();
        return new HashSet<>(movies);
    }

    private List<Object[]> findAllGenres(Config.PrefKey.Language language) {
        String localization = language == Config.PrefKey.Language.RU ? "MOVIE_RU" : "MOVIE_EN";
        String query = String.format("select genres.genre, cast(sum(genres.cnt) as int) as CNT from" +
                " (select GENRE_1 as genre, count(GENRE_1) as cnt from %s where GENRE_1 is not null group by GENRE_1" +
                " union select GENRE_2, count(GENRE_2) from %s where GENRE_2 is not null group by GENRE_2" +
                " union select GENRE_3, count(GENRE_3) from %s where GENRE_3 is not null group by GENRE_3) as genres" +
                " group by genres.genre order by CNT desc, GENRE asc", localization, localization, localization);
        @SuppressWarnings("unchecked")
        List<Object[]> genres = HibernateUtil.entityManager().createNativeQuery(query).getResultList();
        return genres;
    }

    public Set<SideMenuContainer.GenreItem> getGenres() {
        Config.PrefKey.Language language = getLang();
        List<Object[]> genreList = findAllGenres(language);
        Set<SideMenuContainer.GenreItem> genres = new HashSet<>();
        for (Object[] genre : genreList) {
            genres.add(new SideMenuContainer.GenreItem(genre[0].toString(), (int) genre[1]));
        }
        return genres;
    }

    public Movie processMovieFromMagnet(Movie movie) {
        if (isValid(movie)) {
            Movie dbMovie = getMovieByTitle(movie);
            if (dbMovie != null) return dbMovie;
            saveMovie(movie);
            return movie;
        }
        return null;
    }
}
