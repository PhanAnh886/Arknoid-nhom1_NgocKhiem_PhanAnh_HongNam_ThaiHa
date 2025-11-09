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

public class PauseScene {
    private Scene scene;

    public PauseScene(Main mainApp) {
        Text title = new Text("PAUSED");
        title.setFont(new Font("Arial", 60));
        title.setFill(Color.YELLOW);
        title.setStyle("-fx-font-weight: bold;");

        Text hint = new Text("Press ESC or P to pause game");
        hint.setFont(new Font("Arial", 18));
        hint.setFill(Color.LIGHTGRAY);

        Button resumeButton = createButton("Resume Game", Color.web("#4CAF50"));
        Button menuButton = createButton("Main Menu", Color.web("#2196F3"));
        Button exitButton = createButton("Exit Game", Color.web("#f44336"));

        // Resume lại game
        resumeButton.setOnAction(e -> mainApp.resumeGame());

        // Trở về menu chính
        menuButton.setOnAction(e -> mainApp.showMenu());

        // Thoát game
        exitButton.setOnAction(e -> System.exit(0));

        VBox layout = new VBox(30, title, hint, resumeButton, menuButton, exitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setBackground(new Background(new BackgroundFill(
                Color.rgb(0, 0, 0, 0.9), CornerRadii.EMPTY, Insets.EMPTY)));

        scene = new Scene(layout, 1000, 600);
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
