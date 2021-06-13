package nsu.oop.explorer.backend.model.exceptions;

public class MapOutOfBoundException extends Throwable {
    public MapOutOfBoundException(int x, int y, int width, int height) {
        super("Can't access point: " + pointToString(x, y) + ", map parameters: " + pointToString(width, height));
    }

    static String pointToString(int x, int y) {
        return "(" + x + ", " + y + ")";
    }
}
