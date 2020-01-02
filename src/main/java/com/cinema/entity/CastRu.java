package com.cinema.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cast_ru")
public class CastRu {

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
    private com.cinema.entity.Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private MovieRu movie;

    private enum Role {
        CREATOR, DIRECTOR, ACTOR
    }

    public Integer getId() {
        return id;
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

    public MovieRu getMovie() {
        return movie;
    }

    public void setMovie(MovieRu movie) {
        this.movie = movie;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.cinema.entity.Role getRole() {
        return role;
    }

    public void setRole(com.cinema.entity.Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastRu castRu = (CastRu) o;
        return name.equals(castRu.name) &&
                priority.equals(castRu.priority) &&
                role == castRu.role &&
                movie.equals(castRu.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, priority, role, movie);
    }
}
