package Level;
import objectsInGame.*;
import objectsInGame.bricks.*;
import java.util.ArrayList;

/**
 * Class quản lý level và tạo gạch
 * Version đơn giản - chỉ có 1 level để học
 */
/**
 * Class quản lý level và tạo gạch
 * Version đơn giản - chỉ có 1 level để học
 */
public class Level {
    // Danh sách các gạch trong level
    private ArrayList<Brick> bricks;

    // Tên của level này
    private String levelName;

    // --- Hằng số kích thước gạch ---
    private static final double BRICK_WIDTH = 55;   // Chiều rộng mỗi viên gạch
    private static final double BRICK_HEIGHT = 25;  // Chiều cao mỗi viên gạch
    private static final double BRICK_PADDING = 5;  // Khoảng cách giữa các viên gạch

    /**
     * Constructor - tự động tạo level khi khởi tạo
     */
    public Level() {
        this.levelName = "Level Demo";              // Đặt tên level
        this.bricks = new ArrayList<>();            // Khởi tạo danh sách rỗng
        createLevel();                              // Gọi hàm tạo gạch
    }

    /**
     * Hàm tạo gạch cho level
     * Ở đây tạo 1 level đơn giản với các loại gạch khác nhau
     */
    private void createLevel() {
        // Vị trí bắt đầu vẽ gạch đầu tiên (góc trên bên trái)
        double startX = 100;  // Cách mép trái 100 pixel
        double startY = 50;   // Cách mép trên 50 pixel

        // Tạo lưới gạch 6 hàng x 14 cột
        for (int row = 0; row < 6; row++) {           // Duyệt qua 6 hàng (từ 0 đến 5)
            for (int col = 0; col < 14; col++) {      // Duyệt qua 14 cột (từ 0 đến 13)

                // Tính toán vị trí x, y của gạch hiện tại
                double x = startX + col * (BRICK_WIDTH + BRICK_PADDING);   // x = 100 + cột * 60
                double y = startY + row * (BRICK_HEIGHT + BRICK_PADDING);  // y = 50 + hàng * 30

                // --- PHÂN LOẠI GẠCH THEO VỊ TRÍ ---

                // Hàng đầu tiên (row = 0): Gạch không phá được - tạo tường
                if (row == 0) {
                    bricks.add(new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }

                // Cột giữa (cột 6 và 7): Gạch nổ
                else if (col == 6 || col == 7) {
                    bricks.add(new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }

                // Hàng 2 và 3 (row = 1 hoặc 2): Gạch cứng 2 hits
                else if (row == 1 || row == 2) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 2));
                }

                // Hàng 4 và 5: Gạch cứng 3 hits
                else if (row == 3 || row == 4) {
                    bricks.add(new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 3));
                }

                // Còn lại: Gạch thường
                else {
                    bricks.add(new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT));
                }
            }
        }
    }

    /**
     * Lấy danh sách tất cả gạch trong level
     * @return ArrayList chứa tất cả các gạch
     */
    public ArrayList<Brick> getBricks() {
        return bricks;  // Trả về reference đến danh sách gạch
    }

    /**
     * Lấy tên của level
     * @return tên level dạng String
     */
    public String getLevelName() {
        return levelName;
    }

    /**
     * Kiểm tra đã phá hết gạch chưa
     * (Không tính gạch UnbreakableBrick vì không thể phá)
     * @return true nếu đã phá hết gạch có thể phá
     */
    public boolean isCompleted() {
        // Duyệt qua tất cả các gạch
        for (Brick brick : bricks) {
            // Nếu tìm thấy 1 viên chưa phá VÀ có thể phá được
            if (!brick.isDestroyed() && brick.canBeDestroyed()) {
                return false;  // => Chưa hoàn thành
            }
        }
        // Nếu không tìm thấy viên nào chưa phá => Hoàn thành
        return true;
    }

    /**
     * Reset tất cả gạch về trạng thái ban đầu
     * (Trừ gạch UnbreakableBrick vì nó không bao giờ bị phá)
     */
    public void reset() {
        // Duyệt qua tất cả gạch
        for (Brick brick : bricks) {
            // Chỉ reset những gạch có thể bị phá
            if (brick.canBeDestroyed()) {
                brick.destroyed(false,bricks);  // Đặt lại thành chưa bị phá
                brick.setScored(false);     // Đặt lại thành chưa tính điểm
            }
        }
    }
}