package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javagraphicmain.Main;

public class LevelSelectScene {
    private Scene scene;

    public LevelSelectScene(Main mainApp) {
        Text title = new Text("SELECT LEVEL");
        title.setFont(new Font("Arial", 48));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");

        // Các nút chọn level
        Button level0Button = createLevelButton("Level 0\nDemo");
        Button level1Button = createLevelButton("Level 1\nHeart");
        Button level2Button = createLevelButton("Level 2\nColumns");

        level0Button.setOnAction(e -> mainApp.showGame(0));
        level1Button.setOnAction(e -> mainApp.showGame(1));
        level2Button.setOnAction(e -> mainApp.showGame(2));

        HBox levelButtons = new HBox(30, level0Button, level1Button, level2Button);
        levelButtons.setAlignment(Pos.CENTER);

        Button backButton = createBackButton("Back to Menu");
        backButton.setOnAction(e -> mainApp.showMenu());

        VBox layout = new VBox(40, title, levelButtons, backButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(layout, 1000, 600);
    }

    private Button createLevelButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 20));
        button.setPrefWidth(180);
        button.setPrefHeight(120);
        button.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #45a049; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #4CAF50; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-cursor: hand;"
        ));

        return button;
    }

    private Button createBackButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 20));
        button.setPrefWidth(200);
        button.setPrefHeight(50);
        button.setStyle(
                "-fx-background-color: #f44336; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #da190b; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        ));
        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: #f44336; " +
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
