package com.cinema.view.components;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.presenter.PlayerPresentable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;

import java.util.Objects;

import static com.cinema.CinemaApplication.INJECTOR;

public class CardContainer extends StackPane {

    private Movie movie;
    private VBox card;
    private BorderPane hover;
    private static final Font first = Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 14);
    private static final Font second = Font.font("Helvetica", FontWeight.NORMAL, FontPosture.REGULAR, 14);
    private static final Font third = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 13);

    public CardContainer(Movie movie) {
        super();
        card = new VBox();
        hover = buildHover(movie);

        setMovie(movie);
        getChildren().add(card);
    }

    private BorderPane buildHover(Movie movie) {
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-cursor: hand;");
        VBox vBox = new VBox();
        Text title = new Text(movie.getMovieDetails().getTitle());
        title.setWrappingWidth(Double.parseDouble(Config.getPreference(Config.PrefKey.CARD_WIDTH)) * 0.8);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(first);
        vBox.getChildren().add(title);
        Text year = new Text(Integer.toString(movie.getReleaseDate().getYear()));
        year.setFont(second);
        vBox.getChildren().add(year);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setSpacing(10);
        borderPane.setTop(vBox);
        Text rating = new Text(String.format("%.1f (%d)", movie.getRatingImdb(), movie.getRatingImdbVotes()));
        rating.setFont(third);
        rating.setTextAlignment(TextAlignment.CENTER);
        rating.setWrappingWidth(Double.parseDouble(Config.getPreference(Config.PrefKey.CARD_WIDTH)));
        borderPane.setPadding(new Insets(10, 0, 10, 0));
        borderPane.setBottom(rating);
        return borderPane;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        setupCard();
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> INJECTOR.getInstance(PlayerPresentable.class).tryPlay(movie));
        addEventHandler(MouseEvent.MOUSE_ENTERED, event -> showOnHover());
        addEventHandler(MouseEvent.MOUSE_EXITED, event -> hideOnHover());
    }

    private void showOnHover() {
        getChildren().add(hover);
    }

    private void hideOnHover() {
        getChildren().remove(hover);
    }

    public Movie getMovie() {
        return movie;
    }

    public void setupCard() {
        setDimensions();
        setBackground();
    }

    private void setBackground() {
        int width = Integer.parseInt(Config.getPreference(Config.PrefKey.CARD_WIDTH));
        String url = movie.getMovieRu() != null ? movie.getMovieRu().getPosterThumbnail() : movie.getMovieEn().getPosterThumbnail();
        Image image = new Image(url, width, -1, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        card.setBackground(new Background(backgroundImage));
    }

    private void setDimensions() {
        int width = Integer.parseInt(Config.getPreference(Config.PrefKey.CARD_WIDTH));
        int height = (int) (width * 1.4);
        card.setMinHeight(height);
        card.setMaxHeight(height);
        card.setMaxWidth(width);
        card.setMinWidth(width);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardContainer that = (CardContainer) o;
        return movie.equals(that.movie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie);
    }
}
