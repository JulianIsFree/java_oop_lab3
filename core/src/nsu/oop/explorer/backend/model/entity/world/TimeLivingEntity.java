package nsu.oop.explorer.backend.model.entity.world;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.Cooldown;

public abstract class TimeLivingEntity extends WorldBoundEntity {
    protected Cooldown lifeTime;

    public TimeLivingEntity(float x, float y, float width, float height, Core world, float livingTime) {
        super(x, y, width, height, world);
        lifeTime = new Cooldown(livingTime);
        lifeTime.set();
    }

    @Override
    public void update(float dt) {
        lifeTime.update(dt);
        if (lifeTime.isReady())
            onDeath();

    }

    @Override
    public abstract void onDeath();
}
