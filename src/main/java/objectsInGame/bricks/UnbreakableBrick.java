package objectsInGame.bricks;
import objectsInGame.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Gạch không thể phá - bóng bật lại nhưng không bị phá
 */
public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void destroyed(boolean value, ArrayList<Brick> bricks) {
        // Không cho phép phá gạch này
        super.destroyed(false,bricks);
    }

    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    @Override
    public void render(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.DARKGRAY);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, width, height);

        // Vẽ dấu X để biểu thị không phá được
        gc.setStroke(Color.BLACK);
        gc.strokeLine(x + 5, y + 5, x + width - 5, y + height - 5);
        gc.strokeLine(x + width - 5, y + 5, x + 5, y + height - 5);
    }

    @Override
    public int getScore() {
        return 0; // Không cho điểm
    }
}