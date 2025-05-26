package com.example.plantsvszombies;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import java.util.Random;

public class SunToken {
    private ImageView sunView;
    private Pane pane;
    private GameSceneController controller;  // ← đổi kiểu và tên
    private boolean isCollected = false;

    public SunToken(double x, double y, Pane pane, GameSceneController controller, boolean noDrop) {
        this.pane = pane;
        this.controller = controller;     // ← lưu controller

        Image sunImage = new Image(
                getClass().getResourceAsStream("/Plants/sun.png")
        );
        sunView = new ImageView(sunImage);
        sunView.setFitWidth(50);
        sunView.setFitHeight(50);
        sunView.setX(x - 30);
        sunView.setY(y - 30);

        pane.getChildren().add(sunView);

        if (noDrop) {
            enableCollection();
            scheduleDisappear(sunView, 5);
        } else {
            dropFromSky();
        }
    }

    private void enableCollection() {
        sunView.setOnMouseClicked(event -> collect());
    }

    private void scheduleDisappear(ImageView node, double seconds) {
        Timeline disappear = new Timeline(
                new KeyFrame(Duration.seconds(seconds), e -> {
                    if (!isCollected) {
                        pane.getChildren().remove(node);
                    }
                })
        );
        disappear.setCycleCount(1);
        disappear.play();
    }

    private void dropFromSky() {
        Random random = new Random();
        double maxDrop = 520 - sunView.getY();
        double distance = 100 + random.nextDouble() * (maxDrop - 100);

        TranslateTransition drop = new TranslateTransition(
                Duration.seconds(3), sunView
        );
        drop.setByY(distance);
        drop.setOnFinished(e -> {
            if (!isCollected) {
                enableCollection();
                scheduleDisappear(sunView, 5);
            }
        });
        drop.play();
    }

    private void collect() {
        if (!isCollected) {
            isCollected = true;
            pane.getChildren().remove(sunView);
            controller.addSunPoints(50);   // ← gọi đúng controller
        }
    }
}
