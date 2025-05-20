module com.example.plantsvszombies {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens com.example.plantsvszombies to javafx.fxml;
    opens com.example.plantsvszombies.codeCherrybomb to javafx.fxml;
    opens com.example.plantsvszombies.codePeashooter to javafx.fxml;
    opens com.example.plantsvszombies.codeSunflower to javafx.fxml;
    opens com.example.plantsvszombies.codeWallnut to javafx.fxml;

    exports com.example.plantsvszombies;
    exports com.example.plantsvszombies.codeCherrybomb;
    exports com.example.plantsvszombies.codePeashooter;
    exports com.example.plantsvszombies.codeSunflower;
    exports com.example.plantsvszombies.codeWallnut;
}