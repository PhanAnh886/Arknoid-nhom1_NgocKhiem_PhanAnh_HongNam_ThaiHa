package objectsInGame.bricks;
import objectsInGame.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sound.SoundManager;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Gạch không thể phá - bóng bật lại nhưng không bị phá.
 */
public class UnbreakableBrick extends Brick {
    public UnbreakableBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        loadImage("/image/bricks/unbreakableBrick.png");
    }

    @Override
    public void destroyed(boolean value, CopyOnWriteArrayList<Brick> bricks) {
        SoundManager.getInstance().playGameSound("brick_break_metal");

        // Không cho phép phá gạch này
        super.destroyed(false,bricks);
    }

    @Override
    public boolean canBeDestroyed() {
        return false;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            if (useImage && brickImage != null) {
                // vẽ bằng hình ảnh
                gc.drawImage(brickImage, x, y, width, height);
            } else {
                // dự phòng khi ko vẽ đc bằng hình ảnh.
                gc.setFill(Color.FORESTGREEN);
                gc.fillRect(x, y, width, height);
                gc.setStroke(Color.DARKGREEN);
                gc.strokeRect(x, y, width, height);
            }
        }
    }

    @Override
    public int getScore() {
        return 0; // Không cho điểm
    }
}