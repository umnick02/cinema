package com.cinema.core.service.bt;

import bt.data.DataDescriptor;
import bt.torrent.TorrentDescriptor;

public class TorrentDescriptorImpl implements TorrentDescriptor {

    @Override
    public boolean isActive() {
        System.out.println("isActive");

        return false;
    }

    @Override
    public void start() {
        System.out.println("start");
    }

    @Override
    public void stop() {
        System.out.println("stop");

    }

    @Override
    public void complete() {
        System.out.println("complete");

    }

    @Override
    public DataDescriptor getDataDescriptor() {
        System.out.println("getDataDescriptor");
        return null;
    }
}
