package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import static javafx.application.Application.launch;

public class Jump_zombie extends Zombie {
    private boolean isJumping = true;

    public Jump_zombie(int x, int y, GameSceneController controller) {
        super(
                "/Zombies/jump_zombie_jump.gif",
                "/Zombies/jump_zombie_walk.gif",
                "/Zombies/jump_zombie_eat.gif",
                "/Zombies/jump_zombie_die.gif",
                "/Audio/jump_zombie_voice.mp3",
                "/Audio/normal_zombie_eat.mp3",
                200,
                0.5,
                x,
                y,
                false,
                controller
        );
        imageView.setImage(jumpImage);
        jump();
    }
    public void jump() {
        Timeline jumpTimeline = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            imageView.setX(imageView.getX() - speed * 3); // tốc độ nhảy nhanh hơn đi bộ
        }));
        jumpTimeline.setCycleCount(Timeline.INDEFINITE);
        jumpTimeline.play();

        Timeline delay = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            isJumping = false;
            HP = 100;
            jumpTimeline.stop();
            startWalking();
        }));
        delay.setCycleCount(1);
        delay.play();
    }

    @Override
    public String getZombieType() {
        return "jumpZombie";
    }

    @Override
    public void startWalking() {
        if (!isJumping && HP > 0) {
            isWalking = true;
            imageView.setImage(walkImage);
        }
    }
    @Override
    public void startEating() {
        isWalking = false;
        imageView.setImage(eatImage);
        eatingSound.play();
    }
    @Override
    public void die() {
        if (HP == 0) {
            imageView.setImage(deadImage);
            if (Sound != null) Sound.stop();
            if (eatingSound != null) eatingSound.stop();
            if (movement != null) movement.stop();

            Timeline removeAfterDeath = new Timeline(new KeyFrame(Duration.seconds(0.75), e -> {
                imageView.setVisible(false);
            }));
            removeAfterDeath.setCycleCount(1);
            removeAfterDeath.play();
        }
    }

    public void takeDamage(int damage) {
        HP -= damage;
        if (HP <= 0) {
            die();
        } else if (isJumping && HP <= 100) {
            // Nếu đang nhảy và bị bắn, kết thúc nhảy sớm
            isJumping = false;
            startWalking();
        }
    }
}
