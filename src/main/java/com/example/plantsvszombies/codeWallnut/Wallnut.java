package Wallnut;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashMap;
import java.util.Map;

public class Wallnut {
    private final ImageView imageView;

    // Thêm logic bị ăn
    private final Map<String, Integer> biteLimits;
    private int remainingHealth;

    public Wallnut() {
        Image gif = new Image(getClass().getResource("/wallnut.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        // Cấu hình số lần bị ăn
        biteLimits = new HashMap<>();
        biteLimits.put("zombie1", 8);
        biteLimits.put("zombie2", 10);
        biteLimits.put("zombie3", 12);

        remainingHealth = biteLimits.get("zombie1"); // default
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void defend() {
        System.out.println("Wallnut defends!");
    }

    public void beEatenBy(String zombieType) {
        if (!biteLimits.containsKey(zombieType)) {
            System.out.println("Unknown zombie type: " + zombieType);
            return;
        }

        if (remainingHealth > biteLimits.get(zombieType)) {
            remainingHealth = biteLimits.get(zombieType);
        }

        remainingHealth--;
        System.out.println("Wallnut bitten by " + zombieType + ". Remaining health: " + remainingHealth);

        if (remainingHealth <= 0) {
            imageView.setVisible(false);
            System.out.println("Wallnut has been eaten by " + zombieType + "!");
        }
    }
}
