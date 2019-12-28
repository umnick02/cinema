package com.cinema.repository;

import com.cinema.entity.CastEn;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastEnRepository extends CrudRepository<CastEn, Long> {
}
