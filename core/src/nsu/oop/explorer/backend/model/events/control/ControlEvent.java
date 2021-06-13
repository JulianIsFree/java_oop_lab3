package nsu.oop.explorer.backend.model.events.control;

import nsu.oop.explorer.backend.model.events.Event;

public abstract class ControlEvent extends Event {
    public boolean isKeyboardEvent() {
        return false;
    }
    public boolean isMouseEvent() {
        return false;
    }
    public boolean isSendNameEvent() {
        return false;
    }
}
