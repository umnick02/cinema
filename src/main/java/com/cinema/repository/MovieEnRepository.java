package com.cinema.repository;

import com.cinema.entity.MovieEn;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieEnRepository extends CrudRepository<MovieEn, Long> {
    @Query(value = "select genres.genre, cast(sum(genres.cnt) as int) as CNT from" +
            " (select GENRE_1 as genre, count(GENRE_1) as cnt from MOVIE_EN where GENRE_1 is not null group by GENRE_1" +
            " union select GENRE_2, count(GENRE_2) from MOVIE_EN where GENRE_2 is not null group by GENRE_2" +
            " union select GENRE_3, count(GENRE_3) from MOVIE_EN where GENRE_3 is not null group by GENRE_3) as genres" +
            " group by genres.genre order by CNT desc, GENRE asc", nativeQuery = true)
    List<String[]> findAllGenres();
}
