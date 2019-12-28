package com.cinema.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cast_en")
public class CastEn {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "priority", nullable = false)
    private Short priority;

    @Column(name = "qua")
    private String qua;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private MovieEn movie;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getPriority() {
        return priority;
    }

    public void setPriority(Short priority) {
        this.priority = priority;
    }

    public String getQua() {
        return qua;
    }

    public void setQua(String qua) {
        this.qua = qua;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public MovieEn getMovie() {
        return movie;
    }

    public void setMovie(MovieEn movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastEn castEn = (CastEn) o;
        return name.equals(castEn.name) &&
                priority.equals(castEn.priority) &&
                role == castEn.role &&
                movie.equals(castEn.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority, role, movie);
    }
}
