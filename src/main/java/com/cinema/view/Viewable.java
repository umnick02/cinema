package com.cinema.view;

import com.cinema.entity.Movie;

import java.util.Map;
import java.util.Set;

public interface Viewable {
    void setMovies(Set<Movie> movies);
    void setGenres(Map<String, Integer> genres);
}
