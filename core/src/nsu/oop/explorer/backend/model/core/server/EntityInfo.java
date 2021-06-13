package nsu.oop.explorer.backend.model.core.server;

import java.io.Serializable;

public class EntityInfo implements Serializable {
    public final Float x;
    public final Float y;
    public final int textureId;

    public EntityInfo(Float x, Float y, int textureId) {
        this.x = x;
        this.y = y;
        this.textureId = textureId;
    }
}
