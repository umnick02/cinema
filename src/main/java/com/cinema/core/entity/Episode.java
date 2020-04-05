package com.cinema.core.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(columnList = "series_id desc, season desc, episode desc")
})
public class Episode implements Magnetize {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Movie series;

    @Column(name = "SERIES_ID", nullable = false, updatable = false, insertable = false)
    private Long seriesId;

    @Column(nullable = false)
    private Short season;

    @Column(nullable = false)
    private Short episode;

    @Column(name = "title")
    private String title;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "rating_votes")
    private Integer ratingVotes;

    @Column(name = "poster", columnDefinition = "varchar(511)")
    private String poster;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Embedded
    private Magnet magnet;

    @Override
    public String getHash() {
        return magnet.getHash();
    }

    @Override
    public String getFile() {
        return magnet.getFile();
    }

    @Override
    public Magnet.Status getStatus() {
        return magnet.getStatus();
    }

    @Override
    public void setFile(String file) {
        magnet.setFile(file);
    }

    @Override
    public void setStatus(Magnet.Status status) {
        magnet.setStatus(status);
    }

    public Magnet getMagnet() {
        return magnet;
    }

    public void setMagnet(Magnet magnet) {
        this.magnet = magnet;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRatingVotes() {
        return ratingVotes;
    }

    public void setRatingVotes(Integer ratingVotes) {
        this.ratingVotes = ratingVotes;
    }

    public Integer getId() {
        return id;
    }

    public Movie getSeries() {
        return series;
    }

    public void setSeries(Movie series) {
        this.series = series;
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

    public Long getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(Long seriesId) {
        this.seriesId = seriesId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Episode episode = (Episode) o;
        return seriesId.equals(episode.seriesId) &&
                season.equals(episode.season) &&
                this.episode.equals(episode.episode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seriesId, season, episode);
    }
}
