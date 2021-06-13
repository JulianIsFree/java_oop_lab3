package nsu.oop.explorer.backend.model.entity.world;

import com.badlogic.gdx.math.Vector2;
import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.Attackable;
import nsu.oop.explorer.backend.model.entity.world.playable.Player;
import nsu.oop.explorer.backend.model.events.inner.RemoveBulletEvent;
import nsu.oop.explorer.backend.util.GameConstants;

import java.util.List;

public class AntiPlayerBullet extends TimeLivingEntity implements Attackable {
    private final float speed;
    private final Vector2 direction;
    private final int ownerId;

    public AntiPlayerBullet(float x, float y, Vector2 direction, int ownerId, Core world, float livingTime) {
        super(x, y, GameConstants.bulletWidth, GameConstants.bulletHeight, world, livingTime);
        speed = 256 * 3;
        this.direction = direction;
        this.ownerId = ownerId;
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        if (!lifeTime.isReady()) {
            List<Player> players = world.getPlayerList();
            for (Player p : players) {
                if (this.collide(p)) {
                    if (p.getId() == ownerId)
                        continue;

                    p.receiveDamage(getDamage());
                    onDeath();
                    break;
                }
            }

            direction.setLength(speed * dt);
            System.out.println("Bullet: " + speed*dt + " " + dt);
            move(direction);
        }
    }

    @Override
    public void onDeath() {
        world.addInnerEvent(new RemoveBulletEvent(this));
    }

    @Override
    public void receiveDamage(float damage) {

    }

    @Override
    public float getDamage() {
        return 10;
    }
}
