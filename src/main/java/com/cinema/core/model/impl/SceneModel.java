package com.cinema.core.model.impl;

import com.cinema.core.entity.Movie;
import com.cinema.core.entity.SubtitleHolder;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import javafx.event.Event;

import java.util.HashSet;
import java.util.Set;

public class SceneModel extends ObservableModel {

    public static final SceneModel INSTANCE = new SceneModel();

    private Set<MovieModel> movieModels = new HashSet<>();
    private MovieModel activeMovieModel;
    private FilterModel filterModel;
    private int page = 1;
    private int limit = 9;

    private SceneModel() {}

    public Set<MovieModel> getMovieModels() {
        return movieModels;
    }

    public void setActiveMovieModel(MovieModel activeMovieModel) {
        this.activeMovieModel = activeMovieModel;
    }

    public MovieModel getActiveMovieModel() {
        return activeMovieModel;
    }

    public SubtitleHolder getActiveSubtitleHolder() {
        if (activeMovieModel.getMovie().isSeries()) {
            return activeMovieModel.getActiveSeasonModel().getActiveEpisodeModel().getEpisode();
        } else {
            return activeMovieModel.getMovie();
        }
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
