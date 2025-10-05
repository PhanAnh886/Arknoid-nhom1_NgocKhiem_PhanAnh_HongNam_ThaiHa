package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball extends MovableObject {
    public Ball(double x, double y, double radius) {
        super(x, y, radius*2, radius*2); // đường kính
        this.dx = 2.5; // tốc độ ban đầu
        this.dy = -2.5;
    }
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.ORANGERED);
        gc.fillOval(x, y, width, height);
    }

    // phản xạ
    public void bounceX() { dx = -dx; }
    public void bounceY() { dy = -dy; }

    // reset vị trí nếu cần
    public void reset(double x, double y, double vx, double vy) {
        this.x = x; this.y = y; this.dx = vx; this.dy = vy;
    }

}
