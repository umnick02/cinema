package com.cinema.javafx.controller.movies;

import com.cinema.core.model.ModelEventType;
import com.cinema.core.model.impl.MovieModel;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovieController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class);

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
    @FXML
    private TabPane tabPane;

    public MovieController(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    @FXML
    public void initialize() {
        if (movieModel.isSeries()) {
            seasonsTab.getStyleClass().remove("hide");
            tabPane.addEventHandler(ModelEventType.SEASONS_SHOW.getEventType(), event -> {
                logger.info("Handle event {} from source {} on target {}", event.getEventType(), event.getSource(), event.getTarget());
                tabPane.getSelectionModel().select(seasonsTab);
            });
        }
        moviePane.setMinSize(width * ratio, width * 1.4);
        moviePane.setMaxSize(width * ratio, width * 1.4);

        tabsPane.setMinSize(moviePane.getMinWidth() - width, moviePane.getMinHeight());
        tabsPane.setMaxSize(moviePane.getMaxWidth() - width, moviePane.getMaxHeight());

        posterPane.setMinSize(width, moviePane.getMinHeight());
        posterPane.setMaxSize(width, moviePane.getMaxHeight());
        Image image = new Image(movieModel.getMovie().getPoster(), width, -1, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        posterPane.setBackground(new Background(backgroundImage));
    }
}
