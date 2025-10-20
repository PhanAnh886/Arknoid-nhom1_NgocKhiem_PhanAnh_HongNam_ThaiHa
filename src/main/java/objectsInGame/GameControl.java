package objectsInGame;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;

/**
 * Class điều khiển game chính
 */
public class GameControl extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;

    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks = new ArrayList<>();

    private AnimationTimer loop;
    private int score = 0;
    private int lives = 3;
    private int highScore = 0;

    // Hình nền và âm thanh
    private Image backgroundImage;
    private MediaPlayer bgMusic;
    private static AudioClip soundBounce, soundBrick, soundLose, soundClick;

    /**
     * Hàm khởi tạo game
     */
    public GameControl() {
        canvas = new Canvas(1000, 600);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        // Paddle & Ball
        paddle = new Paddle(420, 500, 160, 20);
        ball = new Ball(500 - 17, 500 - 34, 17);

        // Khởi tạo âm thanh & hình ảnh
        initResources();

        // Tạo danh sách gạch
        createBricks();

        // Điều khiển chuột
        setupMouseControl();

        // Vòng lặp game
        startGameLoop();
    }

    /**
     * Khởi tạo hình ảnh & âm thanh
     */
    private void initResources() {
        try {
            backgroundImage = new Image(getClass().getResource("/images/background.jpg").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không tải được background: " + e.getMessage());
        }

        try {
            soundBounce = new AudioClip(getClass().getResource("/sounds/bounce.wav").toExternalForm());
            soundBrick  = new AudioClip(getClass().getResource("/sounds/brick.wav").toExternalForm());
            soundLose   = new AudioClip(getClass().getResource("/sounds/lose.wav").toExternalForm());
            soundClick  = new AudioClip(getClass().getResource("/sounds/click.wav").toExternalForm());
        } catch (Exception e) {
            System.err.println("Không tải được âm thanh: " + e.getMessage());
        }

        try {
            Media bgm = new Media(getClass().getResource("/sounds/background.mp3").toExternalForm());
            bgMusic = new MediaPlayer(bgm);
            bgMusic.setCycleCount(MediaPlayer.INDEFINITE);
            bgMusic.setVolume(0.35);
            bgMusic.play();
        } catch (Exception e) {
            System.err.println("Không tải được nhạc nền: " + e.getMessage());
        }
    }

    /**
     * Tạo danh sách gạch
     */
    private void createBricks() {
        int x1 = 10, y1 = 50;
        int width = 55, height = 25;

        for (int i = 0; i < 80; i++) {
            bricks.add(new Brick(x1 + (i % 16) * 60, y1, width, height));
            if ((i + 1) % 16 == 0) {
                y1 += 30;
            }
        }
    }

    /**
     * Thiết lập chuột
     */
    private void setupMouseControl() {
        canvas.setOnMouseMoved(event -> {
            double mouseX = event.getX();
            paddle.setX(mouseX - paddle.getWidth() / 2);

            if (!ball.isLaunched()) {
                ball.setX(mouseX - ball.getWidth() / 2);
                ball.setX(Math.min(1000 - paddle.getWidth() / 2 - 17,
                        Math.max(ball.getX(), paddle.getWidth() / 2 - 17)));
            }
        });

        canvas.setOnMouseClicked(event -> {
            if (soundClick != null) soundClick.play();
            ball.setLaunched(true);
        });
    }

    /**
     * Bắt đầu vòng lặp game
     */
    void startGameLoop() {
        loop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double dt = 1.0 / 60.0;
                update(dt);
                renderAll();
            }
        };
        loop.start();
    }

    /**
     * Cập nhật logic
     */
    private void update(double dt) {
        ball.update(dt, paddle, bricks);
        paddle.update();

        // Bóng rơi xuống -> mất mạng
        if (ball.getY() + ball.getHeight() >= 600 && ball.isLaunched()) {
            lives--;
            playLoseSound();

            if (lives > 0) {
                ball.ballLose();
            } else {
                if (score > highScore) highScore = score;
                loop.stop();

                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        Platform.exit();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }

        // Kiểm tra phá hết gạch
        boolean allDestroyed = true;
        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) {
                allDestroyed = false;
                break;
            }
        }

        if (allDestroyed && lives > 0) {
            if (score > highScore) highScore = score;
            for (Brick brick : bricks) brick.setDestroyed(false);
            ball.ballLose();
        }

        // Cập nhật điểm khi phá gạch
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && ball.intersects(brick)) {
                brick.setDestroyed(true);
                score += 10;
                if (soundBrick != null) soundBrick.play();

                // Rung nhẹ khi phá gạch
                shakeCanvas();
                break;
            }
        }
    }

    /**
     * Rung nhẹ canvas khi phá gạch
     */
    private void shakeCanvas() {
        canvas.setTranslateX(Math.random() * 6 - 3);
        canvas.setTranslateY(Math.random() * 6 - 3);
        new Thread(() -> {
            try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            Platform.runLater(() -> {
                canvas.setTranslateX(0);
                canvas.setTranslateY(0);
            });
        }).start();
    }

    /**
     * Vẽ toàn bộ frame
     */
    private void renderAll() {
        if (backgroundImage != null) {
            gc.drawImage(backgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        }

        for (Brick brick : bricks) {
            if (!brick.isDestroyed()) brick.render(gc);
        }

        paddle.render(gc);
        ball.render(gc);

        // Hiển thị UI
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Score: " + score, 20, 25);
        gc.fillText("Lives: " + lives, 20, 50);
        gc.fillText("High Score: " + highScore, 850, 25);

        if (lives <= 0) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Arial", 40));
            gc.fillText("GAME OVER", 380, 300);
        }
    }

    // ===================== ÂM THANH CHUNG ======================

    public static void playBounceSound() {
        if (soundBounce != null) soundBounce.play();
    }

    public static void playBrickSound() {
        if (soundBrick != null) soundBrick.play();
    }

    public static void playLoseSound() {
        if (soundLose != null) soundLose.play();
    }

    public static void playClickSound() {
        if (soundClick != null) soundClick.play();
    }
}
