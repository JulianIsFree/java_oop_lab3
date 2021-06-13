package nsu.oop.explorer.backend.model.entity;

public class Cooldown {
    private float cd; // in millis
    private float timer;

    /**
     *
     * @param cd in seconds
     */
    public Cooldown(float cd) {
        this.cd = cd;
        timer = 0;
    }


    public void update(float dt) {
        if (timer > 0)
            timer -= dt;
    }

    public void set() {
        timer = cd;
    }

    public boolean isReady() {
        return timer <= 0;
    }
}