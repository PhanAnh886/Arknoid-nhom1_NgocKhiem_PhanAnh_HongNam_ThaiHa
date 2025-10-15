package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

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
        if(mouseClicked) {
            this.dy = -250;
            setClicked(false);
        }
    }

    public void setSpawnBall() {
        setDy(0);
        setDx(0);
        setY(500-34);
        setX(500-17);
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
        //lấy vị trí gần tâm x,y nhất
        double closestX = Math.max(brick.getX(), Math.min(cx, brick.getX() + brick.getWidth()));
        double closestY = Math.max(brick.getY(), Math.min(cy, brick.getY() + brick.getHeight()));
        //khoảng cách đến điểm gàn nhất
        double dx = cx - closestX;
        double dy = cy - closestY;
        //so sánh vs bán kính(py ta go)
        return (dx * dx + dy * dy) <= (r * r);
    }

    public void update(double dt, Paddle paddle, ArrayList<Brick> bricks) {
        if (launched) {
            super.update(dt);
            if (intersects(paddle)) {
                double ballCenterX = getX() + getWidth() / 2;
                double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
                double relativeHitPos = (ballCenterX - paddleCenterX) / (paddle.getWidth() / 2);
                double maxBounceAngle = Math.toRadians(60);
                double bounceAngle = relativeHitPos * maxBounceAngle;

                double ballSpeed = Math.sqrt(getDx() * getDx() + getDy() * getDy());

                setDx(ballSpeed * Math.sin(bounceAngle));
                setDy(-Math.abs(ballSpeed * Math.cos(bounceAngle))); // bật lên
            }

            if (x <= 0) { //chạm bên trái
                x = 0;
                bounceX();
            }
            else if (x + width >= 1000) { //chạm bên phải
                x = 1000 - width;
                bounceX();
            }

            // chạm trần
            if (y <= 0) {
                y = 0;
                bounceY();
            }

            // roi xuong (thua)
            if (y + height >= 600) {
                ballLose();// reset bóng về paddle
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
        gc.setFill(Color.ORANGERED);
        gc.fillOval(x, y, width, height);
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
