package com.cinema.core.model.impl;

import com.cinema.core.dao.EpisodeDAO;
import com.cinema.core.entity.Episode;

public class EpisodeModel {

    private static EpisodeDAO episodeDAO = EpisodeDAO.INSTANCE;

    private Episode episode;

    public EpisodeModel(Episode episode) {
        this.episode = episode;
    }

    public Episode getEpisode() {
        return episode;
    }

    public static void update(Episode episode) {
        episodeDAO.update(episode);
    }
}
