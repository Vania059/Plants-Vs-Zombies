package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Sunflower extends Plant{
    private final ImageView imageView;
    private GameSceneController controller;
    private final Map<String, Integer> biteLimits;
    private int remainingHealth;
    private Timeline sunProduction;

    public Sunflower(Tile tile, Pane pane, GameSceneController controller) {
        super(tile, pane);
        this.controller = controller;
        Image gif = new Image(getClass().getResource("/Plants/sunflower.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 35);

        // Cấu hình số lần bị ăn
        biteLimits = new HashMap<>();
        biteLimits.put("bossZombie", 2);
        biteLimits.put("jumpZombie", 3);
        biteLimits.put("normalZombie", 4);

        remainingHealth = biteLimits.get("normalZombie");

        startBehavior();
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public void startBehavior() {
        sunProduction = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            new SunToken(imageView.getLayoutX() + 40, imageView.getLayoutY(), pane, controller, true);
        }));
        sunProduction.setCycleCount(Timeline.INDEFINITE);
        sunProduction.play();
    }

    @Override
    public void stopBehavior() {
        if (sunProduction != null) sunProduction.stop();
    }

    public void beEatenBy(Zombie zombie) {
        String zombieType = zombie.getZombieType();

        if (!biteLimits.containsKey(zombieType)) {
            System.out.println("Unknown zombie type: " + zombieType);
            return;
        }

        if (remainingHealth > biteLimits.get(zombieType)) {
            remainingHealth = biteLimits.get(zombieType);
        }

        remainingHealth--;
        System.out.println("Sunflower bitten by " + zombieType + ". Remaining health: " + remainingHealth);

        if (remainingHealth <= 0) {
            imageView.setVisible(false);
            stopBehavior();
            tile.setPlant(null);
            pane.getChildren().remove(getNode());
            zombie.startWalking();
            System.out.println("Sunflower has been eaten by " + zombieType + "!");
        }
    }
}
