package com.cinema.view.builder;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public enum ComponentBuilder {
    INSTANCE;

    public Text mainText(String value) {
        Text text = new Text(value);
        text.setStyle("-fx-font-size: 1.1em; -fx-fill: #CB7936;");
        return text;
    }

    public Text italicText(String value) {
        Text text = new Text(value);
        text.setStyle("-fx-font-style: italic; -fx-font-size: 0.8em; -fx-fill: #bababa;");
        return text;
    }

    public Text regularText(String value) {
        Text text = new Text(value);
        text.setStyle("-fx-font-size: 0.9em; -fx-fill: #bababa;");
        return text;
    }

    public Label multilineText(String value) {
        Label label = new Label(value);
        label.setWrapText(true);
        label.setStyle("-fx-font-size: 0.9em; -fx-text-fill: #bababa;");
        return label;
    }

    public Button buildIcon(String iconClass) {
        Button button = new Button();
        button.getStyleClass().add("icon");
        button.getStyleClass().add(iconClass);
        return button;
    }

    public ImageView buildImage(String url) {
        ImageView imageView;
        if (url != null) {
            Image image = new Image(url);
            imageView = new ImageView(image);
        } else {
            imageView = new ImageView();
        }
        double width = 120;
        imageView.setFitWidth(width);
        imageView.setFitHeight(width * 9 / 16);
        return imageView;
    }
}
