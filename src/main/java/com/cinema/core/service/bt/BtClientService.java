package com.cinema.core.service.bt;

import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.BitSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import bt.Bt;
import bt.data.Bitfield;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.metainfo.Torrent;
import bt.processor.magnet.MagnetContext;
import bt.runtime.BtClient;
import bt.runtime.Config;
import bt.torrent.fileselector.TorrentFileSelector;
import com.cinema.core.model.impl.TorrentModel;
import com.cinema.core.service.bt.selectors.piece.SequentialPositioningSelector;
import com.cinema.core.service.Stoppable;

import com.cinema.core.entity.Episode;
import com.cinema.core.entity.Movie;
import com.cinema.core.service.bt.selectors.file.impl.EpisodeFileSelector;
import com.cinema.core.service.bt.selectors.file.impl.MovieFileSelector;
import com.cinema.core.entity.Source;
import com.cinema.core.config.Preferences;
import com.cinema.core.service.bt.selectors.file.SkipFileSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.cinema.core.config.Preferences.EXECUTORS;

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
    private final Storage storage = new FileSystemStorage(Paths.get(Preferences.getPreference(Preferences.PrefKey.STORAGE)));
    private final SequentialPositioningSelector selector = new SequentialPositioningSelector();
    private BtClient btClient;
    private Source source;

    public Torrent downloadTorrent(String hash) {
        stop();
        Torrent[] torrent = new Torrent[1];
        BtClient btClient = Bt.client()
                .config(config)
                .module(dhtModule)
                .storage(storage)
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

    public void downloadTorrentFiles(Source source) {
        logger.info("Start download [{}]", source.getFile());
        if (btClient != null) {
            if (this.source.equals(source) && btClient.isStarted()) {
                return;
            }
            stop();
        }
        this.source = source;
        btClient = Bt.client()
                .config(config)
                .module(dhtModule)
                .storage(storage)
                .magnet("magnet:?xt=urn:btih:" + source.getHash())
                .autoLoadModules()
                .initEagerly()
                .selector(selector)
                .fileSelector(buildFileSelector(source))
                .stopWhenDownloaded()
                .build();
        logger.info("Download file [{}]", source.getFile());
        btClient.startAsync(TorrentModel.INSTANCE::setTorrentSessionState, 1000);
    }

    public CompletableFuture<Boolean> toPosition(float ratio) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                int i = 0;
                while (selector.getPieceIterator() == null && i++ < 30) {
                    Thread.sleep(1000);
                }
                if (i >= 30) {
                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }

            int newPosition = selector.getPieceIterator().toPosition(ratio);
            try {
                int i = 0;
                while (getBufferSize(newPosition) < 100 && i++ < 100) {
                    Thread.sleep(1000);
                }
                if (i >= 100) {
                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }, EXECUTORS);
    }

    private int getBufferSize(int newPosition) {
        try {
            Field field = btClient.getClass().getDeclaredField("context");
            field.setAccessible(true);
//            MagnetContext context = (MagnetContext) field.get(btClient);
            Bitfield bitfield = ((MagnetContext) field.get(btClient)).getBitfield();
            Field lockField = bitfield.getClass().getDeclaredField("lock");
            lockField.setAccessible(true);
            ReentrantLock lock = (ReentrantLock) lockField.get(bitfield);
            int i;
            lock.lock();
            try {
                BitSet bitSet = bitfield.getBitmask();
//            String[] set = bitSet.toString().replaceAll("[{}]", "").split(",");
//            TorrentModel.INSTANCE.setPieceMax(Integer.parseInt(set[set.length - 1].trim()));
                int miss = 0;
                for (i = newPosition; i <= newPosition + 100; i++) {
                    if (!bitSet.get(i) && ++miss > 1) {
                        break;
                    } else if (bitSet.get(i)) {
                        miss = 0;
                    }
                }
            } finally {
                lock.unlock();
            }
            System.out.println("Buffer size: " + (i - newPosition));
            return i - newPosition;
        } catch (Exception e) {
            System.out.println("getBufferSize: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void stop() {
        if (btClient != null && btClient.isStarted()) {
            btClient.stop();
        }
    }

    private TorrentFileSelector buildFileSelector(Source source) {
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
