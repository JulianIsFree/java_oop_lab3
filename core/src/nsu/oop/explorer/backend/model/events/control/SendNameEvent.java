package nsu.oop.explorer.backend.model.events.control;

public class SendNameEvent extends ControlEvent {
    private String name;
    public SendNameEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isSendNameEvent() {
        return true;
    }
}
