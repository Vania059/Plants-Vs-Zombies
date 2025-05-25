package com.example.plantsvszombies;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public abstract class Plant {
    protected Pane pane; // để sinh đạn hoặc sun
    protected Tile tile;

    public Plant(Tile tile, Pane pane) {
        this.tile = tile;
        this.pane = pane;
        tile.setPlant(this);
    }

    // Trả về node thực sự hiển thị lên gamePane (ImageView, Group, ...)
    public abstract Node getNode();

    public abstract void startBehavior();
}