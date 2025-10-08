package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color; // chỉnh màu sắc

/**
 * class paddle
 */
public class Paddle extends MovableObject {
    private double minX = 0, maxX = 1000; // giới hạn (cập nhật từ GameControl)

    /**
     * constructor paddle truyền tham số
     *
     * @param x      hoành độ
     * @param y      tung độ
     * @param width  độ dài
     * @param height chiều cao
     */
    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * cập nhập paddle(mới tạo cho có chứ ch cho nhận hành động từ chuột, phím)
     *
     * @param dt fps 60 nên truyền vào 1/60s
     */
    @Override
    public void update(double dt) {
        super.update(dt);
        // clamp trong khung
        if (x < minX) x = minX;
        if (x + width > maxX) x = maxX - width;
    }

    /**
     * vẽ lên màn hình
     *
     * @param gc công cụ vẽ lên canvas
     */
    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.DARKBLUE);
        gc.fillRect(x, y, width, height);
    }

    /**
     * hàm truyền giới hạn cho paddle (chưa sử dụng)
     *
     * @param minX bên trái
     * @param maxX bên phải
     */
    public void setBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }
}
