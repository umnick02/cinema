package com.cinema.core.model.impl;

import com.cinema.core.dao.EpisodeDAO;
import com.cinema.core.entity.Episode;

public enum EpisodeModel {
    INSTANCE;

    private EpisodeDAO episodeDAO = EpisodeDAO.INSTANCE;

    public void update(Episode episode) {
        episodeDAO.update(episode);
    }
}
