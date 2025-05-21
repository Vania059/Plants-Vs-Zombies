package pvz;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class PvZGame extends Application {
    private int sunPoints = 0; //start with 0 suntoken
    private Text sunPointsText;
    private Pane pane;
    private Tile[][] grid;
    private List<PlantCard> plantCards;
    private PlantCard selectedCard;

    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();
        if (pane == null) {
            System.out.println("Error: Pane initialization failed");
            return;
        }

        grid = new Tile[5][9];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col] = new Tile(col, row, this);
                grid[row][col].setPane(pane);
            }
        }

        //background image
        Image backgroundImage;
        try {
            System.out.println("Resource path: " + getClass().getResource("/mainBG.png"));
            backgroundImage = new Image(getClass().getResourceAsStream("/mainBG.png"));
        } catch (Exception e) {
            System.err.println("Failed to load background image: " + e.getMessage());
            backgroundImage = null;
            Rectangle fallbackBackground = new Rectangle(800, 600, Color.LIGHTGRAY);
            pane.getChildren().add(fallbackBackground);
        }

        if (backgroundImage != null) {
            ImageView background = new ImageView(backgroundImage);
            background.setFitWidth(800);
            background.setFitHeight(600);
            pane.getChildren().add(background);
        }

        //suntoken count
        sunPointsText = new Text(25, 75, "Points: " + sunPoints);
        sunPointsText.setFont(new Font(10));
        sunPointsText.setFill(Color.BLACK);
        pane.getChildren().add(sunPointsText);

        //plant cards
        plantCards = new ArrayList<>();
        System.out.println("Initializing plant cards with pane: " + (pane != null));
        plantCards.add(new PlantCard(100, "Sunflower", 50, pane, this));
        plantCards.add(new PlantCard(185, "Walnut", 50, pane, this));
        plantCards.add(new PlantCard(270, "Peashooter", 100, pane, this));
        plantCards.add(new PlantCard(355, "CherryBomb", 150, pane, this));
        System.out.println("Total nodes in pane after adding plant cards: " + pane.getChildren().size());

        //sun drop
        Random random = new Random();
        Timeline skyDrop = new Timeline(new KeyFrame(Duration.seconds(7 + random.nextDouble() * 3), e -> {
            int randomRow = random.nextInt(5);
            int randomCol = random.nextInt(9);
            Tile spawnTile = grid[randomRow][randomCol];
            new SunToken(spawnTile.getCenterX(), spawnTile.getCenterY() - 30, pane, this, false);
        }));
        skyDrop.setCycleCount(Timeline.INDEFINITE);
        skyDrop.play();

        //scene
        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("PvZ Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addSunPoints(int points) {
        sunPoints += points;
        sunPointsText.setText("Points: " + sunPoints);
        updatePlantCards();
    }

    public int getSunPoints() {
        return sunPoints;
    }

    public void deductSunPoints(int points) {
        sunPoints -= points;
        sunPointsText.setText("Points: " + sunPoints);
        updatePlantCards();
    }

    public Tile[][] getGrid() {
        return grid;
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
            plant = new Sunflower(tile, pane, this);
        } else if (selectedCard.getPlantType().equals("Wallnut")) {
            plant = new Walnut(tile);
        } else if (selectedCard.getPlantType().equals("Peashooter")) {
            plant = new Peashooter(tile, pane);
        } else if (selectedCard.getPlantType().equals("CherryBomb")) {
            plant = new CherryBomb(tile, pane);
        }
        if (plant != null) {
            pane.getChildren().addAll(plant.getNodes());
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

    public static void main(String[] args) {
        launch(args);
    }
}