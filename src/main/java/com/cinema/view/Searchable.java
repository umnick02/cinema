package com.cinema.view;

import com.cinema.entity.Movie;

import java.util.Set;

public interface Searchable {
    String getText();
    void showResults(Set<Movie> movies);
}
