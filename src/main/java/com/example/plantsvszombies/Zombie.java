package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.nio.file.Paths;

public abstract class Zombie {
    protected Image walkImage;
    protected Image eatImage;
    protected Image jumpImage;
    protected ImageView imageView;
    protected int HP;
    protected double speed;
    protected boolean isWalking;
    protected Timeline movement;
    protected MediaPlayer Sound;
    protected String soundPath;
    protected MediaPlayer eatingSound;
    protected Image deadImage;


    public Zombie(String jumpGifPath, String walkGifPath, String eatGifPath, String deadGifPath, String soundPath, String eatingSoundPath, int HP, double speed, int x, int y, boolean isWalking) {
        this.walkImage = new Image(getClass().getResource(walkGifPath).toExternalForm());
        this.eatImage = new Image(getClass().getResource(eatGifPath).toExternalForm());
        this.deadImage = new Image(getClass().getResource(deadGifPath).toExternalForm());
        if (jumpGifPath != null) {
            this.jumpImage = new Image(getClass().getResource(jumpGifPath).toExternalForm());
        }
        this.imageView = new ImageView(walkImage);
        this.imageView.setFitWidth(150);
        this.imageView.setFitHeight(150);
        this.imageView.setPreserveRatio(true);
        this.imageView.setX(x);
        this.imageView.setY(y);
        this.HP = HP;
        this.speed = speed;
        this.isWalking = isWalking;
        Sound = createMediaPlayer(soundPath);
        if (Sound != null) {
            Sound.setCycleCount(MediaPlayer.INDEFINITE);
            Sound.play();
        }
        eatingSound = createMediaPlayer(eatingSoundPath);
        if (eatingSound != null) eatingSound.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public ImageView getView() {
        return imageView;
    }
    public void moveToPlant(double plantX) {
        if (movement != null) movement.stop(); // Dừng nếu đang di chuyển

        movement = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (isWalking && imageView.getX() > plantX + 5) {
                imageView.setX(imageView.getX() - speed); // đi sang trái
            } else if (isWalking) {
                startEating();
                movement.stop();
            }
        }));
        movement.setCycleCount(Timeline.INDEFINITE);
        movement.play();
    }
    public abstract void startWalking();
    public abstract void startEating();
    public abstract void die();
    public void takeDamage(int damage) {
        HP -= damage;
        if (HP <= 0) {
            die();
        }
    }
    private MediaPlayer createMediaPlayer(String soundPath) {
        try {
            String mediaURL = getClass().getResource(soundPath).toExternalForm();
            Media media = new Media(mediaURL);
            return new MediaPlayer(media);
        } catch (Exception e) {
            System.err.println("Could not load media: " + soundPath);
            e.printStackTrace();
            return null;
        }
    }
}