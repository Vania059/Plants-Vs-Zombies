package codeZombie;

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
    protected ImageView imageView;
    protected int HP;
    protected double speed;
    protected boolean isWalking;
    protected Timeline movement;
    protected MediaPlayer Sound;
    protected Timeline soundLoop;

    public Zombie(String walkGifPath, String eatGifPath,String soundPath, int HP, double speed, int x, int y, boolean isWalking) {
        this.walkImage = new Image(walkGifPath);
        this.eatImage = new Image(eatGifPath);
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
        Sound.setCycleCount(MediaPlayer.INDEFINITE);
        Sound.play();
    }

    public ImageView getView() {
        return imageView;
    }
    public void moveToPlant(double plantX) {
        movement = new Timeline(new KeyFrame(Duration.millis(50), e -> {
            if (isWalking && imageView.getX() > plantX + 5) {
                imageView.setX(imageView.getX() - speed); // move left
            } else {
                startEating(); // stop moving and start eating
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
    private MediaPlayer createMediaPlayer(String filePath) {
        Media media = new Media(Paths.get(filePath).toUri().toString());
        MediaPlayer player = new MediaPlayer(media);
        return player;
    }
}
