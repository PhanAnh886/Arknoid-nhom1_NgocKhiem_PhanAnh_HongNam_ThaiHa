package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import objectsInGame.bricks.*;
import javafx.scene.effect.DropShadow;
import java.util.ArrayList;

/**
 * class ball
 */
public class Ball extends MovableObject {
    private boolean launched = false;
    private boolean mouseClicked = true; // mặc định ban đầu là cho bắn bóng

    //Constructor with 3 inputs (cuz width = height = radius x2)
    public Ball(double x, double y, double radius) {
        super(x, y, radius * 2, radius * 2); // đường kính
        this.dx = 0; // tốc độ ban đầu
        this.dy = 0;
    }

    /**
     * method check if the ball launched or not
     *
     * @return launch state of the ball
     */
    public boolean isLaunched() {
        return launched;
    }

    /**
     * method set click state,control clicked element, kiểm soát để click không hoạt động trong khi bóng đang bay
     *
     * @param clicked trạng thái trả về của click
     */
    public void setClicked(boolean clicked) {
        mouseClicked = clicked;
    }

    /**
     * method chuyển và tắt trạng thái đã phóng bóng, còn dy = -250 hoạt động nhưu công tắc bật 1 lần r tắt
     *
     * @param value giá trị để thay đổi launched
     */
    public void setLaunched(boolean value) {
        this.launched = value;
        if (mouseClicked) {//restrict clicked multiple time which make the ball increase speed
            this.dy = -250;
            setClicked(false);
        }
    }

    /**
     * reset về trạng thái của ball lúc đầu (ở vị trí cân đối chih giữa màn hình và paddle)
     */
    public void setSpawnBall() {
        setDy(0);
        setDx(0);
        setY(500 - 34);
        setX(500 - 17);
    }

    /**
     * method giúp trả về trạng thái đã mất bóng, đồng thời reset launched về khi ch phóng và reset trạng thái bóng
     * lúc đầu
     *
     * @return trạng thái đã mất bóng
     */
    public boolean ballLose() {
        launched = false;
        setSpawnBall();
        return true;
    }

    /**
     * method check va chạm vs bricks
     *
     * @param brick brick bị va chạm
     * @return đã va chạm hay chưa
     */
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

    /**
     * hàm update chính của ball, điều tiết hoạt động cơ bản
     *
     * @param dt     1/60(bây h ch có tác dụng lắm, để chỉnh tốc độ theo frame)
     * @param paddle truyền vào đối tượng paddle mà ball tương tác
     * @param bricks truyền vào các đối tượng bricks mà ball tương tác
     */
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
            }

            // chạm bên trái
            if (x <= 0) {
                x = 0;
                bounceX();
            } else if (x + width >= 1000) { //chạm bên phải
                x = 1000 - width;
                bounceX();
            }

            // chạm trần
            if (y <= 0) {
                y = 0;
                bounceY();
            }


            // va chạm với gạch
            for (Brick brick : bricks) {
                //ball chỉ chạm bóng khi isDestroyed là true(ko tính các loại gạch đặc biệt)
                if (!brick.isDestroyed() && intersects(brick)) {
                    double overlapX = Math.min(x + width - brick.getX(), brick.getX() + brick.getWidth() - x);
                    double overlapY = Math.min(y + height - brick.getY(), brick.getY() + brick.getHeight() - y);

                    if (overlapX < overlapY) bounceX();
                    else bounceY();

                    brick.destroyed(true,bricks);
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
     * hàm nếu va cham xảy ra theo phương ngang
     */
    public void bounceX() {
        dx = -dx;
    }

    /**
     * hàm nếu va chạm xảy ra theo phương dọc
     */
    public void bounceY() {
        dy = -dy;
    }
}
