package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Class đạn bắn từ paddle
 */
public class Bullet extends MovableObject {
    private boolean active = false;

    public Bullet(double x, double y) {
        super(x, y, 5, 15);  // Đạn nhỏ 5x15 pixel
        this.dy = -400;  // Bắn lên trên nhanh
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void update(double dt) {
        if (active) {
            super.update(dt);
            // Tắt đạn nếu ra khỏi màn hình
            if (y < 0) {
                active = false;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (active) {
            gc.setFill(Color.YELLOW);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.ORANGE);
            gc.setLineWidth(1);
            gc.strokeRect(x, y, width, height);
        }
    }
}