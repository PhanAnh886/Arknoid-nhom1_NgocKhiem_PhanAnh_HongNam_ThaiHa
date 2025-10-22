package objectsInGame.bricks;
import objectsInGame.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * Gạch thường - phá được bằng 1 hit
 */
public class NormalBrick extends Brick {
    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            gc.setFill(Color.FORESTGREEN);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.DARKGREEN);
            gc.strokeRect(x, y, width, height);
        }
    }

    @Override
    public int getScore() {
        return 10; // 10 điểm cho gạch thường
    }
}

