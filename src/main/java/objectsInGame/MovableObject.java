package objectsInGame;

import javafx.scene.canvas.GraphicsContext;

public abstract class MovableObject extends GameObject {
    protected double dx;
    protected double dy; // vận tốc

    public MovableObject(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * cập nhật vị trí theo vận tốc
     * @param dt
     */
    @Override
    public void update(double dt) {
        x += dx * dt;
        y += dy * dt;
    }

    public void setVelocity(double vx, double vy) { this.dx = vx; this.dy = vy; }
    public double getDx() { return dx; }
    public double getDy() { return dy; }

}
