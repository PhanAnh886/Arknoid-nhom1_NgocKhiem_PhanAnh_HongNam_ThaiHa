package ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
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
import javafx.util.Duration;
import javagraphicmain.Main;

/**
 * Màn hình chuyển level - Hiển thị thông tin và có animation
 */
public class NextLevelScene {
    private Scene scene;

    public NextLevelScene(Main mainApp, int currentLevel, int score, int lives) {
        Text congratsText = new Text("LEVEL COMPLETED!");
        congratsText.setFont(new Font("Arial", 50));
        congratsText.setFill(Color.GOLD);
        congratsText.setStyle("-fx-font-weight: bold;");

        Text levelText = new Text("Level " + currentLevel + " → Level " + (currentLevel + 1));
        levelText.setFont(new Font("Arial", 28));
        levelText.setFill(Color.WHITE);

        Text scoreText = new Text("Score: " + score);
        scoreText.setFont(new Font("Arial", 24));
        scoreText.setFill(Color.LIGHTGREEN);

        Text livesText = new Text("Lives Remaining: " + lives);
        livesText.setFont(new Font("Arial", 24));
        livesText.setFill(Color.ORANGE);

        Button continueButton = createButton("Continue", Color.web("#4CAF50"));
        Button menuButton = createButton("Main Menu", Color.web("#2196F3"));

        continueButton.setOnAction(e -> mainApp.nextLevel(currentLevel + 1));
        menuButton.setOnAction(e -> mainApp.showMenu());

        VBox layout = new VBox(30, congratsText, levelText, scoreText, livesText,
                continueButton, menuButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(
                Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(layout, 1000, 600);

        // === ANIMATION ===
        addAnimations(congratsText, continueButton);
    }

    /**
     * Thêm hiệu ứng animation cho text và button
     */
    private void addAnimations(Text text, Button button) {
        // Animation cho text: Scale + Fade
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(500), text);
        scaleIn.setFromX(0.5);
        scaleIn.setFromY(0.5);
        scaleIn.setToX(1.2);
        scaleIn.setToY(1.2);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(300), text);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        SequentialTransition textAnim = new SequentialTransition(scaleIn, scaleOut);
        textAnim.play();

        // Animation cho button: Fade in
        FadeTransition buttonFade = new FadeTransition(Duration.millis(800), button);
        buttonFade.setFromValue(0);
        buttonFade.setToValue(1);
        buttonFade.setDelay(Duration.millis(500));
        buttonFade.play();
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