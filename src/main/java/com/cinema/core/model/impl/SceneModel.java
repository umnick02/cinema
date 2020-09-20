package com.cinema.core.model.impl;

import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.entity.Subtitle;
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
        if (SceneModel.INSTANCE.getActiveMovieModel().getMovie().isSeries()) {
            return SceneModel.INSTANCE.getActiveMovieModel().getActiveSeasonModel().getActiveEpisodeModel().getEpisode();
        } else {
            return SceneModel.INSTANCE.getActiveMovieModel().getMovie();
        }
    }

    public Subtitle getActiveSubtitle() {
        if (SceneModel.INSTANCE.getActiveMovieModel().getMovie().isSeries()) {
            return SceneModel.INSTANCE.getActiveMovieModel().getActiveSeasonModel().getActiveEpisodeModel().getEpisode().getSubtitle();
        } else {
            return SceneModel.INSTANCE.getActiveMovieModel().getMovie().getSubtitle();
        }
    }

    public Magnet getActiveMagnet() {
        if (SceneModel.INSTANCE.getActiveMovieModel().getMovie().isSeries()) {
            return SceneModel.INSTANCE.getActiveMovieModel().getActiveSeasonModel().getActiveEpisodeModel().getEpisode().getMagnet();
        } else {
            return SceneModel.INSTANCE.getActiveMovieModel().getMovie().getMagnet();
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
