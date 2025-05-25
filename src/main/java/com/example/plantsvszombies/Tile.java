package com.example.plantsvszombies;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
    private static final int OFFSET_X = 95;
    private static final int OFFSET_Y = 55;
    private static final int TILE_WIDTH = 85;
    private static final int TILE_HEIGHT = 95;
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
        tileRect = new Rectangle(TILE_WIDTH, TILE_HEIGHT);
        tileRect.setFill(Color.TRANSPARENT); // Không màu nền
        tileRect.setStroke(null); // Không viền
        tileRect.setOpacity(1);

        tileRect.setOnMouseEntered(event -> {
            tileRect.setFill(Color.rgb(150, 255, 150, 0.3)); // Sáng hơn khi hover
        });

        tileRect.setOnMouseExited(event -> {
            tileRect.setFill(Color.TRANSPARENT); // Trở lại bình thường
        });

        // Đặt vị trí layout ở đây
        tileRect.setLayoutX(getCenterX() - tileRect.getWidth() / 2);
        tileRect.setLayoutY(getCenterY() - tileRect.getHeight() / 2);

        tileRect.setOnMouseClicked(e -> {
            if (controller.getSelectedCard() != null && !hasPlant()) {
                controller.plantSelectedPlant(this);
            }
        });
    }

    public double getCenterX() {
        return OFFSET_X + col * TILE_WIDTH + TILE_WIDTH / 2;
    }

    public double getCenterY() {
        return OFFSET_Y + row * TILE_HEIGHT + TILE_HEIGHT / 2;
    }

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }

    public Rectangle getTileNode() {
        return tileRect;
    }

    public boolean hasPlant() {
        return plant != null;
    }

    public void clearPlant() {
        this.plant = null;
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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}