package com.example.plantsvszombies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
    private static final int OFFSET_X = 210;
    private static final int OFFSET_Y = 80;
    private static final int TILE_SIZE = 80;
    private int col;
    private int row;
    private Plant plant;
    private Pane pane;
    private GameSceneController controller;
    private Rectangle tileRect;

    public Tile(int col, int row, GameSceneController controller) {
        this.col = col;
        this.row = row;
        this.controller = controller;
        this.pane = null;

        // Tạo ô gạch ở đúng vị trí layout
        tileRect = new Rectangle(TILE_SIZE, TILE_SIZE);
        tileRect.setFill(Color.rgb(200, 255, 200, 0.2)); // Màu nền nhạt
        tileRect.setStroke(Color.LIGHTGRAY); // Viền nhẹ
        tileRect.setStrokeWidth(1);

        // Đặt vị trí layout ở đây
        tileRect.setLayoutX(getCenterX() - TILE_SIZE / 2);
        tileRect.setLayoutY(getCenterY() - TILE_SIZE / 2);

        tileRect.setOnMouseClicked(event -> {
            if (pane != null) {
                System.out.println("Tile clicked at row " + row + ", col " + col);
                controller.plantSelectedPlant(this);
            } else {
                System.out.println("Tile clicked but pane is null at row " + row + ", col " + col);
            }
        });
    }

    public double getCenterX() {
        return OFFSET_X + col * TILE_SIZE + TILE_SIZE / 2;
    }

    public double getCenterY() {
        return OFFSET_Y + row * TILE_SIZE + TILE_SIZE / 2;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
        if (pane != null && !pane.getChildren().contains(tileRect)) {
            pane.getChildren().add(tileRect);
            tileRect.toFront(); // Ensure tile is clickable
        }
    }

    public Rectangle getTileRect() {
        return tileRect;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}