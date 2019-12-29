package com.cinema.ui;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class UIBuilder {
    public static AnchorPane wrapNodeToAnchor(Node node) {
        AnchorPane anchorPane = new AnchorPane(node);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        return anchorPane;
    }
}
