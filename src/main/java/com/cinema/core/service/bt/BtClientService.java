package com.cinema.core.service.bt;

import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.metainfo.Torrent;
import bt.runtime.BtClient;
import bt.runtime.Config;
import bt.torrent.fileselector.TorrentFileSelector;
import bt.torrent.selector.SequentialSelector;
import com.cinema.core.entity.MagnetHolder;
import com.cinema.core.model.impl.TorrentModel;
import com.cinema.core.service.Stoppable;

import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Movie;
import com.cinema.core.service.bt.selectors.file.impl.EpisodeFileSelector;
import com.cinema.core.service.bt.selectors.file.impl.MovieFileSelector;
import com.cinema.core.service.bt.selectors.file.SkipFileSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum BtClientService implements Stoppable {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(BtClientService.class);

    private final DHTModule dhtModule = new DHTModule(new DHTConfig() {
        @Override
        public boolean shouldUseRouterBootstrap() {
            return true;
        }
    });
    private final Config config = new Config() {
        @Override
        public int getNumOfHashingThreads() {
            return Runtime.getRuntime().availableProcessors() * 2;
        }
    };
    private final SequentialSelector selector = new SequentialSelector();
    private BtClient btClient;
    private MagnetHolder magnetHolder;

    public Torrent downloadTorrent(String hash) {
        stop();
        Torrent[] torrent = new Torrent[1];
        BtClient btClient = Bt.client()
                .config(config)
                .module(dhtModule)
                .magnet(hash)
                .autoLoadModules()
                .afterTorrentFetched(t -> torrent[0] = t)
                .initEagerly()
                .fileSelector(SkipFileSelector.INSTANCE)
                .stopWhenDownloaded()
                .build();
        CompletableFuture<?> future = btClient.startAsync();
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
        return torrent[0];
    }

    public void downloadTorrentFiles(MagnetHolder magnetHolder) {
        logger.info("Start download [{}]", magnetHolder.getMagnet().getFullFile());
        if (btClient != null) {
            if (this.magnetHolder.equals(magnetHolder) && btClient.isStarted()) {
                return;
            }
            stop();
        }
        this.magnetHolder = magnetHolder;
        Storage storage = new FileSystemStorage(Paths.get(magnetHolder.getMagnet().getFullFile().split("\\\\")[0]));
        btClient = Bt.client()
                .config(config)
                .module(dhtModule)
                .storage(storage)
                .magnet("magnet:?xt=urn:btih:" + magnetHolder.getMagnet().getHash())
                .autoLoadModules()
                .initEagerly()
                .selector(selector)
                .fileSelector(buildFileSelector(magnetHolder))
                .stopWhenDownloaded()
                .build();
        TorrentModel.INSTANCE.setBtClient(btClient);
        logger.info("Download file [{}]", magnetHolder.getMagnet().getFile());
        btClient.startAsync(TorrentModel.INSTANCE::setTorrentSessionState, 1000);
    }

    @Override
    public void stop() {
        if (btClient != null && btClient.isStarted()) {
            btClient.stop();
        }
    }

    private TorrentFileSelector buildFileSelector(MagnetHolder source) {
        TorrentFileSelector fileSelector;
        if (source instanceof Movie) {
            fileSelector = new MovieFileSelector((Movie) source);
        } else if (source instanceof Episode) {
            fileSelector = new EpisodeFileSelector((Episode) source);
        } else {
            fileSelector = SkipFileSelector.INSTANCE;
        }
        return fileSelector;
    }
}
