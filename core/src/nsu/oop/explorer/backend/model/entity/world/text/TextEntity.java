package nsu.oop.explorer.backend.model.entity.world.text;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.world.TimeLivingEntity;
import nsu.oop.explorer.backend.model.events.inner.RemoveTextEvent;

public abstract class TextEntity extends TimeLivingEntity {
    private String text;
    public TextEntity(float x, float y, Core world, String text) {
        super(x, y, 1, 1, world, 2);
        this.text = text;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TextEntity))
            return false;
        TextEntity e = (TextEntity)obj;
        return e.text.equals(this.text) &&
                e == this;
    }

    @Override
    public void onDeath() {
        world.addInnerEvent(new RemoveTextEvent(this));
    }

    public String getString() {
        return text;
    }
}
