package com.cinema.core.service.bt.selectors.file;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;

public class SkipFileSelector extends TorrentFileSelector {

    private SkipFileSelector() {}

    public static final SkipFileSelector INSTANCE = new SkipFileSelector();

    @Override
    protected SelectionResult select(TorrentFile file) {
        return SelectionResult.skip();
    }
}
