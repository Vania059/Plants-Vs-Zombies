package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


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
        this.walkImage = new Image(getClass().getResource(walkGifPath).toExternalForm());;
        this.eatImage = new Image(getClass().getResource(eatGifPath).toExternalForm());;
        this.imageView = new ImageView(walkImage);
        this.deadImage = new Image(getClass().getResource(deadGifPath).toExternalForm());;
        this.imageView.setFitWidth(200);
        this.imageView.setFitHeight(200);
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
    public void moveToPlant(Tile[][] grid) {
        movement = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            boolean foundPlant = false;

            double zombieCenterY = imageView.getY() + imageView.getBoundsInParent().getHeight() / 2;
            int row = (int)((zombieCenterY - 55) / 95); // OFFSET_Y = 55, TILE_HEIGHT = 95

            if (row >= 0 && row < grid.length) {
                for (Tile tile : grid[row]) {
                    Plant plant = tile.getPlant();
                    if (plant != null && plant.getNode() != null) {
                        // Lấy khoảng cách giữa zombie và plant
                        Bounds zombieBounds = imageView.getBoundsInParent();
                        Bounds plantBounds = plant.getNode().getBoundsInParent();

                        // Kiểm tra va chạm
                        if (zombieBounds.intersects(plantBounds)) {
                            foundPlant = true;
                            break;
                        }
                    }
                }
            }

            if (isWalking && !foundPlant) {
                imageView.setX(imageView.getX() - speed); // Di chuyển bình thường
            } else if (foundPlant) {
                startEating();
                movement.stop(); // Dừng di chuyển để ăn
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