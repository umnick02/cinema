package com.cinema.core.entity;

import com.cinema.core.config.Lang;
import com.cinema.core.converter.SubtitleConverter;

import javax.persistence.*;
import java.util.List;

@Embeddable
public class Magnet {

    @Column(name = "magnet", columnDefinition = "text")
    private String hash;

    @Column(name = "file", columnDefinition = "text")
    private String file;

    @Convert(converter = SubtitleConverter.class)
    @Column(name = "subtitle", columnDefinition = "text")
    private List<Subtitle> subtitles;

    @Column(name = "file_size")
    private Long fileSize;

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

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public List<Subtitle> getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(List<Subtitle> subtitles) {
        this.subtitles = subtitles;
    }

    public static class Subtitle {
        private Lang lang;
        private String file;

        public Lang getLang() {
            return lang;
        }

        public void setLang(Lang lang) {
            this.lang = lang;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }
    }
}
