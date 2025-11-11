package objectsInGame.bricks;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Gạch cứng - cần nhiều lần hit mới phá
 */
public class StrongBrick extends Brick {
    private int hitPoints; // Số lần hit còn lại
    private int maxHitPoints;

    public StrongBrick(double x, double y, double width, double height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.maxHitPoints = hitPoints;
        if (hitPoints == 3) {
            loadImage("/image/bricks/hardenBrick3HitLeft.png");

        } else if (hitPoints == 2) {
            loadImage("/image/bricks/hardenBrick2HitLeft.png");

        } else {
            loadImage("/image/bricks/hardenBrick1HitLeft.png");

        }
    }

    @Override
    public void destroyed(boolean value, CopyOnWriteArrayList<Brick> bricks) {
        //nếu set là true thì giảm hitPoints cho đến khi = 0 thì destroy(true)
        if (value) {
            hitPoints--;
            if (hitPoints <= 0) {
                super.destroyed(true,bricks);
            }
        } else {// nếu set là false thì trả về false và luôn set hitPoints trong trạng thái max
            super.destroyed(false,bricks);
            this.hitPoints = maxHitPoints;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            // Màu đậm dần theo độ cứng
            if (hitPoints == 3) {
                loadImage("/image/bricks/hardenBrick3HitLeft.png");
            } else if (hitPoints == 2) {
                loadImage("/image/bricks/hardenBrick2HitLeft.png");
            } else {
                loadImage("/image/bricks/hardenBrick1HitLeft.png");
            }

            if (useImage && brickImage != null) {
                // vẽ bằng hình ảnh
                gc.drawImage(brickImage, x, y, width, height);
            } else {
                // dự phòng khi ko vẽ đc bằng hình ảnh
                gc.setFill(Color.FORESTGREEN);
                gc.fillRect(x, y, width, height);
                gc.setStroke(Color.DARKGREEN);
                gc.strokeRect(x, y, width, height);
            }
        }
    }

    @Override
    public int getScore() {
        return 20 * maxHitPoints; // Điểm cao hơn theo độ cứng
    }
}