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

public class MovieDetailsContainer extends StackPane {
    private Movie movie;

    private static final Font first = Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 22);
    private static final Font second = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 16);
    private static final String fontColor = "#ffffff";
    public MovieDetailsContainer(Movie movie) {
        super();
        this.movie = movie;

        setStyle("-fx-background-color: black");

        VBox detailsBox = new VBox();

        Image image = new Image(movie.getPoster());
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.9);
        imageView.setEffect(new InnerShadow(BlurType.THREE_PASS_BOX, Color.BLACK,64,0,-16,0));
        setViewDimensions(imageView);
        HBox imageBorder = new HBox();
        imageBorder.setAlignment(Pos.CENTER_LEFT);
        imageBorder.getChildren().add(imageView);

        HBox posterBorder = new HBox();
        posterBorder.setAlignment(Pos.CENTER_RIGHT);
        JsonArray posters = Json.parse(movie.getPosters()).asArray();
        if (posters.size() > 0) {
            ImageView posterView = new ImageView(new Image(posters.get(0).asString()));
            setPosterDimensions(posterView);
            FadeTransition fadeOut = new FadeTransition(Duration.millis(1500), posterView);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1500), posterView);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(10), new EventHandler<>() {
                int i = 1;

                @Override
                public void handle(ActionEvent event) {
                    fadeOut.setOnFinished(fadeOutEvent -> {
                        Image poster = new Image(posters.get(i++ % posters.size()).asString());
                        posterView.setImage(poster);
                        setPosterDimensions(posterView);
                        fadeIn.play();
                    });
                    fadeOut.play();
                }
            }));
            fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
            fiveSecondsWonder.play();
            posterBorder.getChildren().add(posterView);
            ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                setViewDimensions(imageView);
                setPosterDimensions(posterView);
                detailsBox.setPrefWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - imageView.getFitWidth());
            };
            INJECTOR.getInstance(RootContainer.class).getStackPane().widthProperty().addListener(stageSizeListener);
            INJECTOR.getInstance(RootContainer.class).getStackPane().heightProperty().addListener(stageSizeListener);
        }
        getChildren().add(posterBorder);
        getChildren().add(imageBorder);


        Pane darkPane = new Pane();
        darkPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.2)");
        getChildren().add(darkPane);


        detailsBox.setPadding(new Insets(30, 10, 30, 10));
        detailsBox.setSpacing(20);
        detailsBox.setPrefWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - imageView.getFitWidth());
        detailsBox.setMaxHeight(INJECTOR.getInstance(RootContainer.class).getStackPane().getHeight());

        HBox titleBox = new HBox();
        Text title = new Text(movie.getMovieDetails().getTitle());
        title.setFont(first);
        title.setFill(Paint.valueOf(fontColor));
        title.setEffect(new DropShadow(BlurType.THREE_PASS_BOX, Color.BLACK,8,0,2,2));
        titleBox.getChildren().add(title);
        detailsBox.getChildren().add(titleBox);

        HBox aboutBox = new HBox();
        aboutBox.setSpacing(20);
        StringBuilder sb = new StringBuilder();
        if (movie.getMovieDetails().getGenre1() != null) {
            sb.append(movie.getMovieDetails().getGenre1());
        }
        if (movie.getMovieDetails().getGenre2() != null) {
            sb.append(" / ");
            sb.append(movie.getMovieDetails().getGenre2());
        }
        if (movie.getMovieDetails().getGenre3() != null) {
            sb.append(" / ");
            sb.append(movie.getMovieDetails().getGenre3());
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
        detailsBox.getChildren().add(aboutBox);

        HBox trailerBox = new HBox();
        Button trailerButton = new Button("Trailer");
        trailerButton.setOnMouseClicked(event -> {
            WebView webview = new WebView();
            webview.getEngine().load(movie.getMovieDetails().getTrailer());
            getChildren().add(webview);
        });
        trailerBox.getChildren().add(trailerButton);
        detailsBox.getChildren().add(trailerBox);

        HBox descriptionBox = new HBox();
        Text descr = buildText(movie.getMovieDetails().getDescription(), second);
        descr.setWrappingWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() -imageView.getFitWidth() - 20);
        descriptionBox.getChildren().add(descr);
        detailsBox.getChildren().add(descriptionBox);

        HBox playBox = new HBox();
        Button playButton = new Button("Play");
        playButton.setOnMouseClicked(event -> {
            INJECTOR.getInstance(PlayerPresentable.class).tryPlay(movie);
        });
        playBox.getChildren().add(playButton);
        detailsBox.getChildren().add(playBox);

        HBox anchorPane = new HBox();
        anchorPane.getChildren().add(detailsBox);
        anchorPane.setAlignment(Pos.CENTER_RIGHT);
        getChildren().add(anchorPane);
    }

    private String intByGroup(int value) {
        if (value >= 1000) {
            return String.format("%d %d", value / 1000, value % 1000);
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

    private void setPosterDimensions(ImageView imageView) {
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

    private void setViewDimensions(ImageView imageView) {
        imageView.setFitHeight(INJECTOR.getInstance(RootContainer.class).getStackPane().getHeight());
        imageView.setFitWidth(imageView.getFitHeight() / imageView.getImage().getHeight() * imageView.getImage().getWidth());
    }
}
