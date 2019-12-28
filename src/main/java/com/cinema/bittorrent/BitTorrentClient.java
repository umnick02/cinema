package com.cinema.bittorrent;

import bt.Bt;
import bt.data.Storage;
import bt.data.file.FileSystemStorage;
import bt.dht.DHTConfig;
import bt.dht.DHTModule;
import bt.runtime.BtClient;
import bt.runtime.Config;
import bt.torrent.selector.SequentialSelector;
import com.google.inject.Module;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.Duration;
import java.util.concurrent.Callable;

@Service
public class BitTorrentClient implements Callable<Void> {

    private String magnet;

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    @Override
    public Void call() {
        if (magnet == null) throw new NullPointerException("Magnet link is null!");
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
        Storage storage = new FileSystemStorage(Paths.get("FOLDER_FOR_FILES"));
        BtClient btClient = Bt.client()
                .config(config)
                .storage(storage)
                .magnet(magnet)
                .autoLoadModules()
                .initEagerly()
                .selector(SequentialSelector.sequential())
                .module(dhtModule)
                .stopWhenDownloaded()
                .build();
        System.err.println("Starting leecher...");
        long t0 = System.currentTimeMillis();
        btClient.startAsync(state -> {
            System.err.println("Peers: " + state.getConnectedPeers().size() + "; Downloaded: " + (((double)state.getPiecesComplete()) / state.getPiecesTotal()) * 100 + "%");
        }, 5000).join();
        System.err.println("Done in " + Duration.ofMillis(System.currentTimeMillis() - t0));
        return null;
    }
}
