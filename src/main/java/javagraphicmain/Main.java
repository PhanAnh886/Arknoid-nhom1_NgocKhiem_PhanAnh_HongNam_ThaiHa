package javagraphicmain;

import javafx.application.Application;  // Lớp khởi động JavaFX
import javafx.scene.Scene;              // Màn cảnh hiển thị
import javafx.stage.Stage;              // Cửa sổ chính của ứng dụng
import sound.SoundManager;
import objectsInGame.*;
import ui.*;

/**
 * bắt đầu chương trình
 * phan anh
 */
public class Main extends Application {
    private Stage primaryStage; //stage chính của cả chương trình, nơi các scene được trình chiếu
    private GameControl gameControl;
    Scene gameScene; // dùng cho cơ chế lưu lại để dừng tạm thời
    // và bật vòng lặp trong gameControl mà ko phải tạo 1 scene mới chứa 1 cái gameconTrol mới

    private SoundManager soundManager;


    // Lưu level hiện tại để không reset khi thua
    private int currentLevelIndex = 0;
    private int currentScore = 0;
    private int currentLives = 3;

    @Override
    public void start(Stage stage) throws Exception {//override from a method available
        // in class Application
        this.primaryStage = stage; //lưu stage vào biến của class
        primaryStage.setTitle("Arkanoid");

        // Khởi tạo SoundManager
        soundManager = SoundManager.getInstance();

        showMenu(); // Bắt đầu ở menu
        primaryStage.show(); // Hiển thị cửa sổ-hiển thị stage ra màn hình
    }

    //--------------METHODS INTERACT WITH EACH OTHER VIA BUTTON DEFINED BY  SCENE CLASSES IN UI PACKAGE------------

    /**
     * Hiển thị menu chính
     */
    public void showMenu() {
        // Load và play menu music
        soundManager.loadBackgroundMusic("/sound/Dark Clouds Covering The Horizon - Loading Background Music.wav");
        soundManager.playBackgroundMusic();
        MenuScene menuScene = new MenuScene(this);
        primaryStage.setScene(menuScene.getScene()); //đặt scene lên stage
    }

    /**
     * Hiển thị game với level cụ thể
     */
    public void showGame(int levelIndex) {
        // Stop menu music khi vào game (hoặc load game music nếu có)
        soundManager.stopBackgroundMusic();

        this.currentLevelIndex = levelIndex;
        gameControl = new GameControl(this, levelIndex);
        gameScene = new Scene(gameControl, 800, 600);
        primaryStage.setScene(gameScene); //switch scene sang game scene
        gameControl.requestFocus();
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
     * Hiển thị mà hình next level khi win 1 level
     *
     * @param currentLevel level hiện tại
     * @param score        điểm
     * @param lives        mạng
     */
    public void showNextLevel(int currentLevel, int score, int lives) {
        NextLevelScene nextLevelScene = new NextLevelScene(this, currentLevel, score, lives);
        primaryStage.setScene(nextLevelScene.getScene());
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

    @Override
    public void stop() {
        // Cleanup khi thoát game
        soundManager.dispose();
    }

}
