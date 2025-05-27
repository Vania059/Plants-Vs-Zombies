package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.effect.ColorAdjust;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


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
    protected GameSceneController controller;



    public Zombie(String jumpGifPath, String walkGifPath, String eatGifPath, String deadGifPath, String soundPath, String eatingSoundPath, int HP, double speed, int x, int y, boolean isWalking, GameSceneController controller) {
        if (jumpGifPath != null) {
            this.jumpImage = new Image(getClass().getResource(jumpGifPath).toExternalForm());
        }
        this.walkImage = new Image(getClass().getResource(walkGifPath).toExternalForm());
        this.eatImage = new Image(getClass().getResource(eatGifPath).toExternalForm());
        this.imageView = new ImageView(walkImage);
        this.deadImage = new Image(getClass().getResource(deadGifPath).toExternalForm());
        this.imageView.setFitWidth(200);
        this.imageView.setFitHeight(200);
        this.imageView.setPreserveRatio(true);
        this.imageView.setLayoutX(x);
        this.imageView.setLayoutY(y);
        this.HP = HP;
        this.speed = speed;
        this.isWalking = isWalking;
        this.controller = controller;
        Sound = createMediaPlayer(soundPath);
        Sound.setCycleCount(MediaPlayer.INDEFINITE);
        Sound.play();
        eatingSound = createMediaPlayer(eatingSoundPath);
        eatingSound.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public ImageView getView() {
        return imageView;
    }

    public abstract String getZombieType();

    public void moveToPlant(Tile[][] grid) {
        movement = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            boolean foundPlant = false;

            double zombieCenterY = imageView.getLayoutY() + imageView.getBoundsInParent().getHeight() / 2;
            int row = (int)((zombieCenterY - 55) / 95); // OFFSET_Y = 55, TILE_HEIGHT = 95

            Plant targetPlant = null;

            if (row >= 0 && row < grid.length) {
                for (Tile tile : grid[row]) {
                    Plant plant = tile.getPlant();
                    if (plant != null && plant.getNode() != null) {
                        double zombieX = imageView.getLayoutX();
                        double plantX = plant.getNode().getLayoutX();

                        if (Math.abs(zombieX - plantX) < 5) { // thử 30, có thể chỉnh 40 hoặc 25 tùy hình ảnh
                            foundPlant = true;
                            targetPlant = plant;
                            break;
                        }
                    }
                }
            }

            if (isWalking && !foundPlant) {
                imageView.setLayoutX(imageView.getLayoutX() - speed); // Di chuyển bình thường

                // Kiểm tra nếu chạm mép trái màn hình
                if (imageView.getLayoutX() <= 10) {
                    movement.stop(); // Dừng chuyển động
                    controller.showLoseScreen(); // HIỆN losePane
                }

            } else if (foundPlant) {
                startEating();
                movement.stop(); // Dừng di chuyển để ăn


                final Plant finalTargetPlant = targetPlant;
                Timeline biteTimer = new Timeline();
                biteTimer.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5), ev -> {
                    if (finalTargetPlant instanceof Peashooter peashooter) {
                        peashooter.beEatenBy(this);

                        if (!peashooter.getNode().isVisible()) {
                            ev.consume(); // Ngừng timeline biteTimer
                            biteTimer.stop();
                            isWalking = true;
                            imageView.setImage(walkImage);
                            if (eatingSound != null) eatingSound.stop(); // Tắt âm thanh ăn
                            if (Sound != null) Sound.play(); // Phát lại âm thanh đi bộ
                            moveToPlant(grid); // Tiếp tục di chuyển
                        }
                    }

                    if (finalTargetPlant instanceof Sunflower sunflower) {
                        sunflower.beEatenBy(this);

                        if (!sunflower.getNode().isVisible()) {
                            ev.consume(); // Ngừng timeline biteTimer
                            biteTimer.stop();
                            isWalking = true;
                            imageView.setImage(walkImage);
                            if (eatingSound != null) eatingSound.stop(); // Tắt âm thanh ăn
                            if (Sound != null) Sound.play(); // Phát lại âm thanh đi bộ
                            moveToPlant(grid); // Tiếp tục di chuyển
                        }
                    }

                    if (finalTargetPlant instanceof Wallnut wallnut) {
                        wallnut.beEatenBy(this);

                        if (!wallnut.getNode().isVisible()) {
                            ev.consume(); // Ngừng timeline biteTimer
                            biteTimer.stop();
                            isWalking = true;
                            imageView.setImage(walkImage);
                            if (eatingSound != null) eatingSound.stop(); // Tắt âm thanh ăn
                            if (Sound != null) Sound.play(); // Phát lại âm thanh đi bộ
                            moveToPlant(grid); // Tiếp tục di chuyển
                        }
                    }


                }));
                biteTimer.setCycleCount(Timeline.INDEFINITE);
                biteTimer.play();
            }

        }));
        movement.setCycleCount(Timeline.INDEFINITE);
        movement.play();
    }
    public abstract void startWalking();
    public abstract void startEating();
    public abstract void die();
    public void takeDamage(int damage) {
        this.HP -= damage;

        if (HP <= 0) {
            die();
            return;
        }

        // Hiệu ứng chớp trắng nhẹ
        ColorAdjust flash = new ColorAdjust();
        flash.setBrightness(0.5);  // Sáng nhẹ hơn, kiểu trong suốt

        imageView.setEffect(flash);

        Timeline flashTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> {
                    imageView.setEffect(null);
                })
        );
        flashTimeline.setCycleCount(1);
        flashTimeline.play();
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