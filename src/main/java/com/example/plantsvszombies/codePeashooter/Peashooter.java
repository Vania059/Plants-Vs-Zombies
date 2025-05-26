/*
package codePeashooter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

public class Peashooter {
    private final ImageView imageView;

    // Số lần còn lại trước khi Peashooter bị ăn xong
    private final Map<String, Integer> biteLimits;
    private int remainingHealth;

    public Peashooter() {
        // Bước 1: Hiển thị ảnh thường
        Image idleImage = new Image(getClass().getResource("/peashooter.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        // Bước 2: Sau 2 giây, đổi thành peashootershoot.gif
        Timeline shootAnimation = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Image shootImage = new Image(getClass().getResource("/peashootershoot.gif").toExternalForm());
            imageView.setImage(shootImage);
            System.out.println("Peashooter starts shooting!");
        }));

        shootAnimation.setCycleCount(1);
        shootAnimation.play();


        // Tạo số lần bị gặm cho từng loại zombie
        biteLimits = new HashMap<>();
        biteLimits.put("zombie1", 3); // zombie1 cắn 3 lần
        biteLimits.put("zombie2", 5); // zombie2 cắn 5 lần
        biteLimits.put("zombie3", 7); // zombie3 cắn 7 lần

        // Chọn zombie1 "zb tên khác thì thay tên nha'
        remainingHealth = biteLimits.get("zombie1");
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void shoot() {
        System.out.println("Peashooter shoots!");
    }

    // Gọi khi bị zombie gặm
    public void beEatenBy(String zombieType) {
        if (!biteLimits.containsKey(zombieType)) {
            System.out.println("Unknown zombie type: " + zombieType);
            return;
        }

        // Cập nhật máu nếu chưa đúng loại zombie cũ
        if (remainingHealth > biteLimits.get(zombieType)) {
            remainingHealth = biteLimits.get(zombieType);
        }

        remainingHealth--;

        System.out.println("Peashooter bitten by " + zombieType + ". Remaining health: " + remainingHealth);

        if (remainingHealth <= 0) {
            imageView.setVisible(false); // hoặc remove khỏi parent cũng đc neu muốn
            System.out.println("Peashooter has been eaten by " + zombieType + "!");
        }
    }
}
*/
