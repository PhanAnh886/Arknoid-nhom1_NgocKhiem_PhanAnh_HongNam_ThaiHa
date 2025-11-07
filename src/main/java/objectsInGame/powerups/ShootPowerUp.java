package objectsInGame.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Power-up cho phép paddle bắn đạn
 */
public class ShootPowerUp extends PowerUp {

    public ShootPowerUp(double x, double y) {
        super("SHOOT");
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isActive()) {
            // Nền màu xanh dương
            gc.setFill(Color.DEEPSKYBLUE);
            gc.fillOval(x, y, width, height);

            gc.setStroke(Color.DARKBLUE);
            gc.setLineWidth(2);
            gc.strokeOval(x, y, width, height);

            // Vẽ ký hiệu tên lửa
            gc.setFill(Color.WHITE);
            double centerX = x + width / 2;
            double centerY = y + height / 2;
            gc.fillRect(centerX - 2, centerY - 5, 4, 10);
            gc.fillPolygon(
                    new double[]{centerX - 4, centerX, centerX + 4},
                    new double[]{centerY, centerY - 8, centerY},
                    3
            );
        }
    }
}