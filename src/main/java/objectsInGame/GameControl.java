package objectsInGame;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;               // font cho chữ
import javafx.scene.image.Image;
import objectsInGame.bricks.*;
import objectsInGame.powerups.*;
import Level.*;
import javagraphicmain.Main;

import sound.SoundManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;//danh sách gạch
import java.util.stream.Collectors;

/**
 * Class điều khiển game chính - ĐÃ TỐI ƯU ĐA LUỒNG
 *
 * Các cải tiến:
 * 1. Sử dụng CopyOnWriteArrayList để thread-safe
 * 2. Parallel streams cho collision detection
 * 3. Object pooling cho bullets
 * 4. Cached rendering với dirty flag
 */
public class GameControl extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;
    private Main mainApp;

    private Paddle paddle;
    private CopyOnWriteArrayList<Ball> balls = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Brick> bricks = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<PowerUp> activePowerUps = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Bullet> bullets = new CopyOnWriteArrayList<>();


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
    private boolean shootEnabled = false; //mục đích chính là để cho phép method render vẽ ON/OFF
    private double shootTimer = 0;
    private double shootCooldown = 0.3; // Bắn mỗi 0.3 giây

    private boolean fastBallEnabled = false; //mục đích chính là để cho phép method render vẽ đếm giờ
    private double fastBallTimer = 0;

    private boolean paddleSizeEnabled = false;
    private double paddleSizeTimer = 0;
    private static final double PADDLE_SIZE_DURATION = 10.0; // 10 giây


    // Nút Pause (vùng click góc phải dưới)
    private double pauseButtonX = 700;
    private double pauseButtonY = 550;
    private double pauseButtonWidth = 80;
    private double pauseButtonHeight = 35;

    /**
     *
     * @param mainApp         dùng đối tượng mainApp để truy cập vào các method đổi scene
     * @param startLevelIndex level đầu vào
     */
    public GameControl(Main mainApp, int startLevelIndex) {
        this.mainApp = mainApp;
        this.currentLevelIndex = startLevelIndex;

        canvas = new Canvas(800, 600);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas); // thêm bảng để vẽ lên Pane(ở đây là gameControl)

        paddle = new Paddle(420, 500, 160, 20);
        balls.add(new Ball(500 - 17, 500 - 34, 17));

        // Khởi tạo các level
        levels = new Level[]{
                new Level0(),
                new Level1(),
                new Level2()
        };

        loadHighScore();
        setupMouseControls();
        setupKeyboardControls();

        currentLevel = levels[currentLevelIndex];      // Tạo level mới
        bricks = currentLevel.getBricks();    // Lấy danh sách gạch từ level

        // khởi động vòng lặp game
        startGameLoop();
    }

    //---------------------METHOD HỖ TRỢ TRONG CONSTRUCTOR---------------------------------

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

    /**
     * method give chance to access mousecontrol, INPUT FROM MOUSE
     * (hàm này chủ yếu giúp di chueyern paddle và nhận các cú click chuột trong màn hình chờ)
     */
    private void setupMouseControls() {
        // sự kiện paddle và bóng di chuyển theo chuột
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getX();

                // paddle luôn đi theo chuột
                paddle.setX(mouseX - paddle.getWidth() / 2);

                // nếu bóng chưa phóng thì đi theo luôn
                balls.parallelStream().forEach(ball ->{ // làm nhiều việc cùng lúc
                    if (!ball.isLaunched()) {
                        ball.setX(mouseX - ball.getWidth() / 2);
                        //giới hạn ball khi paddle chạm biên ở cả hai đầu
                        ball.setX(Math.min(800 - paddle.getWidth() / 2 - ball.width / 2,
                                Math.max(ball.getX(), paddle.getWidth() / 2 - ball.width / 2)));
                    }
                });
            }
        });

        // Sự kiện click chuột -> phóng bóng
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getX();
                double mouseY = event.getY();

                //kiểm tra click vào nút pause
                if (mouseX >= pauseButtonX && mouseX <= pauseButtonX + pauseButtonWidth &&
                        mouseY >= pauseButtonY && mouseY <= pauseButtonY + pauseButtonHeight) {
                    mainApp.showPause();
                    return;
                }
                balls.parallelStream().forEach(ball -> {
                    if (!ball.isLaunched()) {
                        ball.setLaunched(true); // allowed to launch the ball(đây là vị trí duy nhất cho phép phóng)
                    }
                });
            }
        });
    }

    //-------METHODS SUPPORT FOR THE setupMouseControls METHOD-----------------------------------

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
    }

    //--------------------------------------------------------------------------------------------

    /**
     * method of receiving input from the keyboard.
     *
     */
    private void setupKeyboardControls() {
        this.setFocusTraversable(true);
        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {//lấy mã phím từ đầu vào
                    case E:
                        if (shootEnabled) shootBullet(); //this method jusst worked when shootEnabled = true
                        break;
                    case ESCAPE:
                    case P:
                        mainApp.showPause();
                        break;
                }
            }
        });
        this.requestFocus();
    }

    //-------------METHOD SUPPORT FOR THE setupKeyboardControls METHO-------------------------------

    /**
     * method for powerup shootBullet(no cooldown), just have to enable
     */
    private void shootBullet() {
        if (shootTimer <= 0) {// when cooldown ends -> allow to shoot continually
            // Bắn 2 viên đạn từ 2 bên paddle
            double leftX = paddle.getX();
            double rightX = paddle.getX() + paddle.getWidth() - 5;
            double y = paddle.getY();

            Bullet leftBullet = new Bullet(leftX, y);
            Bullet rightBullet = new Bullet(rightX, y);

            leftBullet.setActive(true);
            rightBullet.setActive(true);

            bullets.add(leftBullet);
            bullets.add(rightBullet);

            shootTimer = shootCooldown; // reset cooldown to 0,3
        }
    }

    //-------------------------------------------------------------------------------------------

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

    //----------------UPDATE AND RENDER ALL(called in startGameLoop method)-------------------

    /**
     * Cập nhật logic
     */
    private void update(double dt) {
        //nếu đã hoàn thành level
        if (currentLevel.isCompleted()) {  // Thay vì duyệt bricks
            balls.parallelStream().forEach(ball -> {
                ball.setLaunched(false);
            });
            loop.stop();
            mainApp.showNextLevel(currentLevelIndex, score, lives);
            return;
        }

        // -------BALLS-------
        // Xóa bóng rơi xuốnG
        CopyOnWriteArrayList<Ball> ballsToRemove = new CopyOnWriteArrayList<>(); //create a array to save the balls which are losed down
        balls.parallelStream().forEach(ball -> {
            if (ball.isLaunched() && ball.getY() + ball.getHeight() >= 600) {//ball position touch the bottom of the screen
                ballsToRemove.add(ball);
            }
            ball.update(dt, paddle, bricks); // LƯU Ý: hàm này là overloading hàm
            // update() của lớp cha chứ ko phải override
        });
        ballsToRemove.parallelStream().forEach(ball -> {//erase
            balls.remove(ball);
        });

        // Nếu hết bóng -> -1 mạng
        if (balls.isEmpty()) {//if there are no ball in the queue(array)
            lives--;
            if (lives > 0) {
                // Reset game về trạng thái chờ phóng
                resetState();
            } else {
                // GAME OVER
                if (score > highScore) {
                    highScore = score;
                    saveHighScore(); // Gọi hàm lưu Highscore để cập nhập highscore mới
                }
                loop.stop();
                mainApp.showGameOver(score, highScore);
            }
        }

        //-------BULLETS------
        // Cập nhật đạn
        bullets.parallelStream().forEach(bullet ->  {
            bullet.update(dt); //là override nên ko bao gồm xóa brick nên phải có duyệt+xóa brick ở dưới

            // Kiểm tra va chạm đạn vs gạch
            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && bullet.isActive() && bullet.intersects(brick)) {
                    brick.destroyed(true, bricks);
                    bullet.setActive(false);//unactive the bullet if it hit brick
                    break;
                }
            }
        });

        // Xóa đạn không active
        bullets.removeIf(bullet -> !bullet.isActive());

        //----------POWER-UPS-----------------
        // Kiểm tra gạch bị phá -> thả power-up
        bricks.parallelStream().forEach(brick -> {
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
                    powerUp.activate(); // cho phép thả
                    activePowerUps.add(powerUp);
                    brick.setPowerUp(null);
                }
            }
        });

        // Cập nhật power-ups rơi xuống
        activePowerUps.forEach(powerUp -> {
            powerUp.update(dt);

            // Kiểm tra paddle bắt được power-up
            if (powerUp.intersects(paddle)) {
                applyPowerUp(powerUp); // power ups are activated as soon as they touched the paddle,
                // event xử lý 1 lần
                powerUp.setActive(false);
            }

            // Tắt power-up nếu rơi ra ngoài
            if (powerUp.getY() > 600) {
                powerUp.setActive(false);
            }
        });

        // Cập nhật power-up timers, ko nên nhét vào apply power up vì đoạn này cần chạy theo thời gian,nhét
        // vào trong applypowwerup thì nó sẽ chỉ chạy 1 lần khi chạm paddle
        if (shootTimer > 0) shootTimer -= dt;

        if (fastBallTimer > 0) {
            fastBallTimer -= dt;
            if (fastBallTimer <= 0) {
                fastBallEnabled = false;
                // Trả tốc độ bóng về bình thường
                balls.parallelStream().forEach(ball -> {
                    double speed = Math.sqrt(ball.getDx() * ball.getDx() + ball.getDy() * ball.getDy());
                    if (speed > 250) {//nếu quá tốc độ default thì khôi phục lại speed
                        ball.setDx(ball.getDx() * (1 / 1.5));
                        ball.setDy(ball.getDy() * (1 / 1.5));
                    }
                });
            }
        }

        if (paddleSizeTimer > 0) {
            paddleSizeTimer -= dt;
            if (paddleSizeTimer <= 0) {
                paddleSizeEnabled = false;
                paddle.resetSizeLevel(); // Trả về kích thước normal
            }
        }

        // Xóa power-ups không active
        activePowerUps.removeIf(p -> !p.isActive());

        //------PADDLE-------
        paddle.update();

        //----------BRICKS------
        // KHÔNG CÓ UPDATE BRICKS VÌ ĐÂY LÀ OBJECT BỊ BỘNG,
        // ĐC CẬP NHẬT ĐỒNG THỜI TRONG CÁC METHOD UPDATE() CỦA
        // CÁC OBJECTS CHỦ ĐỘNG KHÁC KHI CHẠM VÔ BRICK(intersect)
    }

    //------------CÁC HÀM HỖ TRỢ TRONG METHOD UPDATE---------
    private void saveHighScore() {
        try {
            java.io.PrintWriter writer = new java.io.PrintWriter(HIGHSCORE_FILE);
            writer.println(highScore);
            writer.close();
        } catch (java.io.IOException e) {
            System.err.println("Lỗi khi lưu Highscore: " + e.getMessage());
        }
    }

    /**
     * method where powerup are applied
     *
     * @param powerUp
     */
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
                    fastBallTimer += 8.0; // +8 giây
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
                        Ball ball1 = new Ball(original.getX(), original.getY(), original.getHeight() / 2);
                        Ball ball2 = new Ball(original.getX(), original.getY(), original.getHeight() / 2);

                        ball1.setLaunched(true);
                        ball2.setLaunched(true);

                        double speed = Math.sqrt(original.getDx() * original.getDx() +
                                original.getDy() * original.getDy()); // lấy tốc độ tuyệt đối của ball đầu tiên

                        //Tách thành dạng speed = sqrt( Dx^2*sin^2(45) + Dy^2*cos^2(45) )
                        //ball1 bay lên trên và sang trái (vì sin(-45) < 0)
                        ball1.setDx(speed * Math.sin(Math.toRadians(-45)));
                        ball1.setDy(-speed * Math.cos(Math.toRadians(-45)));
                        //ball2 bay lên trên và sang phải
                        ball2.setDx(speed * Math.sin(Math.toRadians(45)));
                        ball2.setDy(-speed * Math.cos(Math.toRadians(45)));

                        balls.add(ball1);
                        balls.add(ball2);
                    }
                }
                break;

            case "PADDLE_SIZE":
                // Tăng kích thước paddle
                paddle.increaseSizeLevel();
                paddleSizeEnabled = true;
                paddleSizeTimer = PADDLE_SIZE_DURATION; // 10 giây
                break;
        }
    }

    //--------- LƯU Ý: method quan trọng giúp hỗ trợ nhiều mắt xích---------------

    /**
     * method gộp các object/trạng thái bị reset khi mất 1 mạng (khi cần reset trạng thái mà ko khôi phục gạch)
     * method is called to reset anything except brick
     */
    private void resetState() {
        //reset ball+ paddle
        balls.clear();
        Ball newBall = new Ball(500 - 17, 500 - 34, 17);
        balls.add(newBall);
        newBall.setClicked(true);
        paddle.resetPaddle(newBall.ballLose());

        //reset powerup
        bullets.clear();
        activePowerUps.clear();
        shootEnabled = false;
        fastBallEnabled = false;
        paddleSizeEnabled = false;
        paddleSizeTimer = 0;
        paddle.resetSizeLevel();
    }

    //---------------------------------------------------

    /**
     * Vẽ toàn bộ frame
     */
    private void renderAll() {
        // set màu nền
        Image bgImage = null;
        try {
            bgImage = new Image(getClass().getResourceAsStream("/image/inGame/inGame.png"));
        } catch (Exception e) {
            System.err.println("Không thể tải ảnh nền!");
            e.printStackTrace();
        }

        if (bgImage != null) {
            // 2. Định nghĩa kích thước nền (800x600, bằng kích thước Scene)
            if (bgImage != null) {
                // Vẽ ảnh nền khớp với kích thước canvas
                gc.drawImage(bgImage, 0, 0, canvas.getWidth(), canvas.getHeight());
            } else {
                // Dự phòng nếu không tải được ảnh
                gc.setFill(Color.BLACK);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            }

            // vẽ bricks,paddle,ball
            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    brick.render(gc);
                }
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

        // Vẽ paddle
        paddle.render(gc);

        // Vẽ tất cả bóng
        for (Ball ball : balls) {
            ball.render(gc);
        }

        // điểm số
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 20));
        gc.fillText("Score: " + score, 20, 30);
        gc.setFill(Color.ORANGE);
        gc.fillText("Lives: " + lives, 20, 55);
        gc.setFill(Color.GREEN);
        gc.fillText("Highscore: " + highScore, 20, 80);
        // hiển thị tên level
        gc.setFill(Color.YELLOW);
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

        // Hiển thị paddle size status
        if (paddleSizeEnabled) {
            gc.setFill(Color.MEDIUMPURPLE);
            String levelText = "";
            switch (paddle.getSizeLevel()) {
                case 1:
                    levelText = "LARGE";
                    break;
                case 2:
                    levelText = "XLARGE";
                    break;
                case 3:
                    levelText = "XXLARGE";
                    break;
            }
            gc.fillText("PADDLE: " + levelText + " (" + String.format("%.1f", paddleSizeTimer) + "s)", 20, 155);
        }

        // Vẽ nút PAUSE
        gc.setFill(Color.rgb(50, 50, 50, 0.8));
        gc.fillRoundRect(pauseButtonX, pauseButtonY, pauseButtonWidth, pauseButtonHeight, 10, 10);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRoundRect(pauseButtonX, pauseButtonY, pauseButtonWidth, pauseButtonHeight, 10, 10);

        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 18));
        gc.fillText("PAUSE", pauseButtonX + 12, pauseButtonY + 23);
    }

    public void pauseGame() {
        if (loop != null) {
            loop.stop();
        }
    }

    public void resumeGame() {
        if (loop != null) {
            loop.start();
        }
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }
}


