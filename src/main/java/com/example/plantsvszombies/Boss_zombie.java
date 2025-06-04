package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Boss_zombie extends Zombie {
    int x;
    int y;
    public Boss_zombie(int x, int y, GameSceneController controller) {
        super (
                null,
                "/Zombies/boss_zombie_walk.gif",
                "/Zombies/boss_zombie_hit.gif",
                "/Zombies/boss_zombie_die.gif",
                "/Audio/normal_zombie_voice.mp3",
                "/Audio/boss_zombie_hit.mp3",
                350,
                0.7,
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
        return "bossZombie";
    }

    @Override
    public void startWalking() {
        isWalking = true;
        imageView.setImage(walkImage);
    }
    @Override
    public void startEating() {
        isWalking = false;
        imageView.setImage(eatImage);
        if (eatingSound != null &&
                eatingSound.getStatus() != MediaPlayer.Status.PLAYING &&
                eatingSound.getStatus() != MediaPlayer.Status.DISPOSED &&
                eatingSound.getStatus() != MediaPlayer.Status.UNKNOWN) {
            try {
                eatingSound.play();
            } catch (Exception ex) {
                System.err.println("Error when playing eatingSound: " + ex.getMessage());
                if (eatingSound.getError() != null) {
                    System.err.println("MediaPlayer error: " + eatingSound.getError());
                }
            }
        } else if (eatingSound == null) {
            System.err.println("eatingSound is null in Boss_zombie.");
        }
    }
    @Override
    public void die() {
        if (HP == 0) {
            imageView.setImage(deadImage);
            cleanup();
            Timeline removeAfterDeath = new Timeline(new KeyFrame(Duration.seconds(1.25), e -> {
                imageView.setVisible(false);
            }));
            removeAfterDeath.setCycleCount(1);
            removeAfterDeath.play();
        }
        if (controller != null) {
            controller.getZombies().remove(this); // remove khỏi danh sách
            if (controller.getZombies().isEmpty()) {
                controller.showWinScreen(); // Hiện màn hình thắng
            }
        }
    }
}