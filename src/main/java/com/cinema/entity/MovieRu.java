package com.cinema.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "movie_ru")
public class MovieRu implements MovieInternalize {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "title", insertable = false, updatable = false)
    @NaturalId
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "movieRu", optional = false)
    private Movie movie;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "movie", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<CastRu> casts;

    public Integer getId() {
        return id;
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

    public String getTrailerThumbnail() {
        return trailerThumbnail;
    }

    public void setTrailerThumbnail(String trailerThumbnail) {
        this.trailerThumbnail = trailerThumbnail;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public List<CastRu> getCasts() {
        return casts;
    }

    public void setCasts(List<CastRu> casts) {
        this.casts = casts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovieRu movieRu = (MovieRu) o;
        return title.equals(movieRu.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
