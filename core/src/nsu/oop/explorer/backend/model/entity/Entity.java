package nsu.oop.explorer.backend.model.entity;

public abstract class Entity {
    public abstract void update(float dt);
    public abstract void onDeath();
}
