package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.effect.DropShadow;

import java.util.ArrayList;

/**
 * class ball
 */
public class Ball extends MovableObject {
    private boolean launched = false;
    private boolean mouseClicked = true;

    public Ball(double x, double y, double radius) {
        super(x, y, radius * 2, radius * 2); // đường kính
        this.dx = 0; // tốc độ ban đầu
        this.dy = 0;
    }

    public boolean isLaunched() {
        return launched;
    }

    public void setClicked(boolean clicked) {
        mouseClicked = clicked;
    }

    public void setLaunched(boolean value) {
        this.launched = value;
        if (mouseClicked) {
            this.dy = -250;
            setClicked(false);
        }
    }

    public void setSpawnBall() {
        setDy(0);
        setDx(0);
        setY(500 - 34);
        setX(500 - 17);
    }

    public boolean ballLose() {
        launched = false;
        setSpawnBall();
        return true;
    }

    public boolean intersects(Brick brick) {
        double cx = x + width / 2; // tọa độ tâm x,y
        double cy = y + height / 2;
        double r = width / 2;
        // lấy vị trí gần tâm x,y nhất
        double closestX = Math.max(brick.getX(), Math.min(cx, brick.getX() + brick.getWidth()));
        double closestY = Math.max(brick.getY(), Math.min(cy, brick.getY() + brick.getHeight()));
        // khoảng cách đến điểm gần nhất
        double dx = cx - closestX;
        double dy = cy - closestY;
        // so sánh vs bán kính (py ta go)
        return (dx * dx + dy * dy) <= (r * r);
    }

    public void update(double dt, Paddle paddle, ArrayList<Brick> bricks) {
        if (launched) {
            super.update(dt);

            // va chạm với paddle
            if (intersects(paddle)) {
                double ballCenterX = getX() + getWidth() / 2;
                double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
                double relativeHitPos = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2);
                double maxBounceAngle = Math.toRadians(60);
                double bounceAngle = relativeHitPos * maxBounceAngle;

                double ballSpeed = Math.sqrt(getDx() * getDx() + getDy() * getDy());

                setDx(ballSpeed * Math.sin(bounceAngle));
                setDy(-Math.abs(ballSpeed * Math.cos(bounceAngle))); // bật lên

                // phát âm thanh bounce (từ GameControl)
                GameControl.playBounceSound();
            }

            // chạm bên trái
            if (x <= 0) {
                x = 0;
                bounceX();
                GameControl.playBounceSound();
            }
            // chạm bên phải
            else if (x + width >= 1000) {
                x = 1000 - width;
                bounceX();
                GameControl.playBounceSound();
            }

            // chạm trần
            if (y <= 0) {
                y = 0;
                bounceY();
                GameControl.playBounceSound();
            }

            // rơi xuống (thua)
            if (y + height >= 600) {
                // phát âm thanh thua (từ GameControl)
                GameControl.playLoseSound();

                // reset bóng về paddle (giữ nguyên cách bạn gọi ballLose() như cũ)
                ballLose(); // reset bóng về paddle
                paddle.resetPaddle(ballLose());
                setClicked(true);
            }

            // chạm gạch
            for (Brick brick : bricks) {
                if (!brick.isDestroyed() && intersects(brick)) {
                    double overlapX = Math.min(x + width - brick.getX(), brick.getX() + brick.getWidth() - x);
                    double overlapY = Math.min(y + height - brick.getY(), brick.getY() + brick.getHeight() - y);

                    if (overlapX < overlapY) bounceX();
                    else bounceY();

                    brick.setDestroyed(true);

                    // phát âm thanh va chạm với gạch (sử dụng âm thanh bounce để cảm giác va chạm),
                    // phần âm thanh "brick" bạn có thể phát trong GameControl khi xử lý gạch rõ ràng
                    GameControl.playBounceSound();

                    break;
                }
            }

        }
    }

    /**
     * hàm vẽ ball
     *
     * @param gc vẽ lên canvas
     */
    @Override
    public void render(GraphicsContext gc) {
        // hiệu ứng đổ bóng cho bóng
        DropShadow ds = new DropShadow();
        ds.setRadius(8);
        ds.setOffsetX(0);
        ds.setOffsetY(3);
        ds.setColor(Color.rgb(255, 215, 0, 0.6)); // vàng nhạt trong suốt
        gc.setEffect(ds);

        gc.setFill(Color.ORANGERED);
        gc.fillOval(x, y, width, height);

        // reset effect để các phần tử khác không bị ảnh hưởng
        gc.setEffect(null);
    }

    /**
     * hàm nếu va cham xảy ra
     */
    public void bounceX() {
        dx = -dx;
    }

    public void bounceY() {
        dy = -dy;
    }
}
