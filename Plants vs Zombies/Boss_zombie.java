package codeZombie;

public class Boss_zombie extends Zombie {
    int x;
    int y;
    public Boss_zombie(int x, int y) {
        super (
                "/Gif/boss_zombie_walk.gif",
                "/Gif/boss_zombie_hit.gif",
                "src/Sound/sound_effect_zombie.mp3",
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
    }
    @Override
    public void die() {
        if (HP == 0) {
            Sound.stop();
            imageView.setVisible(false);
        }
    }
}
