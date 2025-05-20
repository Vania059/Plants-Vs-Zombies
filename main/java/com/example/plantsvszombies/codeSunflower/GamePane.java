package com.example.plantsvszombies.codeSunflower;

import javafx.scene.layout.Pane;

public class GamePane extends Pane {
    private final Sunflower sunflower;

    public GamePane() {
        sunflower = new Sunflower();
        getChildren().add(sunflower.getImageView());

        sunflower.getImageView().setTranslateX(250);
        sunflower.getImageView().setTranslateY(150);
    }
}
