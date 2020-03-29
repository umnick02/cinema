package com.cinema.javafx.view;

import com.cinema.core.entity.Movie;

import java.util.Set;

public interface Searchable {
    String getText();
    void showMovies(Set<Movie> movies);
    void showLoading();
    void hideLoading();
}
