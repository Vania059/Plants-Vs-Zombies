package com.example.plantsvszombies;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class Peashooter extends Plant {
    private Pane pane;

    public Peashooter(Tile tile, Pane pane) {
        super(tile, Color.LIMEGREEN);
        this.pane = pane;
        startBehavior();
    }

    @Override
    public void startBehavior() {
        Timeline bulletFiring = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            new Bullet(head.getCenterX() + 40, head.getCenterY(), pane);
        }));
        bulletFiring.setCycleCount(Timeline.INDEFINITE);
        bulletFiring.play();
    }
}
