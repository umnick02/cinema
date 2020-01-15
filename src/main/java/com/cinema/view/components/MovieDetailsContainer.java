package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import javafx.beans.value.ChangeListener;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;

import static com.cinema.CinemaApplication.INJECTOR;


public class MovieDetailsContainer extends StackPane {
    private Movie movie;

    public MovieDetailsContainer(Movie movie) {
        super();
        this.movie = movie;

        setStyle("-fx-background-color: black");
        JsonArray posters = Json.parse(movie.getPosters()).asArray();
        Image poster = new Image(posters.get(0).asString());
        ImageView posterView = new ImageView(poster);
        setViewDimensions(posterView, poster);
        BorderPane posterBorder = new BorderPane();
        posterBorder.setRight(posterView);
        getChildren().add(posterBorder);

        Image image = new Image(movie.getMovieDetails().getPoster());
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.9);
        imageView.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.BLACK,64,0,-16,0));
        setViewDimensions(imageView, image);
        BorderPane imageBorder = new BorderPane();
        imageBorder.setLeft(imageView);
        getChildren().add(imageBorder);

        BorderPane darkPane = new BorderPane();
        darkPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2)");
        getChildren().add(darkPane);

        ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
            setViewDimensions(imageView, image);
            setViewDimensions(posterView, poster);
        };
        INJECTOR.getInstance(RootContainer.class).widthProperty().addListener(stageSizeListener);
        INJECTOR.getInstance(RootContainer.class).heightProperty().addListener(stageSizeListener);
    }

    private void setViewDimensions(ImageView imageView, Image image) {
        imageView.setFitHeight(INJECTOR.getInstance(RootContainer.class).getHeight());
        imageView.setFitWidth(imageView.getFitHeight() / image.getHeight() * image.getWidth());
    }
}
