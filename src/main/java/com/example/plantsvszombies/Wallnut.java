package com.example.plantsvszombies;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Wallnut extends Plant{
    private final ImageView imageView;

    public Wallnut(Tile tile, Pane pane) {
        super(tile, pane);
        Image gif = new Image(getClass().getResource("/Plants/wallnut.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        // 3 dòng dưới Ngọc thêm
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 50);

        pane.getChildren().add(imageView);
    }

    @Override
    public Node getNode() {
        return imageView;
    }



    public void defend() {
        System.out.println("Wallnut defends!");
    }

    @Override
    public void startBehavior() {
        System.out.println("Wallnut is ready to block zombies.");
    }
}
