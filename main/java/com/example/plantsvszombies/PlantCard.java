package pvz;

import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class PlantCard {
    private Node plantImage;
    private String plantType;
    private int cost;
    private Pane pane;
    private PvZGame game;
    private boolean isEnabled;

    public PlantCard(double x, String plantType, int cost, Pane pane, PvZGame game) {
        this.plantType = plantType;
        this.cost = cost;
        this.pane = pane;
        this.game = game;
        this.isEnabled = true;

        if (pane == null) {
            System.out.println("Error: Pane is null in PlantCard constructor");
            return;
        }

        // Load image based on plant type
        String imagePath;
        switch (plantType.toLowerCase()) {
            case "walnut":
                imagePath = "/walnutcard.png";
                break;
            case "peashooter":
                imagePath = "/peashootercard.png";
                break;
            case "cherrybomb":
                imagePath = "/cherrybombcard.png";
                break;
            case "sunflower":
            default:
                imagePath = "/sunflowercard.png";
                break;
        }

        try {
            System.out.println("Attempting to load image from path: " + getClass().getResource(imagePath));
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            if (image.isError()) {
                throw new Exception("Image loading error");
            }
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(64);
            imageView.setX(x);
            imageView.setY(10);
            plantImage = imageView;
            System.out.println("Successfully loaded " + imagePath + " for " + plantType);
        } catch (Exception e) {
            System.err.println("Failed to load image for " + plantType + ": " + e.getMessage());
            Rectangle placeholder = new Rectangle(x, 10, 80, 64);
            placeholder.setFill(Color.LIGHTGRAY);
            placeholder.setStroke(Color.BLACK);
            placeholder.setStrokeWidth(1);

            Text placeholderText = new Text(x + 5, 40, plantType);
            placeholderText.setFont(new Font(12));
            placeholderText.setFill(Color.BLACK);

            Pane placeholderPane = new Pane(placeholder, placeholderText);
            plantImage = placeholderPane;
            System.out.println("Using placeholder for " + plantType);
        }

        if (plantImage != null) {
            pane.getChildren().add(plantImage);
            plantImage.toFront(); // Ensure card is clickable
            System.out.println("Added PlantCard for " + plantType + " at x=" + x + ", y=10, children count: " + pane.getChildren().size());
            plantImage.setOnMouseClicked(this::handleClick);
        }

        updateState(game.getSunPoints());
    }

    private void handleClick(MouseEvent event) {
        if (isEnabled) {
            game.selectPlant(plantType);
            System.out.println("Selected plant: " + plantType);
        } else {
            System.out.println("Cannot select plant: " + plantType + " (not enough sun points, need " + cost + ", have " + game.getSunPoints() + ")");
        }
    }

    public void updateState(int sunPoints) {
        isEnabled = sunPoints >= cost;
        if (plantImage != null) {
            plantImage.setOpacity(isEnabled ? 1.0 : 0.5);
        }
    }

    public void setSelected(boolean isSelected) {
        if (plantImage != null) {
            plantImage.setOpacity(isSelected ? 1.0 : (isEnabled ? 1.0 : 0.5));
        }
    }

    public String getPlantType() {
        return plantType;
    }

    public int getCost() {
        return cost;
    }
}