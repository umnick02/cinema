package com.cinema.core.model.impl;

import com.cinema.core.entity.Movie;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import javafx.event.Event;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SceneModel extends ObservableModel {

    public static final SceneModel INSTANCE = new SceneModel();

    private Set<MovieModel> movieModels = new HashSet<>();
    private FilterModel filterModel;
    private int page = 1;
    private int limit = 9;

    private SceneModel() {}

    public Set<Movie> getMovies() {
        return movieModels.stream().map(MovieModel::getMovie).collect(Collectors.toSet());
    }

    public Set<MovieModel> getMovieModels() {
        return movieModels;
    }

    public void setMovies(Set<Movie> movies) {
        movieModels.clear();
        movies.forEach(movie -> {
            MovieModel movieModel = new MovieModel(movie);
            movieModels.add(movieModel);
        });
        fireEvent(new Event(ModelEventType.MOVIES_UPDATE.getEventType()));
    }
}
