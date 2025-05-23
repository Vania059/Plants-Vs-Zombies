package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.awt.*;

public class Boss_zombie extends Zombie {
    int x;
    int y;
    public Boss_zombie(int x, int y) {
        super (
                null,
                "/Gif/boss_zombie_walk.gif",
                "/Gif/boss_zombie_hit.gif",
                "/Gif/boss_zombie_die.gif",
                "/Sound/boss_zombie_voice.mp3",
                "/Sound/boss_zombie_hit.mp3",
                300,
                0.75,
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
        if (HP == 0) {
            imageView.setImage(deadImage);
            if (Sound != null) Sound.stop();
            if (eatingSound != null) eatingSound.stop();
            if (movement != null) movement.stop();
            Timeline removeAfterDeath = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
                imageView.setVisible(false);
            }));
            removeAfterDeath.setCycleCount(1);
            removeAfterDeath.play();
        }
    }
}
