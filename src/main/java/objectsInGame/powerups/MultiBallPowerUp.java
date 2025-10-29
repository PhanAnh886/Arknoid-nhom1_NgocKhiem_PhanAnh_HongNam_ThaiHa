package objectsInGame.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Power-up tạo thêm nhiều bóng
 */
public class MultiBallPowerUp extends PowerUp {

    public MultiBallPowerUp(double x, double y) {
        super("MULTI");
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isActive()) {
            // Nền màu xanh lá
            gc.setFill(Color.LIMEGREEN);
            gc.fillOval(x, y, width, height);

            gc.setStroke(Color.DARKGREEN);
            gc.setLineWidth(2);
            gc.strokeOval(x, y, width, height);

            // Vẽ 3 bóng nhỏ
            gc.setFill(Color.WHITE);
            double centerX = x + width / 2;
            double centerY = y + height / 2;

            gc.fillOval(centerX - 6, centerY - 2, 4, 4);
            gc.fillOval(centerX, centerY - 5, 4, 4);
            gc.fillOval(centerX + 2, centerY + 1, 4, 4);
        }
    }
}