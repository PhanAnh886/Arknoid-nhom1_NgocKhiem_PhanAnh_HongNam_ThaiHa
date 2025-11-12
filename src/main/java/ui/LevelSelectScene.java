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

public class LevelSelectScene {
    private Scene scene;
    private SoundManager soundManager;


    public LevelSelectScene(Main mainApp) {
        soundManager = SoundManager.getInstance();

        Text title = new Text("SELECT LEVEL");
        title.setFont(new Font("Arial", 48));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");

        // Các nút chọn level
        Button level0Button = createLevelButton("Level 0\nDemo");
        Button level1Button = createLevelButton("Level 1\nHeart");
        Button level2Button = createLevelButton("Level 2\nColumns");

        level0Button.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showGame(0);
        });
        level1Button.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showGame(1);
        });
        level2Button.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showGame(2);
        });

        HBox levelButtons = new HBox(30, level0Button, level1Button, level2Button);
        levelButtons.setAlignment(Pos.CENTER);

        Button backButton = createBackButton("Back to Menu");
        backButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showMenu();
        });

        VBox layout = new VBox(40, title, levelButtons, backButton);
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

        button.setOnMouseEntered(e -> {
            soundManager.playSound("button_hover");
            button.setStyle(
                    "-fx-background-color: #45a049; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 15; " +
                            "-fx-cursor: hand;"
            );
        });
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

        button.setOnMouseEntered(e -> {
            soundManager.playSound("button_hover");
            button.setStyle(
                    "-fx-background-color: #da190b; " +
                            "-fx-text-fill: white; " +
                            "-fx-background-radius: 10; " +
                            "-fx-cursor: hand;"
            );
        });
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
