package com.cinema.core.service.bt.selectors.file;

import bt.metainfo.TorrentFile;
import bt.torrent.fileselector.SelectionResult;
import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.core.entity.Magnet;
import com.cinema.core.entity.MagnetHolder;
import com.cinema.core.model.impl.SceneModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractFileSelector extends TorrentFileSelector {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFileSelector.class);

    protected MagnetHolder magnetHolder;

    protected AbstractFileSelector(MagnetHolder magnetHolder) {
        this.magnetHolder = magnetHolder;
    }

    private boolean isVideoFile(TorrentFile file) {
        return file.getPathElements().get(file.getPathElements().size() - 1).endsWith(".avi") ||
                file.getPathElements().get(file.getPathElements().size() - 1).endsWith(".mkv");
    }

    private boolean isVideoFile(Magnet magnet, TorrentFile file) {
        return magnet != null && isVideoFile(file);
    }

    protected abstract void update(MagnetHolder magnetHolder);
    protected abstract boolean isValidFile(TorrentFile file);

    @Override
    public List<SelectionResult> selectFiles(List<TorrentFile> files) {
        if (magnetHolder.getMagnet().getFullFile() != null) {
            File onDiskFile = new File(magnetHolder.getMagnet().getFullFile());
            if (!onDiskFile.exists() || magnetHolder.getMagnet().getFileSize() != onDiskFile.length()) {
//            files.sort(Comparator.comparingLong(TorrentFile::getSize));
                List<SelectionResult> selectionResults = new ArrayList<>();
                for (TorrentFile file : files) {
                    selectionResults.add(select(file));
                }
                return selectionResults;
            }
        }
        logger.info("Find downloaded file [{}] for [{}]. Skip all files", magnetHolder.getMagnet().getFile(), magnetHolder);
        return files.stream().map(f -> SelectionResult.skip()).collect(Collectors.toList());
    }

    @Override
    protected SelectionResult select(TorrentFile file) {
        if (!isValidFile(file)) {
            return SelectionResult.skip();
        }
        if (magnetHolder.getMagnet().getFile() == null || (isVideoFile(magnetHolder.getMagnet(), file) && !SceneModel.INSTANCE.getActiveMovieModel().isPlayable())) {
            magnetHolder.getMagnet().setFile(SceneModel.INSTANCE.getActiveMovieModel().getMovie().getFolderName() + "\\" + String.join("/", file.getPathElements()));
            magnetHolder.getMagnet().setFileSize(file.getSize());
            logger.info("Set file [{}] for [{}]", magnetHolder.getMagnet().getFile(), magnetHolder);
            update(magnetHolder);
        }
        return SelectionResult.select().build();
    }
}
