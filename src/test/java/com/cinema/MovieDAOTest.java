package com.cinema;

import com.cinema.dao.MovieDAO;
import com.cinema.entity.CastEn;
import com.cinema.entity.Movie;
import com.cinema.entity.MovieEn;
import com.cinema.entity.Role;
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
        Injector injector = Guice.createInjector();
        movieDAO = injector.getInstance(MovieDAO.class);
    }

    @Test
    public void create() {
        Movie movie = new Movie();
        MovieEn movieEn = new MovieEn();
        movie.setMovieEn(movieEn);
        movieEn.setMovie(movie);
        movieEn.setTitle("title");
        movie.setMagnet("magnet");
        movieEn.setCasts(new ArrayList<>());
        movieEn.getCasts().add(new CastEn());
        movieEn.getCasts().get(0).setName("vasia");
        movieEn.getCasts().get(0).setPriority((short)1);
        movieEn.getCasts().get(0).setRole(Role.ACTOR);
        movieEn.getCasts().get(0).setMovie(movieEn);
        movieDAO.create(movie);
        Assert.assertNotNull(movie.getId());
    }

    @Test
    public void get() {
        Movie movie = movieDAO.getMovie("title", MovieEn.class);
        Assert.assertEquals("title", movie.getMovieEn().getTitle());
    }
}
