package nsu.oop.explorer.backend.model.events.inner;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.world.playable.Player;

import java.util.List;

public class RespawnPlayerEvent extends InnerEvent {
    private Player player;
    public RespawnPlayerEvent(Player target) {
        this.player = target;
    }

    @Override
    public void handle(Core world) {
        List<Player> players = world.getPlayerList();
        players.remove(player);
        players.add(new Player(0, 0, player.getId(), world, player.getName()));
    }
}
