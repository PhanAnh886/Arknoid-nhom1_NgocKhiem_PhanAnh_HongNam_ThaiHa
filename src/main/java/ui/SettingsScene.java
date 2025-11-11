package ui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class SettingsScene {
    private Scene scene;

    public SettingsScene(Main mainApp) {
        Text title = new Text("SETTINGS");
        title.setFont(new Font("Arial", 48));
        title.setFill(Color.WHITE);
        title.setStyle("-fx-font-weight: bold;");

        // Các tùy chọn (placeholder - có thể mở rộng sau)
        CheckBox soundCheckBox = new CheckBox("Sound Effects (Coming Soon)");
        soundCheckBox.setTextFill(Color.WHITE);
        soundCheckBox.setFont(new Font("Arial", 20));
        soundCheckBox.setSelected(true);
        soundCheckBox.setDisable(true);

        CheckBox musicCheckBox = new CheckBox("Background Music (Coming Soon)");
        musicCheckBox.setTextFill(Color.WHITE);
        musicCheckBox.setFont(new Font("Arial", 20));
        musicCheckBox.setSelected(true);
        musicCheckBox.setDisable(true);

        Text infoText = new Text("More settings will be available in future updates!");
        infoText.setFont(new Font("Arial", 16));
        infoText.setFill(Color.LIGHTGRAY);

        Button backButton = createButton("Back to Menu");
        backButton.setOnAction(e -> mainApp.showMenu());

        VBox layout = new VBox(30, title, soundCheckBox, musicCheckBox, infoText, backButton);
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

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(new Font("Arial", 20));
        button.setPrefWidth(200);
        button.setPrefHeight(50);
        button.setStyle(
                "-fx-background-color: #2196F3; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-cursor: hand;"
        );

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