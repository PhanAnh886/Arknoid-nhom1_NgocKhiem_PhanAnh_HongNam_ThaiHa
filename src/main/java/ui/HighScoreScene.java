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

public class HighScoreScene {
    private Scene scene;

    public HighScoreScene(Main mainApp) {
        Text title = new Text("HIGH SCORE");
        title.setFont(new Font("Arial", 48));
        title.setFill(Color.GOLD);
        title.setStyle("-fx-font-weight: bold;");

        // Đọc high score từ file
        int highScore = loadHighScore();

        Text scoreText = new Text("Best Score: " + highScore);
        scoreText.setFont(new Font("Arial", 36));
        scoreText.setFill(Color.WHITE);

        Text infoText = new Text("Keep playing to beat this record!");
        infoText.setFont(new Font("Arial", 20));
        infoText.setFill(Color.LIGHTGRAY);

        Button backButton = createButton("Back to Menu");
        backButton.setOnAction(e -> mainApp.showMenu());

        Button resetButton = createButton("Reset High Score");
        resetButton.setStyle(
                "-fx-background-color: #f44336; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );
        resetButton.setOnAction(e -> {
            resetHighScore();
            mainApp.showHighScore(); // Refresh màn hình
        });

        VBox layout = new VBox(30, title, scoreText, infoText, backButton, resetButton);
        layout.setAlignment(Pos.CENTER);
        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/image/menuBranchs/menuBranch.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền!");
            e.printStackTrace();
        }

        if (bgImage != null) {
            // 2. Định nghĩa kích thước nền (800x800)
            BackgroundSize bgSize = new BackgroundSize(
                    800, 800, // Chiều rộng và cao của ảnh
                    false, false, // Không tính theo %
                    false, false  // Không "cover" (che phủ) hay "contain" (vừa vặn)
            );

            // 3. Tạo BackgroundImage
            BackgroundImage backgroundImage = new BackgroundImage(
                    bgImage,
                    BackgroundRepeat.NO_REPEAT, // Không lặp lại ảnh
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT,  // Căn giữa
                    bgSize                      // Dùng kích thước đã định nghĩa
            );

            // 4. Set nền mới cho layout
            layout.setBackground(new Background(backgroundImage));
        } else {
            // Dự phòng nếu không tải được ảnh
            layout.setBackground(new Background(new BackgroundFill(
                    Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        scene = new Scene(layout, 800, 600);
    }

    private int loadHighScore() {
        try {
            java.io.File file = new java.io.File("highscore.dat");
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
            java.io.PrintWriter writer = new java.io.PrintWriter("highscore.dat");
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

        button.setOnMouseEntered(e -> {
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