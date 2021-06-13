package nsu.oop.explorer.backend.model.core.server;

import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.world.playable.Player;
import nsu.oop.explorer.backend.model.events.control.ControlEvent;
import nsu.oop.explorer.backend.model.events.control.SendNameEvent;
import nsu.oop.explorer.backend.util.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ServerCore extends Thread {

    @Override
    public void run() {
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                synchronized (connections) {
                    Connection connection = new Connection(socket);
                    Player player = world.createNewPlayer("");
                    synchronized (player) {
                        connections.put(player.getId(), connection);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private ServerSocket serverSocket;
    private final HashMap<Integer, Connection> connections;

    private Core world;
    public ServerCore(Core world, int port) throws IOException {
        this.world = world;
        connections = new HashMap<>();
        serverSocket = new ServerSocket(port);
    }

    public List<PlayerHandler> getHandlers() {
        LinkedList<PlayerHandler> list = new LinkedList<>();
        for (Map.Entry<Integer, Connection> entry: connections.entrySet()) {
            list.add(new PlayerHandler(new Pair<>(entry.getKey(), entry.getValue().getInput())));
        }
        return list;
    }

    public synchronized void sendToAll(ControlEvent e) {
        List<Map.Entry<Integer, Connection>> toRemove = new LinkedList<>();
        for(Map.Entry<Integer, Connection> c : connections.entrySet()) {
            c.getValue().send(e);

            if(c.getValue().isClosed() || c.getValue().isTimeOut()) {
                toRemove.add(c);
            }
        }

        for (Map.Entry<Integer, Connection> c : toRemove) {
            c.getValue().shutdown();
            world.removePlayer(c.getKey());
            connections.remove(c);
        }

    }
}
