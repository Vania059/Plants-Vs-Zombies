import javax.swing.*;
import java.awt.*;

public class Pea {
    private int x, y, speed = 5;
    private Image[] frames;
    private int frameIndex = 0;

    public Pea(int x, int y) {
        this.x = x;
        this.y = y;
        frames = new Image[3];
        for (int i = 0; i < 3; i++) {
            frames[i] = new ImageIcon("images/pea" + (i + 1) + ".png").getImage();
        }
    }

    public void move() {
        x += speed;
        frameIndex = (frameIndex + 1) % frames.length;
    }

    public void draw(Graphics g, Component observer) {
        g.drawImage(frames[frameIndex], x, y, observer);
    }
}
