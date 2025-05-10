package codeZombie;

import javafx.application.Application;
import javafx.stage.Stage;
import static javafx.application.Application.launch;

public class Normal_zombie extends Zombie {
    int x;
    int y;

    public Normal_zombie(int x, int y) {
        super(
                "/Gif/normal_walk.gif",
                "/Gif/normal_eat.gif",
                "src/Sound/sound_effect_zombie.mp3",
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
    }
    @Override
    public void die() {
        if (HP == 0) {
            Sound.stop();
            imageView.setVisible(false);
        }
    }
}
