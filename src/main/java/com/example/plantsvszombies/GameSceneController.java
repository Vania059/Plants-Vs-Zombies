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

    private List<PlantCard> plantCards;
    private PlantCard selectedCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupGrid();
        spawnPlants();
        spawnZombies();
        startSkySun();

        plantCards = new ArrayList<>();

        PlantCard peashooterCard = new PlantCard(10, "Peashooter", 100, gamePane , this);
        PlantCard sunflowerCard = new PlantCard(100, "Sunflower", 50, gamePane, this);
        PlantCard wallnutCard = new PlantCard(190, "Walnut", 50, gamePane, this);

        plantCards.add(peashooterCard);
        plantCards.add(sunflowerCard);
        plantCards.add(wallnutCard);

        // Sau đó cập nhật trạng thái theo điểm mặt trời
        updatePlantCards();
    }

    private void setupGrid() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                // Truyền đúng controller (this) vào Tile
                Tile tile = new Tile(col, row, this);
                tile.setPane(gamePane);
                grid[row][col] = tile;
            }
        }
    }

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

    public int getSunPoints() {
        return sunPoints;
    }

    public void deductSunPoints(int points) {
        sunPoints -= points;
        sunLabel.setText("" + sunPoints);
        updatePlantCards();
    }

    private void spawnPlants() {
        // Thêm 1 Peashooter và 1 Sunflower để test
        Peashooter peashooter = new Peashooter(grid[2][2], gamePane);
        Sunflower sunflower = new Sunflower(grid[1][1], gamePane, this);

        gamePane.getChildren().addAll(peashooter.getNodes());
        gamePane.getChildren().addAll(sunflower.getNodes());
    }

    public void selectPlant(String plantType) {
        for (PlantCard card : plantCards) {
            card.setSelected(card.getPlantType().equals(plantType));
            if (card.getPlantType().equals(plantType)) {
                selectedCard = card;
                System.out.println("Set selectedCard to: " + plantType);
            }
        }
        if (selectedCard == null) {
            System.out.println("Warning: selectedCard is still null after selecting " + plantType);
        }
    }

    public void plantSelectedPlant(Tile tile) {
        System.out.println("Attempting to plant on tile at row " + tile.getRow() + ", col " + tile.getCol());
        System.out.println("Current sun points: " + sunPoints);
        System.out.println("Selected card: " + (selectedCard != null ? selectedCard.getPlantType() : "none"));
        if (selectedCard != null) {
            System.out.println("Selected card cost: " + selectedCard.getCost());
        }
        System.out.println("Tile has plant: " + (tile.getPlant() != null));

        if (selectedCard == null || tile.getPlant() != null || sunPoints < selectedCard.getCost()) {
            System.out.println("Cannot plant: " + (selectedCard == null ? "No card selected" : sunPoints < selectedCard.getCost() ? "Not enough sun points (need " + selectedCard.getCost() + ")" : "Tile occupied"));
            return;
        }
        Plant plant = null;
        if (selectedCard.getPlantType().equals("Sunflower")) {
            plant = new Sunflower(tile, gamePane, this);
        } else if (selectedCard.getPlantType().equals("Wallnut")) {
            plant = new Wallnut(tile, gamePane);
        } else if (selectedCard.getPlantType().equals("Peashooter")) {
            plant = new Peashooter(tile, gamePane);
        } else if (selectedCard.getPlantType().equals("CherryBomb")) {
            plant = new Cherrybomb(tile, gamePane);
        }
        if (plant != null) {
            gamePane.getChildren().addAll(plant.getNodes());
            deductSunPoints(selectedCard.getCost());
            System.out.println("Planted " + selectedCard.getPlantType() + " at row " + tile.getRow() + ", col " + tile.getCol());
        } else {
            System.out.println("Failed to create plant for type: " + selectedCard.getPlantType());
        }
    }

    private void updatePlantCards() {
        for (PlantCard card : plantCards) {
            card.updateState(sunPoints);
        }
    }
}
