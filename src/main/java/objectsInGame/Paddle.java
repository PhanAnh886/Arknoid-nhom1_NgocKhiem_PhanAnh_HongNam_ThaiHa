package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color; // chỉnh màu sắc

public class Paddle extends MovableObject {
    private double minX = 0, maxX = 800; // giới hạn (cập nhật từ GameControl)

    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        // clamp trong khung
        if (x < minX) x = minX;
        if (x + width > maxX) x = maxX - width;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(x, y, width, height);
    }

    public void setBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }
}
