package com.cinema;

import com.cinema.config.GuiceModule;
import com.cinema.dao.MovieDAO;
import com.cinema.entity.*;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class MovieDAOTest {

    private MovieDAO movieDAO;

    @Before
    public void init() {
        Injector injector = Guice.createInjector(new GuiceModule());
        movieDAO = injector.getInstance(MovieDAO.class);
    }

    @Test
    public void test() {
        Movie movie = movieDAO.getMovie("title", MovieEn.class);
        if (movie != null) {
            movieDAO.delete(movie);
        }
        movie = create();
        movieDAO.getEntityManager().detach(movie);
        movie = movieDAO.getMovie(movie.getId());
        Assert.assertNotNull(movie);
        movie.getMovieEn().setDescription("updated desc");
        movie.getEpisodes().get(0).setEpisode((short)2);
        movieDAO.update(movie);
        movieDAO.getEntityManager().detach(movie);
        movie = movieDAO.getMovie(movie.getId());
        Assert.assertEquals("updated desc", movie.getMovieEn().getDescription());
        Assert.assertEquals((short) 2, (short) movie.getEpisodes().get(0).getEpisode());
        movieDAO.delete(movie);
        movie = movieDAO.getMovie(movie.getId());
        Assert.assertNull(movie);
    }

    private Movie create() {
        Movie movie = new Movie();
        movie.setMagnet("magnet");

        movie.setEpisodes(new ArrayList<>());
        Episode episode = new Episode();
        episode.setMagnet("magnet");
        episode.setSeries(movie);
        episode.setSeason((short)1);
        episode.setEpisode((short)1);
        episode.setTitle("episode");
        movie.getEpisodes().add(episode);

        MovieEn movieEn = new MovieEn();
        movie.setMovieEn(movieEn);
        movieEn.setMovie(movie);
        movieEn.setDescription("desc");
        movieEn.setTitle("title");
        movieEn.setCasts(new ArrayList<>());
        movieEn.getCasts().add(new CastEn());
        movieEn.getCasts().get(0).setName("vasia");
        movieEn.getCasts().get(0).setPriority((short)1);
        movieEn.getCasts().get(0).setRole(Role.ACTOR);
        movieEn.getCasts().get(0).setMovie(movieEn);

        MovieRu movieRu = new MovieRu();
        movie.setMovieRu(movieRu);
        movieRu.setMovie(movie);
        movieRu.setTitle("title");
        movieRu.setCasts(new ArrayList<>());
        movieRu.getCasts().add(new CastRu());
        movieRu.getCasts().get(0).setName("vasia");
        movieRu.getCasts().get(0).setPriority((short)1);
        movieRu.getCasts().get(0).setRole(Role.ACTOR);
        movieRu.getCasts().get(0).setMovie(movieRu);

        movieDAO.create(movie);
        return movie;
    }
}
