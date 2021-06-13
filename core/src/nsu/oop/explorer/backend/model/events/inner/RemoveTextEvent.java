package nsu.oop.explorer.backend.model.events.inner;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.world.text.TextEntity;

public class RemoveTextEvent extends InnerEvent {
    private TextEntity textEvent;
    public RemoveTextEvent(TextEntity textEvent) {
        this.textEvent = textEvent;
    }
    @Override
    public void handle(Core world) {
        world.removeTextEntity(textEvent);
    }
}
