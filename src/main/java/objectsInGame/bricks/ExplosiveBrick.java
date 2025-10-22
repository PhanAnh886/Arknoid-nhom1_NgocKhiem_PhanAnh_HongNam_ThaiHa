package objectsInGame.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;

/**
 * Gạch nổ - khi phá sẽ phá các gạch xung quanh
 */
public class ExplosiveBrick extends Brick {
    private static final double EXPLOSION_RADIUS = 80; // Bán kính nổ

    public ExplosiveBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void destroyed(boolean value, ArrayList<Brick> bricks){
        if (value) { // nếu true thì ổ explosive trc xong nổ các brick xung quanh sau
            super.destroyed(true,bricks);
            explode(bricks);
        } else{// kết thúc nổ lan
            super.destroyed(false,bricks);
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            // Màu đỏ cam cho gạch nổ
            gc.setFill(Color.ORANGERED);
            gc.fillRect(x, y, width, height);
            gc.setStroke(Color.DARKRED);
            gc.setLineWidth(2);
            gc.strokeRect(x, y, width, height);

            // Vẽ biểu tượng bom
            gc.setFill(Color.YELLOW);
            double centerX = x + width / 2;
            double centerY = y + height / 2;
            gc.fillOval(centerX - 5, centerY - 5, 10, 10);
        }
    }

    /**
     * Phá các gạch xung quanh khi gạch này bị phá
     * @param bricks danh sách tất cả gạch
     */
    public void explode(ArrayList<Brick> bricks) {
        //tọa độ trung tâm của explosive brick
        double centerX = x + width / 2;
        double centerY = y + height / 2;

        for (Brick brick : bricks) {
            if (brick != this && !brick.isDestroyed() && brick.canBeDestroyed()) {
                //lấy vị trí tọa độ trung tâm của các viên gạch xung quanh
                double brickCenterX = brick.getX() + brick.getWidth() / 2;
                double brickCenterY = brick.getY() + brick.getHeight() / 2;

                // Tính khoảng cách từ tâm của explosive brick đến các brick xung quanh
                double distance = Math.sqrt(
                        Math.pow(centerX - brickCenterX, 2) +
                                Math.pow(centerY - brickCenterY, 2)
                );

                // Nếu trong bán kính nổ thì phá
                if (distance <= EXPLOSION_RADIUS) {
                    brick.destroyed(true,bricks); // có thể nổ lan
                }
            }
        }
    }

    @Override
    public int getScore() {
        return 15;
    }
}