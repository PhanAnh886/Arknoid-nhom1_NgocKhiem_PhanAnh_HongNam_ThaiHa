package objectsInGame;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Cache hình ảnh để tránh load lại liên tục.
 * Singleton pattern - chỉ có 1 instance duy nhất.
 */
public class ImageCache {
    private static ImageCache instance;
    private Map<String, Image> imageMap;

    private ImageCache() {
        imageMap = new HashMap<>();
        preloadAllImages();
    }

    /**
     * Lấy instance duy nhất.
     */
    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    /**
     * Load trước TẤT CẢ hình ảnh khi game khởi động.
     */
    private void preloadAllImages() {
        System.out.println("=== Preloading all images ===");

        // Ball.
        loadImage("/image/ball/ball.peach.png");

        // Paddle.
        loadImage("/image/paddle/stick_2.png");

        // Bricks.
        loadImage("/image/bricks/normalBrick.png");
        loadImage("/image/bricks/exploseBrick.png");
        loadImage("/image/bricks/unbreakableBrick.png");
        loadImage("/image/bricks/hardenBrick1HitLeft.png");
        loadImage("/image/bricks/hardenBrick2HitLeft.png");
        loadImage("/image/bricks/hardenBrick3HitLeft.png");

        // Backgrounds.
        loadImage("/image/inGame/inGame.png");
        loadImage("/image/Menu/Menu.png");
        loadImage("/image/menuBranchs/menuBranch.png");
        loadImage("/image/gameOver/gameOver.png");

        System.out.println("=== Loaded " + imageMap.size() + " images ===");
    }

    /**
     * Load 1 hình ảnh vào cache.
     */
    private void loadImage(String path) {
        try {
            if (!imageMap.containsKey(path)) {
                Image img = new Image(getClass().getResourceAsStream(path));
                imageMap.put(path, img);
                System.out.println("Loaded: " + path);
            }
        } catch (Exception e) {
            System.err.println("Failed to load: " + path);
        }
    }

    /**
     * Lấy hình ảnh từ cache (NHANH).
     */
    public Image getImage(String path) {
        Image img = imageMap.get(path);
        if (img == null) {
            // Nếu chưa có trong cache, load ngay.
            loadImage(path);
            img = imageMap.get(path);
        }
        return img;
    }

    /**
     * Kiểm tra xem có hình ảnh trong cache không.
     */
    public boolean hasImage(String path) {
        return imageMap.containsKey(path) && imageMap.get(path) != null;
    }

    /**
     * Clear cache (dùng khi cần giải phóng bộ nhớ).
     */
    public void clear() {
        imageMap.clear();
    }
}