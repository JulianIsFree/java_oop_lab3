package nsu.oop.explorer.backend.model.core;

import nsu.oop.explorer.backend.model.core.server.PlayerHandler;
import nsu.oop.explorer.backend.model.core.server.ServerCore;
import nsu.oop.explorer.backend.model.entity.Cooldown;
import nsu.oop.explorer.backend.model.entity.Entity;
import nsu.oop.explorer.backend.model.entity.world.AntiPlayerTurret;
import nsu.oop.explorer.backend.model.entity.world.TimeLivingEntity;
import nsu.oop.explorer.backend.model.entity.world.AntiPlayerBullet;
import nsu.oop.explorer.backend.model.entity.world.playable.Player;
import nsu.oop.explorer.backend.model.entity.world.text.TextEntity;
import nsu.oop.explorer.backend.model.events.control.InfoEvent;
import nsu.oop.explorer.backend.model.events.control.LifeControlEvent;
import nsu.oop.explorer.backend.model.events.control.ScoreUpdateEvent;
import nsu.oop.explorer.backend.model.events.inner.InnerEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Core {

    private boolean sendScore;

    class DeltaTimer {
        long t1;
        DeltaTimer() {
            t1 = System.currentTimeMillis();
            }

        // millis from previous call
        long getDeltaTime() {
            long t2 = System.currentTimeMillis();
            long val = t2 - t1;
            t1 = t2;
            return val;
        }

    }

    private LinkedList<AntiPlayerBullet> bulletArrayList;
    private LinkedList<TextEntity> textEntities;
    private LinkedList<Player> players;
    private LinkedList<AntiPlayerTurret> turretList;

    private LinkedList<InnerEvent> innerEvents;
    private HashMap<String, Integer> playerScore;

    private ServerCore serverCore;

    private Cooldown isServerAlive;

    public Core(int port) throws IOException {

        bulletArrayList = new LinkedList<>();
        players = new LinkedList<>();
        innerEvents = new LinkedList<>();
        textEntities = new LinkedList<>();
        turretList = new LinkedList<>();

        playerScore = new HashMap<>();

        serverCore = new ServerCore(this, port);
//        turretList.add(new AntiPlayerTurret(-60, -60, this));

        isServerAlive = new Cooldown(1);
        sendScore = true;
    }

    /**
     * Call to play
     */
    public void run() {
        DeltaTimer timer = new DeltaTimer();
        System.out.println("Core is running...");
        serverCore.start();
        while(true) {
            long dt = timer.getDeltaTime() ;
            update(dt / 1000f);

            handlePlayers();
            handleInnerEvents();

            sendInfo();

            try {
                dt = (1000/120 - dt) > 0 ? (1000/120 - dt) : 0;
                Thread.sleep(1000/120 - dt);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(float dt) {
        isServerAlive.update(dt);
        for (Player p : players) {
            p.update(dt);
        }

        for(AntiPlayerBullet bullet : bulletArrayList) {
            bullet.update(dt);
        }

        for (TimeLivingEntity entity : textEntities) {
            entity.update(dt);
        }

        for (AntiPlayerTurret turret : turretList) {
            turret.update(dt);
        }
    }

    private void handlePlayers() {
        List<PlayerHandler> handlers = serverCore.getHandlers();
        for (PlayerHandler handler : handlers)
            handler.handle(this);
    }

    private void handleInnerEvents() {
        for (InnerEvent event : innerEvents) {
            event.handle(this);
        }
        innerEvents.clear();
    }

    private void sendInfo() {
        //TODO: update it when making any new lists
        int colorCounter = 0;
        for (Player p : players)
            serverCore.sendToAll(new InfoEvent(0, p.getRectangle().x, p.getRectangle().y, colorCounter++));

        for (AntiPlayerBullet bullet : bulletArrayList)
            serverCore.sendToAll(new InfoEvent(1, bullet.getRectangle().x, bullet.getRectangle().y));

        for (AntiPlayerTurret turret : turretList)
            serverCore.sendToAll(new InfoEvent(2, turret.getRectangle().x, turret.getRectangle().y));

        if (sendScore) {
            serverCore.sendToAll(new ScoreUpdateEvent(playerScore));
            sendScore = false;
        }

        if (isServerAlive.isReady()) {
            serverCore.sendToAll(new LifeControlEvent());
            isServerAlive.set();
        }
    }

    public Player createNewPlayer(String name) {
        Player player = new Player(0, 0, this, name);
        //TODO: may be should merge 2 lists?
        players.add(player);

        return player;
    }

    public void removePlayer(int id) {
        for (Player p : players) {
            if (p.getId() == id) {
                players.remove(p);
                break;
            }
        }
    }

    public List<Player> getPlayerList() {
        return players;
    }

    public void addInnerEvent(InnerEvent event) {
        innerEvents.add(event);
    }

    public void removeTextEntity(TextEntity textEntity) {
        textEntities.remove(textEntity);
    }

    public void addTextEntity(TextEntity textEntity) {
        textEntities.add(textEntity);
    }

    public void removeBullet(AntiPlayerBullet bullet) {
        bulletArrayList.remove(bullet);
    }

    public void addBullet(AntiPlayerBullet bullet) {
        bulletArrayList.add(bullet);
    }

    public void removeEntity(AntiPlayerTurret turret) {
        turretList.remove(turret);
    }

    public void updateDeathScore(String name) {
        if (playerScore.containsKey(name))
            playerScore.put(name, playerScore.get(name) + 1);
        else
            playerScore.put(name, 1);
        sendScore = true;
    }
}
