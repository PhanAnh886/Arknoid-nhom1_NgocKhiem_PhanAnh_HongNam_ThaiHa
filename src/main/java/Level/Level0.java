package Level;
import objectsInGame.bricks.*;
import objectsInGame.powerups.*;

import java.util.Random;

/**
 * Level 0 - Demo Level (level cũ)
 */
public class Level0 extends Level {

    public Level0() {
        super();
        this.levelName = "Level 0 - Demo";
        createLevel();
    }

    @Override
    protected void createLevel() {
        double startX = 100;
        double startY = 50;

        Random rand = new Random();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 14; col++) {
                double x = startX + col * (BRICK_WIDTH + BRICK_PADDING);
                double y = startY + row * (BRICK_HEIGHT + BRICK_PADDING);

                Brick brick = null;

                if (row == 0) {
                    brick = new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                }
                else if (col == 6 || col == 7) {
                    brick = new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                }
                else if (row == 1 || row == 2) {
                    brick = new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 2);
                }
                else if (row == 3 || row == 4) {
                    brick = new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 3);
                }
                else {
                    brick = new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                }

                // 20% cơ hội có power-up (chỉ cho gạch có thể phá)
                if (brick.canBeDestroyed() && rand.nextDouble() < 0.2) {
                    int powerType = rand.nextInt(3);
                    PowerUp powerUp = null;

                    switch (powerType) {
                        case 0:
                            powerUp = new ShootPowerUp(x + BRICK_WIDTH/2 - 10, y + BRICK_HEIGHT/2 - 10);
                            break;
                        case 1:
                            powerUp = new FastBallPowerUp(x + BRICK_WIDTH/2 - 10, y + BRICK_HEIGHT/2 - 10);
                            break;
                        case 2:
                            powerUp = new MultiBallPowerUp(x + BRICK_WIDTH/2 - 10, y + BRICK_HEIGHT/2 - 10);
                            break;
                    }
                    brick.setPowerUp(powerUp);
                }

                bricks.add(brick);
            }
        }
    }
}