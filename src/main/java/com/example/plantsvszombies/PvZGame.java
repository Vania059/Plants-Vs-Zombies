/*package com.example.plantsvszombies;

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
import java.util.Random;*/

//public class PvZGame extends Application {
    /*private int sunPoints = 0;
    private Text sunPointsText;
    private Pane pane;
    private Tile[][] grid;

    @Override
    public void start(Stage primaryStage) {
        pane = new Pane();
        // background
        grid = new Tile[5][9];
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                grid[row][col] = new Tile(col, row, this);
                grid[row][col].setPane(pane);
            }
        }

        Image backgroundImage;
        try {
            System.out.println("Resource path: " + getClass().getResource("/Screen/MainYard.png"));
            backgroundImage = new Image(getClass().getResourceAsStream("/Screen/MainYard.png"));
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

        //the points counter
        sunPointsText = new Text(25, 75, "Points: " + sunPoints);
        sunPointsText.setFont(new Font(10));
        sunPointsText.setFill(Color.BLACK);

        pane.getChildren().addAll(sunPointsText);

        Sunflower sunflower = new Sunflower(grid[0][0], pane, this);
        Peashooter peashooter = new Peashooter(grid[1][1], pane);
        pane.getChildren().addAll(sunflower.getNodes());
        pane.getChildren().addAll(peashooter.getNodes());

        Random random = new Random();
        Timeline skyDrop = new Timeline(new KeyFrame(Duration.seconds(7 + random.nextDouble() * 3), e -> {
            int randomRow = random.nextInt(5); // 0 to 4
            int randomCol = random.nextInt(9); // 0 to 8
            Tile spawnTile = grid[randomRow][randomCol];
            new SunToken(spawnTile.getCenterX(), spawnTile.getCenterY() - 30, pane, this, false);
        }));
        skyDrop.setCycleCount(Timeline.INDEFINITE);
        skyDrop.play();

        Scene scene = new Scene(pane, 800, 600);
        primaryStage.setTitle("PvZ Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void addSunPoints(int points) {
        sunPoints += points;
        sunPointsText.setText("Points: " + sunPoints);
    }

    public static void main(String[] args) {
        launch(args);
    }*/

