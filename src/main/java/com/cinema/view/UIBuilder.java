package com.cinema.view;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public class UIBuilder {
    public static AnchorPane wrapNodeToAnchor(Node node) {
        AnchorPane anchorPane = new AnchorPane(node);
        setAnchor(node, 0d, 0d, 0d, 0d);
        return anchorPane;
    }

    public static AnchorPane wrapNodeToAnchorWidth(Node node) {
        AnchorPane anchorPane = new AnchorPane(node);
        AnchorPane.setRightAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        return anchorPane;
    }

    public static AnchorPane wrapNodeToAnchor(Node node, double top) {
        AnchorPane anchorPane = new AnchorPane(node);
        setAnchor(node, top, 0d, 0d, 0d);
        return anchorPane;
    }

    public static void setAnchor(Node node, double top, double bot, double r, double l) {
        AnchorPane.setTopAnchor(node, top);
        AnchorPane.setBottomAnchor(node, bot);
        AnchorPane.setRightAnchor(node, r);
        AnchorPane.setLeftAnchor(node, l);
    }
}
