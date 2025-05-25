package com.example.plantsvszombies;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;

public class Bullet {
    private ImageView bulletView;
    private Pane pane;

    public Bullet(double x, double y, Pane pane) {
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
        move.setOnFinished(e -> pane.getChildren().remove(bulletView));
        move.play();
    }
}