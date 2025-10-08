package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * class ball
 */
public class Ball extends MovableObject {
    public Ball(double x, double y, double radius) {
        super(x, y, radius * 2, radius * 2); // đường kính
        this.dx = 5.5; // tốc độ ban đầu
        this.dy = -5.5;
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
