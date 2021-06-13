package nsu.oop.explorer.backend.model.events.control;

public class InfoEvent extends ControlEvent {
    private int id;
    private int textureId;
    private float x;
    private float y;

    public InfoEvent(int id, float x, float y, int textureId) {
        this.id = id;
        this.textureId = textureId;
        this.x = x;
        this.y = y;
    }

    public InfoEvent(int id, float x, float y) {
        this(id, x, y, 0);
    }

    public int textureId() {
        return textureId;
    }

    public int id() {
        return id;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

}
