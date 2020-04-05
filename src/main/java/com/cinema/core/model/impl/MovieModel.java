package com.cinema.core.model.impl;

import com.cinema.core.dao.MovieDAO;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.Filter;
import com.cinema.core.model.MovieService;

import java.util.*;

public enum MovieModel implements MovieService {
    INSTANCE;

    private MovieDAO movieDAO = MovieDAO.INSTANCE;

    public void update(Movie movie) {
        movieDAO.update(movie);
    }

    @Override
    public Set<Movie> getMovies(Filter filter) {
        return getMovies(filter, 12, 0);
    }

    @Override
    public Set<Movie> getMovies(Filter filter, int limit, int offset) {
        List<Movie> movies = movieDAO.getMovies(filter, limit, offset);
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
