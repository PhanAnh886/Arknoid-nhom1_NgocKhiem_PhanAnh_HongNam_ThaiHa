package objectsInGame;

import javafx.scene.canvas.GraphicsContext;

/**
 * class chung cho toàn bộ object di chuyển đc
 */
public abstract class MovableObject extends GameObject {
    protected double dx;
    protected double dy; // vận tốc

    /**
     * constructor chung
     *
     * @param x      hoành độ
     * @param y      tung độ
     * @param width  chiều dài
     * @param height chiều cao
     */
    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * cập nhật vị trí theo vận tốc
     *
     * @param dt chia ra cho 60 frame trong 1s
     */
    @Override
    public void update(double dt) {
        x += dx * dt;
        y += dy * dt;
    }

    /**
     * set tốc độ
     *
     * @param vx theo x
     * @param vy theo y
     */
    public void setVelocity(double vx, double vy) {
        this.dx = vx;
        this.dy = vy;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

}
