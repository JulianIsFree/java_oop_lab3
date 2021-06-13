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
import nsu.oop.explorer.backend.model.core.Core;

import java.io.IOException;

public class ServerScreen extends AbstractScreen {

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

    private int port;

    private TextButton startButton;
    private TextButton exitButton;
    private TextButton portButton;

    private Skin skin;
    private Stage stage;

    public ServerScreen(ScreenManager screenManager) {
        super(screenManager, 300, 300);
        stage = new Stage();
        stage.getViewport().setCamera(camera);
        Gdx.input.setInputProcessor(stage);
        stage.clear();

        skin = new Skin();
        skin.addRegions(new TextureAtlas(Gdx.files.internal("ui/uiskin.atlas")));
        skin.add("default-font", screenManager.app().getFont());
        skin.load(Gdx.files.internal("ui/uiskin.json"));

        port = 25565;
        initButtons();
    }

    private void initButtons() {
        startButton = new TextButton("Start", skin, "toggle");
        startButton.setSize(120, 120);
        startButton.setPosition(-60,-30);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Core core;
                try {
                    core = new Core(port);
                    core.run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.addActor(startButton);

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

        portButton = new TextButton("Enter port", skin, "toggle");
        portButton.setSize(120, 120);
        portButton.setPosition(90,-30);
        portButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.getTextInput(new PortReader(), "", "25565", "25565");
            }
        });
        stage.addActor(portButton);
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
