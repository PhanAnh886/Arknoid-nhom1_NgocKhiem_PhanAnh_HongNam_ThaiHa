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

public class GameOverScene {
    private Scene scene;

    public GameOverScene(Main mainApp, int finalScore, int highScore) {
        Text title = new Text("GAME OVER");
        title.setFont(new Font("Arial", 60));
        title.setFill(Color.RED);
        title.setStyle("-fx-font-weight: bold;");

        Text scoreText = new Text("Your Score: " + finalScore);
        scoreText.setFont(new Font("Arial", 32));
        scoreText.setFill(Color.WHITE);

        Text highScoreText = new Text("High Score: " + highScore);
        highScoreText.setFont(new Font("Arial", 28));
        highScoreText.setFill(Color.GOLD);


        Button retryButton = createButton("Retry Level", Color.web("#4CAF50"));
        Button menuButton = createButton("Main Menu", Color.web("#2196F3"));
        Button exitButton = createButton("Exit Game", Color.web("#f44336"));

        //  Khi bấm Retry → quay lại chơi game (bắt đầu lại)
        retryButton.setOnAction(e -> mainApp.retryCurrentLevel());

        //  Khi bấm Menu → quay lại menu chính
        menuButton.setOnAction(e -> mainApp.showMenu());

        //  Khi bấm Exit → thoát game
        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(25, title, scoreText, highScoreText, retryButton, menuButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(layout, 800, 600);
    }

    private Button createButton(String text, Color color) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 24));
        button.setPrefWidth(280);
        button.setPrefHeight(55);

        String hexColor = String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));

        button.setStyle(
                "-fx-background-color: " + hexColor + "; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand; " +
                        "-fx-font-weight: bold;"
        );

        // Hiệu ứng hover
        button.setOnMouseEntered(e -> {
            button.setStyle(
                    "-fx-background-color: derive(" + hexColor + ", -20%); " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand; " +
                            "-fx-font-weight: bold;"
            );
        });
        button.setOnMouseExited(e -> {
            button.setStyle(
                    "-fx-background-color: " + hexColor + "; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand; " +
                            "-fx-font-weight: bold;"
            );
        });

        return button;
    }

    public Scene getScene() {
        return scene;
    }
}
