package nsu.oop.explorer.backend.model.entity.world;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.Attackable;
import nsu.oop.explorer.backend.model.entity.Cooldown;

public abstract class AttackerWithCooldown extends WorldBoundEntity implements Attackable {
    private float hp;
    private boolean isAlive;
    private Cooldown cd;
    private float damage;

    protected AttackerWithCooldown(float x, float y, float width, float height, float damage, int hp, Core world) {
        this(x, y, width, height, damage, hp, true, world);
}

    private AttackerWithCooldown(float x, float y, float width, float height, float damage, int hp, boolean isAlive, Core world) {
        super(x, y, width, height, world);
        this.isAlive = isAlive;
        this.hp = hp;
        this.damage = damage;
        cd = new Cooldown(0.1f);
    }

    @Override
    public void update(float dt) {
        cd.update(dt);
    }

    @Override
    public void receiveDamage(float damage) {
        if (isAlive) {
            hp -= damage;
            if (hp < 0) {
                isAlive = false;
                onDeath();
            }
        }
    }

    @Override
    public float getDamage() {
        return damage;
    }

    protected boolean isCdReady() {
        return cd.isReady();
    }

    protected void setCd() {
        cd.set();
    }
}
