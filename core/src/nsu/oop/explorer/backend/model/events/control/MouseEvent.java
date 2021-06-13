package nsu.oop.explorer.backend.model.events.control;

import com.badlogic.gdx.math.Vector2;

public class MouseEvent extends ControlEvent {
    private Vector2 clickPoint;
    private int mouseKey;
    private boolean isUp;

    public MouseEvent(Vector2 pos, int mouseKey, boolean isUp) {
        this(pos.x, pos.y, mouseKey, isUp);
    }

    public MouseEvent(float x, float y, int mouseKey, boolean isUp) {
        clickPoint = new Vector2(x, y);
        this.isUp = isUp;
        this.mouseKey = mouseKey;
    }

    public Vector2 getClickPoint() {
        return clickPoint;
    }

    public int getMouseKey() {
        return mouseKey;
    }

    public boolean isUp() {
        return isUp;
    }
    @Override
    public boolean isMouseEvent() {
        return true;
    }
}
