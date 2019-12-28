package com.cinema.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(columnList = "serial_id desc, season desc, episode desc")
})
public class Episode {
    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie serial;

    @Column(name = "serial_id", nullable = false, updatable = false, insertable = false)
    private Integer serialId;

    @Column(nullable = false)
    private Short season;

    @Column(nullable = false)
    private Short episode;

    private String title;
    private Float rating;
    private String posterUrl;
    private LocalDate releaseDate;
    private String magnet;


    public Integer getId() {
        return id;
    }

    public Movie getSerial() {
        return serial;
    }

    public void setSerial(Movie serial) {
        this.serial = serial;
    }

    public Short getSeason() {
        return season;
    }

    public void setSeason(Short season) {
        this.season = season;
    }

    public Short getEpisode() {
        return episode;
    }

    public void setEpisode(Short episode) {
        this.episode = episode;
    }

    public Integer getSerialId() {
        return serialId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return serialId.equals(episode.serialId) &&
                season.equals(episode.season) &&
                this.episode.equals(episode.episode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialId, season, episode);
    }
}
