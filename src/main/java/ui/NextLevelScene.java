package ui;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.util.Duration;
import javagraphicmain.Main;
import sound.SoundManager;


/**
 * Màn hình chuyển level - Hiển thị thông tin và có animation
 */
public class NextLevelScene {
    private Scene scene;
    private SoundManager soundManager;


    public NextLevelScene(Main mainApp, int currentLevel, int score, int lives) {
        soundManager = SoundManager.getInstance();


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

        continueButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.nextLevel(currentLevel + 1);
        });

        menuButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showMenu();
        });

        VBox layout = new VBox(30, congratsText, levelText, scoreText, livesText,
                continueButton, menuButton);
        layout.setAlignment(Pos.CENTER);
        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/image/nextLevel/nextLevel.png"));
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
        button.setPrefWidth(300);
        button.setPrefHeight(50);

        // Style cơ bản - Vàng, vuông, có bóng
        button.setStyle(
                "-fx-background-color: #FFD700; " +           // Màu vàng
                        "-fx-text-fill: #000000; " +                  // Chữ đen
                        "-fx-background-radius: 0; " +                // Không bo tròn (vuông)
                        "-fx-border-radius: 0; " +                    // Viền vuông
                        "-fx-cursor: hand; " +
                        "-fx-font-weight: bold; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 3, 3);" // Bóng đổ
        );

        // Hiệu ứng hover - Sáng hơn + bóng lớn hơn + bounce
        button.setOnMouseEntered(e -> {
            soundManager.playSound("button_hover");

            // Tạo hiệu ứng bounce (phóng to)
            javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(
                    javafx.util.Duration.millis(100), button
            );
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();

            button.setStyle(
                    "-fx-background-color: #FFC700; " +       // Vàng sáng hơn
                            "-fx-text-fill: #000000; " +
                            "-fx-background-radius: 0; " +
                            "-fx-border-radius: 0; " +
                            "-fx-cursor: hand; " +
                            "-fx-font-weight: bold; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 15, 0, 5, 5);" // Bóng to hơn
            );
        });

        // Hiệu ứng mouse exit - Trở về bình thường
        button.setOnMouseExited(e -> {
            // Trả về kích thước ban đầu
            javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(
                    javafx.util.Duration.millis(100), button
            );
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();

            button.setStyle(
                    "-fx-background-color: #FFD700; " +
                            "-fx-text-fill: #000000; " +
                            "-fx-background-radius: 0; " +
                            "-fx-border-radius: 0; " +
                            "-fx-cursor: hand; " +
                            "-fx-font-weight: bold; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0, 3, 3);"
            );
        });

        // Hiệu ứng khi click - Nhấn xuống
        button.setOnMousePressed(e -> {
            javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(
                    javafx.util.Duration.millis(50), button
            );
            st.setToX(0.95);
            st.setToY(0.95);
            st.play();
        });

        button.setOnMouseReleased(e -> {
            javafx.animation.ScaleTransition st = new javafx.animation.ScaleTransition(
                    javafx.util.Duration.millis(50), button
            );
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });

        return button;
    }

    public Scene getScene() {
        return scene;
    }
}