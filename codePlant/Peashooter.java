import javax.swing.*;
import java.awt.*;

public class Peashooter extends Plant {
    private Image[] frames;
    private int frameIndex = 0;

    public Peashooter(int x, int y) {
        super(x, y);
        frames = new Image[9];
        for (int i = 0; i < 9; i++) {
            frames[i] = new ImageIcon("images/peashooter" + (i + 1) + ".png").getImage();
        }
    }

    public void update() {
        frameIndex = (frameIndex + 1) % frames.length;
    }

    public Pea shoot() {
        return new Pea(x + 50, y + 20);
    }

    @Override
    public void draw(Graphics g, Component observer) {
        g.drawImage(frames[frameIndex], x, y, observer);
    }
}
