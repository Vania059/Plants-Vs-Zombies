package com.example.plantsvszombies.codeWallnut;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Wallnut {
    private final ImageView imageView;

    public Wallnut() {
        Image gif = new Image(getClass().getResource("/Plants/wallnut.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void defend() {
        System.out.println("Wallnut defends!");
    }
}
