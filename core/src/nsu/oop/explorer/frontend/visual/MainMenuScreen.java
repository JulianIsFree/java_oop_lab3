package nsu.oop.explorer.frontend.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.io.IOException;

public class MainMenuScreen extends AbstractScreen {
    class IpReader implements Input.TextInputListener {
        @Override
        public void input(String text) {
            ip = text;
            ipButton.setText(ip);
        }

        @Override
        public void canceled() {

        }
    }

    class PlayerNameReader implements Input.TextInputListener {
        @Override
        public void input(String text) {
            playerName = text;
            playerNameButton.setText(text);
        }

        @Override
        public void canceled() {

        }
    }

    class PortReader implements Input.TextInputListener {

        @Override
        public void input(String text) {
            port = Integer.valueOf(text);
            portButton.setText(String.valueOf(port));
        }

        @Override
        public void canceled() {

        }
    }

    private TextButton playButton;
    private TextButton exitButton;

    private TextButton ipButton;
    private TextButton portButton;
    private TextButton playerNameButton;

    private String ip;
    private String playerName;
    private int port;




    private Skin skin;
    private Stage stage;

    public MainMenuScreen(final ScreenManager screenManager) {
        super(screenManager, 300, 300);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        skin.add("default-font", screenManager.app().getFont());
        skin.load(Gdx.files.internal("ui/uiskin.json"));

        ip = "";
        playerName="Player";
        port = 0;
        initButtons(screenManager);
    }

    private void initButtons(final ScreenManager screenManager) {
        playButton = new TextButton("Play", skin, "default");
        playButton.setSize(120, 120);
        playButton.setPosition(-60,-30);
        playButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               try {
                   screenManager.push(new OnlineGameScreen(screenManager, ip, port, playerName));
               } catch (IOException e) {
                   playButton.setText("No connection");
               }
           }
        });
        stage.addActor(playButton);

        exitButton = new TextButton("Exit", skin, "toggle");
        exitButton.setSize(120, 120);
        exitButton.setPosition(-60,-180);
        exitButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Gdx.app.exit();
           }
        });
        stage.addActor(exitButton);

        ipButton = new TextButton("Enter ip", skin, "default");
        ipButton.setSize(120, 120);
        ipButton.setPosition(90,-30);
        ipButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Gdx.input.getTextInput(new IpReader(), "", "", "192.168.0.1");
           }
        });
        stage.addActor(ipButton);

        portButton = new TextButton("Enter port", skin, "default");
        portButton.setSize(120, 120);
        portButton.setPosition(90,-180);
        portButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Gdx.input.getTextInput(new PortReader(), "", "", "25565");
           }
        });
        stage.addActor(portButton);

        playerNameButton = new TextButton("Enter player name", skin, "default");
        playerNameButton.setSize(120, 120);
        playerNameButton.setPosition(-60,120);
        playerNameButton.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
               Gdx.input.getTextInput(new PlayerNameReader(), "", "", "Player1");
           }
        });
        stage.addActor(playerNameButton);
    }


    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    protected void handle() {

    }

    @Override
    public void update(float dt) {
        stage.act(dt);
    }

    @Override
    public void draw() {
        Gdx.gl.glClearColor(0,1,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT );
        stage.draw();
    }
}
