import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Peashooter Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.setVisible(true);

        panel.startGame();
    }
}
