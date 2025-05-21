package pvz;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile {
    private static final int OFFSET_X = 50;
    private static final int OFFSET_Y = 100;
    private static final int TILE_SIZE = 80;
    private int col;
    private int row;
    private Plant plant;
    private Pane pane;
    private PvZGame game;
    private Rectangle tileRect;

    public Tile(int col, int row, PvZGame game) {
        this.col = col;
        this.row = row;
        this.game = game;
        this.pane = null;
        this.tileRect = new Rectangle(getCenterX() - TILE_SIZE / 2, getCenterY() - TILE_SIZE / 2, TILE_SIZE, TILE_SIZE);
        tileRect.setFill(Color.TRANSPARENT);
        tileRect.setOnMouseClicked(event -> {
            if (pane != null) {
                System.out.println("Tile clicked at row " + row + ", col " + col);
                game.plantSelectedPlant(this);
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