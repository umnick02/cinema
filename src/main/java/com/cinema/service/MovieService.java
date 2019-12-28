package com.cinema.service;

import com.cinema.entity.Movie;
import com.cinema.repository.CastEnRepository;
import com.cinema.repository.MovieEnRepository;
import com.cinema.repository.MovieRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final CastEnRepository castEnRepository;
    private final MovieEnRepository movieEnRepository;

    public MovieService(MovieRepository movieRepository, CastEnRepository castEnRepository, MovieEnRepository movieEnRepository) {
        this.movieRepository = movieRepository;
        this.castEnRepository = castEnRepository;
        this.movieEnRepository = movieEnRepository;
    }

    @Transactional
    public Movie saveMovie(Movie movie) {
        movieEnRepository.save(movie.getMovieEn());
        castEnRepository.saveAll(movie.getMovieEn().getCasts());
        movieRepository.save(movie);
        return movie;
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public Set<Movie> getMovies(Pageable pageable, Language language) {
        return new HashSet<>(language == Language.RU ? movieRepository.findMoviesRu(pageable) : movieRepository.findMoviesEn(pageable));
    }
}
