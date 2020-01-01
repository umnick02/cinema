package com.cinema.view.components;

import com.cinema.view.Anchorable;
import com.cinema.view.UIBuilder;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

@Singleton
public class MenuContentContainer extends HBox implements Anchorable {
    private final AnchorPane wrapper;

    public MenuContentContainer() {
        super();
        setMaxHeight(30);
        setMinHeight(30);
        setFillHeight(true);
        getChildren().addAll(buildSearch(), buildSubmitButton());
        setAlignment(Pos.CENTER);
        wrapper = UIBuilder.wrapNodeToAnchorWidth(this);
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

    @Override
    public AnchorPane getWrapper() {
        return wrapper;
    }
}
