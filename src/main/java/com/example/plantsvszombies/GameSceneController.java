package com.example.plantsvszombies;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class GameSceneController implements Initializable {

    @FXML
    private AnchorPane gamePane;
    @FXML
    private Label sunLabel;
    @FXML
    private AnchorPane levelPane;
    @FXML
    private AnchorPane winPane;
    @FXML
    private AnchorPane losePane;
    @FXML
    private Label level2Label;

    private List<Zombie> zombies = new ArrayList<>();
    private Tile[][] grid = new Tile[5][9];
    private int sunPoints = 0;

    private List<PlantCard> plantCards;
    private PlantCard selectedCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        winPane.setVisible(false);
        losePane.setVisible(false);

        setupGrid();
        spawnZombies();
        startSkySun();

        plantCards = new ArrayList<>();

        PlantCard peashooterCard = new PlantCard(250, "peashooter", 100, gamePane, this);
        PlantCard sunflowerCard = new PlantCard(330, "sunflower", 50, gamePane, this);
        PlantCard wallnutCard = new PlantCard(410, "walnut", 50, gamePane, this);
        PlantCard cherrybombCard = new PlantCard(490, "cherrybomb", 150, gamePane, this);

        plantCards.add(peashooterCard);
        plantCards.add(sunflowerCard);
        plantCards.add(wallnutCard);
        plantCards.add(cherrybombCard);

        // Sau đó cập nhật trạng thái theo điểm mặt trời
        updatePlantCards();

        // Đặt vị trí ban đầu: ở ngoài cùng dưới màn hình
        levelPane.setTranslateY(540); // Giả sử scene cao khoảng 540px

        // Bước 1: Pane chạy từ dưới lên giữa (300)
        TranslateTransition up = new TranslateTransition(Duration.seconds(1), levelPane);
        up.setToY(0); // Đi lên giữa màn hình

        // Bước 2: Dừng lại 2 giây ở giữa
        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        // Bước 3: Chạy từ giữa xuống lại dưới
        TranslateTransition down = new TranslateTransition(Duration.seconds(1), levelPane);
        down.setToY(540); // Quay về vị trí dưới

        // Khi kết thúc chạy xuống thì ẩn pane
        down.setOnFinished(event -> levelPane.setVisible(false));

        // Kết hợp chuỗi
        up.setOnFinished(e -> pause.play());
        pause.setOnFinished(e -> down.play());

        // Bắt đầu chuỗi
        up.play();
        levelPane.toFront();
        levelPane.setVisible(true);

        level2Label.setOnMouseClicked(event -> {
            startLevel2();
        });
    }

    private void setupGrid() {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                // Truyền đúng controller (this) vào Tile
                Tile tile = new Tile(col, row, this);
                tile.setPane(gamePane);
                grid[row][col] = tile;

                tile.getTileNode().setOnMouseClicked(e -> {
                    if (selectedCard != null && !tile.hasPlant()) {
                        plantSelectedPlant(tile, gamePane);
                    }
                });
            }
        }
    }

    public Tile[][] getGrid() {
        return grid;
    }

    private void spawnZombies() {
        int x = 1000; // vị trí ngoài scene

        // Tạo Timeline spawn zombie
        Timeline spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(20), e -> {
            List<Integer> lanes = new ArrayList<>(Arrays.asList(0, 100, 200, 300, 400));
            Collections.shuffle(lanes);
            for (int i = 0; i < 3; i++) {
                int y = lanes.get(i);
                Normal_zombie zombie = new Normal_zombie(x, y, this);
                zombies.add(zombie);
                gamePane.getChildren().add(zombie.getView());
                zombie.startWalking();
                zombie.moveToPlant(grid);
            }
        }));
        spawnTimeline.setCycleCount(5); // hoặc Timeline.INDEFINITE nếu muốn lặp mãi

        // Tạo PauseTransition 15s trước khi bắt đầu spawn
        PauseTransition delay = new PauseTransition(Duration.seconds(30));
        delay.setOnFinished(event -> {
            // Spawn lần đầu sau 15s
            Media media = new Media(PlayGame.class.getResource("/Audio/zombies_coming.mp3").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            List<Integer> lanes = new ArrayList<>(Arrays.asList(0, 100, 200, 300, 400));
            Collections.shuffle(lanes);
            for (int i = 0; i < 3; i++) {
                int y = lanes.get(i);
                Normal_zombie zombie = new Normal_zombie(x, y, this);
                zombies.add(zombie);
                gamePane.getChildren().add(zombie.getView());
                zombie.startWalking();
                zombie.moveToPlant(grid);
            }

            // Bắt đầu Timeline sau lần spawn đầu tiên
            spawnTimeline.play();
        });

        // Bắt đầu đếm 15s
        delay.play();
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    private void startSkySun() {
        Random random = new Random();
        Timeline skySun = new Timeline(new KeyFrame(Duration.seconds(7), e -> {
            int col = random.nextInt(8);
            int row = random.nextInt(5);
            Tile tile = grid[row][col];
            new SunToken(tile.getCenterX(), tile.getCenterY() - 530, gamePane, this, false);
        }));
        skySun.setCycleCount(Timeline.INDEFINITE);
        skySun.play();
    }

    public void addSunPoints(int points) {
        sunPoints += points;
        sunLabel.setText("" + sunPoints);
        updatePlantCards();
    }

    public int getSunPoints() {
        return sunPoints;
    }

    public void deductSunPoints(int points) {
        sunPoints -= points;
        sunLabel.setText("" + sunPoints);
        updatePlantCards();
    }

    public void selectPlant(String plantType) {
        for (PlantCard card : plantCards) {
            card.setSelected(card.getPlantType().equals(plantType));
            if (card.getPlantType().equals(plantType)) {
                selectedCard = card;
                System.out.println("Set selectedCard to: " + plantType);
            }
        }
        if (selectedCard == null) {
            System.out.println("Warning: selectedCard is still null after selecting " + plantType);
        }
    }

    public void plantSelectedPlant(Tile tile, Pane pane) {
        if (tile.hasPlant()) {
            System.out.println("Tile at row " + tile.getRow() + ", col " + tile.getCol() + " already has a plant!");
            // (Có thể hiện thông báo lên UI cho người chơi ở đây)
            return;
        }
        System.out.println("Attempting to plant on tile at row " + tile.getRow() + ", col " + tile.getCol());
        System.out.println("Current sun points: " + sunPoints);
        System.out.println("Selected card: " + (selectedCard != null ? selectedCard.getPlantType() : "none"));
        int cost = selectedCard.getCost();
        String type = selectedCard.getPlantType().toLowerCase();
        Plant plant = null;
        switch (type) {
            case "sunflower":
                plant = new Sunflower(tile, gamePane, this);
                break;
            case "peashooter":
                plant = new Peashooter(tile, gamePane, this);
                break;
            case "cherrybomb":
                plant = new Cherrybomb(tile, gamePane, this);
                break;
            case "walnut":
                plant = new Wallnut(tile, gamePane);
                break;
        }
        if (plant != null) {
            tile.setPlant(plant);
            gamePane.getChildren().addAll(plant.getNode());
            deductSunPoints(cost); // Trừ điểm tại đây
            // >>> Thêm dòng này: reset selectedCard sau khi trồng!
            selectedCard = null;
            // >>> Optionally: cập nhật trạng thái hiển thị (bỏ highlight)
            for (PlantCard card : plantCards) {
                card.setSelected(false);
            }
        }
    }

    private void updatePlantCards() {
        for (PlantCard card : plantCards) {
            card.updateState(sunPoints);
        }
    }

    public PlantCard getSelectedCard() {
        return selectedCard;
    }

    public void showWinScreen() {
        winPane.setVisible(true);
        winPane.toFront();
        winPane.setOpacity(1);
        winPane.setScaleX(0.1);
        winPane.setScaleY(0.1);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), winPane);
        scale.setFromX(0.1);
        scale.setFromY(0.1);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setCycleCount(0);
        scale.play();
    }

    public void showLoseScreen() {
        losePane.setVisible(true);
        losePane.toFront();
        losePane.setOpacity(1);
        losePane.setScaleX(0.1);
        losePane.setScaleY(0.1);

        // Phóng to lên 1.0 trong 0.5 giây
        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), losePane);
        scale.setFromX(0.1);
        scale.setFromY(0.1);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setCycleCount(0);
        scale.play();
    }

    public void startLevel2() {
        // Ẩn winPane
        winPane.setVisible(false);

        // Xóa zombie cũ
        for (Zombie zombie : zombies) {
            gamePane.getChildren().remove(zombie.getView());
        }
        zombies.clear();

        // Xóa plants trên grid
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                Tile tile = grid[row][col];
                if (tile.hasPlant()) {
                    Plant plant = tile.getPlant();
                    if (plant instanceof Sunflower) {
                        ((Sunflower) plant).stopBehavior();
                    }
                    if (plant instanceof Peashooter) {
                        ((Peashooter) plant).stopBehavior();
                    }
                    gamePane.getChildren().remove(plant.getNode());
                    tile.setPlant(null);
                }
            }
        }

        List<Node> nodesToRemove = new ArrayList<>();
        for (Node node : gamePane.getChildren()) {
            Object ud = node.getUserData();
            if ("bullet".equals(ud) || "suntoken".equals(ud)) {
                nodesToRemove.add(node);
            }
        }
        gamePane.getChildren().removeAll(nodesToRemove);

        // Reset sunPoints
        sunPoints = 0; // hoặc theo ý bạn
        sunLabel.setText("" + sunPoints);

        // Bắt đầu spawn zombie cho level 2
        spawnZombiesLevel2();
    }

    private void spawnZombiesLevel2() {
        int x = 1000;

        // VD: spawn cả Normal_zombie và Jump_zombie
        Timeline spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(12), e -> {
            List<Integer> lanes = new ArrayList<>(Arrays.asList(0, 100, 200, 300, 400));
            Collections.shuffle(lanes);
            for (int i = 0; i < 2; i++) {
                int y = lanes.get(i);
                Normal_zombie zombie = new Normal_zombie(x, y, this);
                zombies.add(zombie);
                gamePane.getChildren().add(zombie.getView());
                zombie.startWalking();
                zombie.moveToPlant(grid);
            }
            // Thêm Jump_zombie vào lane khác
            int yj = lanes.get(2);
            Jump_zombie jumpZombie = new Jump_zombie(x, yj, this);
            zombies.add(jumpZombie);
            gamePane.getChildren().add(jumpZombie.getView());
            // jumpZombie sẽ tự startJumpingAndMoving trong constructor
        }));
        spawnTimeline.setCycleCount(7); // nhiều wave hơn level 1
        spawnTimeline.play();
    }
}
