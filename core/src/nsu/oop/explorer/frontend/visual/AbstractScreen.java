package nsu.oop.explorer.frontend.visual;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class AbstractScreen implements Screen {
    protected ScreenManager screenManager;
    protected SpriteBatch batch;

    protected OrthographicCamera camera;
    private Viewport cameraViewport;

    public AbstractScreen(ScreenManager screenManager, int width, int height) {
        this.screenManager = screenManager;
        camera = new OrthographicCamera();
        cameraViewport = new ExtendViewport(width, height, camera);
        batch = new SpriteBatch();
    }

    public Vector2 getMousePos() {
        Vector3 pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        pos = camera.unproject(pos);
        return new Vector2(pos.x, pos.y);
    }

    protected abstract void handle();
    public abstract void update(float dt);
    public abstract void draw();

    @Override
    public void resize(int width, int height) {
        cameraViewport.update(width, height);
    }

    @Override
    public void render(float dt) {
        handle();
        update(dt);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        draw();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
