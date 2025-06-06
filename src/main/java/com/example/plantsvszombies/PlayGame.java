package com.example.plantsvszombies;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class PlayGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Welcome.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("/Welcome.css").toExternalForm());
            String css = getClass().getResource("/fxml/Welcome.css").toExternalForm();
            scene.getStylesheets().add(css);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);

            primaryStage.setOnCloseRequest(event -> {
                event.consume();

                handleCloseRequest(primaryStage);
            });

            primaryStage.show();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void handleCloseRequest(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to exit?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            stage.close();
        }
    }

    public static void main(String[] args) {
        Media media = new Media(PlayGame.class.getResource("/Audio/Crazy Dave (Intro Theme).mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        launch(args);
    }
}
