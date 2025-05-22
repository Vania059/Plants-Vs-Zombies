package com.example.plantsvszombies;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public abstract class Plant {
    protected Circle head;
    protected Pane pane; // để sinh đạn
    protected Tile tile;

    public Plant(Tile tile, Pane pane, Color headColor) {
        this.tile = tile;
        this.pane = pane;

        double x = tile.getCenterX();
        double y = tile.getCenterY();

        head = new Circle(x, y, 40);
        head.setFill(headColor);

        tile.setPlant(this);
    }

    public Node[] getNodes() {
        return new Node[]{head};
    }

    public abstract void startBehavior();
}