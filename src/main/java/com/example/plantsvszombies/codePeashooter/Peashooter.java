package com.example.plantsvszombies.codePeashooter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Peashooter {
    private final ImageView imageView;

    public Peashooter() {
        // Bước 1: Hiển thị ảnh thường
        Image idleImage = new Image(getClass().getResource("/Plants/peashooter.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        // Bước 2: Sau 2 giây, đổi thành peashootershoot.gif
        Timeline shootAnimation = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Image shootImage = new Image(getClass().getResource("/Plants/peashootershoot.gif").toExternalForm());
            imageView.setImage(shootImage);
            System.out.println("Peashooter starts shooting!");
        }));

        shootAnimation.setCycleCount(1);
        shootAnimation.play();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void shoot() {
        System.out.println("Peashooter shoots!");
    }
}
