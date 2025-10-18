package objectsInGame;

import javafx.animation.AnimationTimer;      //tạo vòng lặp game
import javafx.scene.canvas.Canvas;           //mặt vẽ
import javafx.scene.canvas.GraphicsContext;  //dụng cụ vẽ
import javafx.scene.layout.Pane;             //khung chứa, để dán lên
import javafx.scene.paint.Color;             //màu sắc
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;               // font cho chữ
import javafx.application.Platform;          // Để đóng ứng dụng

import java.util.ArrayList;                  //danh sách gạch

/**
 * class điều khiển game
 */
public class GameControl extends Pane {
    private Canvas canvas;
    private GraphicsContext gc;

    private Paddle paddle;
    private Ball ball;
    private ArrayList<Brick> bricks = new ArrayList<>();

    private AnimationTimer loop;

    // thông tin game
    private int score = 0;            // điểm người chơi
    private int lives = 3;            // số mạng còn lại
    private int highScore = 0;        // điểm cao nhất

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
     * hàm control game chính
     */
    public GameControl() {
        canvas = new Canvas(1000, 600);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);// thêm node canvas vào node lá Pane

        // khởi tạo đối tượng
        paddle = new Paddle(420, 500, 160, 20);
        ball = new Ball(500 - 17, 500 - 34, 17);

        // tạo gạch
        int x1 = 10;
        int y1 = 50;
        int width = 55;
        int height = 25;

        for (int i = 0; i < 80; i++) {
            bricks.add(new Brick(x1 + (i % 16) * 60, y1, width, height));

            // khi đủ 16 viên thì xuống hàng
            if ((i + 1) % 16 == 0) {
                y1 += 30;  //tăng y để vẽ hàng mới
            }
        }

        // sự kiện paddle và bóng di chuyển theo chuột
        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (gameState == GameState.PLAYING) {
                    double mouseX = event.getX();

                    // paddle luôn đi theo chuột
                    paddle.setX(mouseX - paddle.getWidth() / 2);

                    // nếu bóng chưa phóng thì đi theo luôn
                    if (!ball.isLaunched()) {
                        ball.setX(mouseX - ball.getWidth() / 2);
                        //giới hạn ball khi paddle chạm biên ở cả hai đầu
                        ball.setX(Math.min(1000 - paddle.getWidth() / 2 - ball.width / 2, Math.max(ball.getX(), paddle.getWidth() / 2 - ball.width / 2)));
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
                        ball.setLaunched(true);
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

        // khởi động vòng lặp game
        startGameLoop();
    }

    /**
     * vòng lặp game
     */
    void startGameLoop() {
        loop = new AnimationTimer() {
            @Override
            public void handle(long now) { //gọi mỗi frame
                double dt = 1.0 / 60.0;
                update(dt);
                renderAll();
            }
        };
        loop.start();
    }

    /**
     * cập nhật logic game
     *
     * @param dt 1/60 s
     */
    private void update(double dt) {
        switch (gameState) {
            case MENU:
                // Chờ người chơi click để bắt đầu
                break;

            case PLAYING:


                // Kiểm tra phá hết gạch
                boolean allDestroyed = true;
                for (Brick brick : bricks) {
                    if (!brick.isDestroyed()) {
                        allDestroyed = false;
                        break;
                    }
                }
                if (allDestroyed) {
                    gameState = GameState.NEXT_LEVEL;
                    ball.setLaunched(false);
                    return;
                }

                // Nếu bóng rơi khỏi màn hình -> mất mạng
                if (ball.isLaunched() && ball.getY() + ball.getHeight() >= 600) {
                    lives--;

                    if (lives <= 0) {
                        gameState = GameState.GAME_OVER;
                    } else {
                        resetState();
                    }
                }


                for (Brick brick : bricks) {
                    if (brick.isDestroyed() && !brick.isScored()) {
                        //brick.setDestroyed(true);
                        score += 10; // Tăng điểm khi phá gạch
                        brick.setScored(true); // Đảm bảo chỉ cộng điểm 1 lần
                        // Cập nhật highscore ngay khi có điểm mới lớn hơn highscore
                        if (score > highScore) {
                            highScore = score;
                        }
                    }
                }

                ball.update(dt, paddle, bricks);
                paddle.update();
                break;

        }
    }

    /**
     * method called when losed+clicked, reset game về trạng thái lcus đầu trừ highscore
     */
    private void resetGame() {
        score = 0;
        lives = 3;
        for (Brick brick : bricks) brick.setDestroyed(false);
        resetState();
        gameState = GameState.PLAYING;
    }

    /**
     * method gộp các object/trạng thái bị reset khi lose game
     */
    private void resetState() {
        ball.ballLose();
        paddle.resetPaddle(ball.ballLose());
        ball.setClicked(true);
    }

    /**
     * method called when all bricks are broked
     */
    private void nextLevel() {
        for (Brick brick : bricks) brick.setDestroyed(false);
        ball.ballLose();
        gameState = GameState.PLAYING;
    }

    /**
     * vẽ lại frame
     *
     */
    private void renderAll() {
        switch (gameState) {
            case MENU:
                gc.setFill(Color.BLACK);
                gc.setFont(new Font("Arial", 48));
                gc.fillText("ARKANOID", 370, 250); // tiêu đề game

                gc.setFont(new Font("Arial", 24));
                gc.fillText("Click chhuột để bắt đầu", 390, 320);
                gc.setFont(new Font("Arial", 20));
                gc.fillText("Điều khiển: Di chuột để di chuyển thanh đỡ", 310, 370);
                gc.fillText("Phá hết gạch để qua màn!", 390, 400);
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
                paddle.render(gc);
                ball.render(gc);

                // điểm số
                gc.setFill(Color.WHITE);
                gc.setFont(new Font("Arial", 20));
                gc.fillText("Score: " + score, 20, 30);
                gc.fillText("Lives: " + lives, 20, 55);
                gc.fillText("Highscore: " + highScore, 20, 80);
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
                gc.setFill(Color.GREEN);
                gc.setFont(new Font("Arial", 40));
                gc.fillText("NEXT LEVEL!", 420, 300);
                gc.setFont(new Font("Arial", 24));
                gc.fillText("Điểm hiện tại: " + score, 420, 350);
                gc.fillText("Click để tiếp tục", 420, 390);
                break;
        }
    }
}

