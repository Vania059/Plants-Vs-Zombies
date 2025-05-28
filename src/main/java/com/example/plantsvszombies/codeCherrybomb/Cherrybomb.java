/*
package Cherrybomb;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class Cherrybomb {
    private final ImageView imageView;

    // Trạng thái bị ăn
    private boolean isEaten = false;

    // Danh sách zombie có thể ăn được
    private final Set<String> validZombieTypes;

    public Cherrybomb() {
        // Bước 1: Hiển thị cherrybomb.gif
        Image idleImage = new Image(getClass().getResource("/cherrybomb.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        // Zombie hợp lệ
        validZombieTypes = new HashSet<>();
        validZombieTypes.add("zombie1");
        validZombieTypes.add("zombie2");
        validZombieTypes.add("zombie3");

        // Bước 2: Sau 1.2 giây, chuyển sang cherrybombpowie.gif
        Timeline changeToExplosion = new Timeline(new KeyFrame(Duration.seconds(1.2), e -> {
            if (!isEaten) {
                Image explodeImage = new Image(getClass().getResource("/cherrybombpowie.gif").toExternalForm());
                imageView.setImage(explodeImage);
                System.out.println("Cherrybomb explodes!");
            }
        }));

        // Bước 3: Sau 3.2 giây (tức 2 giây sau khi nổ), ẩn luôn hình
        Timeline disappear = new Timeline(new KeyFrame(Duration.seconds(3.2), e -> {
            imageView.setVisible(false);
            System.out.println("Cherrybomb has disappeared!");
        }));

        changeToExplosion.setCycleCount(1);
        disappear.setCycleCount(1);

        changeToExplosion.play();
        disappear.play();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void explode() {
        System.out.println("Cherrybomb explodes!");
    }

    public void beEatenBy(String zombieType) {
        if (!validZombieTypes.contains(zombieType)) {
            System.out.println("Unknown zombie type: " + zombieType);
            return;
        }

        if (!isEaten) {
            isEaten = true;
            imageView.setVisible(false); // hoặc remove khỏi pane
            System.out.println("Cherrybomb was eaten by " + zombieType + " before exploding!");
        }
    }
}
*/
