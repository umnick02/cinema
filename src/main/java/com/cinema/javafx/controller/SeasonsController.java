package com.cinema.javafx.controller;

import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Movie;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.cinema.core.utils.StringUtils.longToString;

public class SeasonsController {

    Set<SeasonStat> seasonStats;
    StackPane container;
    private short maxSeason;

    public SeasonsController(StackPane container, Movie movie) {
        this.container = container;
        seasonStats = computeStats(movie.getEpisodes());
        seasonStats.stream().max(Comparator.comparingInt(SeasonStat::getSeason)).ifPresent(seasonStat -> maxSeason = seasonStat.season);
    }

    private Set<SeasonStat> computeStats(Set<Episode> episodes) {
        Map<Short, SeasonStat> stats = new HashMap<>();
        episodes.forEach(e -> {
            stats.putIfAbsent(e.getSeason(), new SeasonStat(e.getSeason(), e.getReleaseDate()));
            stats.get(e.getSeason()).incr(e);
        });
        return new HashSet<>(stats.values());
    }

    public List<Button> buildEpisodes() {
        List<Button> buttons = new ArrayList<>();
        seasonStats.stream().sorted(Comparator.comparingInt(SeasonStat::getSeason)).forEach(stat -> {
            Button button = buildSeasonCard(stat);
            button.setOnMouseClicked(event -> changeEpisode(stat));
            buttons.add(button);
        });
        return buttons;
    }

    public short getMaxSeason() {
        return maxSeason;
    }

    public void changeEpisode(short season) {
        seasonStats.stream().filter(s -> s.getSeason() == season).findFirst().ifPresent(this::changeEpisode);
    }

    public void changeEpisode(SeasonStat stat) {
        if (container.getChildren().size() > 1) {
            container.getChildren().remove(1);
        }
        EpisodesController episodesController = new EpisodesController(this, stat);
        container.getChildren().add(episodesController.getRootContainer());
    }

    public void hideEpisodes() {
        if (container.getChildren().size() > 1) {
            container.getChildren().remove(1);
        }
    }

    private Button buildSeasonCard(SeasonStat stat) {
        Button button = new Button();
        button.setStyle("-fx-font-size: 0.8em;");
        button.setText(String.format("Сезон %d (%d эп.)\n%s\n%.1f (%s)",
                stat.season, stat.episodes.size(),
                stat.release.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                stat.getRating(), longToString(stat.getVotes())
        ));
        return button;
    }

    public static class SeasonStat {
        private double rating;
        private int votes;
        private Set<Episode> episodes = new HashSet<>();
        private short season;
        private LocalDate release;

        private SeasonStat(short season, LocalDate release) {
            this.season = season;
            this.release = release;
        }

        public short getSeason() {
            return season;
        }

        public Set<Episode> getEpisodes() {
            return episodes;
        }

        private double getRating() {
            return rating / episodes.size();
        }

        private int getVotes() {
            return votes / episodes.size();
        }

        private void incr(Episode episode) {
            episodes.add(episode);
            rating += episode.getRating();
            votes += episode.getRatingVotes();
            if (episode.getReleaseDate().isBefore(release)) {
                release = episode.getReleaseDate();
            }
        }
    }
}
