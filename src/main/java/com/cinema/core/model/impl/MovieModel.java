package com.cinema.core.model.impl;

import com.cinema.core.dao.MovieDAO;
import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Movie;
import com.cinema.core.model.Filter;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import javafx.event.Event;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class MovieModel extends ObservableModel {

    private static final MovieDAO MOVIE_DAO = MovieDAO.INSTANCE;

    private final Movie movie;
    private Set<SeasonModel> seasonModels;
    private SeasonModel activeSeasonModel;

    public MovieModel(Movie movie) {
        this.movie = movie;
        if (movie.getEpisodes() != null && movie.getEpisodes().size() > 0) {
            seasonModels = new LinkedHashSet<>();
            Map<Short, Set<Episode>> seasons = new HashMap<>();
            movie.getEpisodes().forEach(episode -> {
                seasons.putIfAbsent(episode.getSeason(), new HashSet<>());
                seasons.get(episode.getSeason()).add(episode);
            });
            seasons.keySet().stream()
                    .sorted(Comparator.comparingInt(Short::shortValue))
                    .forEach((season) -> seasonModels.add(new SeasonModel(seasons.get(season))));
        }
    }

    public SeasonModel getPrevSeasonModel() {
        if (seasonModels == null) {
            return null;
        }
        SeasonModel prevSeasonModel = null;
        for (SeasonModel seasonModel : seasonModels) {
            if (seasonModel.equals(activeSeasonModel)) {
                return prevSeasonModel;
            } else {
                prevSeasonModel = seasonModel;
            }
        }
        return prevSeasonModel;
    }

    public SeasonModel getNextSeasonModel() {
        if (seasonModels == null) {
            return null;
        }
        Iterator<SeasonModel> seasonModelIterator = seasonModels.iterator();
        while (seasonModelIterator.hasNext()) {
            SeasonModel seasonModel = seasonModelIterator.next();
            if (seasonModel.equals(activeSeasonModel)) {
                try {
                    return seasonModelIterator.next();
                } catch (NoSuchElementException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public void nextSeasonModel() {
        SeasonModel seasonModel = getNextSeasonModel();
        if (seasonModel != null) {
            setActiveSeasonModel(seasonModel);
        }
    }

    public void prevSeasonModel() {
        SeasonModel seasonModel = getPrevSeasonModel();
        if (seasonModel != null) {
            setActiveSeasonModel(seasonModel);
        }
    }

    public void setActiveSeasonModel(SeasonModel activeSeasonModel) {
        this.activeSeasonModel = activeSeasonModel;
        fireEvent(new Event(ModelEventType.SEASON_CHANGE.getEventType()));
    }

    public SeasonModel getActiveSeasonModel() {
        return activeSeasonModel;
    }

    public boolean isSeries() {
        return movie.getEpisodes() != null && movie.getEpisodes().size() > 0;
    }

    public Movie getMovie() {
        return movie;
    }

    public Set<SeasonModel> getSeasonModels() {
        return seasonModels;
    }

    public static void update(Movie movie) {
        MOVIE_DAO.update(movie);
    }

    public static Set<Movie> getMovies() {
        return getMovies(FilterModel.getFilter(), 12, 0);
    }

    public static Set<Movie> getMovies(Filter filter, int limit, int offset) {
        List<Movie> movies = MOVIE_DAO.getMovies(filter, limit, offset);
        return new HashSet<>(movies);
    }

    public boolean isPlayable() {
        return movie.getFile() != null && Files.exists(Path.of(movie.getFile())) && movie.getStatus() == Magnet.Status.PLAYABLE;
    }

    public boolean isDownloaded() {
        return movie.getFile() != null && Files.exists(Path.of(movie.getFile())) && movie.getStatus() == Magnet.Status.DOWNLOADED;
    }

    public Movie processMovieFromMagnet(Movie movie) {
        if (movie.getOriginalTitle() != null) {
            Movie dbMovie = MOVIE_DAO.getMovie(movie.getOriginalTitle());
            if (dbMovie != null) return dbMovie;
            MOVIE_DAO.create(movie);
            return movie;
        }
        return null;
    }
}
