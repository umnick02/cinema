package com.cinema.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "finish_date")
    private LocalDate finishDate;

    @Column(name = "original_title", updatable = false, nullable = false)
    @NaturalId(mutable = true)
    private String originalTitle;

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

    @Column(name = "posters", columnDefinition = "text")
    private String posters;

    @Column(name = "is_custom")
    private Boolean isCustom;

    @Column(name = "magnet", columnDefinition = "text")
    private String magnet;

    @Column(name = "poster", columnDefinition = "varchar(511)")
    private String poster;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Cast> casts;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Episode> episodes;

    @Column(name = "title", updatable = false, nullable = false)
    private String title;

    @Column(name = "title_ru", updatable = false, nullable = false)
    private String titleRu;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "description_ru", columnDefinition = "text")
    private String descriptionRu;

    @Column(name = "url")
    private String url;

    @Column(name = "country")
    private String country;

    @Column(name = "genre_1")
    private String genre1;

    @Column(name = "genre_2")
    private String genre2;

    @Column(name = "genre_3")
    private String genre3;

    @Column(name = "trailer", columnDefinition = "varchar(511)")
    private String trailer;

    @Column(name = "trailer_ru", columnDefinition = "varchar(511)")
    private String trailerRu;

    @Column(name = "file", columnDefinition = "text")
    private String file;

    @Enumerated(EnumType.STRING)
    @Column(name = "file_status")
    private FileStatus fileStatus;

    public enum FileStatus {
        UNPLAYABLE, PLAYABLE, DOWNLOADED;
        public static boolean canPlay(FileStatus status) {
            return status == PLAYABLE || status == DOWNLOADED;
        }
    }

    public enum Type {
        SERIES("Сериал"), MOVIE("Фильм"), CARTOON("Мультфильм");

        private String value;

        Type(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public Set<Cast> getCasts() {
        return casts;
    }

    public void setCasts(Set<Cast> casts) {
        this.casts = casts;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Float getRatingImdb() {
        return ratingImdb;
    }

    public void setRatingImdb(Float ratingImdb) {
        this.ratingImdb = ratingImdb;
    }

    public Integer getRatingImdbVotes() {
        return ratingImdbVotes;
    }

    public void setRatingImdbVotes(Integer ratingImdbVotes) {
        this.ratingImdbVotes = ratingImdbVotes;
    }

    public Float getRatingKp() {
        return ratingKp;
    }

    public void setRatingKp(Float ratingKp) {
        this.ratingKp = ratingKp;
    }

    public Integer getRatingKpVotes() {
        return ratingKpVotes;
    }

    public void setRatingKpVotes(Integer ratingKpVotes) {
        this.ratingKpVotes = ratingKpVotes;
    }

    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    public String getPosters() {
        return posters;
    }

    public void setPosters(String posters) {
        this.posters = posters;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
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

    public Set<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(Set<Episode> episodes) {
        this.episodes = episodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleRu() {
        return titleRu;
    }

    public void setTitleRu(String titleRu) {
        this.titleRu = titleRu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionRu() {
        return descriptionRu;
    }

    public void setDescriptionRu(String descriptionRu) {
        this.descriptionRu = descriptionRu;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGenre1() {
        return genre1;
    }

    public void setGenre1(String genre1) {
        this.genre1 = genre1;
    }

    public String getGenre2() {
        return genre2;
    }

    public void setGenre2(String genre2) {
        this.genre2 = genre2;
    }

    public String getGenre3() {
        return genre3;
    }

    public void setGenre3(String genre3) {
        this.genre3 = genre3;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getTrailerRu() {
        return trailerRu;
    }

    public void setTrailerRu(String trailerRu) {
        this.trailerRu = trailerRu;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
