package com.cinema;

import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.service.bt.selectors.DraftFilesSelector;
import com.google.inject.AbstractModule;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TorrentFileSelector.class).to(DraftFilesSelector.class);
    }
}
