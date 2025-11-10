package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javagraphicmain.Main;

public class MenuScene {
    private Scene scene;

    public MenuScene(Main mainApp) {
        Text title = new Text("ARKANOID");
        title.setFont(new Font("Arial", 60));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");


        // Các nút
        Button startButton = createMenuButton("Start Game");
        Button selectLevelButton = createMenuButton("Select Level");
        Button highScoreButton = createMenuButton("High Score");
        Button settingsButton = createMenuButton("Settings");
        Button exitButton = createMenuButton("Exit");

        // Xử lý sự kiện
        startButton.setOnAction(e -> mainApp.showGame(0));
        selectLevelButton.setOnAction(e -> mainApp.showLevelSelect());
        highScoreButton.setOnAction(e -> mainApp.showHighScore());
        settingsButton.setOnAction(e -> mainApp.showSettings());
        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(20, title, startButton, selectLevelButton,
                highScoreButton, settingsButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(layout, 800, 600);
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 24));
        button.setPrefWidth(300);
        button.setPrefHeight(50);
        button.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        // Hiệu ứng hover
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #1976D2; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        ));

        return button;
    }

    public Scene getScene() {
        return scene;
    }
}
