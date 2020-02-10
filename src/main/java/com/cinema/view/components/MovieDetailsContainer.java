package com.cinema.view.components;

import com.cinema.entity.Cast;
import com.cinema.entity.Movie;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.cinema.CinemaApplication.INJECTOR;
import static com.cinema.config.Config.PrefKey.Language.RU;
import static com.cinema.config.Config.getLang;
import static com.cinema.view.components.RootContainer.buildAnchorPane;

public class MovieDetailsContainer {
    private Movie movie;

    private static final Font first = Font.font("Helvetica", FontWeight.BOLD, FontPosture.ITALIC, 22);
    private static final Font second = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 16);
    private static final Font third = Font.font("Helvetica", FontWeight.BOLD, FontPosture.REGULAR, 14);
    private static final String fontColor = "#ffffff";

    AnchorPane anchorPane;
    private StackPane stackPane;
    private PlayerContainer playerContainer;
    private ImageView poster;
    private ImageView background;
    private HBox backgroundBox;
    private VBox detailsBox;
    private WebView webView;

    private Stage stage;

    public MovieDetailsContainer(Movie movie) {
        this.movie = movie;
        stackPane = buildStackPane();
        anchorPane = buildAnchorPane(stackPane);
        detailsBox = buildDetailsBox(stackPane, movie);
        HBox hBox = buildContainer(detailsBox);
        stackPane.getChildren().add(hBox);
        Platform.runLater(() -> {
            poster = buildPoster(movie.getPoster());
            HBox posterBox = buildImageBox(poster);
            JsonArray posters = Json.parse(movie.getPosters()).asArray();
            if (posters.size() > 0) {
                background = buildBackground(posters.get(0).asString());
                backgroundBox = buildBackgroundBox(posters, background);
            }
            stackPane.getChildren().add(buildDarkBackground());
            stackPane.getChildren().add(backgroundBox);
            stackPane.getChildren().add(posterBox);
            if (background != null) {
                detailsBox.setMinWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - poster.getFitWidth());
                detailsBox.setMaxWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - poster.getFitWidth());
                ((Text) ((HBox)detailsBox.getChildren().get(3)).getChildren().get(0))
                        .setWrappingWidth(Math.min(800, INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - poster.getFitWidth() - 20));
                ChangeListener<Number> stageSizeListener = (observable, oldValue, newValue) -> {
                    setBackgroundDimensions(background);
                    setPosterDimensions(poster);
                    detailsBox.setMinWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - poster.getFitWidth());
                    detailsBox.setMaxWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - poster.getFitWidth());
                    ((Text) ((HBox)detailsBox.getChildren().get(3)).getChildren().get(0))
                            .setWrappingWidth(Math.min(800, INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - poster.getFitWidth() - 20));
                };
                INJECTOR.getInstance(RootContainer.class).getStackPane().widthProperty().addListener(stageSizeListener);
                INJECTOR.getInstance(RootContainer.class).getStackPane().heightProperty().addListener(stageSizeListener);
            }
            stackPane.getChildren().remove(hBox);
            stackPane.getChildren().add(hBox);
            stage = ((Stage) stackPane.getScene().getWindow());
        });
        this.playerContainer = new PlayerContainer(stackPane);
    }

    private HBox buildContainer(Pane pane) {
        HBox hBox = new HBox();
        hBox.getChildren().add(pane);
        hBox.setAlignment(Pos.CENTER_RIGHT);
        return hBox;
    }

    private HBox buildDescriptionBox() {
        HBox descriptionBox = new HBox();
        Text description = buildText(movie.getDescription(), second);
        description.setWrappingWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth() - 20);
        descriptionBox.getChildren().add(description);
        return descriptionBox;
    }

    private HBox buildPlayBox() {
        HBox playBox = new HBox();
        Button playButton = new Button("Play");
        playButton.setOnMouseClicked(event -> {
            stackPane.getChildren().add(playerContainer.anchorPane);
            playerContainer.playerPresentable.tryPlay(movie);
        });
        playBox.getChildren().add(playButton);
        return playBox;
    }

    private WebView getWebView() {
        if (webView == null) {
            webView = buildWebView();
        }
        return webView;
    }

    private WebView buildWebView() {
        WebView webview = new WebView();
        webview.requestFocus();
        webview.setOnMouseClicked(webViewEvent -> {
            if (webViewEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (webViewEvent.getClickCount() == 2) {
                    stage.setFullScreen(!stage.isFullScreen());
                }
            }
        });
        webview.setOnKeyPressed(webViewEvent -> {
            if (webViewEvent.getCode() == KeyCode.ESCAPE) {
                stackPane.getChildren().remove(webview);
                webview.getEngine().load(null);
            }
        });
        return webview;
    }

    private HBox buildTrailerBox(StackPane stackPane, String url) {
        HBox trailerBox = new HBox();
        Button trailerButton = new Button("Trailer");
        trailerButton.setOnMouseClicked(event -> {
            getWebView().getEngine().load(url);
            stackPane.getChildren().add(webView);
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
                    movie.getRatingImdb(), longByGroup(movie.getRatingImdbVotes()), movie.getRatingKp(), longByGroup(movie.getRatingKpVotes())),
                    second));
        } else {
            aboutBox.getChildren().add(buildText(String.format("%.1f (%s)",
                    movie.getRatingImdb(), longByGroup(movie.getRatingImdbVotes())),  second));
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

    private StackPane buildStackPane() {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: black");
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

    private VBox buildDetailsBox(StackPane stackPane, Movie movie) {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(30, 10, 30, 10));
        vBox.setSpacing(20);
        vBox.setMaxWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth());
        vBox.setMinWidth(INJECTOR.getInstance(RootContainer.class).getStackPane().getWidth());
        HBox title = buildTitleBox(movie.getTitle());
        HBox about = buildAboutBox(movie);
        VBox castAndDetails = buildCastAndDetailsBox(movie);
        HBox trailer = buildTrailerBox(stackPane,
                (getLang() == RU && movie.getTrailerRu() != null) ? movie.getTrailerRu() : movie.getTrailer()
        );
        HBox castAndTrailer = new HBox();
        castAndTrailer.getChildren().addAll(castAndDetails, trailer);
        HBox description = buildDescriptionBox();
        vBox.getChildren().add(title);
        vBox.getChildren().add(about);
        vBox.getChildren().add(castAndTrailer);
        vBox.getChildren().add(description);
        if (!movie.getSeries()) {
            vBox.getChildren().add(buildPlayBox());
        } else {
            VBox serialContainer = new EpisodesContainer(movie.getEpisodes()).serialContainer;
            vBox.getChildren().add(serialContainer);
        }
        return vBox;
    }

    public static String longByGroup(long value) {
        if (value >= 1000000) {
            return String.format("%d %03d %03d", value / 1000000, (value % 1000000) / 1000, value % 1000000 % 1000);
        } else if (value >= 1000) {
            return String.format("%d %03d", value / 1000, value % 1000);
        } else {
            return Long.toString(value);
        }
    }

    private VBox buildCastAndDetailsBox(Movie movie) {
        List<Cast> casts = movie.getCasts();
        VBox vBox = new VBox();
        List<String> directors = new ArrayList<>();
        List<String> actors = new ArrayList<>();
        List<String> composers = new ArrayList<>();
        List<String> writers = new ArrayList<>();
        for (Cast cast : casts) {
            switch (cast.getRole()) {
                case ACTOR:
                    StringBuilder actor = new StringBuilder();
                    actor.append(cast.getName());
                    if (cast.getQua() != null) {
                        actor.append(" ...as ");
                        actor.append(cast.getQua());
                    }
                    actors.add(actor.toString());
                    break;
                case DIRECTOR:
                    directors.add(cast.getName());
                    break;
                case COMPOSER:
                    composers.add(cast.getName());
                    break;
                case WRITER:
                    writers.add(cast.getName());
                    break;
                default:
                    break;
            }
        }
        HBox directorBox = new HBox();
        directorBox.setSpacing(20);
        directorBox.getChildren().addAll(buildText("Director", third), buildText(String.join(", ", directors), third));
        HBox composerBox = new HBox();
        composerBox.setSpacing(20);
        composerBox.getChildren().addAll(buildText("Composer", third), buildText(String.join(", ", composers), third));
        HBox writerBox = new HBox();
        writerBox.setSpacing(20);
        writerBox.getChildren().addAll(buildText("Writer", third), buildText(String.join(", ", writers), third));
        HBox actorBox = new HBox();
        actorBox.setSpacing(20);
        VBox actorsVBox = new VBox();
        for (String a : actors) {
            actorsVBox.getChildren().add(buildText(a, third));
        }
        actorBox.getChildren().addAll(buildText("Actors", third), actorsVBox);
        vBox.getChildren().addAll(directorBox, writerBox, composerBox, actorBox);
        if (movie.getCountry() != null) {
            HBox country = new HBox();
            country.setSpacing(20);
            country.getChildren().addAll(buildText("Country", third), buildText(movie.getCountry().substring(1, movie.getCountry().length() - 1).replaceAll("\"", ""), third));
            vBox.getChildren().addAll(country);
        }
        if (movie.getCompany() != null) {
            HBox company = new HBox();
            company.setSpacing(20);
            company.getChildren().addAll(buildText("Company", third), buildText(movie.getCompany().substring(1, movie.getCompany().length() - 1).replaceAll("\"", ""), third));
            vBox.getChildren().addAll(company);
        }
        if (movie.getBudget() != null) {
            HBox budget = new HBox();
            budget.setSpacing(20);
            budget.getChildren().addAll(buildText("Budget", third), buildText(longByGroup(movie.getBudget()) + " " + movie.getCurrency(), third));
            vBox.getChildren().addAll(budget);
        }
        return vBox;
    }

    private Text buildText(String value, Font font) {
        Text text = new Text(value);
        text.setFont(font);
        text.setFill(Paint.valueOf(fontColor));
        return text;
    }

    private void setBackgroundDimensions(ImageView imageView) {
        AnchorPane root = INJECTOR.getInstance(RootContainer.class).getAnchorPane();
        double imgRatio = imageView.getImage().getWidth() / imageView.getImage().getHeight();
        if (root.getWidth() > imageView.getImage().getWidth()) {
            if (root.getHeight() > root.getWidth() / imgRatio) {
                imageView.setFitWidth(root.getWidth());
                imageView.setFitHeight(root.getWidth() / imgRatio);
            } else {
                imageView.setFitHeight(root.getHeight());
                imageView.setFitWidth(root.getHeight() * imgRatio);
            }
        }
    }

    private void setPosterDimensions(ImageView imageView) {
        imageView.setFitHeight(INJECTOR.getInstance(RootContainer.class).getStackPane().getHeight());
        imageView.setFitWidth(imageView.getFitHeight() / imageView.getImage().getHeight() * imageView.getImage().getWidth());
    }
}
