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
    private Stage primaryStage; //stage chính của cả chương trình, nơi các scene được trình chiếu
    private GameControl gameControl;
    Scene gameScene; // dùng cho cơ chế lưu lại để dừng tạm thời
    // và bật vòng lặp trong gameControl mà ko phải tạo 1 scene mới chứa 1 cái gameconTrol mới

    // Lưu level hiện tại để không reset khi thua
    private int currentLevelIndex = 0;
    private int currentScore = 0;
    private int currentLives = 3;

    @Override
    public void start(Stage stage) throws Exception {//override from a method available
                                                     // in class Application
        this.primaryStage = stage; //lưu stage vào biến của class
        primaryStage.setTitle("Arkanoid");

        showMenu(); // Bắt đầu ở menu
        primaryStage.show(); // Hiển thị cửa sổ-hiển thị stage ra màn hình
    }

    //--------------METHODS INTERACT WITH EACH OTHER VIA BUTTON DEFINED BY  SCENE CLASSES IN UI PACKAGE------------
    /**
     * Hiển thị menu chính
     */
    public void showMenu() {
        MenuScene menuScene = new MenuScene(this);
        primaryStage.setScene(menuScene.getScene()); //đặt scene lên stage
    }

    /**
     * Hiển thị game với level cụ thể
     */
    public void showGame(int levelIndex) {
        this.currentLevelIndex = levelIndex;
        gameControl = new GameControl(this, levelIndex);
        gameScene = new Scene(gameControl, 1000, 600);
        primaryStage.setScene(gameScene); //switch scene sang game scene
        gameControl.requestFocus(); // Quan trọng để nhận phím
    }

    /**
     * Hiển thị game over
     */
    public void showGameOver(int score, int highScore) {
        GameOverScene gameOverScene = new GameOverScene(this, score, highScore);
        primaryStage.setScene(gameOverScene.getScene());
    }

    /**
     * Hiển thị pause
     */
    public void showPause() {
        if (gameControl != null) {
            gameControl.pauseGame(); // call method pause trong gameControl giúp dùng loop tạm thời
            PauseScene pauseScene = new PauseScene(this); // mở scene game pause
            primaryStage.setScene(pauseScene.getScene());
        }
    }

    /**
     * Resume game
     */
    public void resumeGame() {
        if (gameControl != null) {
            gameControl.resumeGame(); // call method resume trong gameControl giúp khôi phục loop
            primaryStage.setScene(gameScene);
        }
    }

    /**
     * Hiển thị màn chọn level
     */
    public void showLevelSelect() {
        LevelSelectScene levelSelectScene = new LevelSelectScene(this);
        primaryStage.setScene(levelSelectScene.getScene());
    }

    /**
     * Hiển thị high score
     */
    public void showHighScore() {
        HighScoreScene highScoreScene = new HighScoreScene(this);
        primaryStage.setScene(highScoreScene.getScene());
    }

    /**
     * Hiển thị settings
     */
    public void showSettings() {
        SettingsScene settingsScene = new SettingsScene(this);
        primaryStage.setScene(settingsScene.getScene());
    }

    /**
     * Retry level hiện tại (không reset về level 0)
     */
    public void retryCurrentLevel() {
        if (gameControl != null) {
            showGame(gameControl.getCurrentLevelIndex());
        }
    }

    /**
     * Chuyển sang level tiếp theo
     */
    public void nextLevel(int nextLevelIndex) {
        showGame(nextLevelIndex);
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
