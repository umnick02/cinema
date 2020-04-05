package com.cinema.core.entity;

public interface Magnetize {
    String getHash();
    String getFile();
    Magnet.Status getStatus();
    void setFile(String file);
    void setStatus(Magnet.Status status);
}
