package Level;
import objectsInGame.bricks.*;
import objectsInGame.powerups.*;

import java.util.Random;

/**
 * Level 0 - Demo Level (level cũ).
 */
public class Level0 extends Level {

    public Level0() {
        super();
        this.levelName = "Level 0 - Demo";
        createLevel();
    }

    @Override
    protected void createLevel() {
        double startX = 35;
        double startY = 40;

        int[][] pattern = {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {0,0,0,2,4,2,4,4,2,4,2,0,0,0},
                {0,0,3,3,4,2,2,2,2,4,3,3,0,0},
                {0,0,3,4,4,3,3,3,3,4,4,3,0,0},
                {0,0,0,3,4,3,4,4,3,4,3,0,0,0},
                {0,0,0,0,4,3,3,3,3,4,0,0,0,0},
                {0,0,0,0,4,2,3,3,2,4,0,0,0,0},
                {0,0,0,0,0,5,4,4,5,0,0,0,0,0}
        };

        Random rand = new Random();

        for (int row = 0; row < pattern.length; row++) {
            for (int col = 0; col < 14; col++) {
                double x = startX + col * (BRICK_WIDTH + BRICK_PADDING);
                double y = startY + row * (BRICK_HEIGHT + BRICK_PADDING);

                Brick brick = null;

                switch (pattern[row][col]) {
                    case 1:
                        brick = new UnbreakableBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 2:
                        brick = new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 2);
                        break;
                    case 3:
                        brick = new StrongBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT, 3);
                        break;
                    case 4:
                        brick = new ExplosiveBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                    case 5:
                        brick = new NormalBrick(x, y, BRICK_WIDTH, BRICK_HEIGHT);
                        break;
                }

                // 15% cơ hội có power-up (chỉ cho gạch có thể phá).
                if (brick != null && brick.canBeDestroyed() && rand.nextDouble() < 0.15) {
                    int powerType = rand.nextInt(4);
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
                        case 3:
                            powerUp = new PaddleSizePowerUp(x + BRICK_WIDTH/2 - 10, y + BRICK_HEIGHT/2 - 10);
                            break;
                    }
                    brick.setPowerUp(powerUp);
                }

                if (brick != null) {
                    bricks.add(brick);
                }
            }
        }
    }
}