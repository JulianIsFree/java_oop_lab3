package nsu.oop.explorer.backend.model.events.control;

public class KeyboardEvent extends ControlEvent {
    private int key;
    private boolean isUp;

    public KeyboardEvent(int key, boolean isUp) {
        this.key = key;
        this.isUp = isUp;
    }

    public int key() {
        return key;
    }

    public boolean isUp() {
        return isUp;
    }

    @Override
    public boolean isKeyboardEvent() {
        return true;
    }
}
