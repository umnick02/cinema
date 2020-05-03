package com.cinema.core.model.impl;

import com.cinema.core.dao.EpisodeDAO;
import com.cinema.core.entity.Episode;

public class EpisodeModel {

    private static EpisodeDAO episodeDAO = EpisodeDAO.INSTANCE;

    private Episode episode;
    private SeasonModel seasonModel;

    public EpisodeModel(SeasonModel seasonModel, Episode episode) {
        this.episode = episode;
        this.seasonModel = seasonModel;
    }

    public Episode getEpisode() {
        return episode;
    }

    public SeasonModel getSeasonModel() {
        return seasonModel;
    }

    public static void update(Episode episode) {
        episodeDAO.update(episode);
    }
}
