package nsu.oop.explorer.backend.model.entity.world;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.Cooldown;
import nsu.oop.explorer.backend.model.entity.world.playable.Player;
import nsu.oop.explorer.backend.util.GameConstants;

import java.util.List;

public class AntiPlayerTurret extends AttackerWithCooldown {
    //TODO: describe this class
    private Circle attackArea;
    private Cooldown attack;
    public AntiPlayerTurret(float x, float y, Core world) {
        super(x, y, GameConstants.turretWidth, GameConstants.turretHeight,5, 100, world);
        attackArea = new Circle(x, y, 16);
        attack = new Cooldown(0.5f);
    }

    @Override
    public void update(float dt) {
        List<Player> players = world.getPlayerList();
        Vector2 nearest = null;

        for (Player player : players) {
            Vector2 dist = new Vector2();
            player.getRectangle().getPosition(dist);

            if (nearest == null) {
                nearest = dist;
            } else {
                if (nearest.len() > dist.len()) {
                    nearest = dist;
                }
            }
        }

        attack.update(dt);
        Vector2 direction = nearest;
        if (direction != null) {
            if (attack.isReady()) {
                attack.set();
                direction.x -= getRectangle().x;
                direction.y -= getRectangle().y;
                world.addBullet(
                        new AntiPlayerBullet(getRectangle().x,getRectangle().y, direction, -1, world, 2));
            }
        }
    }

    @Override
    public void onDeath() {

    }
}
