package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick extends GameObject {
    private boolean destroyed = false;

    public Brick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            gc.setFill(Color.FORESTGREEN);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, width, height);
        }
    }
}
