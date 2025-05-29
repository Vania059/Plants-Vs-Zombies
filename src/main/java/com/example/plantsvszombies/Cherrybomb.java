package com.example.plantsvszombies;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class Cherrybomb extends Plant{
    private final ImageView imageView;
    private final int TILE_WIDTH = 85;
    private final int TILE_HEIGHT = 95;
    private final int OFFSET_X = 95;
    private final int OFFSET_Y = 55;

    public Cherrybomb(Tile tile, Pane pane, GameSceneController controller) {
        super(tile, pane);
        // Bước 1: Hiển thị cherrybomb.gif
        Image idleImage = new Image(getClass().getResource("/Plants/cherrybomb.gif").toExternalForm());
        imageView = new ImageView(idleImage);
        imageView.setFitWidth(80);
        imageView.setPreserveRatio(true);
        imageView.setLayoutX(tile.getCenterX() - 50);
        imageView.setLayoutY(tile.getCenterY() - 50);

        // Bước 2: Sau 2 giây, chuyển sang cherrybombpowie.gif
        Timeline changeToExplosion = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> {
            Image explodeImage = new Image(getClass().getResource("/Plants/cherrybombpowie.gif").toExternalForm());
            imageView.setImage(explodeImage);
            imageView.setFitWidth(240); // To vừa 3 tile
            imageView.setPreserveRatio(true);
            imageView.setLayoutX(tile.getCenterX() - 120); // Dịch ảnh nổ ra giữa vùng 3x3
            imageView.setLayoutY(tile.getCenterY() - 120);

        // Xác định vùng nổ 3x3 quanh tile Cherrybomb
        int bombRow = tile.getRow();
        int bombCol = tile.getCol();

        for (Zombie zombie : controller.getZombies()) {
            double zombieCenterX = zombie.getView().getLayoutX() + zombie.getView().getFitWidth() / 2;
            double zombieCenterY = zombie.getView().getLayoutY() + zombie.getView().getFitHeight() / 2;
            int zCol = (int) ((zombieCenterX - OFFSET_X) / TILE_WIDTH);
            int zRow = (int) ((zombieCenterY - OFFSET_Y) / TILE_HEIGHT);

            if (Math.abs(zRow - bombRow) <= 1 && Math.abs(zCol - bombCol) <= 1) {
                zombie.HP = 0;
                zombie.die();
            }
        }
    }));

        // Bước 3: Sau 3.2 giây (tức 2 giây sau khi nổ), ẩn luôn hình
        Timeline disappear = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            imageView.setVisible(false);  // hoặc remove hẳn nếu muốn
            tile.setPlant(null);
            pane.getChildren().remove(getNode());
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
    public void startBehavior() {}

    @Override
    public void stopBehavior() {}

}
