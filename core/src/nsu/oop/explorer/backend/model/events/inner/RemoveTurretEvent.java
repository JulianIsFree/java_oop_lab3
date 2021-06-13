package nsu.oop.explorer.backend.model.events.inner;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.world.AntiPlayerTurret;

public class RemoveTurretEvent extends InnerEvent {
    private AntiPlayerTurret turret;
    public RemoveTurretEvent(AntiPlayerTurret turret) {
        this.turret = turret;
    }

    @Override
    public void handle(Core world) {
        world.removeEntity(turret);
    }
}
