package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javagraphicmain.Main;
import sound.SoundManager;

public class HighScoreScene {
    private Scene scene;
    private SoundManager soundManager;

    public HighScoreScene(Main mainApp) {
        soundManager = SoundManager.getInstance();

        Text title = new Text("HIGH SCORE");
        title.setFont(new Font("Arial", 48));
        title.setFill(Color.GOLD);
        title.setStyle("-fx-font-weight: bold;");

        int highScore = loadHighScore();

        Text scoreText = new Text("Best Score: " + highScore);
        scoreText.setFont(new Font("Arial", 36));
        scoreText.setFill(Color.WHITE);

        Text infoText = new Text("Keep playing to beat this record!");
        infoText.setFont(new Font("Arial", 20));
        infoText.setFill(Color.LIGHTGRAY);

        Button backButton = createButton("Back to Menu");
        backButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showMenu();
        });

        Button resetButton = createButton("Reset High Score");
        resetButton.setStyle(
                "-fx-background-color: #f44336; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );
        resetButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            resetHighScore();
            mainApp.showHighScore();
        });

        VBox layout = new VBox(30, title, scoreText, infoText, backButton, resetButton);
        layout.setAlignment(Pos.CENTER);

        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/image/menuBranchs/menuBranch.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền!");
        }

        if (bgImage != null) {
            BackgroundSize bgSize = new BackgroundSize(800, 800, false, false, false, false);
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, bgSize);
            layout.setBackground(new Background(backgroundImage));
        } else {
            layout.setBackground(new Background(new BackgroundFill(
                    Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        scene = new Scene(layout, 800, 600);
    }

    private int loadHighScore() {
        try {
            java.io.File file = new java.io.File("HighScore.txt");
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                if (scanner.hasNextInt()) {
                    int score = scanner.nextInt();
                    scanner.close();
                    return score;
                }
                scanner.close();
            }
        } catch (Exception e) {
            System.err.println("Error loading high score: " + e.getMessage());
        }
        return 0;
    }

    private void resetHighScore() {
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter("HighScore.txt");
            writer.println(0);
            writer.close();
        } catch (Exception e) {
            System.err.println("Error resetting high score: " + e.getMessage());
        }
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 20));
        button.setPrefWidth(250);
        button.setPrefHeight(50);
        button.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

        // THÊM ÂM THANH HOVER
        button.setOnMouseEntered(e -> {
            soundManager.playSound("button_hover");
            String currentColor = button.getStyle().contains("#f44336") ? "#da190b" : "#1976D2";
            button.setStyle(
                    "-fx-background-color: " + currentColor + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            );
        });
        button.setOnMouseExited(e -> {
            String currentColor = button.getStyle().contains("#da190b") ? "#f44336" : "#2196F3";
            button.setStyle(
                    "-fx-background-color: " + currentColor + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            );
        });

        return button;
    }

    public Scene getScene() {
        return scene;
    }
}