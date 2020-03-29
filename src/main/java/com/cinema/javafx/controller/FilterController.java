package com.cinema.javafx.controller;

import com.cinema.core.model.Filter;
import com.cinema.javafx.view.components.FilterContainer;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.MovieModel;
import javafx.scene.layout.Pane;

import java.util.Set;

public class FilterController {

    private FilterContainer filterContainer;
    private final MovieModel movieModel = MovieModel.INSTANCE;

    public FilterController(Pane filterPane) {
        filterContainer = new FilterContainer(this, filterPane);
    }

    public Set<Movie> getMovies() {
        Filter filter = filterContainer.getFilter();
        return movieModel.getMovies(filter);
    }
}
