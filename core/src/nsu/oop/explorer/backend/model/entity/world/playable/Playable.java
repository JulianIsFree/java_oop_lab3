package nsu.oop.explorer.backend.model.entity.world.playable;

import nsu.oop.explorer.backend.model.events.control.ControlEvent;

public interface Playable {
    void handle(ControlEvent e);
}
