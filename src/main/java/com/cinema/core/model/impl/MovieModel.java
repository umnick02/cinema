package com.cinema.core.model.impl;

import com.cinema.core.config.Preferences;
import com.cinema.core.dao.MovieDAO;
import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Movie;
import com.cinema.core.entity.Series;
import com.cinema.core.model.Filter;
import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.ObservableModel;
import javafx.event.Event;

import java.io.File;
import java.util.*;

public class MovieModel extends ObservableModel {

    private static final MovieDAO MOVIE_DAO = MovieDAO.INSTANCE;

    private final Movie movie;
    private Set<SeasonModel> seasonModels;
    private SeasonModel activeSeasonModel;

    public MovieModel(Movie movie) {
        this.movie = movie;
        if (movie instanceof Series) {
            if (((Series) movie).getEpisodes() != null && ((Series) movie).getEpisodes().size() > 0) {
                seasonModels = new LinkedHashSet<>();
                Map<Short, Set<Episode>> seasons = new HashMap<>();
                ((Series) movie).getEpisodes().forEach(episode -> {
                    seasons.putIfAbsent(episode.getSeason(), new HashSet<>());
                    seasons.get(episode.getSeason()).add(episode);
                });
                seasons.keySet().stream()
                        .sorted(Comparator.comparingInt(Short::shortValue))
                        .forEach((season) -> seasonModels.add(new SeasonModel(this, seasons.get(season))));
            }
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
        if (this.activeSeasonModel == null || !this.activeSeasonModel.equals(activeSeasonModel)) {
            this.activeSeasonModel = activeSeasonModel;
            fireEvent(new Event(ModelEventType.SEASON_CHANGE.getEventType()));
        }
    }

    public SeasonModel getActiveSeasonModel() {
        return activeSeasonModel;
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

    public static void save(Movie movie) {
        MOVIE_DAO.save(movie);
    }

    public static Set<Movie> getMovies() {
        return getMovies(FilterModel.getFilter(), 12, 0);
    }

    public static Movie getMovie(String originalTitle) {
        return MovieDAO.INSTANCE.getMovie(originalTitle);
    }

    public static Set<Movie> getMovies(Filter filter, int limit, int offset) {
        List<Movie> movies = MOVIE_DAO.getMovies(filter, limit, offset);
        return new HashSet<>(movies);
    }

    public boolean isPlayable() {
        File onDiskFile = getFile();
        if (onDiskFile == null) {
            return false;
        }
        return onDiskFile.exists() && onDiskFile.length() > movie.getMagnet().getFileSize() * Preferences.PRELOAD_MIN / movie.getDuration();
    }

    public boolean isDownloaded() {
        File onDiskFile = getFile();
        if (onDiskFile == null) {
            return false;
        }
        return onDiskFile.exists() && onDiskFile.length() == movie.getMagnet().getFileSize();
    }

    private File getFile() {
        if (movie.isSeries()) {
            Episode episode = activeSeasonModel.getActiveEpisodeModel().getEpisode();
            if (episode.getMagnet().getFullFile() == null) {
                return null;
            }
            return new File(episode.getMagnet().getFullFile());
        } else {
            if (movie.getMagnet().getFullFile() == null) {
                return null;
            }
            return new File(movie.getMagnet().getFullFile());
        }
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
