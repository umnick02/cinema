package com.cinema.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "movie")
public class Movie implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "is_series")
    private Boolean isSeries;

    @Column(name = "release_date")
    private LocalDate releaseDate;

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

    @Column(name = "budget")
    private Long budget;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

    @Column(name = "gross")
    private Long gross;

    @Column(name = "posters", columnDefinition = "text")
    private String posters;

    @Column(name = "is_custom")
    private Boolean isCustom;

    @Column(name = "magnet", columnDefinition = "text")
    private String magnet;

    @Column(name = "company")
    private String company;

    @Column(name = "poster_thumbnail", columnDefinition = "varchar(511)")
    private String posterThumbnail;

    @Column(name = "poster", columnDefinition = "varchar(511)")
    private String poster;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Cast> casts;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Episode> episodes;

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

    public FileStatus getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public Long getId() {
        return id;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
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

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosterThumbnail() {
        return posterThumbnail;
    }

    public void setPosterThumbnail(String posterThumbnail) {
        this.posterThumbnail = posterThumbnail;
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

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
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
