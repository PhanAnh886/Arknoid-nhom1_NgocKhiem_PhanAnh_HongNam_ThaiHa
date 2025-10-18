package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * class gạch
 */
public class Brick extends GameObject {
    private boolean destroyed = false;
    private boolean scored = false;

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
     * method set destroyed state for the bricks
     *
     * @param value trạng thái
     */
    public void setDestroyed(boolean value) {
        this.destroyed = value;
    }

    /**
     * method kiểm tra trạng thái ã tính điểm cho cục brick này chưa
     *
     * @return dã check điểm chưa
     */
    public boolean isScored() {
        return scored;
    }

    /**
     * method thay đổi trạng thái đã chekc điểm ch(thường truyền vào true để ko tính điểm nx)
     *
     * @param value true/false
     */
    public void setScored(boolean value) {
        scored = value;
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
