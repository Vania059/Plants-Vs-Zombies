package pvz;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import java.util.Random;

public class SunToken {
    private ImageView sunView;
    private Pane pane;
    private PvZGame game;
    private boolean isCollected = false;

    public SunToken(double x, double y, Pane pane, PvZGame game, boolean noDrop) {
        this.pane = pane;
        this.game = game;

        Image sunImage = new Image(getClass().getResourceAsStream("/sun.png"));
        sunView = new ImageView(sunImage);
        sunView.setFitWidth(30);
        sunView.setFitHeight(30);
        sunView.setX(x - 15);
        sunView.setY(y - 15);


        pane.getChildren().add(sunView);

        if (noDrop) {
            sunView.setOnMouseClicked(event -> {
                collect();
            });

            Timeline disappear = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                if (!isCollected) {
                    pane.getChildren().remove(sunView);
                }
            }));
            disappear.setCycleCount(1);
            disappear.play();
        } else {
            Random random = new Random();
            double maxDropDistance = 500 - y;
            double dropDistance = 100 + random.nextDouble() * (maxDropDistance - 100);
            TranslateTransition drop = new TranslateTransition(Duration.seconds(3), sunView);
            drop.setByY(dropDistance);
            drop.setOnFinished(e -> {
                if (!isCollected) {
                    sunView.setOnMouseClicked(event -> {
                        collect();
                    });

                    Timeline disappear = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                        if (!isCollected) {
                            pane.getChildren().remove(sunView);
                        }
                    }));
                    disappear.setCycleCount(1);
                    disappear.play();
                }
            });
            drop.play();
        }
    }

    private void collect() {
        if (!isCollected) {
            isCollected = true;
            pane.getChildren().remove(sunView);
            game.addSunPoints(50);
        }
    }
}