package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class Peashooter extends Plant{
    private final ImageView imageView;
    private final GameSceneController controller;
    private final Map<String, Integer> biteLimits;
    private int remainingHealth;
    private boolean isDead = false;

    public Peashooter(Tile tile, Pane pane, GameSceneController controller) {
        super(tile, pane);
        this.controller = controller;
        // Bước 1: Hiển thị ảnh thường
        Image idleImage = new Image(getClass().getResource("/Plants/peashooter.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        // 3 dòng dưới Ngọc thêm
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 50);


        // Bước 2: Sau 2 giây, đổi thành peashootershoot.gif
        Timeline shootAnimation = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Image shootImage = new Image(getClass().getResource("/Plants/peashootershoot.gif").toExternalForm());
            imageView.setImage(shootImage);
            // Tạo đạn tại thời điểm đổi ảnh
            Timeline firstBullet = new Timeline(new KeyFrame(Duration.seconds(0.5), ev -> {
                double bulletX = imageView.getLayoutX() + imageView.getFitWidth();
                double bulletY = imageView.getLayoutY() + imageView.getFitHeight() / 2 + 30; // chỉnh thấp 1 chút
                new Bullet(bulletX, bulletY, pane, controller.getZombies());
                System.out.println("Peashooter shoots first bullet!");

                // Sau viên đầu tiên, bắt đầu hành vi bắn định kỳ
                startBehavior();
            }));
            firstBullet.setCycleCount(1);
            firstBullet.play();
        }));
        shootAnimation.setCycleCount(1);
        shootAnimation.play();

        // Tạo số lần bị gặm cho từng loại zombie
        biteLimits = new HashMap<>();
        biteLimits.put("bossZombie", 3); // zombie1 cắn 3 lần
        biteLimits.put("jumpZombie", 5); // zombie2 cắn 5 lần
        biteLimits.put("normalZombie", 7); // zombie3 cắn 7 lần

        // Chọn zombie1 "zb tên khác thì thay tên nha'
        remainingHealth = biteLimits.get("normalZombie");
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public void startBehavior() {
        Timeline bulletFiring = new Timeline(new KeyFrame(Duration.seconds(1.2), e -> {
            double bulletX = imageView.getLayoutX() + imageView.getFitWidth();
            double bulletY = imageView.getLayoutY() + imageView.getFitHeight() / 2 + 30;
            new Bullet(bulletX, bulletY, pane, controller.getZombies());
        }));
        bulletFiring.setCycleCount(Timeline.INDEFINITE);
        bulletFiring.play();
    }
    public void beEatenBy(Zombie zombie) {
        String zombieType = zombie.getZombieType();

        if (!biteLimits.containsKey(zombieType)) {
            System.out.println("Unknown zombie type: " + zombieType);
            return;
        }

        int maxBites = biteLimits.get(zombieType);

        // Nếu zombie mới cắn, reset remainingHealth nếu cần
        if (remainingHealth > maxBites) {
            remainingHealth = maxBites;
        }

        remainingHealth--;

        System.out.println(this.getClass().getSimpleName() + " bitten by " + zombieType + ". Remaining health: " + remainingHealth);

        if (remainingHealth <= 0) {
            imageView.setVisible(false);
            tile.setPlant(null);
            System.out.println(this.getClass().getSimpleName() + " has been eaten by " + zombieType + "!");
        }
    }

    public boolean isDead() {
        return isDead;
    }
}
