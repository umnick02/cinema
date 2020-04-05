package com.cinema.core.service.bt.selectors.impl;

import bt.metainfo.TorrentFile;
import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Magnetize;
import com.cinema.core.model.impl.EpisodeModel;
import com.cinema.core.service.bt.selectors.AbstractFileSelector;

public class EpisodeFileSelector extends AbstractFileSelector {

    private final EpisodeModel episodeModel = EpisodeModel.INSTANCE;

    public EpisodeFileSelector(Episode episode) {
        super(episode);
    }

    protected boolean isValidFile(TorrentFile file) {
        return file.getPathElements().get(file.getPathElements().size() - 1).toLowerCase()
                .contains(String.format("s%02de%02d", ((Episode) magnetize).getSeason(), ((Episode) magnetize).getEpisode()));
    }

    @Override
    protected void update(Magnetize magnetize) {
        episodeModel.update((Episode) magnetize);
    }
}
