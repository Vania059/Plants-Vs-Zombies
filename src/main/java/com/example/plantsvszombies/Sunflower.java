package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Sunflower extends Plant{
    private final ImageView imageView;
    private GameSceneController controller;

    public Sunflower(Tile tile, Pane pane, GameSceneController controller) {
        super(tile, pane);
        this.controller = controller;
        Image gif = new Image(getClass().getResource("/Plants/sunflower.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        // 3 dòng dưới Ngọc thêm
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 35);

        pane.getChildren().add(imageView);
        startBehavior();
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public void startBehavior() {
        Timeline sunProduction = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            new SunToken(imageView.getLayoutX() + 40, imageView.getLayoutY(), pane, controller, true);
        }));
        sunProduction.setCycleCount(Timeline.INDEFINITE);
        sunProduction.play();
    }
}
