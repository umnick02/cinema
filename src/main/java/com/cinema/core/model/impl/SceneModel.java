package com.cinema.core.model.impl;

import com.cinema.core.entity.Movie;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import javafx.event.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SceneModel extends ObservableModel {

    private static final Logger logger = LogManager.getLogger(SceneModel.class);

    public static final SceneModel INSTANCE = new SceneModel();

    private Set<MovieModel> movieModels = new HashSet<>();
    private MovieModel activeMovieModel;
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

    public void setActiveMovieModel(MovieModel activeMovieModel) {
        this.activeMovieModel = activeMovieModel;
    }

    public MovieModel getActiveMovieModel() {
        return activeMovieModel;
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
