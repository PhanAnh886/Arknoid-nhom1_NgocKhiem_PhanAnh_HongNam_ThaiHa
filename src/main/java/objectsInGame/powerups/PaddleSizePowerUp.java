package objectsInGame.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Power-up tăng kích thước paddle.
 * Có 4 cấp độ: Normal (160) → Large (200) → XLarge (240) → XXLarge (280).
 */
public class PaddleSizePowerUp extends PowerUp {

    public PaddleSizePowerUp(double x, double y) {
        super("PADDLE_SIZE"); // Type mới.
        this.x = x;
        this.y = y;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (isActive()) {
            // Nền màu tím.
            gc.setFill(Color.MEDIUMPURPLE);
            gc.fillOval(x, y, width, height);

            gc.setStroke(Color.DARKVIOLET);
            gc.setLineWidth(2);
            gc.strokeOval(x, y, width, height);

            // Vẽ biểu tượng mũi tên kép trái-phải (←→).
            gc.setFill(Color.WHITE);
            double centerX = x + width / 2;
            double centerY = y + height / 2;

            // Mũi tên trái ←
            gc.fillPolygon(
                    new double[]{centerX - 7, centerX - 2, centerX - 2},
                    new double[]{centerY, centerY - 3, centerY + 3},
                    3
            );

            // Mũi tên phải →.
            gc.fillPolygon(
                    new double[]{centerX + 7, centerX + 2, centerX + 2},
                    new double[]{centerY, centerY - 3, centerY + 3},
                    3
            );

            // Vạch giữa.
            gc.setLineWidth(1.5);
            gc.setStroke(Color.WHITE);
            gc.strokeLine(centerX - 2, centerY, centerX + 2, centerY);
        }
    }
}