package objectsInGame.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Power-up tăng tốc độ bóng.
 */
public class FastBallPowerUp extends PowerUp {

    public FastBallPowerUp(double x, double y) {
        super("FAST");
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isActive()) {
            // Nền màu đỏ.
            gc.setFill(Color.RED);
            gc.fillOval(x, y, width, height);

            gc.setStroke(Color.DARKRED);
            gc.setLineWidth(2);
            gc.strokeOval(x, y, width, height);

            // Vẽ dấu mũi tên nhanh.
            gc.setFill(Color.WHITE);
            double centerX = x + width / 2;
            double centerY = y + height / 2;

            // Mũi tên chỉ lên.
            gc.fillPolygon(
                    new double[]{centerX - 5, centerX, centerX + 5},
                    new double[]{centerY + 3, centerY - 6, centerY + 3},
                    3
            );
            gc.fillPolygon(
                    new double[]{centerX - 4, centerX, centerX + 4},
                    new double[]{centerY + 6, centerY, centerY + 6},
                    3
            );
        }
    }
}