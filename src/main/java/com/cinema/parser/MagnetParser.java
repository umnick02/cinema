package com.cinema.parser;

import com.cinema.entity.Movie;
import com.cinema.entity.MovieEn;

public class MagnetParser {

    public Movie parse(String magnet) throws Exception {
        Movie movie = new Movie();
        movie.setCustom(false);
        movie.setMagnet(magnet);
        movie.setMovieEn(new MovieEn());
        new ImdbParser("https://www.imdb.com/title/tt0903747/?ref_=nv_sr_srsg_0", movie).call();
        return movie;
    }
}
