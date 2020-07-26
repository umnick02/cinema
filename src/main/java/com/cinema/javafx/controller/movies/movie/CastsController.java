package com.cinema.javafx.controller.movies.movie;

import com.cinema.core.dto.Cast;
import com.cinema.core.entity.Movie;
import com.cinema.javafx.controller.movies.movie.casts.CastController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CastsController {

    private final Movie movie;

    @FXML
    public VBox castsPane;

    @FXML
    public Label castDirector;

    @FXML
    public Label castWriter;

    @FXML
    public Label castComposer;

    public CastsController(Movie movie) {
        this.movie = movie;
    }

    @FXML
    public void initialize() {
        List<Cast> casts = movie.getCasts();
        castDirector.setText("Режиссёр: " +
                casts.stream().filter(c -> c.getRole() == Cast.Role.DIRECTOR).map(Cast::getName).collect(Collectors.joining(", ")));
        castWriter.setText("Сценарист: " +
                casts.stream().filter(c -> c.getRole() == Cast.Role.WRITER).map(Cast::getName).collect(Collectors.joining(", ")));
        castComposer.setText("Композитор: " +
                casts.stream().filter(c -> c.getRole() == Cast.Role.COMPOSER).map(Cast::getName).collect(Collectors.joining(", ")));
        casts.stream()
                .filter(c -> c.getRole() == Cast.Role.ACTOR)
                .sorted(Comparator.comparing(Cast::getPriority))
                .forEach(c -> {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/movie/cast/cast.fxml"));
                    loader.setControllerFactory(param -> {
                        if (param.isAssignableFrom(CastController.class)) {
                            new CastController(c.getName());
                        }
                        return null;
                    });
                    try {
                        castsPane.getChildren().add(loader.load());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
