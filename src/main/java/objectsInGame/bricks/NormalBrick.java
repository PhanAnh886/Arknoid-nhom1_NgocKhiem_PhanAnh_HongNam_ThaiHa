package objectsInGame.bricks;
import objectsInGame.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import sound.SoundManager;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gạch thường - phá được bằng 1 hit.
 */
public class NormalBrick extends Brick {
    public NormalBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        loadImage("/image/bricks/normalBrick.png");
    }

    @Override
    public void destroyed(boolean value, CopyOnWriteArrayList<Brick> bricks) {
        if (value) {
            // Phát âm thanh khi phá gạch.
            SoundManager.getInstance().playGameSound("brick_break_normal");
        }
        super.destroyed(value, bricks);
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
        return 10; // 10 điểm cho gạch thường.
    }
}

