package com.cinema.view.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

@Singleton
public class RootContainer {
    private StackPane stackPane = buildStackPane();
    private AnchorPane anchorPane = buildAnchorPane(stackPane);

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public StackPane getStackPane() {
        return stackPane;
    }

    @Inject
    public RootContainer(SplitContainer splitContainer) {
        stackPane.getChildren().add(splitContainer.splitPane);
    }

    private StackPane buildStackPane() {
        StackPane stackPane = new StackPane();
        return stackPane;
    }

    public static AnchorPane buildAnchorPane(Node pane) {
        AnchorPane anchorPane = new AnchorPane(pane);
        AnchorPane.setBottomAnchor(pane, 0d);
        AnchorPane.setRightAnchor(pane, 0d);
        AnchorPane.setLeftAnchor(pane, 0d);
        AnchorPane.setTopAnchor(pane, 0d);
        return anchorPane;
    }
}
