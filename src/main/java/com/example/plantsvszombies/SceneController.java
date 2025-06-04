package com.example.plantsvszombies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SceneController {
    private Stage stage;
    private Scene scene;
    private String username;

    @FXML
    TextField usernameTextField;

    public void switchToSecondScreen(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SecondScreen.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/fxml/SecondScreen.css").toExternalForm());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSignIn(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/SignIn.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/fxml/SignIn.css").toExternalForm());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void switchToLogIn(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/LogIn.fxml"));
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/fxml/LogIn.css").toExternalForm());
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void handleSignIn(javafx.event.ActionEvent event) {
        String username = usernameTextField.getText().trim();

        if (!username.isEmpty()) {
            try {
                if (isExistingUser(username)) {
                    showAlert("Username already exists!");
                } else {
                    saveUsername(username);
                    this.username = username;
                    switchToMainMenu(event);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please enter your username!");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleLogIn(javafx.event.ActionEvent event) throws IOException {
        String username = usernameTextField.getText().trim();

        if (!username.isEmpty()) {
            if (isExistingUser(username)) {
                this.username = username;
                switchToMainMenu(event);
            } else {
                showAlert("The username does not exist!");
            }
        } else {
            showAlert("Please enter your username!");
        }

    }

    public void switchToMainMenu(javafx.event.ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();

            UsernameController controller = loader.getController();

            controller.display(username);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fxml/MainMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveUsername(String username) throws IOException {
        File file = new File("users.txt");
        if (!file.exists()) file.createNewFile();

        List<String> usernames = Files.readAllLines(file.toPath());
        if (!usernames.contains(username)) {
            FileWriter writer = new FileWriter(file, true);
            writer.write(username + "\n");
            writer.close();
        }
    }

    public boolean isExistingUser(String username) throws IOException {
        File file = new File("users.txt");
        if (!file.exists()) return false;

        List<String> usernames = Files.readAllLines(file.toPath());
        return usernames.contains(username);
    }

    public void exit(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Do you want to exit?");

        if (alert.showAndWait().get() == ButtonType.OK){
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }
}
