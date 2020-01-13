package com.cinema.view.components;

import com.cinema.config.Config;
import com.cinema.entity.Movie;
import com.cinema.presenter.PlayerPresentable;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.util.Objects;

import static com.cinema.CinemaApplication.INJECTOR;

public class CardContainer extends VBox {

    private Movie movie;

    public CardContainer(Movie movie) {
        super();
        setMovie(movie);
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
        setupCard();
        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> INJECTOR.getInstance(PlayerPresentable.class).tryPlay(movie));
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
        setBackground(new Background(backgroundImage));
    }

    private void setDimensions() {
        int width = Integer.parseInt(Config.getPreference(Config.PrefKey.CARD_WIDTH));
        int height = (int) (width * 1.4);
        setMinHeight(height);
        setMaxHeight(height);
        setMaxWidth(width);
        setMinWidth(width);
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
