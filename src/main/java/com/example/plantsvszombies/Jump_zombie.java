package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Jump_zombie extends Zombie {
    private Timeline jumpTimeline;
    private boolean isJumping = true;
    private boolean isEating = false;

    public Jump_zombie(int x, int y, GameSceneController controller) {
        super(
                "/Zombies/jump_zombie_jump.gif",
                "/Zombies/jump_zombie_walk.gif",
                "/Zombies/jump_zombie_eat.gif",
                "/Zombies/jump_zombie_die.gif",
                "/Audio/jump_zombie_voice.mp3",
                "/Audio/normal_zombie_eat.mp3",
                250,
                0.3,
                x,
                y,
                false,
                controller
        );
        imageView.setImage(jumpImage);
        jump();
    }
    public void jump() {
        this.jumpTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            imageView.setLayoutX(imageView.getLayoutX() - speed * 1.75);
        }));
        this.jumpTimeline.setCycleCount(Timeline.INDEFINITE);
        this.jumpTimeline.play();
        Timeline monitor = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (HP <= 100 && !isEating) {
                isJumping = false;
                jumpTimeline.stop();
                startWalking();
            } else if (!isEating && HP >= 100 && !isJumping) {
                isJumping = true;
                jumpTimeline.play();
                imageView.setImage(jumpImage);
            }
        }));
        monitor.setCycleCount(Timeline.INDEFINITE);
        monitor.play();
    }
    @Override
    public String getZombieType() {
        return "jumpZombie";
    }

    @Override
    public void startWalking() {
        if (!isJumping && HP > 0) {
            isWalking = true;
            isEating = false;
            imageView.setImage(walkImage);
            stopEatingSound();
            playWalkSound();
        }
    }
    @Override
    public void startEating() {
        if (jumpTimeline != null) jumpTimeline.stop(); // Dừng nhảy
        if (movement != null) movement.stop();
        isEating = true;
        isWalking = false;
        isJumping = false;
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
            System.err.println("eatingSound is null when startEating is called.");
        }
    }
    @Override
    public void die() {
        if (HP <= 0) {
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
        if (jumpTimeline != null) jumpTimeline.stop();
    }
}