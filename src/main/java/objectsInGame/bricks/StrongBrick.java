package objectsInGame.bricks;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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
    }

    @Override
    public void destroyed(boolean value, ArrayList<Brick> bricks) {
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
                gc.setFill(Color.DARKVIOLET);
            } else if (hitPoints == 2) {
                gc.setFill(Color.PURPLE);
            } else {
                gc.setFill(Color.MEDIUMPURPLE);
            }

            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            gc.strokeRect(x, y, width, height);

            // Hiển thị số hit còn lại
            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf(hitPoints), x + width/2 - 5, y + height/2 + 5);
        }
    }

    @Override
    public int getScore() {
        return 20 * maxHitPoints; // Điểm cao hơn theo độ cứng
    }
}