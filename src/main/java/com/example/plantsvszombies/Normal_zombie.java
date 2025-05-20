package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import static javafx.application.Application.launch;

public class Normal_zombie extends Zombie {
    int x;
    int y;

    public Normal_zombie(int x, int y) {
        super(
                "/Zombies/normal_zombie_walk.gif",
                "/Zombies/normal_zombie_eat.gif",
                "/Zombies/normal_zombie_die.gif",
                "/Audio/normal_zombie_voice.mp3",
                "/Audio/normal_zombie_eat.mp3",
                100,
                0.5,
                x,
                y,
                true
        );
        this.x = x;
        this.y = y;
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
        eatingSound.play();
    }
    @Override
    public void die() {
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
