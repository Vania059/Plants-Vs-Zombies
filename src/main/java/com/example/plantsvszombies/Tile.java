package com.example.plantsvszombies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
    public static final int TILE_SIZE = 80;

    private int col, row;
    private Plant plant;
    private Pane pane;
    private GameSceneController controller;
    private Rectangle tileRect;
    private double layoutX, layoutY;

    public Tile(int col, int row, GameSceneController controller) {
        this.col = col;
        this.row = row;
        this.controller = controller;
        this.plant = null;
        this.pane = null;

        tileRect = new Rectangle(TILE_SIZE, TILE_SIZE);
        tileRect.setFill(Color.TRANSPARENT);
        tileRect.setStroke(Color.TRANSPARENT); // Để test thì để LIGHTGRAY

        // Sự kiện click trồng Sunflower
        tileRect.setOnMouseClicked(event -> {
            if (plant == null && pane != null) {
                Sunflower sf = new Sunflower(this, pane, controller);
                pane.getChildren().addAll(sf.getNodes());
                this.plant = sf;
            }
        });
    }

    // Khi set vị trí, cập nhật cả cho tileRect
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

    // Nếu muốn tile tự cộng điểm mặt trời (khi plant tạo sun token)
    public void addSunPoints(int points) {
        if (controller != null) {
            controller.addSunPoints(points);
        }
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Plant getPlant() {
        return plant;
    }

    public Rectangle getTileRect() {
        return tileRect;
    }
}