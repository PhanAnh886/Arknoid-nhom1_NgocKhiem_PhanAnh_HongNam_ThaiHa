package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * class gạch
 */
public class Brick extends GameObject {
    private boolean destroyed = false;

    /**
     * constructor gạch
     *
     * @param x      hoành độ
     * @param y      tung độ
     * @param width  chiều dài
     * @param height chiều rộng
     */
    public Brick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * hàm thông báo đã bị phá
     */
    public void destroy() {
        destroyed = true;
    }

    /**
     * hàm trả vè khi bị intersects
     *
     * @return trạng thái đã bị phá
     */
    public boolean isDestroyed() {
        return destroyed;
    }

    /**
     * hàm vẽ lên canvas
     *
     * @param gc dụng cụ vẽ
     */
    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            gc.setFill(Color.FORESTGREEN);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.RED);
            gc.strokeRect(x, y, width, height);
        }
    }
}
