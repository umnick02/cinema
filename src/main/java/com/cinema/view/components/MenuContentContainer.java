package com.cinema.view.components;

import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

@Singleton
public class MenuContentContainer extends HBox {

    public MenuContentContainer() {
        super();
        AnchorPane.setTopAnchor(this, 0d);
        AnchorPane.setLeftAnchor(this, 0d);
        AnchorPane.setRightAnchor(this, 0d);
        setMaxHeight(30);
        setMinHeight(30);
        setFillHeight(true);
        getChildren().addAll(buildSearch(), buildSubmitButton());
        setAlignment(Pos.CENTER);
    }

    private TextField buildSearch() {
        TextField textField = new TextField();
        return textField;
    }

    private Button buildSubmitButton() {
        Button button = new Button("Search");
        button.setDefaultButton(true);
        return button;
    }
}
