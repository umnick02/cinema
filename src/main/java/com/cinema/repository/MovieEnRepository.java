package com.cinema.repository;

import com.cinema.entity.MovieEn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieEnRepository extends CrudRepository<MovieEn, Long> {
}
