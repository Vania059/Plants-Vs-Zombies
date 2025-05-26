package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

import java.util.List;

public class Bullet {
    private ImageView bulletView;
    private Timeline collisionCheck;
    private boolean hit = false;
    private Pane pane;

    public Bullet(double x, double y, Pane pane, List<Zombie> zombies) {
        this.pane = pane;

        // Load the bullet image
        Image bulletImage = new Image(getClass().getResourceAsStream("/Plants/pea.png"));
        bulletView = new ImageView(bulletImage);
        bulletView.setFitWidth(20);
        bulletView.setFitHeight(20);

        // Đặt viên đạn xuất phát từ đúng đầu họng súng (giữa viên đạn trùng với điểm (x, y) truyền vào)
        bulletView.setLayoutX(x - bulletView.getFitWidth() / 2);
        bulletView.setLayoutY(y - bulletView.getFitHeight() / 2);

        pane.getChildren().add(bulletView);

        // Di chuyển viên đạn sang phải (giữ nguyên trục y)
        TranslateTransition move = new TranslateTransition(Duration.seconds(2.5), bulletView);
        move.setByX(600); // có thể điều chỉnh cho phù hợp chiều rộng sân
        move.setCycleCount(1);
        move.setOnFinished(e -> {
            // Dừng kiểm tra va chạm khi bullet hết hành trình
            if (collisionCheck != null) collisionCheck.stop();
            pane.getChildren().remove(bulletView);
        });
        move.play();

        // Timeline kiểm tra va chạm mỗi 20ms
        collisionCheck = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            for (Zombie zombie : zombies) {
                // Kiểm tra zombie còn sống và visible
                if (zombie.getView().isVisible() && bulletView.getBoundsInParent().intersects(zombie.getView().getBoundsInParent())) {
                    // Gây damage (ví dụ 1 máu)
                    zombie.takeDamage(1);
                    hit = true;
                    // Remove bullet khỏi pane
                    pane.getChildren().remove(bulletView);
                    // Dừng di chuyển và kiểm tra va chạm
                    move.stop();
                    collisionCheck.stop();
                    break;
                }
            }
        }));
        collisionCheck.setCycleCount(Timeline.INDEFINITE);
        collisionCheck.play();
    }
}