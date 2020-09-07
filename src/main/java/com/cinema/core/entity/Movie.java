package com.cinema.core.entity;

import com.cinema.core.dto.Award;
import com.cinema.core.dto.Cast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.cinema.core.utils.StringUtils.longToString;

@Entity
@Table(name = "movie")
public class Movie implements Source {

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
    private boolean isCustom;

    @Column(name = "poster", columnDefinition = "varchar(511)")
    private String poster;

    @Column(name = "casts", columnDefinition = "text")
    private String casts;

    @Column(name = "award", columnDefinition = "text")
    private String award;

    @CreationTimestamp
    private LocalDateTime created;

    @UpdateTimestamp
    private LocalDateTime updated;

    @OneToMany(mappedBy = "series", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Episode> episodes;

    @Column(name = "title", updatable = false, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "country_1")
    private String country1;

    @Column(name = "country_2")
    private String country2;

    @Column(name = "country_3")
    private String country3;

    @Column(name = "genre_1")
    private String genre1;

    @Column(name = "genre_2")
    private String genre2;

    @Column(name = "genre_3")
    private String genre3;

    @Column(name = "trailer", columnDefinition = "varchar(511)")
    private String trailer;

    @Embedded
    private Magnet magnet;

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

    public Magnet getMagnet() {
        return magnet;
    }

    public void setMagnet(Magnet magnet) {
        this.magnet = magnet;
    }

    @Override
    public String getHash() {
        return magnet.getHash();
    }

    @Override
    public void setHash(String hash) {
        magnet.setHash(hash);
    }

    @Override
    public String getFile() {
        return magnet.getFile();
    }

    @Override
    public void setFile(String file) {
        magnet.setFile(file);
    }

    @Override
    public void setFileSize(Long fileSize) {
        magnet.setFileSize(fileSize);
    }

    @Override
    public Long getFileSize() {
        return magnet.getFileSize();
    }

    @Override
    public Set<Magnet.Subtitle> getSubtitles() {
        return magnet.getSubtitles();
    }

    @Override
    public void setSubtitles(Set<Magnet.Subtitle> subtitles) {
        magnet.setSubtitles(subtitles);
    }

    public Award getAward() {
        return new Gson().fromJson(this.award, Award.class);
    }

    public void setAward(Award award) {
        this.award = new Gson().toJson(award);
    }

    public short getMaxSeason() {
        return episodes.stream().map(Episode::getSeason).max(Short::compareTo).orElseGet(() -> (short) 0);
    }

    public String fetchTitle() {
        if (type != Movie.Type.SERIES) {
            return String.format("%s (%d)", title, releaseDate.getYear());
        } else if (finishDate == null) {
            return String.format("%s (%d — %s)", title, releaseDate.getYear(),  ". . .");
        } else {
            return String.format("%s (%d — %d)", title, releaseDate.getYear(),  finishDate.getYear());
        }
    }

    public String fetchType() {
        if (type != Movie.Type.SERIES) {
            return type.toString();
        }
        return episodes.stream()
                .max(Comparator.comparing(Episode::getSeason))
                .map(value -> String.format("Сериал (%d сезонов)", value.getSeason()))
                .orElse(null);
    }

    public String fetchGenres() {
        StringBuilder sb = new StringBuilder();
        if (genre1 != null) {
            sb.append(genre1);
        }
        if (genre2 != null) {
            sb.append(" / ");
            sb.append(genre2);
        }
        if (genre3 != null) {
            sb.append(" / ");
            sb.append(genre3);
        }
        return "Жанры: " + sb.toString();
    }

    public String fetchCountry() {
        StringBuilder sb = new StringBuilder();
        if (country1 != null) {
            sb.append(country1);
        }
        if (country2 != null) {
            sb.append(" / ");
            sb.append(country2);
        }
        if (country3 != null) {
            sb.append(" / ");
            sb.append(country3);
        }
        return "Страна: " + sb;
    }

    public String fetchDuration() {
        if (duration >= 60) {
            return String.format("Длительность: %dч %dм", duration / 60, duration % 60);
        } else {
            return String.format("Длительность: %dм", duration);
        }
    }

    public String fetchRating() {
        return String.format("IMDB %.1f (%s) / KP %.1f (%s)",
                ratingImdb, longToString(ratingImdbVotes), ratingKp, longToString(ratingKpVotes));
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

    public List<Cast> getCasts() {
        return new Gson().fromJson(this.casts, new TypeToken<List<Cast>>() {}.getType());
    }

    public void addCasts(Cast... casts) {
        Gson gson = new Gson();
        @SuppressWarnings("unchecked")
        List<Cast> castList = this.casts != null ? (List<Cast>) gson.fromJson(this.casts, List.class) : new ArrayList<>();
        castList.addAll(Arrays.asList(casts));
        this.casts = gson.toJson(castList);
    }

    public void setCasts(List<Cast> casts) {
        this.casts = new Gson().toJson(casts);
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountry1() {
        return country1;
    }

    public void setCountry1(String country1) {
        this.country1 = country1;
    }

    public String getCountry2() {
        return country2;
    }

    public void setCountry2(String country2) {
        this.country2 = country2;
    }

    public String getCountry3() {
        return country3;
    }

    public void setCountry3(String country3) {
        this.country3 = country3;
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

    @Override
    public String toString() {
        return getTitle();
    }
}
