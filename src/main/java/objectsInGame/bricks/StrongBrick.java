package objectsInGame.bricks;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import objectsInGame.ImageCache;
import java.util.concurrent.CopyOnWriteArrayList;
import sound.SoundManager;

/**
 * Gạch cứng - cần nhiều lần hit mới phá.
 */
public class StrongBrick extends Brick {
    private int hitPoints; // Số lần hit còn lại.
    private int maxHitPoints;

    // Cache 3 trạng thái ảnh
    private static Image img3Hits;
    private static Image img2Hits;
    private static Image img1Hit;
    private static boolean imagesLoaded = false;

    public StrongBrick(double x, double y, double width, double height, int hitPoints) {
        super(x, y, width, height);
        this.hitPoints = hitPoints;
        this.maxHitPoints = hitPoints;

        // Load ảnh 1 LẦN DUY NHẤT cho tất cả StrongBrick.
        if (!imagesLoaded) {
            ImageCache cache = ImageCache.getInstance();
            img3Hits = cache.getImage("/image/bricks/hardenBrick3HitLeft.png");
            img2Hits = cache.getImage("/image/bricks/hardenBrick2HitLeft.png");
            img1Hit = cache.getImage("/image/bricks/hardenBrick1HitLeft.png");
            imagesLoaded = true;
        }

        updateImage();
    }

    /**
     * Cập nhật hình ảnh dựa trên hitPoints.
     */
    private void updateImage() {
        if (hitPoints == 3) {
            brickImage = img3Hits;
        } else if (hitPoints == 2) {
            brickImage = img2Hits;
        } else {
            brickImage = img1Hit;
        }
        useImage = (brickImage != null);
    }

    @Override
    public void destroyed(boolean value, CopyOnWriteArrayList<Brick> bricks) {
        //nếu set là true thì giảm hitPoints cho đến khi = 0 thì destroy(true).
        if (value) {
            hitPoints--;
            SoundManager.getInstance().playGameSound("brick_break_normal");

            if (hitPoints <= 0) {
                super.destroyed(true,bricks);
            } else {
                // CẬP NHẬT ẢNH KHI BỊ HIT
                updateImage();
            }
        } else {// nếu set là false thì trả về false và luôn set hitPoints trong trạng thái max.
            super.destroyed(false,bricks);
            this.hitPoints = maxHitPoints;
            updateImage();
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            if (useImage && brickImage != null) {
                gc.drawImage(brickImage, x, y, width, height);
            } else {
                // Dự phòng.
                gc.setFill(Color.FORESTGREEN);
                gc.fillRect(x, y, width, height);
                gc.setStroke(Color.DARKGREEN);
                gc.strokeRect(x, y, width, height);
            }
        }
    }

    @Override
    public int getScore() {
        return 20 * maxHitPoints; // Điểm cao hơn theo độ cứng
    }
}