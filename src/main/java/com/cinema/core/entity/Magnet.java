package com.cinema.core.entity;

import com.cinema.core.config.Preferences;

import javax.persistence.*;

@Embeddable
public class Magnet {

    @Column(name = "magnet", columnDefinition = "text")
    private String hash;

    @Column(name = "file", columnDefinition = "text")
    private String file;

    @Column(name = "file_size")
    private Long fileSize;

    public Magnet() {}

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFullFile() {
        return Preferences.getPreference(Preferences.PrefKey.STORAGE) + file;
    }
}
