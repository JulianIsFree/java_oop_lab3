package nsu.oop.explorer.backend.model.entity;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class RectangularEntity extends Entity {
    private Rectangle rectangle;

    public RectangularEntity(float x, float y, float width, float height) {
        rectangle = new Rectangle(x, y, width, height);
    }

    public boolean collide(float x, float y) {
        return rectangle.contains(x, y);
    }

    public boolean collide(Circle c) {
        return rectangle.contains(c);
    }

    public boolean collide(Rectangle rect) {
        return rectangle.overlaps(rect);
    }

    public boolean collide(RectangularEntity entity) {
        return rectangle.overlaps(entity.getRectangle());
    }

    public final Rectangle getRectangle() {
        return rectangle;
    }

    protected void move(float dx, float dy) {
        rectangle.x += dx;
        rectangle.y += dy;
    }

    protected void move(Vector2 dv) {
        move(dv.x, dv.y);
    }
}
