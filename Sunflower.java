package pvz;

import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.util.Duration;

public class Sunflower extends Plant {
    private Pane pane;
    private PvZGame game;

    public Sunflower(Tile tile, Pane pane, PvZGame game) {
        super(tile, Color.YELLOW);
        this.pane = pane;
        this.game = game;
        startBehavior();
    }

    @Override
    public void startBehavior() {
        Timeline sunProduction = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            new SunToken(head.getCenterX(), head.getCenterY(), pane, game, true);
        }));
        sunProduction.setCycleCount(Timeline.INDEFINITE);
        sunProduction.play();
    }
}