package com.cinema.view.components;

import com.google.inject.Inject;
import com.google.inject.Singleton;
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

    public static AnchorPane buildAnchorPane(StackPane stackPane) {
        AnchorPane anchorPane = new AnchorPane(stackPane);
        AnchorPane.setBottomAnchor(stackPane, 0d);
        AnchorPane.setRightAnchor(stackPane, 0d);
        AnchorPane.setLeftAnchor(stackPane, 0d);
        AnchorPane.setTopAnchor(stackPane, 0d);
        return anchorPane;
    }
}
