package objectsInGame;

import javafx.scene.canvas.GraphicsContext; //vẽ hình, màu, text lên Canvas

public abstract class GameObject {
    protected double x, y;        // Tọa độ
    protected double width, height; // Kích thước

    public GameObject(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void update(double dt) {}

    // đối tượng tự vẽ lên GraphicsContext
    public abstract void render(GraphicsContext gc);

    //va chạm
    public boolean intersects(GameObject other) {
        return this.x < other.x + other.width &&
                this.x + this.width > other.x &&
                this.y < other.y + other.height &&
                this.y + this.height > other.y;
    }

    // getter + setter
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
