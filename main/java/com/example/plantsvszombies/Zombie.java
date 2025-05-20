package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public abstract class Zombie {
    protected Image walkImage;
    protected Image eatImage;
    protected ImageView imageView;
    protected int HP;
    protected double speed;
    protected boolean isWalking;
    protected Timeline movement;
    protected MediaPlayer Sound;
    protected String soundPath;
    protected MediaPlayer eatingSound;
    protected Image deadImage;


    public Zombie(String walkGifPath, String eatGifPath, String deadGifPath, String soundPath, String eatingSoundPath, int HP, double speed, int x, int y, boolean isWalking) {
        this.walkImage = new Image(walkGifPath);
        this.eatImage = new Image(eatGifPath);
        this.imageView = new ImageView(walkImage);
        this.deadImage = new Image(deadGifPath);
        this.imageView.setFitWidth(150);
        this.imageView.setFitHeight(150);
        this.imageView.setPreserveRatio(true);
        this.imageView.setX(x);
        this.imageView.setY(y);
        this.HP = HP;
        this.speed = speed;
        this.isWalking = isWalking;
        Sound = createMediaPlayer(soundPath);
        Sound.setCycleCount(MediaPlayer.INDEFINITE);
        Sound.play();
        eatingSound = createMediaPlayer(eatingSoundPath);
        eatingSound.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public ImageView getView() {
        return imageView;
    }
    public void moveToPlant(double plantX) {
        movement = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (isWalking && imageView.getX() > plantX + 5) {
                imageView.setX(imageView.getX() - speed); // move left
            } else {
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
