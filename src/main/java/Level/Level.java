package Level;
import objectsInGame.*;
import objectsInGame.bricks.*;
import objectsInGame.powerups.*;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Class cơ sở cho tất cả các level
 * Abstract class để các level khác extend
 */
public abstract class Level {
    // Danh sách các gạch trong level
    protected CopyOnWriteArrayList<Brick> bricks;

    // Tên của level này
    protected String levelName;

    // --- Hằng số kích thước gạch ---
    protected static final double BRICK_WIDTH = 50;
    protected static final double BRICK_HEIGHT = 35;
    protected static final double BRICK_PADDING = 1;

    /**
     * Constructor - các level con sẽ gọi createLevel()
     */
    public Level() {
        this.bricks = new CopyOnWriteArrayList<>();
    }

    /**
     * Hàm tạo gạch cho level - mỗi level sẽ override
     */
    protected abstract void createLevel();

    /**
     * Lấy danh sách tất cả gạch trong level
     */
    public CopyOnWriteArrayList<Brick> getBricks() {
        return bricks;
    }

    /**
     * Lấy tên của level
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Kiểm tra đã phá hết gạch chưa
     */
    public boolean isCompleted() {
        for (Brick brick : bricks) {
            if (!brick.isDestroyed() && brick.canBeDestroyed()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Reset tất cả gạch về trạng thái ban đầu
     */
    public void reset() {
        for (Brick brick : bricks) {
            if (brick.canBeDestroyed()) {
                brick.destroyed(false, bricks);
                brick.setScored(false);
            }
        }
    }
}