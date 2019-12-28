package com.cinema.ui.components;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class SceneBuilder {
    private static RootContainer root;

    public static RootContainer buildRootContainer() {
        if (root == null) {
            root = new RootContainer();
        }
        return root;
    }

    static AnchorPane wrapNodeToAnchor(Node node) {
        AnchorPane anchorPane = new AnchorPane(node);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        return anchorPane;
    }
}
