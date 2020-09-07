package com.cinema.core.entity;

import java.util.Set;

public interface Source {
    String getHash();
    void setHash(String hash);
    String getFile();
    void setFile(String file);
    void setFileSize(Long fileSize);
    Long getFileSize();
    Set<Magnet.Subtitle> getSubtitles();
    void setSubtitles(Set<Magnet.Subtitle> subtitles);
}
