package com.example.plantsvszombies;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameSceneController implements Initializable {

    @FXML
    private AnchorPane gamePane;
    @FXML
    private Label sunLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private AnchorPane levelPane;
    @FXML
    private AnchorPane winPane;
    @FXML
    private AnchorPane losePane;
    @FXML
    private Label continueLabel;

    public Pane backgroundLayer = new Pane();
    public Pane plantLayer = new Pane();
    public Pane zombieLayer = new Pane();
    public Pane bulletLayer = new Pane();
    public Pane uiLayer = new Pane(); // win/lose panel

    private String username;

    private final List<Zombie> zombies = new ArrayList<>();
    private final Tile[][] grid = new Tile[5][9];
    private int sunPoints = 0;

    private List<PlantCard> plantCards;
    private PlantCard selectedCard;

    private Level currentLevel = Level.LEVEL_1;

    private enum Level {
        LEVEL_1,
        LEVEL_2,
        LEVEL_3
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gamePane.getChildren().addAll(
                backgroundLayer,
                plantLayer,
                zombieLayer,
                bulletLayer,
                uiLayer
        );

        winPane.setVisible(false);
        losePane.setVisible(false);

        setupGrid();
        startSkySun();

        plantCards = new ArrayList<>();

        PlantCard peashooterCard = new PlantCard(190, "peashooter", 100, gamePane, this);
        PlantCard sunflowerCard = new PlantCard(270, "sunflower", 50, gamePane, this);
        PlantCard wallnutCard = new PlantCard(350, "walnut", 50, gamePane, this);
        PlantCard cherrybombCard = new PlantCard(430, "cherrybomb", 150, gamePane, this);

        plantCards.add(peashooterCard);
        plantCards.add(sunflowerCard);
        plantCards.add(wallnutCard);
        plantCards.add(cherrybombCard);

        updatePlantCards();

        startLevel(Level.LEVEL_1);
    }

    private void showLevelIntro(String levelText, Runnable onFinished) {
        levelPane.setVisible(true); // Hiển thị Pane chứa dòng chữ "Level x"
        levelPane.toFront();        // Đưa nó lên trên cùng

        levelLabel.setText(levelText);

        // Hiệu ứng chạy lên
        TranslateTransition up = new TranslateTransition(Duration.seconds(1), levelPane);
        up.setToY(0);

        // Dừng 2 giây
        PauseTransition pause = new PauseTransition(Duration.seconds(2));

        // Hiệu ứng chạy xuống
        TranslateTransition down = new TranslateTransition(Duration.seconds(1), levelPane);
        down.setToY(540);
        down.setOnFinished(event -> {
            levelPane.setVisible(false);
            if (onFinished != null) onFinished.run(); // Gọi spawnZombies sau hiệu ứng
        });

        // Chuỗi hiệu ứng
        up.setOnFinished(e -> pause.play());
        pause.setOnFinished(e -> down.play());

        // Khởi động hiệu ứng
        levelPane.setTranslateY(540); // Ẩn Pane lúc ban đầu
        up.play();
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

        Timeline spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            List<Integer> lanes = new ArrayList<>(Arrays.asList(0, 100, 190, 290, 370));
            Collections.shuffle(lanes);
            for (int i = 0; i < 1; i++) {
                int y = lanes.get(i);
                Normal_zombie zombie = new Normal_zombie(x, y, this);
                zombies.add(zombie);
                gamePane.getChildren().add(zombie.getView());
                zombie.startWalking();
                zombie.moveToPlant(grid);
            }
        }));
        spawnTimeline.setCycleCount(10); // hoặc Timeline.INDEFINITE nếu muốn lặp mãi

        // Tạo PauseTransition 25s trước khi bắt đầu spawn
        PauseTransition delay = new PauseTransition(Duration.seconds(25));
        delay.setOnFinished(event -> {
            // Spawn lần đầu sau 15s
            Media media = new Media(PlayGame.class.getResource("/Audio/zombies_coming.mp3").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            List<Integer> lanes = new ArrayList<>(Arrays.asList(0, 100, 190, 290, 380));
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
        if (sunPoints < cost) {
            System.out.println("Not enough sun points to plant " + selectedCard.getPlantType());
            return;
        }
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
        if (winPane.isVisible()) return; // Đã hiện thì thôi
        winPane.setVisible(true);

        winPane.toFront();
        winPane.setOpacity(1);
        winPane.setScaleX(0.1);
        winPane.setScaleY(0.1);

        String nextLevelText = "Continue to Level ";
        switch (currentLevel) {
            case LEVEL_1:
                nextLevelText += "2";
                break;
            case LEVEL_2:
                nextLevelText += "3";
                break;
            case LEVEL_3:
                nextLevelText = "Back to Menu";  // hoặc gì đó phù hợp
                break;
        }
        continueLabel.setText(nextLevelText);

        ScaleTransition scale = new ScaleTransition(Duration.seconds(0.5), winPane);
        scale.setFromX(0.1);
        scale.setFromY(0.1);
        scale.setToX(1.0);
        scale.setToY(1.0);
        scale.setCycleCount(1);
        scale.play();
    }

    public void showLoseScreen() {
        if (losePane.isVisible()) return; // Đã hiện thì thôi
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
        scale.setCycleCount(1);
        scale.play();
    }

    public void switchToMainMenu(MouseEvent event) throws IOException {
        for (Zombie zombie : zombies) {
            zombie.cleanup();
        }
        zombies.clear();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainMenu.fxml"));
            Parent root = loader.load();

            UsernameController controller = loader.getController();
            controller.display(username); // Gửi username sang MainMenuController

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/fxml/MainMenu.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetGameState() {
        // Remove zombies
        for (Zombie zombie : zombies) {
            zombie.cleanup();
            gamePane.getChildren().remove(zombie.getView());
        }
        zombies.clear();

        // Remove plants
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                Tile tile = grid[row][col];
                if (tile.hasPlant()) {
                    Plant plant = tile.getPlant();
                    plant.stopBehavior();
                    gamePane.getChildren().remove(plant.getNode());
                    tile.setPlant(null);
                }
            }
        }

        // Remove bullets and sun tokens
        List<Node> toRemove = new ArrayList<>();
        for (Node node : gamePane.getChildren()) {
            Object ud = node.getUserData();
            if ("bullet".equals(ud) || "suntoken".equals(ud)) {
                toRemove.add(node);
            }
        }
        gamePane.getChildren().removeAll(toRemove);

        // Reset sun points and UI
        sunPoints = 0;
        sunLabel.setText("" + sunPoints);
        selectedCard = null;
        for (PlantCard card : plantCards) {
            card.setSelected(false);
        }
    }

    private void startLevel(Level level) {
        currentLevel = level;

        resetGameState(); // reset trạng thái game (đã tạo ở mục 1)

        switch (level) {
            case LEVEL_1:
                showLevelIntro("Level 1", () -> spawnZombies());
                break;
            case LEVEL_2:
                showLevelIntro("Level 2", () -> spawnZombiesLevel2());
                break;
            // Nếu có thêm LEVEL_3 thì thêm case vào đây
            case LEVEL_3:
                showLevelIntro("Level 3", () -> spawnZombiesLevel3());
                break;
        }
    }

    public void onContinueClicked() throws IOException {
        winPane.setVisible(false);
        losePane.setVisible(false);
        if (currentLevel == Level.LEVEL_1) {
            startLevel(Level.LEVEL_2);
        } else if (currentLevel == Level.LEVEL_2) {
            startLevel(Level.LEVEL_3);
        } else {
            // Nếu là level cuối rồi, có thể quay về menu hoặc làm gì đó
            switchToMainMenu(null);
        }
    }

    public void onReplayClicked() {
        winPane.setVisible(false);
        losePane.setVisible(false);
        startLevel(currentLevel);  // reset lại đúng level hiện tại
    }

    public void spawnZombiesLevel2() {
        int x = 1000;

        // VD: spawn cả Normal_zombie và Jump_zombie
        Timeline spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(5), e -> {
            List<Integer> lanes = new ArrayList<>(Arrays.asList(0, 100, 190, 290, 370));
            Collections.shuffle(lanes);
            for (int i = 0; i < 1; i++) {
                int y = lanes.get(i);
                Normal_zombie zombie = new Normal_zombie(x, y, this);
                zombies.add(zombie);
                gamePane.getChildren().add(zombie.getView());
                zombie.startWalking();
                zombie.moveToPlant(grid);
            }
            // Thêm Jump_zombie vào lane khác
            int yj = lanes.get(2);
            Jump_zombie zombie = new Jump_zombie(x, yj, this);
            zombies.add(zombie);
            gamePane.getChildren().add(zombie.getView());
            zombie.startWalking();
            zombie.moveToPlant(grid);
            // jumpZombie sẽ tự startJumpingAndMoving trong constructor
        }));
        spawnTimeline.setCycleCount(10); // nhiều wave hơn level 1
        spawnTimeline.play();
    }

    public void spawnZombiesLevel3() {
        int x = 1000;

        // VD: spawn cả Normal_zombie và Jump_zombie
        Timeline spawnTimeline = new Timeline(new KeyFrame(Duration.seconds(25), e -> {
            List<Integer> lanes = new ArrayList<>(Arrays.asList(0, 100, 190, 290, 370));
            Collections.shuffle(lanes);

            Random rand = new Random();
            double t1 = 1 + rand.nextDouble() * 3;  // khoảng 1-4s
            double t2 = 4 + rand.nextDouble() * 3;  // khoảng 4-7s
            double t3 = 7 + rand.nextDouble() * 3;  // khoảng 7-10s

            // Normal_zombie
            PauseTransition delay1 = new PauseTransition(Duration.seconds(t1));
            delay1.setOnFinished(ev -> {
                int y1 = lanes.get(0);
                Normal_zombie normalZombie = new Normal_zombie(x, y1, this);
                zombies.add(normalZombie);
                gamePane.getChildren().add(normalZombie.getView());
                normalZombie.startWalking();
                normalZombie.moveToPlant(grid);
            });

            // Jump_zombie
            PauseTransition delay2 = new PauseTransition(Duration.seconds(t2));
            delay2.setOnFinished(ev -> {
                int y2 = lanes.get(1);
                Jump_zombie jumpZombie = new Jump_zombie(x, y2, this);
                zombies.add(jumpZombie);
                gamePane.getChildren().add(jumpZombie.getView());
                jumpZombie.jump();
                jumpZombie.moveToPlant(grid);
            });

            // Boss_zombie
            PauseTransition delay3 = new PauseTransition(Duration.seconds(t3));
            delay3.setOnFinished(ev -> {
                int y3 = lanes.get(2);
                Boss_zombie bossZombie = new Boss_zombie(x, y3, this);
                zombies.add(bossZombie);
                gamePane.getChildren().add(bossZombie.getView());
                bossZombie.startWalking();
                bossZombie.moveToPlant(grid);
            });

            delay1.play();
            delay2.play();
            delay3.play();
        }));

        spawnTimeline.setCycleCount(3);
        spawnTimeline.play();
    }
}
