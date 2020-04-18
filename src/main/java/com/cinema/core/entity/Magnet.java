package com.cinema.core.entity;

import javax.persistence.*;

@Embeddable
public class Magnet {

    @Column(name = "magnet", columnDefinition = "text")
    private String hash;

    @Column(name = "file", columnDefinition = "text")
    private String file;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_status")
    private Status status;

    public Magnet() {}

    public Magnet(String hash) {
        this.hash = hash;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        UNPLAYABLE, PLAYABLE, DOWNLOADED;
        public static boolean canPlay(Status status) {
            return status == PLAYABLE || status == DOWNLOADED;
        }
    }
}
