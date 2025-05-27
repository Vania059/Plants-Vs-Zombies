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
                "/Zombies/normal_zombie_walk.gif",
                "/Zombies/boss_zombie_hit.gif",
                "/Zombies/boss_zombie_die.gif",
                "/Audio/boss_zombie_voice.mp3",
                "/Audio/boss_zombie_hit.mp3",
                300,
                0.75,
                x,
                y,
                true,
                null
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