import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    private Peashooter peashooter;
    private java.util.List<Pea> peas;
    private Timer timer;
    private int tick = 0;

    public GamePanel() {
        setBackground(Color.BLACK);
        peashooter = new Peashooter(100, 300);
        peas = new ArrayList<>();
        timer = new Timer(1000 / 30, this); // 30 FPS

        // Bắn mỗi 2 giây
        new Timer(2000, e -> {
            peas.add(peashooter.shoot());
        }).start();
    }

    public void startGame() {
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        tick++;
        peashooter.update();

        for (Pea pea : peas) {
            pea.move();
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        peashooter.draw(g, this);
        for (Pea pea : peas) {
            pea.draw(g, this);
        }
    }
}
