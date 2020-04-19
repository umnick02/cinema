//package com.cinema.javafx.view.components;
//
//import com.google.inject.Singleton;
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.ProgressIndicator;
//import javafx.scene.control.TextField;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.*;
//
//@Singleton
//public class MenuContentContainer {
//
//    TextField textField = buildTextField();
//    Button button = buildButton();
//    HBox hBox = buildHBox(textField, button);
//    private final int width = 24;
//    private final ImageView searchIcon = buildSearchIcon();
//    private final ProgressIndicator progressIndicator = buildIndicator();
//
//    private ImageView buildSearchIcon() {
//        ImageView searchIcon = new ImageView("/icons/search.png");
//        searchIcon.setFitWidth(width*0.9);
//        searchIcon.setFitHeight(width*0.9);
//        return searchIcon;
//    }
//
//    private ProgressIndicator buildIndicator() {
//        ProgressIndicator progressIndicator = new ProgressIndicator();
//        progressIndicator.setMinWidth(width*0.8);
//        progressIndicator.setMinHeight(width*0.8);
//        progressIndicator.setMaxWidth(width*0.8);
//        progressIndicator.setMaxHeight(width*0.8);
//        return progressIndicator;
//    }
//
//    private HBox buildHBox(TextField textField, Button button) {
//        HBox hBox = new HBox();
//        AnchorPane.setTopAnchor(hBox, 0d);
//        AnchorPane.setLeftAnchor(hBox, 0d);
//        AnchorPane.setRightAnchor(hBox, 0d);
//        hBox.setMaxHeight(30);
//        hBox.setMinHeight(30);
//        hBox.setFillHeight(true);
//        hBox.setSpacing(10);
//        hBox.setAlignment(Pos.CENTER);
//        hBox.getChildren().addAll(textField, button);
//        return hBox;
//    }
//
//    private TextField buildTextField() {
//        TextField textField = new TextField();
//        textField.setMinWidth(200);
//        textField.setPrefWidth(500);
//        return textField;
//    }
//
//    private Button buildButton() {
//        Button button = new Button();
//        button.setMaxWidth(width);
//        button.setMinWidth(width);
//        button.setMinHeight(width);
//        button.setMaxHeight(width);
//        button.setDefaultButton(true);
//        ImageView imageView = new ImageView("/icons/search.png");
//        imageView.setFitWidth(width*0.9);
//        imageView.setFitHeight(width*0.9);
//        button.setGraphic(imageView);
//        return button;
//    }
//
//    void showLoadingIcon() {
//        if (button.getGraphic() != progressIndicator) button.setGraphic(progressIndicator);
//    }
//
//    void hideLoadingIcon() {
//        if (button.getGraphic() != searchIcon) button.setGraphic(searchIcon);
//    }
//}
