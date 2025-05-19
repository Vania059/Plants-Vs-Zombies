package Plants;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class GameSceneController implements Initializable {

    @FXML private AnchorPane gamePane;

    private List<Normal_zombie> zombies = new ArrayList<>();
    private Tile[][] grid = new Tile[5][9];
    private int sunPoints = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGrid();
        spawnPlants();
        spawnZombies();
        startSkySun();
    }

    private void setupGrid() {
        double tileWidth = 80;
        double tileHeight = 100;
        double startX = 180;
        double startY = 80;

        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                // Truyền đúng controller (this) vào Tile
                Tile tile = new Tile(col, row, this);
                tile.setLayoutX(startX + col * tileWidth);
                tile.setLayoutY(startY + row * tileHeight);
                tile.setPane(gamePane);
                grid[row][col] = tile;
            }
        }
    }

    private void spawnPlants() {
        // Thêm 1 Peashooter và 1 Sunflower để test
        Peashooter peashooter = new Peashooter(grid[2][2], gamePane);
        Sunflower sunflower = new Sunflower(grid[1][1], gamePane, this);

        gamePane.getChildren().addAll(peashooter.getNodes());
        gamePane.getChildren().addAll(sunflower.getNodes());
    }

    private void spawnZombies() {
        int[] lanesY = {100, 200, 300}; // 3 hàng cho level 1

        for (int i = 0; i < 3; i++) {
            int x = 1000 + i * 100;
            int y = lanesY[i % lanesY.length];
            Normal_zombie zombie = new Normal_zombie(x, y);
            zombies.add(zombie);
            gamePane.getChildren().add(zombie.getView());
        }

        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            for (Normal_zombie zombie : zombies) {
                zombie.startWalking();
                zombie.moveToPlant(150);
            }
        }));
        delay.setCycleCount(1);
        delay.play();
    }

    private void startSkySun() {
        Random random = new Random();
        Timeline skySun = new Timeline(new KeyFrame(Duration.seconds(7), e -> {
            int col = random.nextInt(9);
            int row = random.nextInt(5);
            Tile tile = grid[row][col];
            new SunToken(tile.getCenterX(), tile.getCenterY() - 40, gamePane, this, false);
        }));
        skySun.setCycleCount(Timeline.INDEFINITE);
        skySun.play();
    }

    public void addSunPoints(int points) {
        sunPoints += points;
        System.out.println("Sun points: " + sunPoints);
    }
}
