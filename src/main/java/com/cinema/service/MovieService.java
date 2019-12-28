package com.cinema.service;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.repository.CastEnRepository;
import com.cinema.repository.MovieEnRepository;
import com.cinema.repository.MovieRepository;
import com.cinema.repository.MovieRuRepository;
import com.cinema.ui.components.SideMenuContainer;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final CastEnRepository castEnRepository;
    private final MovieEnRepository movieEnRepository;
    private final MovieRuRepository movieRuRepository;

    public MovieService(MovieRepository movieRepository, CastEnRepository castEnRepository,
                        MovieEnRepository movieEnRepository, MovieRuRepository movieRuRepository) {
        this.movieRepository = movieRepository;
        this.castEnRepository = castEnRepository;
        this.movieEnRepository = movieEnRepository;
        this.movieRuRepository = movieRuRepository;
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

    public Set<Movie> getMovies(Pageable pageable, Config.PrefKey.Language language) {
        return new HashSet<>(language == Config.PrefKey.Language.RU ? movieRepository.findMoviesRu(pageable) : movieRepository.findMoviesEn(pageable));
    }

    public Set<SideMenuContainer.GenreItem> getGenres(Config.PrefKey.Language language) {
        List<String[]> genreList = language == Config.PrefKey.Language.RU ? movieRuRepository.findAllGenres() : movieEnRepository.findAllGenres();
        Set<SideMenuContainer.GenreItem> genres = new HashSet<>();
        for (String[] genre : genreList) {
            genres.add(new SideMenuContainer.GenreItem(genre[0], Integer.parseInt(genre[1])));
        }
        return genres;
    }
}
