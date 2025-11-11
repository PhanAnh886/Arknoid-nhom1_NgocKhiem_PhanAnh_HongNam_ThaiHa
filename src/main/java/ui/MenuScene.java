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

public class MenuScene {
    private Scene scene;
    private SoundManager soundManager;


    public MenuScene(Main mainApp) {
        soundManager = SoundManager.getInstance();

        // Load và play background music
        soundManager.loadBackgroundMusic("/sounds/Dark Clouds Covering The Horizon - Loading Background Music.wav");
        soundManager.playBackgroundMusic();

        // Các nút
        Button startButton = createMenuButton("Start Game");
        Button selectLevelButton = createMenuButton("Select Level");
        Button highScoreButton = createMenuButton("High Score");
        Button settingsButton = createMenuButton("Settings");
        Button exitButton = createMenuButton("Exit");

        // Xử lý sự kiện
        // Xử lý sự kiện với âm thanh
        startButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showGame(0);
        });

        selectLevelButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showLevelSelect();
        });

        highScoreButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showHighScore();
        });

        settingsButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showSettings();
        });

        exitButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            System.exit(0);
        });

        VBox layout = new VBox(20, startButton, selectLevelButton,
                highScoreButton, settingsButton, exitButton);
        layout.setAlignment(Pos.BOTTOM_CENTER);
        layout.setPadding(new Insets(0, 0, 20, 0));

        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/image/Menu/Menu.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền!");
            e.printStackTrace();
        }

        if (bgImage != null) {
            // 2. Định nghĩa kích thước nền (800x600, bằng kích thước Scene)
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

        // Hiệu ứng hover VỚI ÂM THANH
        button.setOnMouseEntered(e -> {
            SoundManager.getInstance().playSound("button_hover");
            button.setStyle(
                    "-fx-background-color: #1976D2; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            );
        });
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
