package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static javafx.application.Application.launch;

public class Normal_zombie extends Zombie {
    private boolean isEating = false;
    int x;
    int y;
    private Tile tile;

    public Normal_zombie(int x, int y, GameSceneController controller) {
        super(
                null,
                "/Zombies/normal_zombie_walk.gif",
                "/Zombies/normal_zombie_eat.gif",
                "/Zombies/normal_zombie_die.gif",
                "/Audio/normal_zombie_voice.mp3",
                "/Audio/normal_zombie_eat.mp3",
                100,
                0.5,
                x,
                y,
                true,
                controller
        );
        this.x = x;
        this.y = y;
    }

    @Override
    public String getZombieType() {
        return "normalZombie";
    }

    @Override
    public void startWalking() {
        isWalking = true;
        imageView.setImage(walkImage);
    }
    @Override
    public void startEating() {
        isWalking = false;
        isEating = true;
        imageView.setImage(eatImage);
        eatingSound.play();
    }
    @Override
    public void die() {
        imageView.setImage(deadImage);
        cleanup();
        Timeline removeAfterDeath = new Timeline(new KeyFrame(Duration.seconds(1.25), e -> {
            imageView.setVisible(false);

            // REMOVE zombie khỏi danh sách
            if (controller != null) {
                controller.getZombies().remove(this); // remove khỏi danh sách
                if (controller.getZombies().isEmpty()) {
                    controller.showWinScreen(); // Hiện màn hình thắng
                }
            }

        }));
        removeAfterDeath.setCycleCount(1);
        removeAfterDeath.play();
    }
}