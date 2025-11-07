package objectsInGame;

import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;               // font cho chữ
import objectsInGame.bricks.*;
import objectsInGame.powerups.*;
import Level.*;

import java.util.ArrayList;                  //danh sách gạch

/**
 * Class điều khiển game chính
 */
public class GameControl extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;

    private Paddle paddle;
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Brick> bricks = new ArrayList<>();
    private ArrayList<PowerUp> activePowerUps = new ArrayList<>();
    private ArrayList<Bullet> bullets = new ArrayList<>();

    private Level currentLevel;  // Level hiện tại
    private int currentLevelIndex = 0;
    private Level[] levels;


    private AnimationTimer loop;

    // thông tin game
    private int score = 0;            // điểm người chơi
    private int lives = 3;            // số mạng còn lại
    private int highScore = 0;        // điểm cao nhất
    private static final String HIGHSCORE_FILE = "highscore.dat";

    // Power-up timers
    private boolean shootEnabled = false;
    private double shootTimer = 0;
    private double shootCooldown = 0.3; // Bắn mỗi 0.3 giây

    private boolean fastBallEnabled = false;
    private double fastBallTimer = 0;

    // trạng thái game
    private enum GameState {
        MENU,       //màn hình menu
        PLAYING,    // đang chơi
        GAME_OVER,  // thua hết mạng
        NEXT_LEVEL  // phá hết gạch
    }

    private GameState gameState = GameState.MENU;
    ; // mặc định bắt đầu ở trạng thái menu

    /**
     * Hàm khởi tạo game
     */
    public GameControl() {
        canvas = new Canvas(1000, 600);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        paddle = new Paddle(420, 500, 160, 20);
        balls.add(new Ball(500 - 17, 500 - 34, 17));

        // Khởi tạo các level
        levels = new Level[]{
                new Level0(),
                new Level1(),
                new Level2()
        };

        setupMouseControls();
        setupKeyboardControls();
        currentLevel = levels[0];      // Tạo level mới
        bricks = currentLevel.getBricks();    // Lấy danh sách gạch từ level

        // QUAN TRỌNG: Request focus để canvas nhận được sự kiện
        //canvas.setFocusTraversable(true);
        //this.setFocusTraversable(true);
        //canvas.requestFocus();

        // khởi động vòng lặp game
        startGameLoop();
    }

    private void setupMouseControls() {


        // sự kiện paddle và bóng di chuyển theo chuột
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (gameState == GameState.PLAYING) {
                    double mouseX = event.getX();

                    // paddle luôn đi theo chuột
                    paddle.setX(mouseX - paddle.getWidth() / 2);

                    // nếu bóng chưa phóng thì đi theo luôn
                    for (Ball ball : balls) {
                        if (!ball.isLaunched()) {
                            ball.setX(mouseX - ball.getWidth() / 2);
                            //giới hạn ball khi paddle chạm biên ở cả hai đầu
                            ball.setX(Math.min(1000 - paddle.getWidth() / 2 - ball.width / 2, Math.max(ball.getX(), paddle.getWidth() / 2 - ball.width / 2)));
                        }
                    }
                }
            }
        });

        // Sự kiện click chuột -> phóng bóng
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                switch (gameState) {
                    case MENU:
                        gameState = GameState.PLAYING;
                        break;
                    case PLAYING:
                        for (Ball ball : balls) {
                            if (!ball.isLaunched()) {
                                ball.setLaunched(true);
                            }
                        }
                        break;
                    case GAME_OVER:
                        resetGame();
                        break;

                    case NEXT_LEVEL:
                        nextLevel();
                        break;
                }
            }
        });
    }

    private void setupKeyboardControls() {
        this.setFocusTraversable(true);
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (gameState == GameState.PLAYING && shootEnabled) {
                    switch (event.getCode()) {
                        case SPACE:
                            shootBullet();
                            break;
                    }
                }
            }
        });
    }

    private void shootBullet() {
        if (shootTimer <= 0) {
            // Bắn 2 viên đạn từ 2 bên paddle
            double leftX = paddle.getX() + 10;
            double rightX = paddle.getX() + paddle.getWidth() - 15;
            double y = paddle.getY() - 15;

            Bullet leftBullet = new Bullet(leftX, y);
            Bullet rightBullet = new Bullet(rightX, y);

            leftBullet.setActive(true);
            rightBullet.setActive(true);

            bullets.add(leftBullet);
            bullets.add(rightBullet);

            shootTimer = shootCooldown;
        }
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
        switch (gameState) {
            case MENU:
                // Chờ người chơi click để bắt đầu
                break;

            case PLAYING:
                //nếu đã hoàn thành level
                if (currentLevel.isCompleted()) {  // Thay vì duyệt bricks
                    gameState = GameState.NEXT_LEVEL;
                    for (Ball ball : balls) {
                        ball.setLaunched(false);
                    }
                    return;
                }

                // Cập nhật power-up timers
                if (shootTimer > 0) shootTimer -= dt;
                if (fastBallTimer > 0) {
                    fastBallTimer -= dt;
                    if (fastBallTimer <= 0) {
                        fastBallEnabled = false;
                        // Trả tốc độ bóng về bình thường
                        for (Ball ball : balls) {
                            double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
                            if (speed > 250) {
                                ball.setDx(ball.getDx() * 0.7);
                                ball.setDy(ball.getDy() * 0.7);
                            }
                        }
                    }
                }

                // Cập nhật bóng
                ArrayList<Ball> ballsToRemove = new ArrayList<>();
                for (Ball ball : balls) {
                    if (ball.isLaunched() && ball.getY() + ball.getHeight() >= 600) {
                        ballsToRemove.add(ball);
                    }
                    ball.update(dt, paddle, bricks);
                }

                // Xóa bóng rơi xuống
                for (Ball ball : ballsToRemove) {
                    balls.remove(ball);
                }

                // Nếu hết bóng -> -1 mạng
                if (balls.isEmpty()) {
                    lives--;
                    if (lives > 0) {
                        // Reset game về trạng thái chờ phóng
                        resetBallAndPaddle();
                    } else {
                        // GAME OVER
                        if (score > highScore) {
                            highScore = score;
                            saveHighScore(); // Gọi hàm lưu Highscore
                        }
                        gameState = GameState.GAME_OVER;
                    }
                }

                // Cập nhật đạn
                for (Bullet bullet : bullets) {
                    bullet.update(dt);

                    // Kiểm tra va chạm đạn vs gạch
                    for (Brick brick : bricks) {
                        if (!brick.isDestroyed() && bullet.isActive() && bullet.intersects(brick)) {
                            brick.destroyed(true, bricks);
                            bullet.setActive(false);
                            break;
                        }
                    }
                }

                // Xóa đạn không active
                bullets.removeIf(bullet -> !bullet.isActive());

                // Cập nhật power-ups rơi xuống
                for (PowerUp powerUp : activePowerUps) {
                    powerUp.update(dt);

                    // Kiểm tra paddle bắt được power-up
                    if (powerUp.intersects(paddle)) {
                        applyPowerUp(powerUp);
                        powerUp.setActive(false);
                    }

                    // Tắt power-up nếu rơi ra ngoài
                    if (powerUp.getY() > 600) {
                        powerUp.setActive(false);
                    }
                }

                // Xóa power-ups không active
                activePowerUps.removeIf(p -> !p.isActive());

                // Kiểm tra gạch bị phá -> thả power-up
                for (Brick brick : bricks) {
                    if (brick.isDestroyed() && !brick.isScored()) {
                        score += brick.getScore();
                        brick.setScored(true);

                        if (score > highScore) {
                            highScore = score;
                        }

                        // Thả power-up nếu có
                        if (brick.getPowerUp() != null) {
                            PowerUp powerUp = brick.getPowerUp();
                            powerUp.setX(brick.getX() + brick.getWidth() / 2 - 10);
                            powerUp.setY(brick.getY() + brick.getHeight() / 2 - 10);
                            powerUp.activate();
                            activePowerUps.add(powerUp);
                            brick.setPowerUp(null);
                        }
                    }
                }
                paddle.update();
                break;

        }
    }

    // lưu trữ điểm cao
    private void loadHighScore() {
        try {
            java.io.File file = new java.io.File(HIGHSCORE_FILE);
            if (file.exists()) {
                java.util.Scanner scanner = new java.util.Scanner(file);
                if (scanner.hasNextInt()) {
                    highScore = scanner.nextInt();
                }
                scanner.close();
            }
        } catch (java.io.IOException e) {
            System.err.println("Lỗi khi tải Highscore: " + e.getMessage());
            highScore = 0; // Nếu lỗi, đặt lại là 0
        }
    }

    private void saveHighScore() {
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter(HIGHSCORE_FILE);
            writer.println(highScore);
            writer.close();
        } catch (java.io.IOException e) {
            System.err.println("Lỗi khi lưu Highscore: " + e.getMessage());
        }
    }

    private void applyPowerUp(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case "SHOOT":
                shootEnabled = true;
                shootTimer = 0;
                // Hiệu ứng 10 giây
                break;

            case "FAST":
                if (!fastBallEnabled) {
                    fastBallEnabled = true;
                    fastBallTimer = 8.0; // 8 giây
                    // Tăng tốc độ tất cả bóng đang bay
                    for (Ball ball : balls) {
                        if (ball.isLaunched()) {
                            ball.setDx(ball.getDx() * 1.5);
                            ball.setDy(ball.getDy() * 1.5);
                        }
                    }
                }
                break;

            case "MULTI":
                // Tạo thêm 2 bóng từ bóng đầu tiên
                if (!balls.isEmpty()) {
                    Ball original = balls.get(0);
                    if (original.isLaunched()) {
                        Ball ball1 = new Ball(original.getX(), original.getY(), 17);
                        Ball ball2 = new Ball(original.getX(), original.getY(), 17);

                        ball1.setLaunched(true);
                        ball2.setLaunched(true);

                        double speed = Math.sqrt(original.getDx() * original.getDx() +
                                original.getDy() * original.getDy());

                        ball1.setDx(speed * Math.sin(Math.toRadians(-45)));
                        ball1.setDy(-speed * Math.cos(Math.toRadians(-45)));

                        ball2.setDx(speed * Math.sin(Math.toRadians(45)));
                        ball2.setDy(-speed * Math.cos(Math.toRadians(45)));

                        balls.add(ball1);
                        balls.add(ball2);
                    }
                }
                break;
        }
    }


    /**
     * method called when losed+clicked, reset game về trạng thái lcus đầu trừ highscore
     */
    private void resetGame() {
        score = 0;
        lives = 3;
        currentLevelIndex = 0;
        loadHighScore();
        currentLevel = levels[currentLevelIndex];
        bricks = currentLevel.getBricks();
        currentLevel.reset();  // Gọi method reset của Level thay vì duyệt bricks
        resetBallAndPaddle();
        gameState = GameState.PLAYING;
    }

    /**
     * method gộp các object/trạng thái bị reset khi lose game (khi cần ret trạng thái)
     */
    private void resetState() {
        balls.clear();
        Ball newBall = new Ball(500 - 17, 500 - 34, 17);
        balls.add(newBall);
        newBall.ballLose();
        paddle.resetPaddle(true);
        newBall.setClicked(true);

        bullets.clear();
        activePowerUps.clear();
        shootEnabled = false;
        fastBallEnabled = false;
    }

    private void resetBallAndPaddle() {
        // Tắt các hiệu ứng Power-Up
        shootEnabled = false;
        fastBallEnabled = false;

        // Xóa tất cả các PowerUp và Bullet đang hoạt động
        activePowerUps.clear();
        bullets.clear();

        // Tái tạo lại bóng
        balls.clear();

        // Tạo bóng mới ở trên thanh đỡ
        Ball newBall = new Ball(paddle.getX() + paddle.getWidth() / 2 - 5, paddle.getY() - 10, 5);
        balls.add(newBall);

        // Đặt lại trạng thái Paddle
        paddle.setX(450);
    }

    /**
     * method called when all bricks are broked
     */
    private void nextLevel() {
        // lên level
        currentLevelIndex++;

        if (currentLevelIndex >= levels.length) {
            // Hết level -> thắng game
            currentLevelIndex = 0; // Chơi lại từ đầu
        }

        currentLevel = levels[currentLevelIndex];
        bricks = currentLevel.getBricks(); // Cập nhật danh sách gạch
        resetState();
        gameState = GameState.PLAYING;
    }

    /**
     * Vẽ toàn bộ frame
     */
    private void renderAll() {
        switch (gameState) {
            case MENU:
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                gc.setFill(Color.WHITE);
                gc.setFont(new Font("Arial", 48));
                gc.fillText("ARKANOID", 370, 250); // tiêu đề game

                gc.setFont(new Font("Arial", 24));
                gc.fillText("Click to play", 390, 320);
                gc.setFont(new Font("Arial", 20));
                gc.fillText("Điều khiển: Di chuột để di chuyển thanh đỡ", 310, 370);
                gc.fillText("Phá hết gạch để qua màn!", 390, 400);
                gc.fillText("SPACE để bắn (khi có power-up)", 330, 430);
                break;

            case PLAYING:
                // set màu nền
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                // vẽ bricks,paddle,ball
                for (Brick brick : bricks) {
                    if (!brick.isDestroyed()) {
                        brick.render(gc);
                    }
                }

                // Vẽ power-ups
                for (PowerUp powerUp : activePowerUps) {
                    powerUp.render(gc);
                }

                // Vẽ bullets
                for (Bullet bullet : bullets) {
                    bullet.render(gc);
                }

                paddle.render(gc);

                // Vẽ tất cả bóng
                for (Ball ball : balls) {
                    ball.render(gc);
                }

                // điểm số
                gc.setFill(Color.WHITE);
                gc.setFont(new Font("Arial", 20));
                gc.fillText("Score: " + score, 20, 30);
                gc.fillText("Lives: " + lives, 20, 55);
                gc.fillText("Highscore: " + highScore, 20, 80);
                // hiển thị tên level
                gc.fillText(currentLevel.getLevelName(), 750, 30);

                // Hiển thị power-ups active
                if (shootEnabled) {
                    gc.setFill(Color.DEEPSKYBLUE);
                    gc.fillText("SHOOT: ON", 20, 105);
                }
                if (fastBallEnabled) {
                    gc.setFill(Color.RED);
                    gc.fillText("FAST: " + String.format("%.1f", fastBallTimer), 20, 130);
                }
                break;

            // Hiển thị game over nếu hết mạng
            case GAME_OVER:
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.RED);
                gc.setFont(new Font("Arial", 40));
                gc.fillText("GAME OVER", 400, 300);

                gc.setFill(Color.YELLOW);
                gc.setFont(new Font("Arial", 24));
                gc.fillText("Highscore: " + highScore, 420, 350);
                gc.fillText("Click để chơi lại", 420, 390);
                break;

            // Hiển thị thông báo level up
            case NEXT_LEVEL:
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
                gc.setFill(Color.GREEN);
                gc.setFont(new Font("Arial", 40));
                gc.fillText("LEVEL COMPLETED!", 370, 300);
                gc.setFont(new Font("Arial", 24));
                gc.fillText("Điểm: " + score, 450, 350);
                gc.fillText("Click để tiếp tục", 420, 390);;
                break;
        }
    }
}
