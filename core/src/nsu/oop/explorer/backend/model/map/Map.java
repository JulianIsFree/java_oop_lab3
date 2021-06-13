package nsu.oop.explorer.backend.model.map;

import nsu.oop.explorer.backend.model.exceptions.MapOutOfBoundException;
import nsu.oop.explorer.backend.model.map.cells.Cell;

public class Map {
    private final int seed;
    private final int width;
    private final int height;

    private Cell[][] map;

    public Map(int seed, Cell[][] map) {
        this.map = map;
        this.width = map.length;
        this.height = map[0].length;
        this.seed = seed;
    }

    public boolean doCollide(int x, int y) throws MapOutOfBoundException {
        return doCollide(x, y, true);
    }

    public boolean doCollide(int x, int y, boolean canCollide) throws MapOutOfBoundException {
        if (x < 0 | x >= width | y < 0 | y >= height)
            throw new MapOutOfBoundException(x, y, width, height);
        return map[x][y].doCollide() & canCollide;
    }
}
