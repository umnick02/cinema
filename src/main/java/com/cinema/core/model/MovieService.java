package com.cinema.core.model;

import com.cinema.core.entity.Movie;

import java.util.Set;

public interface MovieService {
    Set<Movie> getMovies(Filter filter);
    Set<Movie> getMovies(Filter filter, int limit, int offset);
}
