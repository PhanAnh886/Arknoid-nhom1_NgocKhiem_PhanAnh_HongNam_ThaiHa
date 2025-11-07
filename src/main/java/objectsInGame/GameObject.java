package objectsInGame;

import javafx.scene.canvas.GraphicsContext; //vẽ hình, màu, text lên Canvas
import objectsInGame.bricks.*;

/**
 * class chung cho mọi object
 */
public abstract class GameObject {
    protected double x, y;        // Tọa độ
    protected double width, height; // Kích thước

    /**
     * constructor chung cho object
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * cập nhật trnagj thái
     *
     * @param dt 1/60
     */
    public void update(double dt) {
    }

    /**
     * hàm vẽ chung
     */
    public abstract void render(GraphicsContext gc);

    /**
     * hàm xử lý va chạm
     *
     * @param other vật bị va chạm với this object
     * @return trạng thái đã va chạm hay chưa
     */
    public boolean intersects(GameObject other) {
        return this.x < other.x + other.width &&
                this.x + this.width > other.x &&
                this.y < other.y + other.height &&
                this.y + this.height > other.y;
    }

    // getter + setter
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
