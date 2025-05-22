package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Peashooter extends Plant{
    private final ImageView imageView;

    public Peashooter(Tile tile, Pane pane) {
        super(tile, pane, null);
        // Bước 1: Hiển thị ảnh thường
        Image idleImage = new Image(getClass().getResource("/Plants/peashooter.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        // 3 dòng dưới Ngọc thêm
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 50);

        pane.getChildren().add(imageView);

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

    @Override
    public void startBehavior() {
        Timeline bulletFiring = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            new Bullet(head.getCenterX() + 40, head.getCenterY(), pane);
            System.out.println("Peashooter shoots!");
        }));
        bulletFiring.setCycleCount(Timeline.INDEFINITE);
        bulletFiring.play();
    }
}
