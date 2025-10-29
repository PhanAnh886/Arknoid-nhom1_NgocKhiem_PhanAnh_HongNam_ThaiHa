package objectsInGame.powerups;

import javafx.scene.canvas.GraphicsContext;
import objectsInGame.MovableObject;

public abstract class PowerUp extends MovableObject {
    private boolean active = false;
    private String type; // loại power up

    public PowerUp(String type) {
        super(0, 0, 20, 20); // tạm thời, vị trí sẽ được set khi brick bị phá
        this.type = type;
        this.setDx(0);
        this.setDy(150); // tốc độ rơi
    }

    public void activate() {
        active = true;
    }

    public boolean isActive() {
        return active;
    }

    public String getType() {
        return type;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void update(double dt) {
        if (active) {
            super.update(dt);
        }
    }

    @Override
    public abstract void render(GraphicsContext gc);
}


