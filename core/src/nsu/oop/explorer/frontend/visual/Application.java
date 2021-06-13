package nsu.oop.explorer.frontend.visual;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Application extends Game {
    private SpriteBatch batch;

    private BitmapFont font;

    private ScreenManager screenManager;
    private int startScreen;

    private AbstractScreen getStartScreen() {
        switch (startScreen) {
            case(0):
                return new MainMenuScreen(screenManager);
            default:
                return new ServerScreen(screenManager);
        }
    }

    public Application(int screenId) {
        startScreen = screenId;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        screenManager = new ScreenManager(this);
        screenManager.push(getStartScreen());

        initFonts();
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Arcon.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.BLACK;
        font = generator.generateFont(params);
    }

    @Override
    public void render() {
        super.render();

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            Gdx.app.exit();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public BitmapFont getFont() {
        return font;
    }
}
