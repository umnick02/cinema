package com.cinema.javafx.view;

import com.cinema.core.model.Filter;
import com.cinema.core.entity.Movie;

import java.util.Map;
import java.util.Set;

public interface Viewable {
    void showMovies(Set<Movie> movies);
    void showGenres(Map<String, Integer> genres);
    Filter getFilter();
}
