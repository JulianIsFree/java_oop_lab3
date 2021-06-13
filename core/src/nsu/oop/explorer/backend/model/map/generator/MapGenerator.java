package nsu.oop.explorer.backend.model.map.generator;

import nsu.oop.explorer.backend.model.map.Map;
import nsu.oop.explorer.backend.model.map.cells.Cell;
import nsu.oop.explorer.backend.model.noise.DiscreteDistribution;

public abstract class MapGenerator {
    protected abstract Cell createCellByHeight(int x, int y, double noise, DiscreteDistribution noiseDistribution);
    public abstract Map generateMap(int seed, int width, int height);
}
