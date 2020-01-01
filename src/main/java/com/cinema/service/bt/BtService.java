package com.cinema.service.bt;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import bt.torrent.fileselector.TorrentFileSelector;
import bt.torrent.selector.SequentialSelector;
import com.cinema.entity.Movie;
import com.cinema.model.MovieModel;
import com.cinema.service.parser.MagnetParser;
import com.cinema.service.bt.selectors.DraftFilesSelector;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Paths;
import java.time.Duration;

@Singleton
public class BtService {

    private static final Logger logger = LogManager.getLogger(BtService.class);

    private BtClient btClient;
    private String magnet;
    private final TorrentFileSelector fileSelector;
    private final MagnetParser magnetParser;
    private final MovieModel movieModel;

    @Inject
    public BtService(TorrentFileSelector fileSelector, MagnetParser magnetParser, MovieModel movieModel) {
        this.fileSelector = fileSelector;
        this.magnetParser = magnetParser;
        this.movieModel = movieModel;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    public BtClient getBtClient() {
        return btClient;
    }

    public void start() {
        if (magnet == null) throw new NullPointerException("Magnet link is null for movie!");
        Config config = new Config() {
            @Override
            public int getNumOfHashingThreads() {
                return Runtime.getRuntime().availableProcessors() * 2;
            }
        };
        Module dhtModule = new DHTModule(new DHTConfig() {
            @Override
            public boolean shouldUseRouterBootstrap() {
                return true;
            }
        });
        Storage storage = new FileSystemStorage(Paths.get(com.cinema.config.Config.getPreference(com.cinema.config.Config.PrefKey.STORAGE)));
        btClient = Bt.client()
                .config(config)
                .storage(storage)
                .magnet(magnet)
                .autoLoadModules()
                .afterTorrentFetched(torrent -> {
                    Movie movie = movieModel.processMovieFromMagnet(magnetParser.parse(magnet, torrent));
                    ((DraftFilesSelector) fileSelector).setMovie(movie);
                })
                .initEagerly()
                .fileSelector(fileSelector)
                .selector(SequentialSelector.sequential())
                .module(dhtModule)
                .stopWhenDownloaded()
                .build();
        long t0 = System.currentTimeMillis();
        btClient.startAsync(state -> {
            System.err.println("Peers: " + state.getConnectedPeers().size() + "; Downloaded: " + (((double)state.getPiecesComplete()) / state.getPiecesTotal()) * 100 + "%");
        }, 5000).join();
        System.err.println("Done in " + Duration.ofMillis(System.currentTimeMillis() - t0));
    }
}
