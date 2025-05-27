package com.example.plantsvszombies;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.HashMap;
import java.util.Map;

public class Wallnut extends Plant{
    private final ImageView imageView;
    private final Map<String, Integer> biteLimits;
    private int remainingHealth;

    public Wallnut(Tile tile, Pane pane) {
        super(tile, pane);
        Image gif = new Image(getClass().getResource("/Plants/wallnut.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 50);

        biteLimits = new HashMap<>();
        biteLimits.put("bossZombie", 8);
        biteLimits.put("jumpZombie", 10);
        biteLimits.put("normalZombie", 12);

        remainingHealth = biteLimits.get("normalZombie"); // default
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public void startBehavior() {}

    @Override
    public void stopBehavior() {}

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
        System.out.println("Wallnut bitten by " + zombieType + ". Remaining health: " + remainingHealth);

        if (remainingHealth <= 0) {
            imageView.setVisible(false);
            tile.setPlant(null);
            pane.getChildren().remove(getNode());
            System.out.println("Wallnut has been eaten by " + zombieType + "!");
        }
    }
}
