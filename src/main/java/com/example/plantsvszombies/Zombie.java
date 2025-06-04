package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.effect.ColorAdjust;

import java.util.HashMap;
import java.util.Map;


public abstract class Zombie {
    private static final Map<String, Image> imageCache = new HashMap<>();

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
            this.jumpImage = loadImageOnce(jumpGifPath);
        }
        this.walkImage = loadImageOnce(walkGifPath);
        this.eatImage = loadImageOnce(eatGifPath);
        this.deadImage = loadImageOnce(deadGifPath);

        this.imageView = new ImageView(walkImage);
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
        if (Sound != null) {
            Sound.setCycleCount(MediaPlayer.INDEFINITE);
            try {
                Sound.play();
            } catch (Exception ex) {
                System.err.println("Error playing Sound: " + ex.getMessage());
                if (Sound.getError() != null) {
                    System.err.println("MediaPlayer error: " + Sound.getError());
                }
            }
        }

        eatingSound = createMediaPlayer(eatingSoundPath);
        if (eatingSound != null) {
            eatingSound.setCycleCount(MediaPlayer.INDEFINITE);
        }

    }

    // Load ảnh và lưu vào cache để dùng lại
    private Image loadImageOnce(String path) {
        if (!imageCache.containsKey(path)) {
            Image img = new Image(getClass().getResource(path).toExternalForm());
            imageCache.put(path, img);
        }
        return imageCache.get(path);
    }
    public ImageView getView() {
        return imageView;
    }

    public abstract String getZombieType();
    public abstract void startWalking();
    public abstract void startEating();
    public abstract void die();

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
                        if (Math.abs(zombieX - plantX) < 5) {
                            foundPlant = true;
                            targetPlant = plant;
                            break;
                        }
                    }
                }
            }

            if (foundPlant) {
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
                            stopEatingSound();
                            playWalkSound();
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
                            stopEatingSound();
                            playWalkSound();
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
                            stopEatingSound();
                            playWalkSound();
                            moveToPlant(grid); // Tiếp tục di chuyển
                        }
                    }


                }));
                biteTimer.setCycleCount(Timeline.INDEFINITE);
                biteTimer.play();
            } else {
                // Không còn plant, zombie LUÔN tiếp tục di chuyển trái cho tới khi hết sân
                imageView.setLayoutX(imageView.getLayoutX() - speed);
                if (imageView.getLayoutX() <= 0) {
                    movement.stop();
                    controller.showLoseScreen();
                }
            }
        }));
        movement.setCycleCount(Timeline.INDEFINITE);
        movement.play();
    }

    protected void stopEatingSound() {
        if (eatingSound != null &&
                eatingSound.getStatus() != MediaPlayer.Status.DISPOSED &&
                eatingSound.getStatus() != MediaPlayer.Status.UNKNOWN &&
                eatingSound.getStatus() != MediaPlayer.Status.STOPPED) {
            try {
                eatingSound.stop();
            } catch (Exception ex) {
                System.err.println("Error stopping eatingSound: " + ex.getMessage());
                if (eatingSound.getError() != null) {
                    System.err.println("MediaPlayer error: " + eatingSound.getError());
                }
            }
        }
    }

    protected void playWalkSound() {
        if (Sound != null
                && Sound.getStatus() != MediaPlayer.Status.DISPOSED
                && Sound.getStatus() != MediaPlayer.Status.UNKNOWN
                && Sound.getStatus() != MediaPlayer.Status.PLAYING) {
            try {
                Sound.play();
            } catch (Exception ex) {
                System.err.println("Error playing Sound: " + ex.getMessage());
                if (Sound.getError() != null) {
                    System.err.println("MediaPlayer error: " + Sound.getError());
                }
            }
        }
    }

    public void cleanup() {
        cleanupMediaPlayer(Sound);
        cleanupMediaPlayer(eatingSound);
        if (movement != null) {
            try { movement.stop(); } catch (Exception ex) { System.err.println("Error stopping movement: " + ex.getMessage()); }
        }
    }

    private void cleanupMediaPlayer(MediaPlayer player) {
        if (player == null) return;
        try {
            MediaPlayer.Status status = player.getStatus();
            if (player.getError() == null &&
                    (status == MediaPlayer.Status.READY ||
                            status == MediaPlayer.Status.PLAYING ||
                            status == MediaPlayer.Status.PAUSED ||
                            status == MediaPlayer.Status.STOPPED)) {
                try {
                    player.dispose();
                } catch (Exception ex) {
                    System.err.println("Error disposing MediaPlayer: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {}
    }

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
            if (soundPath == null) return null;
            var resource = getClass().getResource(soundPath);
            if (resource == null) {
                System.err.println("Sound file NOT found: " + soundPath);
                return null;
            }
            Media media = new Media(resource.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);

            player.setOnError(() -> {
                System.err.println("MediaPlayer error: " + player.getError());
            });

            media.setOnError(() -> {
                System.err.println("Media error: " + media.getError());
            });
            return player;
        } catch (Exception e) {
            System.err.println("Exception loading media: " + soundPath);
            e.printStackTrace();
            return null;
        }
    }
}