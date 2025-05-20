package Peashooter;

import javafx.scene.layout.Pane;

public class GamePane extends Pane {
    private final Peashooter peashooter;

    public GamePane() {
        peashooter = new Peashooter();
        getChildren().add(peashooter.getImageView());

        peashooter.getImageView().setTranslateX(100);
        peashooter.getImageView().setTranslateY(150);
    }
}