package com.example.plantsvszombies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
    public static final int TILE_SIZE = 80;

    private int col, row;
    private Plant plant;
    private Pane pane;
    private GameSceneController controller;    // ← thêm biến controller
    private Rectangle tileRect;
    private double layoutX, layoutY;

    // Thêm parameter controller
    public Tile(int col, int row, GameSceneController controller) {
        this.col = col;
        this.row = row;
        this.controller = controller;       // ← lưu lại
        this.plant = null;
        this.pane = null;

        tileRect = new Rectangle(TILE_SIZE, TILE_SIZE);
        tileRect.setFill(Color.TRANSPARENT);

        // Bắt sự kiện click để trồng sunflower
        tileRect.setOnMouseClicked(event -> {
            if (plant == null && pane != null) {
                Sunflower sf = new Sunflower(this, pane, controller);
                pane.getChildren().addAll(sf.getNodes());
                this.plant = sf;
            }
        });
    }

    public void setLayoutX(double x) {
        this.layoutX = x;
        tileRect.setX(x);
    }

    public void setLayoutY(double y) {
        this.layoutY = y;
        tileRect.setY(y);
    }

    public double getCenterX() {
        return layoutX + TILE_SIZE / 2.0;
    }

    public double getCenterY() {
        return layoutY + TILE_SIZE / 2.0;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
        if (!pane.getChildren().contains(tileRect)) {
            pane.getChildren().add(tileRect);
        }
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Plant getPlant() {
        return plant;
    }
}
