package com.example.plantsvszombies.codeWallnut;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WallnutApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        GamePane gamePane = new GamePane();
        Scene scene = new Scene(gamePane, 600, 400);

        primaryStage.setTitle("Wallnut");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
