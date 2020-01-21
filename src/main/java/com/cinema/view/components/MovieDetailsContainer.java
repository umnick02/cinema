package com.cinema.view.components;

import com.cinema.entity.Movie;
import com.cinema.presenter.PlayerPresentable;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;

import static com.cinema.CinemaApplication.INJECTOR;
import static com.cinema.config.Config.PrefKey.Language.RU;
import static com.cinema.config.Config.getLang;

public class MovieDetailsContainer {
    private Movie movie;

    private static final Font first = Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 22);
    private static final Font second = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 16);
    private static final String fontColor = "#ffffff";

    StackPane stackPane;
    private ImageView poster;
    private ImageView background;
    private HBox backgroundBox;
    private VBox detailsBox;

    public MovieDetailsContainer(Movie movie) {
        this.movie = movie;
        poster = buildPoster(movie.getPoster());
        HBox posterBox = buildImageBox(poster);
        JsonArray posters = Json.parse(movie.getPosters()).asArray();
        if (posters.size() > 0) {
            background = buildBackground(posters.get(0).asString());
            backgroundBox = buildBackgroundBox(posters, background);
        }
        stackPane = buildStackPane(posterBox, backgroundBox);
        detailsBox = buildDetailsBox(stackPane, movie, poster.getFitWidth());
        if (background != null) {
            ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                setBackgroundDimensions(background);
                setPosterDimensions(poster);
                detailsBox.setPrefWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - poster.getFitWidth());
            };
            INJECTOR.getInstance(RootContainer.class).getStackPane().widthProperty().addListener(stageSizeListener);
            INJECTOR.getInstance(RootContainer.class).getStackPane().heightProperty().addListener(stageSizeListener);
        }
        stackPane.getChildren().add(buildContainer(detailsBox));
    }

    private HBox buildContainer(Pane pane) {
        HBox hBox = new HBox();
        hBox.getChildren().add(pane);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        return hBox;
    }

    private HBox buildDescriptionBox(double margin) {
        HBox descriptionBox = new HBox();
        Text description = buildText(movie.getDescription(), second);
        description.setWrappingWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - margin - 20);
        descriptionBox.getChildren().add(description);
        return descriptionBox;
    }

    private HBox buildPlayBox() {
        HBox playBox = new HBox();
        Button playButton = new Button("Play");
        playButton.setOnMouseClicked(event -> INJECTOR.getInstance(PlayerPresentable.class).tryPlay(movie));
        playBox.getChildren().add(playButton);
        return playBox;
    }

    private HBox buildTrailerBox(StackPane stackPane, String url) {
        HBox trailerBox = new HBox();
        Button trailerButton = new Button("Trailer");
        trailerButton.setOnMouseClicked(event -> {
            WebView webview = new WebView();
            webview.getEngine().load(url);
            stackPane.getChildren().add(webview);
        });
        trailerBox.getChildren().add(trailerButton);
        return trailerBox;
    }

    private HBox buildAboutBox(Movie movie) {
        HBox aboutBox = new HBox();
        aboutBox.setSpacing(20);
        StringBuilder sb = new StringBuilder();
        if (movie.getGenre1() != null) {
            sb.append(movie.getGenre1());
        }
        if (movie.getGenre2() != null) {
            sb.append(" / ");
            sb.append(movie.getGenre2());
        }
        if (movie.getGenre3() != null) {
            sb.append(" / ");
            sb.append(movie.getGenre3());
        }
        aboutBox.getChildren().addAll(buildText(sb.toString(), second),
                buildText(movie.getReleaseDate().format(DateTimeFormatter.ofPattern("dd LLLL yyyy")), second),
                buildText(movie.getDuration() + " min", second)
        );
        if (movie.getRatingKp() != null) {
            aboutBox.getChildren().add(buildText(String.format("%.1f (%s) / %.1f (%s)",
                    movie.getRatingImdb(), intByGroup(movie.getRatingImdbVotes()), movie.getRatingKp(), intByGroup(movie.getRatingKpVotes())),
                    second));
        } else {
            aboutBox.getChildren().add(buildText(String.format("%.1f (%s)",
                    movie.getRatingImdb(), intByGroup(movie.getRatingImdbVotes())),  second));
        }
        return aboutBox;
    }

    private HBox buildTitleBox(String title) {
        HBox titleBox = new HBox();
        Text text = new Text(title);
        text.setFont(first);
        text.setFill(Paint.valueOf(fontColor));
        text.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK,8,0,2,2));
        titleBox.getChildren().add(text);
        return titleBox;
    }

    private HBox buildBackgroundBox(JsonArray posters, ImageView background) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        if (posters.size() > 0) {
            setBackgroundDimensions(background);
            FadeTransition fadeOut = new FadeTransition(Duration.millis(1000), background);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), background);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<>() {
                int i = 1;
                @Override
                public void handle(ActionEvent event) {
                    fadeOut.setOnFinished(fadeOutEvent -> {
                        Image poster = new Image(posters.get(i++ % posters.size()).asString());
                        background.setImage(poster);
                        setBackgroundDimensions(background);
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            }));
            fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
            fiveSecondsWonder.play();
            hBox.getChildren().add(background);
        }
        return hBox;
    }

    private HBox buildImageBox(ImageView imageView) {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().add(imageView);
        return hBox;
    }

    private StackPane buildStackPane(HBox posterBox, HBox backgroundBox) {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: black");
        stackPane.getChildren().add(buildDarkBackground());
        if (backgroundBox != null) stackPane.getChildren().add(backgroundBox);
        stackPane.getChildren().add(posterBox);
        return stackPane;
    }

    private Pane buildDarkBackground() {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2)");
        return pane;
    }

    private ImageView buildPoster(String url) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.9);
        imageView.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.BLACK,64,0,-16,0));
        setPosterDimensions(imageView);
        return imageView;
    }

    private ImageView buildBackground(String url) {
        Image image = new Image(url);
        ImageView imageView = new ImageView(image);
        setBackgroundDimensions(imageView);
        return imageView;
    }

    private VBox buildDetailsBox(StackPane stackPane, Movie movie, double margin) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30, 10, 30, 10));
        vBox.setSpacing(20);
        vBox.setPrefWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - margin);
        vBox.setMaxHeight(INJECTOR.getInstance(RootContainer.class).getStackPane().getHeight());
        vBox.getChildren().add(buildTitleBox(movie.getTitle()));
        vBox.getChildren().add(buildAboutBox(movie));
        vBox.getChildren().add(buildTrailerBox(stackPane,
                (getLang() == RU && movie.getTrailerRu() != null) ? movie.getTrailerRu() : movie.getTrailer())
        );
        vBox.getChildren().add(buildDescriptionBox(margin));
        if (!movie.getSeries()) {
            vBox.getChildren().add(buildPlayBox());
        } else {
            vBox.getChildren().add(new EpisodesContainer(movie.getEpisodes()).serialContainer);
        }
        return vBox;
    }

    public static String intByGroup(int value) {
        if (value >= 1000) {
            return String.format("%d %03d", value / 1000, value % 1000);
        } else {
            return Integer.toString(value);
        }
    }

    private Text buildText(String value, Font font) {
        Text text = new Text(value);
        text.setFont(font);
        text.setFill(Paint.valueOf(fontColor));
        return text;
    }

    private void setBackgroundDimensions(ImageView imageView) {
        double byWidth = INJECTOR.getInstance(RootContainer.class).getStackPane().getHeight() / imageView.getImage().getHeight() * imageView.getImage().getWidth();
        double byHeight = INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() / imageView.getImage().getWidth() * imageView.getImage().getHeight();
        if (byWidth > INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth()) {
            imageView.setFitHeight(byHeight);
            imageView.setFitWidth(imageView.getFitHeight() / imageView.getImage().getHeight() * imageView.getImage().getWidth());
        } else {
            imageView.setFitWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth());
            imageView.setFitHeight(imageView.getFitWidth() / imageView.getImage().getWidth() * imageView.getImage().getHeight());
        }
    }

    private void setPosterDimensions(ImageView imageView) {
        imageView.setFitHeight(INJECTOR.getInstance(RootContainer.class).getStackPane().getHeight());
        imageView.setFitWidth(imageView.getFitHeight() / imageView.getImage().getHeight() * imageView.getImage().getWidth());
    }
}
