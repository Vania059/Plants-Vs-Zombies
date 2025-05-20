package com.example.plantsvszombies;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class Bullet {
    private ImageView bulletView;
    private Pane pane;

    public Bullet(double x, double y, Pane pane) {
        this.pane = pane;

        // Load the bullet image
        Image bulletImage = new Image(getClass().getResourceAsStream("/Plants/pea.png"));
        bulletView = new ImageView(bulletImage);
        bulletView.setFitWidth(20);
        bulletView.setFitHeight(20);
        bulletView.setX(x - 10);
        bulletView.setY(y - 10);

        pane.getChildren().add(bulletView);

        TranslateTransition move = new TranslateTransition(Duration.seconds(3), bulletView);
        move.setByX(600);
        move.setCycleCount(1);
        move.setOnFinished(e -> pane.getChildren().remove(bulletView));
        move.play();
    }
}