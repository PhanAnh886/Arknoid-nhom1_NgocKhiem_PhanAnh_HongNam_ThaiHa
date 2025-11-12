package objectsInGame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color; // chỉnh màu sắc
import javafx.scene.image.Image;
import objectsInGame.bricks.*;

/**
 * class paddle.
 */
public class Paddle extends MovableObject {
    private double minX = 0, maxX = 800; // giới hạn (cập nhật từ GameControl)
    // ------KÍCH THƯỚC PADDLE (4 cấp độ)------
    private static final double NORMAL_WIDTH = 160;
    private static final double LARGE_WIDTH = 200;
    private static final double XLARGE_WIDTH = 240;
    private static final double XXLARGE_WIDTH = 280;

    private int sizeLevel = 0; // 0=Normal, 1=Large, 2=XLarge, 3=XXLarge

    // === HÌNH ẢNH ===
    private Image paddleImage;
    private boolean useImage = true; //có dùng hình ảnh ko

    /**
     * constructor paddle truyền tham số.
     *
     * @param x      hoành độ
     * @param y      tung độ
     * @param width  độ dài
     * @param height chiều cao
     */
    public Paddle(double x, double y, double width, double height) {
        super(x, y, width, height);
        loadImage();
    }

    /**
     * Load hình ảnh paddle từ ImageCache.
     */
    private void loadImage() {
        try {
            ImageCache cache = ImageCache.getInstance();
            paddleImage = cache.getImage("/image/paddle/stick_2.png");
            useImage = (paddleImage != null);
            System.out.println("✓ Paddle using cached image");
        } catch (Exception e) {
            System.err.println("✗ Paddle image failed, using default");
            useImage = false;
        }
    }

    /**
     * Tăng cấp độ kích thước paddle.
     */
    public void increaseSizeLevel() {
        if (sizeLevel < 3) { // Max level = 3
            sizeLevel++;
            updatePaddleWidth();
        }
    }

    /**
     * Reset về kích thước bình thường.
     */
    public void resetSizeLevel() {
        sizeLevel = 0;
        updatePaddleWidth();
    }

    /**
     * Cập nhật chiều rộng paddle theo level.
     */
    private void updatePaddleWidth() {
        double oldWidth = width;

        switch (sizeLevel) {
            case 0:
                width = NORMAL_WIDTH;
                break;
            case 1:
                width = LARGE_WIDTH;
                break;
            case 2:
                width = XLARGE_WIDTH;
                break;
            case 3:
                width = XXLARGE_WIDTH;
                break;
        }

        // Giữ nguyên vị trí trung tâm của paddle
        x += (oldWidth - width) / 2;
    }

    /**
     * Lấy level hiện tại.
     */
    public int getSizeLevel() {
        return sizeLevel;
    }

    /**
     * cập nhập paddle(mới tạo cho có chứ ch cho nhận hành động từ chuột, phím).
     *
     */
    public void update() {
        // giới hạn trong khung
        if (x < minX) x = minX;
        if (x + width > maxX) x = maxX - width;
    }

    /**
     * vẽ lên màn hình.
     *
     * @param gc công cụ vẽ lên canvas
     */
    @Override
    public void render(GraphicsContext gc) {
        if (useImage && paddleImage != null) {
            // vẽ bằng hình ảnh
            gc.drawImage(paddleImage, x, y, width, height);

        } else {
            // DỰ PHÒNG: Vẽ bằng màu nếu không có ảnh
            // Màu thay đổi theo level
            switch (sizeLevel) {
                case 0:
                    gc.setFill(Color.DARKBLUE);
                    break;
                case 1:
                    gc.setFill(Color.BLUE);
                    break;
                case 2:
                    gc.setFill(Color.DODGERBLUE);
                    break;
                case 3:
                    gc.setFill(Color.DEEPSKYBLUE);
                    break;
            }
            gc.fillRect(x, y, width, height);

            // Viền
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeRect(x, y, width, height);
        }
    }

    /**
     * hàm truyền giới hạn cho paddle (chưa sử dụng).
     *
     * @param minX bên trái
     * @param maxX bên phải
     */
    public void setBounds(double minX, double maxX) {
        this.minX = minX;
        this.maxX = maxX;
    }

    /**
     * method reset paddle đc gọi đông thời khi method ballLose đc gọi.
     *
     * @param ballLose hàm boolean ballose khi đc gọi thì luôn trả về true
     */
    public void resetPaddle(boolean ballLose) {
        if (ballLose) {
            setX(420);
            setY(500);
            resetSizeLevel();
        }
    }
}
