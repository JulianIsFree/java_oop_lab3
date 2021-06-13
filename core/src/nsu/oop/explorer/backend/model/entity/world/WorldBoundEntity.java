package nsu.oop.explorer.backend.model.entity.world;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.RectangularEntity;

public abstract class WorldBoundEntity extends RectangularEntity {
    protected Core world;
    protected WorldBoundEntity(float x, float y, float width, float height, Core world) {
        super(x, y, width, height);
        this.world = world;
    }
}
