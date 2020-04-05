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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Movie;
import com.cinema.core.service.bt.selectors.impl.EpisodeFileSelector;
import com.cinema.core.service.bt.selectors.impl.MovieFileSelector;
import com.cinema.core.entity.Magnetize;
import com.cinema.core.config.Preferences;
import com.cinema.core.service.bt.selectors.SkipFileSelector;

public class BtClientService {

    private static final Logger logger = LogManager.getLogger(BtClientService.class);

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
    private final Storage storage = new FileSystemStorage(Paths.get(Preferences.getPreference(Preferences.PrefKey.STORAGE)));

    public Torrent downloadTorrent(Magnetize magnetize) {
        Torrent[] torrent = new Torrent[1];
        BtClient btClient = Bt.client()
                .config(config)
                .module(dhtModule)
                .storage(storage)
                .magnet(magnetize.getHash())
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

    public void downloadTorrentFiles(Magnetize magnetize) {
        BtClient btClient = Bt.client()
                .config(config)
                .module(dhtModule)
                .storage(storage)
                .magnet(magnetize.getHash())
                .autoLoadModules()
                .initEagerly()
                .sequentialSelector()
                .fileSelector(buildFileSelector(magnetize))
                .stopWhenDownloaded()
                .build();
        logger.info("Download file [{}]", magnetize.getFile());
        btClient.startAsync(torrentSessionState -> {

        }, 1000);
    }

    private TorrentFileSelector buildFileSelector(Magnetize magnetize) {
        TorrentFileSelector fileSelector;
        if (magnetize instanceof Movie) {
            fileSelector = new MovieFileSelector((Movie) magnetize);
        } else if (magnetize instanceof Episode) {
            fileSelector = new EpisodeFileSelector((Episode) magnetize);
        } else {
            fileSelector = SkipFileSelector.INSTANCE;
        }
        return fileSelector;
    }
}
