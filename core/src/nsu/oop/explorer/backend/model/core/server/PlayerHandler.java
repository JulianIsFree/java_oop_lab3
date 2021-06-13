package nsu.oop.explorer.backend.model.core.server;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.world.playable.Player;
import nsu.oop.explorer.backend.model.events.control.ControlEvent;
import nsu.oop.explorer.backend.util.Pair;

import java.util.List;

public class PlayerHandler {
    private final Integer associatedPlayer;
    private final List<ControlEvent> events;

    PlayerHandler(Integer associatedPlayer, List<ControlEvent> events) {
        this.associatedPlayer = associatedPlayer;
        this.events = events;
    }

    PlayerHandler(Pair<Integer, List<ControlEvent>> pair) {
        this(pair.first, pair.second);
    }

    public void handle(Core world) {
        Player player = null;

        for (Player p : world.getPlayerList())
            if (p.getId() == associatedPlayer) {
                player = p;
                break;
            }

        if (player != null)
            for (ControlEvent e : events)
                player.handle(e);
    }
}
