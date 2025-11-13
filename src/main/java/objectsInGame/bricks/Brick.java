package objectsInGame.bricks;

import javafx.scene.canvas.GraphicsContext;
import objectsInGame.*;
import javafx.scene.image.Image;
import objectsInGame.powerups.PowerUp;

import java.util.concurrent.CopyOnWriteArrayList;


/**
 * class gạch.
 */
public abstract class Brick extends GameObject {
    protected boolean destroyed = false;
    protected boolean scored = false;
    protected PowerUp powerUp; // brick có thể chứa PowerUp.

    //load hình ảnh.
    protected Image brickImage;
<<<<<<< HEAD
    protected boolean useImage = false; //element check

=======
    protected boolean useImage = false; //element check.
>>>>>>> 029424d0357d73b2758c8b869a52cc700f771e56
    /**
     * constructor gạch.
     *
     * @param x      hoành độ
     * @param y      tung độ
     * @param width  chiều dài
     * @param height chiều rộng
     */
    public Brick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    /**
     * Load hình ảnh từ ImageCache.
     */
    protected void loadImage(String imagePath) {
        try {
            ImageCache cache = ImageCache.getInstance();
            brickImage = cache.getImage(imagePath);
            useImage = (brickImage != null);
            if (useImage) {
                System.out.println("✓ Using cached image: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("✗ Failed to load: " + imagePath);
            useImage = false;
        }
    }

    /**
     * method destroy the brick.
     *
     * @param value trạng thái
     */
    public void destroyed(boolean value, CopyOnWriteArrayList<Brick> bricks) {
        this.destroyed = value;
    }

    /**
     * method kiểm tra trạng thái ã tính điểm cho cục brick này chưa.
     *
     * @return dã check điểm chưa.
     */
    public boolean isScored() {
        return scored;
    }

    /**
     * method thay đổi trạng thái đã check điểm ch(thường truyền vào true để ko tính điểm nx).
     *
     * @param value true/false
     */
    public void setScored(boolean value) {
        scored = value;
    }

    /**
<<<<<<< HEAD
     * Kiểm tra xem gạch này có thể bị phá không, dùng để phân biệt vs unbreakable brick
     *
     * @return true nếu có thể phá
=======
     * Kiểm tra xem gạch này có thể bị phá không, dùng để phân biệt vs unbreakable brick.
     * @return true nếu có thể phá.
>>>>>>> 029424d0357d73b2758c8b869a52cc700f771e56
     */
    public boolean canBeDestroyed() {
        return true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    /**
<<<<<<< HEAD
     * Trả về điểm số khi phá gạch này
     *
     * @return điểm số
=======
     * Trả về điểm số khi phá gạch này.
     * @return điểm số.
>>>>>>> 029424d0357d73b2758c8b869a52cc700f771e56
     */
    public abstract int getScore();

    /**
     * hàm vẽ lên canvas.
     *
     * @param gc dụng cụ vẽ.
     */
    @Override
    public abstract void render(GraphicsContext gc);
}
