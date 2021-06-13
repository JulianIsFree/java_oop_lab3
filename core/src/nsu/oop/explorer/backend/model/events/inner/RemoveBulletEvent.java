package nsu.oop.explorer.backend.model.events.inner;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.world.AntiPlayerBullet;

public class RemoveBulletEvent extends InnerEvent {
    private AntiPlayerBullet bullet;
    public RemoveBulletEvent(AntiPlayerBullet bullet) {
        this.bullet = bullet;
    }

    @Override
    public void handle(Core world) {
        world.removeBullet(bullet);
    }
}
