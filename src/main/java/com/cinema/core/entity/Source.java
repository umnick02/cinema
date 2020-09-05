package com.cinema.core.entity;

import java.util.List;

public interface Source {
    String getHash();
    String getFile();
    void setFile(String file);
    void setFileSize(Long fileSize);
    Long getFileSize();
    List<Magnet.Subtitle> getSubtitles();

}
