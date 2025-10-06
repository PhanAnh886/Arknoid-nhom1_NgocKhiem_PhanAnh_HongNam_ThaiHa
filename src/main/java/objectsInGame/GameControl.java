package objectsInGame;

import javafx.animation.AnimationTimer;      // Tạo vòng lặp game
import javafx.scene.canvas.Canvas;           // Mặt vẽ
import javafx.scene.canvas.GraphicsContext;  // Dụng cụ vẽ
import javafx.scene.layout.Pane;             // Khung chứa, để dán lên
import javafx.scene.paint.Color;             // Màu sắc
import java.util.ArrayList;                  // Danh sách gạch

    public class GameControl extends Pane {
        private Canvas canvas;
        private GraphicsContext gc;

        private Paddle paddle;
        private Ball ball;
        private ArrayList<Brick> bricks = new ArrayList<>();

        private AnimationTimer loop;

        public GameControl() {
            canvas = new Canvas(500, 500);
            gc = canvas.getGraphicsContext2D();
            this.getChildren().add(canvas);

            // Khởi tạo đối tượng
            paddle = new Paddle(200, 450, 80, 20);
            ball = new Ball(240, 300, 15);

            // Tạo gạch
            int x1 = 10;
            int y1 = 50;
            int width = 55;
            int height = 25;

            for (int i = 0; i < 32; i++) {
                bricks.add(new Brick(x1 + (i % 8) * 60, y1, width, height));

                // Khi đủ 8 viên thì xuống hàng
                if ((i + 1) % 8 == 0) {
                    y1 += 30;  // Tăng y để vẽ hàng mới
                }
            }

            // Khởi động vòng lặp game
            startGameLoop();
        }

        /** Vòng lặp game chính */
        private void startGameLoop() {

            loop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                    double dt = 1/60;
                    update(dt);
                    renderAll();
                }
            };
            loop.start();
        }

        /** Cập nhật logic game */
        private void update(double dt) {
            // Gọi đúng phương thức update() của bạn
            ball.update(dt);
            paddle.update(dt);
        }

        /** Vẽ lại frame */
        private void renderAll() {
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            // Vẽ bricks
            gc.setFill(Color.ORANGE);
            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    gc.fillRect(brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
                }
            }

            // Vẽ paddle
            gc.setFill(Color.BLUE);
            gc.fillRect(paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

            // Vẽ ball
            gc.setFill(Color.RED);
            gc.fillOval(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
        }
    }

