package javagraphicmain;

import javafx.application.Application ;  // Lớp khởi động JavaFX
import javafx.scene.Scene;              // Màn cảnh hiển thị
import javafx.stage.Stage;              // Cửa sổ chính của ứng dụng
// dùng để đổi mảu background
import objectsInGame.*;
import ui.*;

/**
 * bắt đầu chương trình
 *phan anh
 */
public class Main extends Application {
    private Stage primaryStage;
    private GameControl gameControl;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        primaryStage.setTitle("Arkanoid");

        showMenu(); // Bắt đầu ở menu
        primaryStage.show(); // Hiển thị cửa sổ
    }


    /**
     * Hiển thị menu chính
     */
    public void showMenu() {
        MenuScene menuScene = new MenuScene(this);
        primaryStage.setScene(menuScene.getScene());
    }

    /**
     * Hiển thị game
     */
    public void showGame() {
        gameControl = new GameControl();
        Scene gameScene = new Scene(gameControl, 1000, 600);
        primaryStage.setScene(gameScene);
        gameControl.requestFocus(); // Quan trọng để nhận phím
    }

    /**
     * Hiển thị game over
     */
    public void showGameOver() {
        GameOverScene gameOverScene = new GameOverScene(this);
        primaryStage.setScene(gameOverScene.getScene());
    }

    /**
     * Hiển thị pause
     */
    public void showPause() {
        PauseScene pauseScene = new PauseScene(this);
        primaryStage.setScene(pauseScene.getScene());
    }

    /**
     * Resume game (chưa implement đầy đủ)
     */
    public void resumeGame() {
        if (gameControl != null) {
            Scene gameScene = new Scene(gameControl, 1000, 600);
            primaryStage.setScene(gameScene);
            gameControl.requestFocus();
        }
    }

    /**
     * start point
     *
     * @param args nhu binh thuong
     */
    public static void main(String[] args) {
        // khởi chạy JavaFX
        launch(args);
    }
}
