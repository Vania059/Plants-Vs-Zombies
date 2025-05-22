module com.example.plantsvszombies {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;

    opens com.example.plantsvszombies to javafx.fxml;

    exports com.example.plantsvszombies;
}