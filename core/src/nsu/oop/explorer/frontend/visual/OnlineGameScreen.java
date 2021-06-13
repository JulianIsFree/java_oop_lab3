package nsu.oop.explorer.frontend.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import nsu.oop.explorer.backend.model.core.server.Connection;
import nsu.oop.explorer.backend.model.events.control.*;
import nsu.oop.explorer.backend.util.GameConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class OnlineGameScreen extends AbstractScreen {
    private Connection connection;
    private List<InfoEvent> infoEventList;

    private ArrayList<Texture> playerTextures;
    private Texture bulletTexture;
    private Texture turretTexture;

    private String score;

    class Informer extends Thread {
        @Override
        public void run() {
            while(true) {
                connection.send(new LifeControlEvent());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public OnlineGameScreen(ScreenManager screenManager, String ip, int port, String playerName) throws IOException {
        super(screenManager, 3000, 2000);

        initTextures();
        score="";
        screenManager.app().getFont().getData().setScale(3);
        screenManager.app().getFont().getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        connection = new Connection(ip, port);
        connection.send(new SendNameEvent(playerName));
        infoEventList = new LinkedList<>();

        Informer informer = new Informer();
        informer.start();
    }

    private void initTextures() {
        playerTextures = new ArrayList<>();
        Pixmap playerPixmap = new Pixmap(GameConstants.playerWidth, GameConstants.playerHeight, Pixmap.Format.RGBA8888);
        playerPixmap.setColor(Color.GRAY);
        playerPixmap.fill();
        playerTextures.add(new Texture(playerPixmap));

        playerPixmap.setColor(Color.GOLD);
        playerPixmap.fill();
        playerTextures.add(new Texture(playerPixmap));

        playerPixmap.setColor(Color.PINK);
        playerPixmap.fill();
        playerTextures.add(new Texture(playerPixmap));

        playerPixmap.setColor(Color.RED);
        playerPixmap.fill();
        playerTextures.add(new Texture(playerPixmap));

        Pixmap bulletPixmap = new Pixmap(GameConstants.bulletWidth, GameConstants.bulletHeight, Pixmap.Format.RGBA8888);
        bulletPixmap.setColor(GameConstants.bulletColor);
        bulletPixmap.fill();
        bulletTexture = new Texture(bulletPixmap);

        Pixmap turretPixmap = new Pixmap(GameConstants.turretWidth, GameConstants.turretHeight, Pixmap.Format.RGBA8888);
        turretPixmap.setColor(GameConstants.turretColor);
        turretPixmap.fill();
        turretTexture = new Texture(turretPixmap);
    }

    @Override
    protected void handle() {
        connection.send(new KeyboardEvent(Input.Keys.W, Gdx.input.isKeyPressed(Input.Keys.W)));
        connection.send(new KeyboardEvent(Input.Keys.S, Gdx.input.isKeyPressed(Input.Keys.S)));
        connection.send(new KeyboardEvent(Input.Keys.A, Gdx.input.isKeyPressed(Input.Keys.A)));
        connection.send(new KeyboardEvent(Input.Keys.D, Gdx.input.isKeyPressed(Input.Keys.D)));
        connection.send(new KeyboardEvent(Input.Keys.SHIFT_LEFT, !Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT)));
        connection.send(new KeyboardEvent(Input.Keys.SPACE, !Gdx.input.isKeyJustPressed(Input.Keys.SPACE)));

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            connection.send(new MouseEvent(getMousePos(), Input.Buttons.LEFT, false));
        }
    }

    @Override
    public void update(float dt) {
        handle();
        getInfoEventsFromServer();
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(0,0,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );

        for (InfoEvent e : infoEventList) {
            if (e.id() == 0) {
                batch.draw(playerTextures.get(e.textureId() % GameConstants.numberOfColors), e.x(), e.y());
            } else if (e.id() == 1) {
                batch.draw(bulletTexture, e.x(), e.y());
            } else if (e.id() == 2) {
                batch.draw(turretTexture, e.x(), e.y());
            } else if(e.id() == 3) {
                ScoreUpdateEvent eue = (ScoreUpdateEvent)e;
                this.score = eue.score();
            }
        }
        screenManager.app().getFont().draw(batch, score, 800, 800);
        infoEventList.clear();
    }

    private void getInfoEventsFromServer() {
        if (connection.isTimeOut() || connection.isClosed()) {
            connection.shutdown();
            screenManager.pop();
        } else {
            List<ControlEvent> toHandle = connection.getInput();
            for (ControlEvent e : toHandle) {
                if (e instanceof InfoEvent)
                    infoEventList.add((InfoEvent)e);
            }
        }
    }
    @Override
    public void dispose() {

    }
}
