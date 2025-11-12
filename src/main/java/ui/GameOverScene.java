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

public class GameOverScene {
    private Scene scene;
    private SoundManager soundManager;


    public GameOverScene(Main mainApp, int finalScore, int highScore) {
        soundManager = SoundManager.getInstance();


        Text scoreText = new Text("Your Score: " + finalScore);
        scoreText.setFont(new Font("Arial", 32));
        scoreText.setFill(Color.WHITE);

        Text highScoreText = new Text("High Score: " + highScore);
        highScoreText.setFont(new Font("Arial", 28));
        highScoreText.setFill(Color.GOLD);


        Button retryButton = createButton("Retry Level", Color.web("#4CAF50"));
        Button menuButton = createButton("Main Menu", Color.web("#2196F3"));
        Button exitButton = createButton("Exit Game", Color.web("#f44336"));

        retryButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.retryCurrentLevel();
        });

        menuButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            mainApp.showMenu();
        });

        exitButton.setOnAction(e -> {
            soundManager.playSound("button_click");
            System.exit(0);
        });


        VBox layout = new VBox(25, scoreText, highScoreText, retryButton, menuButton, exitButton);
        layout.setAlignment(Pos.BOTTOM_CENTER);
        layout.setPadding(new Insets(0, 0, 20, 0));
        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/image/gameOver/gameOver.png"));
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
                    BackgroundPosition.DEFAULT,  // ko căn, lấy từ góc trên ảnh ở giưã
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
