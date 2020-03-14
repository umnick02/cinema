package com.cinema.controller;

import com.cinema.entity.Movie;
import com.cinema.model.Filter;
import com.cinema.model.MovieModel;
import com.cinema.view.components.FilterContainer;
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
