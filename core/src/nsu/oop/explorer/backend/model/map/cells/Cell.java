package nsu.oop.explorer.backend.model.map.cells;

public abstract class Cell {
    private final int x;
    private final int y;
    private final int z;
    private boolean doCollide;

    private Cell(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        doCollide = true;
    }

    protected Cell(int x, int y, int z, boolean doCollide) {
        this(x, y, z);
        this.doCollide = doCollide;
    }

    public int getZ() {
        return z;
    }

    public boolean doCollide() {
        return this.doCollide;
    }
}
