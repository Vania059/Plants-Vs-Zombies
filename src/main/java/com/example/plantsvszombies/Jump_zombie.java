package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import static javafx.application.Application.launch;

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
                200,
                0.25,
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
        eatingSound.play();
    }
    @Override
    public void die() {
        if (HP == 0) {
            imageView.setImage(deadImage);
            cleanup();

            Timeline removeAfterDeath = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
                imageView.setVisible(false);
            }));
            removeAfterDeath.setCycleCount(1);
            removeAfterDeath.play();
        }
    }
}