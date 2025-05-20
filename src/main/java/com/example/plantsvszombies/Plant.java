package com.example.plantsvszombies;

import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public abstract class Plant {
    protected Circle head;
    protected Rectangle stem;

    public Plant(Tile tile, Color headColor) {
        double x = tile.getCenterX();
        double y = tile.getCenterY();

        head = new Circle(x, y, 40);
        head.setFill(headColor);

        stem = new Rectangle(x - 10, y + 40, 20, 80);
        stem.setFill(Color.GREEN);

        tile.setPlant(this);
    }

    public Node[] getNodes() {
        return new Node[]{head, stem};
    }

    public abstract void startBehavior();
}