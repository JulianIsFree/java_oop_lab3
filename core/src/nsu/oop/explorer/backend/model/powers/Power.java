package nsu.oop.explorer.backend.model.powers;

import nsu.oop.explorer.backend.model.entity.Cooldown;

public abstract class Power {
    private Cooldown duration;
    private Cooldown cooldown;

    protected Power(float duration, float cooldown) {
        this.duration = new Cooldown(duration);
        this.cooldown = new Cooldown(cooldown);
    }

    public void update(float dt) {
        duration.update(dt);
        cooldown.update(dt);
    }

    public void cast() {
        if (cooldown.isReady()) {
            cooldown.set();
            duration.set();
        }
    }

    public boolean isReady() {
        return cooldown.isReady();
    }

    public boolean isActive() {
        return !duration.isReady();
    }
}
