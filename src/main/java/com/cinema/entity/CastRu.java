package com.cinema.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cast_ru")
public class CastRu {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "priority", nullable = false)
    private Short priority;

    @Column(name = "qua")
    private String qua;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    private MovieRu movie;

    private enum Role {
        CREATOR, DIRECTOR, ACTOR
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public MovieRu getMovie() {
        return movie;
    }

    public void setMovie(MovieRu movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CastRu castRu = (CastRu) o;
        return firstName.equals(castRu.firstName) &&
                Objects.equals(lastName, castRu.lastName) &&
                priority.equals(castRu.priority) &&
                role == castRu.role &&
                movie.equals(castRu.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, priority, role, movie);
    }
}
