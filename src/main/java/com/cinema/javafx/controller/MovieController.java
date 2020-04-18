package com.cinema.javafx.controller;

import com.cinema.core.model.impl.MovieModel;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class MovieController {

    public static final Double width = 1920/9-5d;
    public static final Double ratio = 3d;

    private final MovieModel movieModel;

    @FXML
    private Tab seasonsTab;
    @FXML
    private HBox moviePane;
    @FXML
    private StackPane posterPane;
    @FXML
    private AnchorPane tabsPane;

    public MovieController(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    public void initialize() {
        if (movieModel.isSeries()) {
            seasonsTab.getStyleClass().remove("hide");
        }
        moviePane.setMinSize(width * ratio, width * 1.4);
        moviePane.setMaxSize(width * ratio, width * 1.4);
        posterPane.setMinSize(width, moviePane.getMinHeight());
        posterPane.setMaxSize(width, moviePane.getMaxHeight());
        tabsPane.setMinSize(moviePane.getMinWidth() - width, moviePane.getMinHeight());
        tabsPane.setMaxSize(moviePane.getMaxWidth() - width, moviePane.getMaxHeight());
        Image image = new Image(movieModel.getMovie().getPoster(), width, -1, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        posterPane.setBackground(new Background(backgroundImage));
    }
}
