package com.example.plantsvszombies.codeSunflower;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sunflower {
    private final ImageView imageView;

    public Sunflower() {
        Image gif = new Image(getClass().getResource("/Plants/sunflower.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void produceSun() {
        System.out.println("Sunflower produces sun!");
    }
}
