package com.cinema.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    public Movie() {}
    public Movie(Long id) {
        this.id = id;
    }
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "is_series")
    private Boolean isSeries;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "last_season_date")
    private LocalDate lastSeasonDate;

    @Column(name = "rating_imdb")
    private Float ratingImdb;

    @Column(name = "rating_imdb_votes")
    private Integer ratingImdbVotes;

    @Column(name = "rating_kp")
    private Float ratingKp;

    @Column(name = "rating_kp_votes")
    private Integer ratingKpVotes;

    @Column(name = "duration")
    private Short duration;

    @Column(name = "budget")
    private Long budget;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "gross")
    private Long gross;

    @Column(name = "posters", columnDefinition = "text")
    private String posters;

    @Column(name = "seasons")
    private Short seasons;

    @Column(name = "is_custom")
    private Boolean isCustom;

    @Column(name = "magnet", nullable = false, columnDefinition = "text")
    private String magnet;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToMany(mappedBy = "series", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Episode> episodes;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private MovieEn movieEn;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private MovieRu movieRu;

    @Column(name = "file", columnDefinition = "text")
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getId() {
        return id;
    }

    public Boolean getSeries() {
        return isSeries;
    }

    public void setSeries(Boolean series) {
        isSeries = series;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public LocalDate getLastSeasonDate() {
        return lastSeasonDate;
    }

    public void setLastSeasonDate(LocalDate lastSeasonDate) {
        this.lastSeasonDate = lastSeasonDate;
    }

    public Float getRatingImdb() {
        return ratingImdb;
    }

    public void setRatingImdb(Float ratingImdb) {
        this.ratingImdb = ratingImdb;
    }

    public Float getRatingKp() {
        return ratingKp;
    }

    public void setRatingKp(Float ratingKp) {
        this.ratingKp = ratingKp;
    }

    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Long getGross() {
        return gross;
    }

    public void setGross(Long gross) {
        this.gross = gross;
    }

    public String getPosters() {
        return posters;
    }

    public void setPosters(String posters) {
        this.posters = posters;
    }

    public Short getSeasons() {
        return seasons;
    }

    public void setSeasons(Short seasons) {
        this.seasons = seasons;
    }

    public Boolean getCustom() {
        return isCustom;
    }

    public void setCustom(Boolean custom) {
        isCustom = custom;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }

    public MovieEn getMovieEn() {
        return movieEn;
    }

    public void setMovieEn(MovieEn movieEn) {
        this.movieEn = movieEn;
    }

    public MovieRu getMovieRu() {
        return movieRu;
    }

    public void setMovieRu(MovieRu movieRu) {
        this.movieRu = movieRu;
    }

    public Integer getRatingImdbVotes() {
        return ratingImdbVotes;
    }

    public void setRatingImdbVotes(Integer ratingImdbVotes) {
        this.ratingImdbVotes = ratingImdbVotes;
    }

    public Integer getRatingKpVotes() {
        return ratingKpVotes;
    }

    public void setRatingKpVotes(Integer ratingKpVotes) {
        this.ratingKpVotes = ratingKpVotes;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return movieEn.equals(movie.movieEn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieEn);
    }

    @Override
    public String toString() {
        return movieRu != null ? movieRu.getTitle() : movieEn.getTitle();
    }
}
