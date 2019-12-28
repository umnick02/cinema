package com.cinema.repository;

import com.cinema.entity.Movie;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {
    @Query("from Movie where ratingImdbVotes > 1000 order by ratingImdb desc ")
    @EntityGraph(attributePaths = {"movieEn"})
    List<Movie> findMoviesEn(Pageable pageable);

    @Query("from Movie where ratingImdbVotes > 1000 order by ratingImdb desc ")
    @EntityGraph(attributePaths = {"movieRu"})
    List<Movie> findMoviesRu(Pageable pageable);
}
