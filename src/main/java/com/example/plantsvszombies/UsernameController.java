package com.example.plantsvszombies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class UsernameController {
    private Stage stage;
    private Scene scene;
    private Parent parent;
    private String username;

    @FXML
    Label welcomeLabel;

    public void display(String username) {
        this.username = username;
        welcomeLabel.setText(username);
    }

    public void switchToGameScene (javafx.event.ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameScene.fxml"));
        Parent root = loader.load();

        GameSceneController controller = loader.getController();
        controller.setUsername(username); // Truyền username

        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/fxml/GameScene.css").toExternalForm());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToWelcomeScene (javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SecondScreen.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/fxml/SecondScreen.css").toExternalForm());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void exit(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to exit?");

        if (alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
