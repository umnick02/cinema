package com.cinema.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "movie_en")
public class MovieEn {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "title", updatable = false)
    @NaturalId(mutable = true)
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "url")
    private String url;

    @Column(name = "country")
    private String country;

    @Column(name = "company")
    private String company;

    @Column(name = "genre_1")
    private String genre1;

    @Column(name = "genre_2")
    private String genre2;

    @Column(name = "genre_3")
    private String genre3;

    @Column(name = "poster_thumbnail", columnDefinition = "varchar(511)")
    private String posterThumbnail;

    @Column(name = "poster", columnDefinition = "varchar(511)")
    private String poster;

    @Column(name = "trailer", columnDefinition = "varchar(511)")
    private String trailer;

    @Column(name = "trailer_thumbnail", columnDefinition = "varchar(511)")
    private String trailerThumbnail;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "movieEn", optional = false)
    private Movie movie;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
    private List<CastEn> casts;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPosterThumbnail() {
        return posterThumbnail;
    }

    public void setPosterThumbnail(String posterThumbnail) {
        this.posterThumbnail = posterThumbnail;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getTrailerThumbnail() {
        return trailerThumbnail;
    }

    public void setTrailerThumbnail(String trailerThumbnail) {
        this.trailerThumbnail = trailerThumbnail;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<CastEn> getCasts() {
        return casts;
    }

    public void setCasts(List<CastEn> casts) {
        this.casts = casts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieEn movieEn = (MovieEn) o;
        return title.equals(movieEn.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
