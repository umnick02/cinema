//package com.cinema.javafx.view.components;
//
//import com.cinema.core.entity.Movie;
//import com.cinema.javafx.controller.MoviesController;
//import com.cinema.javafx.view.builder.MovieBuilder;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.TilePane;
//
//import java.io.IOException;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class MoviesContainer {
//
//    private TilePane container;
//    private StackPane contentPane;
//    private final Set<MovieBuilder> movies = new HashSet<>();
//
//    public MoviesContainer(MoviesController moviesController, TilePane container, StackPane contentPane) {
//        this.container = container;
//        this.contentPane = contentPane;
//    }
//
//    public void addMovies(Set<Movie> movies) {
//        Set<Movie> oldMovies = movies.stream().map(MovieBuilder::getMovie).collect(Collectors.toSet());
//        for (Movie movie : movies) {
//            if (!oldMovies.contains(movie)) {
//                try {
//                    MovieBuilder movieBuilder = new MovieBuilder(contentPane, movie);
//                    movies.add(movieBuilder);
//                    container.getChildren().add(movieBuilder.getMovie());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public void resetMovies(Set<Movie> movies) {
//        container.getChildren().clear();
//        movies.clear();
//        addMovies(movies);
//    }
//}
