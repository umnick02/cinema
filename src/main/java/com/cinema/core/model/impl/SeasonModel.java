package com.cinema.core.model.impl;

import com.cinema.core.entity.Episode;
import com.cinema.core.model.ObservableModel;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class SeasonModel extends ObservableModel {

    private Set<EpisodeModel> episodeModels = new LinkedHashSet<>();
    private EpisodeModel activeEpisodeModel;
    private short season;
    private double avgRating;
    private int avgVotes;
    private LocalDate release;
    private MovieModel movieModel;

    public SeasonModel(MovieModel movieModel, Set<Episode> episodes) {
        this.movieModel = movieModel;
        episodes.stream().sorted(Comparator.comparingInt(Episode::getEpisode)).forEach(episode -> {
            episodeModels.add(new EpisodeModel(this, episode));
            if (season == 0) {
                season = episode.getSeason();
            }
            if (release == null || release.isAfter(episode.getReleaseDate())) {
                release = episode.getReleaseDate();
            }
            avgRating += episode.getRating();
            avgVotes += episode.getRatingVotes();
        });
        if (episodes.size() > 0) {
            avgRating /= episodes.size();
            avgVotes /= episodes.size();
        }
    }

    public MovieModel getMovieModel() {
        return movieModel;
    }

    public EpisodeModel getActiveEpisodeModel() {
        return activeEpisodeModel;
    }

    public void setActiveEpisodeModel(EpisodeModel activeEpisodeModel) {
        this.activeEpisodeModel = activeEpisodeModel;
    }

    public Set<EpisodeModel> getEpisodeModels() {
        return episodeModels;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public LocalDate getRelease() {
        return release;
    }

    public int getAvgVotes() {
        return avgVotes;
    }

    public short getSeason() {
        return season;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeasonModel that = (SeasonModel) o;
        return season == that.season &&
                Double.compare(that.avgRating, avgRating) == 0 &&
                avgVotes == that.avgVotes &&
                Objects.equals(episodeModels, that.episodeModels) &&
                Objects.equals(release, that.release);
    }

    @Override
    public int hashCode() {
        return Objects.hash(episodeModels, season, avgRating, avgVotes, release);
    }
}
