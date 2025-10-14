package objectsInGame;

import javafx.animation.AnimationTimer;      //tạo vòng lặp game
import javafx.scene.canvas.Canvas;           //mặt vẽ
import javafx.scene.canvas.GraphicsContext;  //dụng cụ vẽ
import javafx.scene.layout.Pane;             //khung chứa, để dán lên
import javafx.scene.paint.Color;             //màu sắc
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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

    /**
     * hàm control game chính
     */
    public GameControl() {
        canvas = new Canvas(1000, 600);
        gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);// thêm node canvas vào node lá Pane

        // khởi tạo đối tượng
        paddle = new Paddle(420, 500, 160, 20);
        ball = new Ball(500-17, 500-34, 17);

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

        canvas.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double mouseX = event.getX();

                // paddle luôn đi theo chuột
                paddle.setX(mouseX - paddle.getWidth() / 2);

                // nếu bóng chưa phóng thì đi theo luôn
                if (!ball.isLaunched()) {
                    ball.setX(mouseX - ball.getWidth() / 2);
                    ball.setX(Math.min(1000- paddle.getWidth()/2-17,Math.max(ball.getX(),paddle.getWidth()/2-17)));
                }
            }
        });

        // Sự kiện click chuột -> phóng bóng
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ball.setLaunched(true); // đánh dấu là bóng bắt đầu bay
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
        // cập nhập trạng thái( mới ở giai đoạn hiển thị trên màn hình, ch có va chạm hay di chuyển gì cả)
        ball.update(dt,paddle,bricks);
        paddle.update();
    }

    /**
     * vẽ lại frame
     *
     */
    private void renderAll() {
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
    }
}

