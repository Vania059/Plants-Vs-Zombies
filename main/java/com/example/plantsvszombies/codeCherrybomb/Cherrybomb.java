package com.example.plantsvszombies.codeCherrybomb;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Cherrybomb {
    private final ImageView imageView;

    public Cherrybomb() {
        // Bước 1: Hiển thị cherrybomb.gif
        Image idleImage = new Image(getClass().getResource("/Plants/cherrybomb.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        // Bước 2: Sau 1.2 giây, chuyển sang cherrybombpowie.gif
        Timeline changeToExplosion = new Timeline(new KeyFrame(Duration.seconds(1.2), e -> {
            Image explodeImage = new Image(getClass().getResource("/Plants/cherrybombpowie.gif").toExternalForm());
            imageView.setImage(explodeImage);
        }));

        // Bước 3: Sau 3.2 giây (tức 2 giây sau khi nổ), ẩn luôn hình
        Timeline disappear = new Timeline(new KeyFrame(Duration.seconds(3.2), e -> {
            imageView.setVisible(false);  // hoặc remove hẳn nếu muốn
            System.out.println("Cherrybomb has disappeared!");
        }));

        // Chạy cả 2 timeline
        changeToExplosion.setCycleCount(1);
        disappear.setCycleCount(1);

        changeToExplosion.play();
        disappear.play();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void explode() {
        System.out.println("Cherrybomb explodes!");
    }
}
