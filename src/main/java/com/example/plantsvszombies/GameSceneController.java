package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class GameSceneController implements Initializable {

    @FXML private AnchorPane gamePane;
    @FXML private Label sunLabel;

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

    // chưa sửa
    private void spawnPlants() {
        // Thêm 1 Peashooter và 1 Sunflower để test
        Peashooter peashooter = new Peashooter(grid[2][2], gamePane);
        Sunflower sunflower = new Sunflower(grid[1][1], gamePane, this);

        gamePane.getChildren().addAll(peashooter.getNodes());
        gamePane.getChildren().addAll(sunflower.getNodes());
    }

    // Thư sửa
    private void spawnZombies() {
        int[] lanesY = {100, 200, 300, 400};
        Random rand = new Random();
        int x = 1000; // vị trí ngoài scene

        // Spawn lần đầu
        for(int i = 0; i < 3; i++) {
            int randomLaneIndex = rand.nextInt(lanesY.length);
            int y = lanesY[randomLaneIndex];
            Normal_zombie zombie = new Normal_zombie(x, y);
            zombies.add(zombie);
            gamePane.getChildren().add(zombie.getView());
            zombie.startWalking();
            zombie.moveToPlant(150);
        }

        Timeline spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(20), e -> {
            for(int i = 0; i < 3; i++) { // mỗi lần spawn 3 zombie
                int randomLaneIndex = rand.nextInt(lanesY.length);
                int spawnY = lanesY[randomLaneIndex];
                Normal_zombie zombie = new Normal_zombie(x, spawnY);
                zombies.add(zombie);
                gamePane.getChildren().add(zombie.getView());
                zombie.startWalking();
                zombie.moveToPlant(150);
            }
        }));
        spawnTimeline.setCycleCount(10); // hoặc Timeline.INDEFINITE nếu muốn spawn mãi
        spawnTimeline.play();
    }

    private void startSkySun() {
        Random random = new Random();
        Timeline skySun = new Timeline(new KeyFrame(Duration.seconds(7), e -> {
            int col = random.nextInt(9);
            int row = random.nextInt(5);
            Tile tile = grid[row][col];
            new SunToken(tile.getCenterX(), tile.getCenterY() - 540, gamePane, this, false);
        }));
        skySun.setCycleCount(Timeline.INDEFINITE);
        skySun.play();
    }

    public void addSunPoints(int points) {
        sunPoints += points;
        sunLabel.setText("" + sunPoints);
    }
}
