package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.Node;

public class Peashooter extends Plant{
    private final ImageView imageView;

    public Peashooter(Tile tile, Pane pane) {
        super(tile, pane);
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
            // Tạo đạn tại thời điểm đổi ảnh
            double bulletX = imageView.getLayoutX() + imageView.getFitWidth() - 10;
            double bulletY = imageView.getLayoutY() + imageView.getFitHeight() / 2 - 10;
            new Bullet(bulletX, bulletY, pane);

            System.out.println("Peashooter starts shooting!");
            // Sau đó bắt đầu bắn tự động về sau
            startBehavior();
        }));
        shootAnimation.setCycleCount(1);
        shootAnimation.play();
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public void startBehavior() {
        Timeline bulletFiring = new Timeline(new KeyFrame(Duration.seconds(1.2), e -> {
            // Tạo viên đạn xuất hiện ở ngay "miệng súng" peashooter
            double bulletX = imageView.getLayoutX() + imageView.getFitWidth(); // cuối thân peashooter
            double bulletY = imageView.getLayoutY() + imageView.getFitHeight() / 2; // giữa chiều cao cây

            new Bullet(bulletX, bulletY, pane);
        }));
        bulletFiring.setCycleCount(Timeline.INDEFINITE);
        bulletFiring.play();
    }
}
