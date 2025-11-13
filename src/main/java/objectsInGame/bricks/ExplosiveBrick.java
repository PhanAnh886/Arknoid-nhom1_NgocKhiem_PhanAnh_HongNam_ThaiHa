package objectsInGame.bricks;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import sound.SoundManager;

/**
 * Gạch nổ - khi phá sẽ phá các gạch xung quanh.
 */
public class ExplosiveBrick extends Brick {
    private static final double EXPLOSION_RADIUS = 80; // Bán kính nổ

    public ExplosiveBrick(double x, double y, double width, double height) {
        super(x, y, width, height);
        loadImage("/image/bricks/exploseBrick.png");
    }

    @Override
<<<<<<< HEAD
    public void destroyed(boolean value, CopyOnWriteArrayList<Brick> bricks) {
        if (value) { // nếu true thì ổ explosive trc xong nổ các brick xung quanh sau
=======
    public void destroyed(boolean value, CopyOnWriteArrayList<Brick> bricks){
        if (value) { // nếu true thì ổ explosive trc xong nổ các brick xung quanh sau.
>>>>>>> 029424d0357d73b2758c8b869a52cc700f771e56
            SoundManager.getInstance().playGameSound("brick_break_normal");
            super.destroyed(true, bricks);
            explode(bricks);
<<<<<<< HEAD
        } else {// kết thúc nổ lan
            super.destroyed(false, bricks);
=======
        } else{// kết thúc nổ lan.
            super.destroyed(false,bricks);
>>>>>>> 029424d0357d73b2758c8b869a52cc700f771e56
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!destroyed) {
            if (useImage && brickImage != null) {
                // vẽ bằng hình ảnh.
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

    /**
<<<<<<< HEAD
     * Phá các gạch xung quanh khi gạch này bị phá
     *
     * @param bricks danh sách tất cả gạch
=======
     * Phá các gạch xung quanh khi gạch này bị phá.
     * @param bricks danh sách tất cả gạch.
>>>>>>> 029424d0357d73b2758c8b869a52cc700f771e56
     */
    public void explode(CopyOnWriteArrayList<Brick> bricks) {
        //tọa độ trung tâm của explosive brick
        double centerX = x + width / 2;
        double centerY = y + height / 2;

        for (Brick brick : bricks) {
            if (brick != this && !brick.isDestroyed() && brick.canBeDestroyed()) {
                //lấy vị trí tọa độ trung tâm của các viên gạch xung quanh.
                double brickCenterX = brick.getX() + brick.getWidth() / 2;
                double brickCenterY = brick.getY() + brick.getHeight() / 2;

                // Tính khoảng cách từ tâm của explosive brick đến các brick xung quanh.
                double distance = Math.sqrt(
                        Math.pow(centerX - brickCenterX, 2) +
                                Math.pow(centerY - brickCenterY, 2)
                );

                // Nếu trong bán kính nổ thì phá.
                if (distance <= EXPLOSION_RADIUS) {
                    brick.destroyed(true, bricks); // có thể nổ lan
                }
            }
        }
    }

    @Override
    public int getScore() {
        return 15;
    }
}