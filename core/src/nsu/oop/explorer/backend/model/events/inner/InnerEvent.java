package nsu.oop.explorer.backend.model.events.inner;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.events.Event;

public abstract class InnerEvent extends Event {
    public abstract void handle(Core world);
}
