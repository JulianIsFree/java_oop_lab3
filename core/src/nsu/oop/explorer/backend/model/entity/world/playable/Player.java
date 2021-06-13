package nsu.oop.explorer.backend.model.entity.world.playable;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import nsu.oop.explorer.backend.model.core.Core;
import nsu.oop.explorer.backend.model.entity.Attackable;
import nsu.oop.explorer.backend.model.entity.world.AntiPlayerBullet;
import nsu.oop.explorer.backend.model.entity.world.AttackerWithCooldown;
import nsu.oop.explorer.backend.model.entity.world.text.ShieldText;
import nsu.oop.explorer.backend.model.entity.world.text.ShiftText;
import nsu.oop.explorer.backend.model.events.control.ControlEvent;
import nsu.oop.explorer.backend.model.events.control.KeyboardEvent;
import nsu.oop.explorer.backend.model.events.control.MouseEvent;
import nsu.oop.explorer.backend.model.events.control.SendNameEvent;
import nsu.oop.explorer.backend.model.events.inner.RespawnPlayerEvent;
import nsu.oop.explorer.backend.model.powers.Shield;
import nsu.oop.explorer.backend.model.powers.Shift;
import nsu.oop.explorer.backend.util.GameConstants;

public class Player extends AttackerWithCooldown implements Playable {
    private static int counter  = 0;
    private int id;
    private boolean goUp;
    private boolean goDown;
    private boolean goLeft;
    private boolean goRight;

    private Shift shift;
    private Shield shield;

    private String name;
    private float speed;

    public Player(float x, float y, Core world, String name) {
        super(x, y, GameConstants.playerWidth, GameConstants.playerHeight, 10, 100, world);
        this.id = counter++;
        speed = 256;
        goUp = false;
        goLeft = false;
        goRight = false;
        goDown = false;

        shift = new Shift();
        shield = new Shield();
        this.name = name;
    }

    public Player(float x, float y, int id, Core world, String name) {
        this(x, y, world, name);
        counter--;
        this.id = id;
    }

    @Override
    public void onDeath() {
        world.addInnerEvent(new RespawnPlayerEvent(this));
        world.updateDeathScore(name);
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        shift.update(dt);
        shield.update(dt);

        float dx = (goRight ? -1 : 0) + (goLeft ? 1 : 0);
        float dy = (goUp ? -1 : 0) + (goDown ? 1 : 0);
        Vector2 direction = new Vector2(dx, dy);
        direction = direction.setLength(shift.multiply() * speed * dt);
        move(direction);
    }

    @Override
    public void handle(ControlEvent e) {
        if (e.isKeyboardEvent()) {
            int key  = ((KeyboardEvent) e).key();
            boolean isUp = ((KeyboardEvent) e).isUp();

            if (key == Input.Keys.W)
                goUp = !isUp;

            if (key == Input.Keys.S)
                goDown = !isUp;

            if (key == Input.Keys.A)
                goLeft = !isUp;

            if (key == Input.Keys.D)
                goRight = !isUp;

            if (key == Input.Keys.SHIFT_LEFT && !isUp) {
                shift.cast();
                if (shift.isReady()) {
                    world.addTextEntity(new ShiftText(getRectangle().x, getRectangle().y, world));
                }
            }

            if (key == Input.Keys.SPACE && !isUp) {
                shield.cast();
                if (shield.isReady()) {
                    world.addTextEntity(new ShieldText(getRectangle().x, getRectangle().y, world));
                }
            }

        } else if (e.isMouseEvent()){
            int key = ((MouseEvent) e).getMouseKey();
            boolean isUp = ((MouseEvent) e).isUp();
            Vector2 localPoint = ((MouseEvent) e).getClickPoint();

            if (isCdReady() && key == Input.Buttons.LEFT && !isUp )  {
                shoot(localPoint);
            }
        } else if(e.isSendNameEvent()) {
            SendNameEvent ene = (SendNameEvent)e;
            world.updateDeathScore(name);
            this.name = ene.getName();
        }
    }

    private void shoot(Vector2 localPoint) {
        Vector2 playerCenter = new Vector2();
        playerCenter = getRectangle().getCenter(playerCenter);

        Vector2 direction = new Vector2(localPoint.x - playerCenter.x, localPoint.y - playerCenter.y);
        AntiPlayerBullet bullet = new AntiPlayerBullet(playerCenter.x, playerCenter.y, direction, id, world, 2f);

        world.addBullet(bullet);
        setCd();
    }

    @Override
    public void receiveDamage(float damage) {
        if (!shield.isActive())
            super.receiveDamage(damage);
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Player))
            return false;

        return ((Player)object).id == this.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getName() {
        return name;
    }
}
