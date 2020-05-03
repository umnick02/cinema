package com.cinema.core.service.bt.selectors.file;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.core.config.Preferences;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.Magnetize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractFileSelector extends TorrentFileSelector {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFileSelector.class);

    protected Magnetize magnetize;

    protected AbstractFileSelector(Magnetize magnetize) {
        this.magnetize = magnetize;
    }

    private boolean isVideoFile(TorrentFile file) {
        return file.getPathElements().get(file.getPathElements().size() - 1).endsWith(".avi") ||
                file.getPathElements().get(file.getPathElements().size() - 1).endsWith(".mkv");
    }

    private boolean isVideoFile(Magnetize magnetize, TorrentFile file) {
        return magnetize != null && isVideoFile(file);
    }

    protected abstract void update(Magnetize magnetize);
    protected abstract boolean isValidFile(TorrentFile file);

    @Override
    public List<SelectionResult> selectFiles(List<TorrentFile> files) {
        if (magnetize.getFile() == null ||
                !new File(Preferences.getPreference(Preferences.PrefKey.STORAGE) + magnetize.getFile()).exists() ||
                magnetize.getStatus() != Magnet.Status.DOWNLOADED) {
//            files.sort(Comparator.comparingLong(TorrentFile::getSize));
            List<SelectionResult> selectionResults = new ArrayList<>();
            for (TorrentFile file : files) {
                selectionResults.add(select(file));
            }
            return selectionResults;
        }
        logger.info("Find downloaded file [{}] for [{}]. Skip all files", magnetize.getFile(), magnetize);
        return files.stream().map(f -> SelectionResult.skip()).collect(Collectors.toList());
    }

    @Override
    protected SelectionResult select(TorrentFile file) {
        if (!isValidFile(file)) {
            return SelectionResult.skip();
        }
        if (isVideoFile(magnetize, file)) {
            magnetize.setFile(String.join("/", file.getPathElements()));
            magnetize.setStatus(Magnet.Status.UNPLAYABLE);
            logger.info("Set file [{}] for [{}]", magnetize.getFile(), magnetize);
            update(magnetize);
        }
        return SelectionResult.select().build();
    }
}
