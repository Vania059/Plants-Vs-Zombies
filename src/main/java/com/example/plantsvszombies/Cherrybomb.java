package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Cherrybomb extends Plant{
    private final ImageView imageView;

    public Cherrybomb(Tile tile, Pane pane) {
        super(tile, pane);
        // Bước 1: Hiển thị cherrybomb.gif
        Image idleImage = new Image(getClass().getResource("/Plants/cherrybomb.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        // 3 dòng dưới Ngọc thêm
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 50);

        pane.getChildren().add(imageView);

        // Bước 2: Sau 1.2 giây, chuyển sang cherrybombpowie.gif
        Timeline changeToExplosion = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            Image explodeImage = new Image(getClass().getResource("/Plants/cherrybombpowie.gif").toExternalForm());
            imageView.setImage(explodeImage);
            imageView.setFitWidth(240); // To vừa 3 tile
            imageView.setPreserveRatio(true);
            imageView.setLayoutX(tile.getCenterX() - 120); // Dịch ảnh nổ ra giữa vùng 3x3
            imageView.setLayoutY(tile.getCenterY() - 120);

        }));

        // Bước 3: Sau 3.2 giây (tức 2 giây sau khi nổ), ẩn luôn hình
        Timeline disappear = new Timeline(new KeyFrame(Duration.seconds(3.2), e -> {
            imageView.setVisible(false);  // hoặc remove hẳn nếu muốn
            System.out.println("Cherrybomb has disappeared!");
        }));

        // Chạy cả 2 timeline
        changeToExplosion.setCycleCount(1);
        disappear.setCycleCount(1);

        changeToExplosion.play();
        disappear.play();
    }

    @Override
    public Node getNode() {
        return imageView;
    }

    @Override
    public void startBehavior() {
        Timeline bulletFiring = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            new Bullet(imageView.getLayoutX() + 40, imageView.getLayoutY(), pane);
            System.out.println("Cherrybomb explodes!");
        }));
        bulletFiring.setCycleCount(Timeline.INDEFINITE);
        bulletFiring.play();
    }
}
