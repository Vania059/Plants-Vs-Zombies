package Wallnut;

import javafx.scene.layout.Pane;

public class GamePane extends Pane {
    private final Wallnut wallnut;

    public GamePane() {
        wallnut = new Wallnut();
        getChildren().add(wallnut.getImageView());

        wallnut.getImageView().setTranslateX(200);
        wallnut.getImageView().setTranslateY(150);
    }
}
