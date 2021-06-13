package nsu.oop.explorer.backend.model.powers;

public class Shift extends Power {
    public Shift() {
        super(0.5f, 5);
    }

    public float multiply() {
        return isActive() ? 4 : 1;
    }
}