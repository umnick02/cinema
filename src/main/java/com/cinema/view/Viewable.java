package com.cinema.view;

import com.cinema.entity.Movie;
import com.cinema.model.Filter;

import java.util.Map;
import java.util.Set;

public interface Viewable {
    void showMovies(Set<Movie> movies);
    void showGenres(Map<String, Integer> genres);
    Filter getFilter();
}
