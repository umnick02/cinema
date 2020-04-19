//package com.cinema.javafx.controller;
//
//import com.cinema.core.model.Filter;
//import com.cinema.javafx.view.components.FilterContainer;
//import com.cinema.core.entity.Movie;
//import com.cinema.core.model.impl.MovieModel;
//import javafx.scene.layout.Pane;
//
//import java.util.Set;
//
//public class FilterController {
//
//    private FilterContainer filterContainer;
//
//    private MoviesController moviesController;
//    private final MovieModel movieModel = MovieModel.INSTANCE;
//
//    public FilterController(Pane filterPane, Pane searchPane, MoviesController moviesController) {
//        filterContainer = new FilterContainer(this, filterPane);
//        moviesController.resetMovies(getMovies());
//    }
//
//    public Set<Movie> getMovies() {
//        Filter filter = filterContainer.getFilter();
//        return movieModel.getMovies(filter);
//    }
//}
