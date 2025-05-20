package com.example.plantsvszombies;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class Sunflower extends Plant {
    private Pane pane;
    private GameSceneController controller;    // ← đổi lại kiểu

    public Sunflower(Tile tile, Pane pane, GameSceneController controller) {
        super(tile, Color.YELLOW);
        this.pane = pane;
        this.controller = controller;        // ← giờ khớp kiểu
        startBehavior();
    }

    @Override
    public void startBehavior() {
        Timeline sunProduction = new Timeline(
                new KeyFrame(Duration.seconds(5),  // mỗi 5s như yêu cầu
                        e -> new SunToken(
                                head.getCenterX(),
                                head.getCenterY(),
                                pane,
                                controller,    // ← truyền controller
                                true
                        )
                )
        );
        sunProduction.setCycleCount(Timeline.INDEFINITE);
        sunProduction.play();
    }
}
